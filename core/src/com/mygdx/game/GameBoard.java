package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Node.*;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.util.*;
import java.util.List;

/**
 * Represents the game board screen.
 * <br><br>
 * Handles rendering and other game board activities, including moving the camera, switching between players turns,
 * rolling the die, accessing the shop, and executing event nodes. Includes an instance of GameState, which contains
 * all information specific to the current game being played. Can adjust the camera's view of the gameboard by using the
 * WASD keys to move around the screen and CTRL+, CTRL-, and the scroll wheel to zoom in and out.
 * @see GameState
 *
 * @author Franck Limtung (flimtung)
 * @author Kevin Chen (kchen546)
 */
public class GameBoard extends GameScreen {


    /** Contains all information specific to the current game being played*/
    private GameState gameState;

    /* Events */

    /**
     * Event pauses the game and displays the pause screen.
     * @see PauseScreen
     */
    private Observable<PlayerProfile> pauseEvent = new Observable<PlayerProfile>();
    /**
     * Event opens the shop screen.
     * @see ShopScreen
     */
    private Observable<Array<Item>> shopEvent = new Observable<>();
    /**
     * Event occurs at the end of the game. Displays each player's final score.
     * @see EndScreen
     */
    private Observable<GameState> endEvent = new Observable<GameState>();
    /**
     * Event opens the agility test minigame.
     * @see AgilityTestScreen
     */
    public Observable<Void> agilityTestEvent = new Observable<>();
    /**
     * Event saves the current game state.
     * @see MainGame
     */
    private Observable<String> saveGameEvent = new Observable<>();

    /* Input processing */

    /** Used to process inputs from multiple sources at once. */
    private InputMultiplexer inputMultiplexer;
    /** Polls for and processes keyboard clicks. */
    private InputAdapter clickListener;


    /* Screen display */

    /** Screen width. */
    private int width = 1920;
    /** Screen height. */
    private int height = 1080;
    /** Background image. */
    private Texture background;
    /** Stage for HUD of the game. */
    private Stage hudStage;
    /** Contains all GUI elements in the HUD. */
    private Table hudTable;
    /** Button pauses the game. */
    private TextButton pauseButton;
    /** Button rolls the die. */
    private TextButton rollButton;
    /** Button skips to the next player's turn. */
    private TextButton nextTurnButton;
    /** Text displayed indicates the current player's turn. */
    private Label currPlayerLabel;
    /** Text displayed indicates the current player's level. */
    private Label levelLabel;
    /** Text displayed indicates the current player's money. */
    private Label moneyLabel;
    /** Text displayed indicates the current player's score. */
    private Label scoreLabel;
    /** Text displayed indicates the current player's stars. */
    private Label starsLabel;
    /** Text displayed indicates the number rolled. */
    private Label rollLabel;
    /** Text displayed indicates the current round number. */
    private Label roundLabel;
    /** Arrow shown for each possible direction to move from a tile. */
    private Texture tileArrow;
    /** Buttons used to activate items. Regenerated every turn. */
    transient private ArrayList<TextButton> itemButtons;


    /* Camera settings and control */

    /** Camera for 2D scene. */
    private OrthographicCamera camera;
    /** Camera position x-coordinate. */
    private float newCameraX;
    /** Camera position y-coordinate. */
    private float newCameraY;
    /** Camera angle. */
    private float newCameraAngle;
    /** Used to move the camera up when W key is pressed. */
    private boolean cameraMoveUp = false;
    /** Used to move the camera right when D key is pressed. */
    private boolean cameraMoveRight = false;
    /** Used to move the camera down when S key is pressed. */
    private boolean cameraMoveDown = false;
    /** Used to move the camera left when A key is pressed. */
    private boolean cameraMoveLeft = false;
    /** Used to zoom camera in when CTRL + is pressed. Will apply a constant speed when active. */
    private boolean cameraZoomOut = false;
    /** Used to zoom camera out when CTRL - is pressed. Will apply a constant speed when active.*/
    private boolean cameraZoomIn = false;
    /** Used to zoom camera in and out with scroll wheel. Zoom speed with match scrolling speed. */
    private float zoomVel = 0f;

    /* Debug mode */

    /** Button to modify a player's information in debug mode. */
    private TextButton modifyPlayer;
    /** Button to modify a tile's type in debug mode. */
    private TextButton modifyTile;
    /** Button to skip to the final round in debug mode. */
    private TextButton endGame;


    /**
     * Constructor initializes the gameboard.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public GameBoard(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        itemButtons = new ArrayList<>();

        assets.load(Config.getInstance().getBackgroundPath(), Texture.class);
        assets.finishLoading();

        // Initialize background
        background = assets.get(Config.getInstance().getBackgroundPath());
        Image backgroundImage = new Image(background);
        backgroundImage.setSize(width, height);
        stage.addActor(backgroundImage);

        // Initialize tileArrow
        tileArrow = assets.get(Config.getInstance().getMapArrowPath());

        // Setup keyboard shortcuts
        stage.addListener(new InputListener() {
           @Override
           public boolean keyDown(InputEvent event, int keycode) {
               boolean ctrl = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT); // Is Control key pressed?

               if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.P) {
                   // Go to pause screen by notifying observer in MainGame, the Observer Pattern
                   pauseEvent.notifyObservers(null);
               }
               else if (keycode == Input.Keys.I) {
                   // Go to shop screen
                   shopEvent.notifyObservers(gameState.getItems());
               }
               else if (keycode == Input.Keys.SPACE) {
                   rollButton.fire(new ChangeListener.ChangeEvent());
               }
               else if (ctrl && keycode == Input.Keys.S) {
                   // Display save dialog
                   TextField saveNameInput = new TextField("NewGame", skin);
                   Dialog saveGameDialog = new Dialog("Save Menu", skin) {
                       @Override
                       protected void result(Object object) {
                           if ((Boolean) object) {
                               String filename = saveNameInput.getText();
                               saveGameEvent.notifyObservers(filename);
                           }
                       }
                   };
                   saveGameDialog.text("Enter save name:");
                   saveGameDialog.getContentTable().row();
                   saveGameDialog.getContentTable().add(saveNameInput).fill();
                   saveGameDialog.button("Cancel", false);
                   saveGameDialog.button("Continue", true);
                   saveGameDialog.show(hudStage);
               }
               else if (ctrl && keycode == Input.Keys.NUMPAD_ADD) { cameraZoomIn = true; }
               else if (ctrl && keycode == Input.Keys.NUMPAD_SUBTRACT) { cameraZoomOut = true; }
               else if (keycode == Input.Keys.W) { cameraMoveUp = true; }
               else if (keycode == Input.Keys.D) { cameraMoveRight = true; }
               else if (keycode == Input.Keys.S) { cameraMoveDown = true; }
               else if (keycode == Input.Keys.A) { cameraMoveLeft = true; }
               else return false;

               return true;
           }

           @Override
           public boolean keyUp(InputEvent event, int keycode) {
               if (keycode == Input.Keys.W) { cameraMoveUp = false; }
               else if (keycode == Input.Keys.D) { cameraMoveRight = false; }
               else if (keycode == Input.Keys.S) { cameraMoveDown = false; }
               else if (keycode == Input.Keys.A) { cameraMoveLeft = false; }
               else if (keycode == Input.Keys.NUMPAD_ADD) { cameraZoomIn = false; }
               else if (keycode == Input.Keys.NUMPAD_SUBTRACT) { cameraZoomOut = false; }
               else return false;

               return true;
           }
        });

        // Add node click listener, also used for camera zooming by scrolling
        clickListener = new InputAdapter() {
           @Override
           public boolean touchDown(int screenX, int screenY, int pointer, int button) {
               Player currPlayer = gameState.getCurrentPlayer();
               if (!currPlayer.canMove()) return false; // Player can't move anymore, don't do anything with the event

               // Get position of the click
               Vector3 touchPoint = new Vector3(screenX, screenY, 0);
               camera.unproject(touchPoint);

               // Debug movement; the play can go anywhere
               if (gameState.isDebugMode() && (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT))) {
                   for (Node node : gameState.getNodeMap().values()) {
                       if (node.getSprite().getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
                           // Create new "path" to the node
                           currPlayer.getReachablePaths().add(new ArrayList<String>(Arrays.asList(currPlayer.getCurrentTile(), node.getID())));

                           currPlayer.move(node.getID(), gameState.getNodeMap(), batch, gameState.getHardMode());
                           if (!gameState.getNodeMap().get(currPlayer.getCurrentTile()).activate(currPlayer, batch, hudStage, skin, GameBoard.this, gameState.getHardMode())) {
                               turnChange();
                           }

                           return true;
                       }
                   }
               }

               Map<String, Node> nodeMap = gameState.getNodeMap();
               for (ArrayList<String> path : currPlayer.getReachablePaths()) {
                   String nodeID = path.get(path.size() - 1);
                   Sprite sprite = nodeMap.get(nodeID).getSprite();
                   if (sprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
                       // Player selected a reachable node, we update their position and activate the node
                       currPlayer.move(nodeID, nodeMap, batch, gameState.getHardMode());

                       // Attempt to call Node's activate using the GameBoard's activate function. If it returns false, automatically change the turn.
                       // Otherwise, let the Node handle turn changing.
                       if (!gameState.getNodeMap().get(currPlayer.getCurrentTile()).activate(currPlayer, batch, hudStage, skin, GameBoard.this, gameState.getHardMode())) {
                           turnChange();
                       }

                       return true;
                   }
               }

               return false;
           }

           @Override
           public boolean scrolled(float amountX, float amountY) {
               zoomVel = amountY * 0.1f;

               return true;
           }
        };

        // Initialize HUD
        hudStage = new Stage(new FitViewport(1920, 1080), batch);
        initializeHUD();

        // Initialize camera
        camera = new OrthographicCamera(width, height);
    }


    /**
     * Load and layout HUD elements
     */
    private void initializeHUD() {
        // Initialize buttons and labels
        pauseButton = new TextButton("Pause", skin);
        nextTurnButton = new TextButton("Next Turn", skin);
        rollButton = new TextButton("Roll", skin);
        currPlayerLabel = new Label("currPlayerLabel", skin);
        scoreLabel = new Label("scoreLabel", skin);
        starsLabel = new Label("starsL", skin);
        moneyLabel = new Label("moneyLabel", skin);
        rollLabel = new Label("rollLabel", skin);
        rollLabel.setColor(Color.YELLOW);
        roundLabel = new Label("-1", skin);
        rollLabel.setVisible(false);
        levelLabel = new Label("knowledgelevelLabel", skin);

        // Initialize HUD
        hudTable = new Table();
        hudTable.setBackground(skin.getDrawable("window"));
        hudTable.add(pauseButton).growY().left();
        hudTable.add(currPlayerLabel).padRight(50).fill().left();
        //hudTable.add(scoreLabel).padRight(5).uniform();
        hudTable.add(starsLabel).padRight(50).fill().left();
        hudTable.add(moneyLabel).padRight(50).fill().left();
        hudTable.add(levelLabel).padRight(50).fill().left();
        hudTable.add(roundLabel).padRight(50).fill().left();
        hudTable.add(rollButton).padRight(50).growX().right();
        hudTable.add(rollLabel).padRight(50).fill().right();

        //hudTable.add(nextTurnButton).padLeft(5).expandX();
        // Put the hud table into another table to align it properly with the top of the screen
        Table t = new Table();
        t.setBounds(0, (float) (hudStage.getHeight() * 0.92) + 25, hudStage.getWidth(), (float) (hudStage.getHeight() *.08));
        t.add(hudTable).width(hudStage.getWidth()).grow();

        hudStage.addActor(t);


        // Add listeners
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseEvent.notifyObservers(null);
            }
        });

        rollButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Player currPlayer = gameState.getCurrentPlayer();
                if (!currPlayer.canRoll()) return;

                rollLabel.setText("Roll: " + gameState.getCurrentPlayer().rollDie(gameState.getNodeMap()));
                rollLabel.setVisible(true);

                // Play random sound
                int soundIndex = Utility.getRandom(1, 29);
                SoundSystem.getInstance().playSound("rolling/dice-" + Integer.toString(soundIndex) + ".wav");

                // Disable button if the player cannot roll anymore
                checkRollButton();
            }
        });

        // Setup debugmode
        modifyPlayer = new TextButton("(DEBUG) Modify Player", skin);
        modifyPlayer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Dialog playerModDialog = new Dialog("(DEBUG) Modify Player", skin);
                Table dataTable = new Table();
                dataTable.setFillParent(true);

                TextField moneyField = new TextField(Integer.toString(gameState.getCurrentPlayer().getMoney()), skin);
                TextField starField = new TextField(Integer.toString(gameState.getCurrentPlayer().getStars()), skin);
                TextField levelField = new TextField(Integer.toString(gameState.getCurrentPlayer().getLevel()), skin);
                TextButton applyButton = new TextButton("Apply", skin);
                applyButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                        gameState.getCurrentPlayer().setMoney(Integer.parseInt(moneyField.getText()));
                        gameState.getCurrentPlayer().setStars(Integer.parseInt(starField.getText()));
                        int desiredLevel = Integer.parseInt(levelField.getText());
                        try {
                            gameState.getCurrentPlayer().setLevel(desiredLevel);
                        } catch (IllegalArgumentException e) {
                            levelField.setText(Integer.toString(gameState.getCurrentPlayer().getLevel()));
                        }
                    }
                });

                dataTable.add(new Label("Money: ", skin));
                dataTable.add(moneyField);
                dataTable.row();
                dataTable.add(new Label("Stars: ", skin));
                dataTable.add(starField);
                dataTable.row();
                dataTable.add(new Label("Level: ", skin));
                dataTable.add(levelField);
                dataTable.row();
                dataTable.add(applyButton);

                playerModDialog.getContentTable().add(dataTable);
                playerModDialog.button("Close");

                playerModDialog.show(hudStage);
            }
        });
        modifyTile = new TextButton("(DEBUG) Modify Tile", skin);
        modifyTile.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Dialog modifyDialog = new Dialog("(DEBUG) Modify Tile", skin);

                Map<String, Node> nodeMap = gameState.getNodeMap();
                Node currentNode = nodeMap.get(gameState.getCurrentPlayer().getCurrentTile());

                TextButton normalTile = new TextButton("Normal Tile", skin);
                normalTile.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                        String currentKey = currentNode.getID();
                        int x = currentNode.getMapX();
                        int y = currentNode.getMapY();
                        boolean north = currentNode.getNorth();
                        boolean south = currentNode.getSouth();
                        boolean west = currentNode.getWest();
                        boolean east = currentNode.getEast();
                        nodeMap.remove(currentKey);
                        NormalNode newNormal = new NormalNode(x, y, north, east, south, west, nodeMap, assets);
                        nodeMap.put(currentKey, newNormal);
                    }
                });
                TextButton starTile = new TextButton("Star Tile", skin);
                starTile.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                        String currentKey = currentNode.getID();
                        int x = currentNode.getMapX();
                        int y = currentNode.getMapY();
                        boolean north = currentNode.getNorth();
                        boolean south = currentNode.getSouth();
                        boolean west = currentNode.getWest();
                        boolean east = currentNode.getEast();
                        nodeMap.remove(currentKey);
                        StarNode newStar = new StarNode(x, y, north, east, south, west, nodeMap, assets);
                        nodeMap.put(currentKey, newStar);
                    }
                });
                TextButton penaltyTile = new TextButton("Penalty Tile", skin);
                penaltyTile.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                        String currentKey = currentNode.getID();
                        int x = currentNode.getMapX();
                        int y = currentNode.getMapY();
                        boolean north = currentNode.getNorth();
                        boolean south = currentNode.getSouth();
                        boolean west = currentNode.getWest();
                        boolean east = currentNode.getEast();
                        nodeMap.remove(currentKey);
                        PenaltyNode newPenal = new PenaltyNode(x, y, north, east, south, west, nodeMap, assets);
                        nodeMap.put(currentKey, newPenal);
                    }
                });

                TextButton eventTile = new TextButton("Event Tile", skin);
                eventTile.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                        String currentKey = currentNode.getID();
                        int x = currentNode.getMapX();
                        int y = currentNode.getMapY();
                        boolean north = currentNode.getNorth();
                        boolean south = currentNode.getSouth();
                        boolean west = currentNode.getWest();
                        boolean east = currentNode.getEast();
                        nodeMap.remove(currentKey);
                        GlobalPenaltyNode newEvent = new GlobalPenaltyNode(x, y, north, east, south, west, nodeMap, assets);
                        newEvent.addEventListener(penaltyAmount -> gameState.globalPenaltyEvent(penaltyAmount));
                        nodeMap.put(currentKey, newEvent);
                    }
                });

                // Setup ButtonGroup
                // TODO does nothing?
                ButtonGroup selectGroup = new ButtonGroup(normalTile, starTile, penaltyTile, eventTile);
                selectGroup.setMaxCheckCount(1);
                if (currentNode instanceof NormalNode) selectGroup.setChecked("Normal Tile");
                else if (currentNode instanceof StarNode) selectGroup.setChecked("Star Tile");
                else if (currentNode instanceof PenaltyNode) selectGroup.setChecked("Penalty Tile");
                else if (currentNode instanceof GlobalPenaltyNode) selectGroup.setChecked("Event Tile");

                Table dataTable = new Table();
                dataTable.setFillParent(true);

                dataTable.add(new Label("Select Tile Type:", skin));
                dataTable.row();
                dataTable.add(normalTile).fillX().uniform();
                dataTable.row();
                dataTable.add(starTile).fillX().uniform();
                dataTable.row();
                dataTable.add(penaltyTile).fillX().uniform();
                dataTable.row();
                dataTable.add(eventTile).fillX().uniform();

                modifyDialog.getContentTable().add(dataTable);
                modifyDialog.button("Close");

                modifyDialog.show(hudStage);
            }
        });
        endGame = new TextButton("(DEBUG) Skip to Final Round", skin);
        endGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameState.setRound(Config.getInstance().getMaxRounds());
            }
        });

        // Layout debug stuff
        endGame.setVisible(false);
        endGame.setPosition(hudStage.getWidth() - endGame.getWidth(), 0);
        modifyTile.setWidth(endGame.getWidth());
        modifyTile.setVisible(false);
        modifyTile.setPosition(hudStage.getWidth() - modifyTile.getWidth(), 50); // Put on top of the other button
        modifyPlayer.setWidth(endGame.getWidth());
        modifyPlayer.setVisible(false);
        modifyPlayer.setPosition(hudStage.getWidth() - modifyPlayer.getWidth(), 100);  // Put on top of the other button

        hudStage.addActor(endGame);
        hudStage.addActor(modifyTile);
        hudStage.addActor(modifyPlayer);


        // Setup input multiplexer so all input handlers work at the same time
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(hudStage);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(clickListener);

    }

    /**
     * Set the new position for the camera to the current player.
     * Used to smoothly move the camera towards the current players position.
     */
    private void moveCameraPlayer() {
        Sprite currPlayerSprite = gameState.getCurrentPlayer().getSprite();
        newCameraX = currPlayerSprite.getX() + currPlayerSprite.getWidth() / 2.0f;
        newCameraY = currPlayerSprite.getY() + currPlayerSprite.getHeight() / 2.0f;
        // Get angle between current camera position and new one.
        newCameraAngle = MathUtils.atan2(newCameraY - camera.position.y, newCameraX - camera.position.x);
    }

    /**
     * Called when the screen switches to the gameboard screen.
     */
    @Override
    public void show() {
        if (gameState == null) {
            throw new IllegalStateException("Switched to GameBoard without a GameState set");
        }

        if (gameState.isDebugMode()) {
            hudStage.setDebugAll(true); // Enable draw debug if we are in debug mode
            modifyPlayer.setVisible(true);
            modifyTile.setVisible(true);
            endGame.setVisible(true);
        }

        moveCameraPlayer();
        updateItemButtons();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }


    /**
     * Renders the gameboard screen and processes gameboard logic.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        /*
         * Rendering section
         */

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        Batch batch = stage.getBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw nodes and arrows indicating possible directions
        // Arrows drawn first so they are below everything else
        Map<String, Node> nodeMap = gameState.getNodeMap();
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, 0.5f); //set alpha to 50%
        for (Node node : nodeMap.values()) {
            if (node.getNorth()) {
                batch.draw(tileArrow, node.getXPos(), node.getYPos() + 65, tileArrow.getWidth() / 2.0f,
                        tileArrow.getHeight() / 2.0f, tileArrow.getWidth(), tileArrow.getHeight(), 0.4f,
                        0.4f, 0, 0, 0, 100, 100, false, false);
            }
            if (node.getEast()) {
                batch.draw(tileArrow, node.getXPos() + 75, node.getYPos(), tileArrow.getWidth() / 2.0f,
                        tileArrow.getHeight() / 2.0f, tileArrow.getWidth(), tileArrow.getHeight(), 0.4f,
                        0.4f, 270, 0, 0, 100, 100, false, false);
            }
            if (node.getSouth()) {
                batch.draw(tileArrow, node.getXPos(), node.getYPos() - 90, tileArrow.getWidth() / 2.0f,
                        tileArrow.getHeight() / 2.0f, tileArrow.getWidth(), tileArrow.getHeight(), 0.4f,
                        0.4f, 180, 0, 0, 100, 100, false, false);
            }
            if (node.getWest()) {
                batch.draw(tileArrow, node.getXPos() - 75, node.getYPos(), tileArrow.getWidth() / 2.0f,
                        tileArrow.getHeight() / 2.0f, tileArrow.getWidth(), tileArrow.getHeight(), 0.4f,
                        0.4f, 90, 0, 0, 100, 100, false, false);
            }
        }
        batch.setColor(c.r, c.g, c.b, 1f); //set alpha back to normal

        for (Node node : nodeMap.values()) {
            node.draw(batch);
        }

        // Render player sprites
        List<Player> playerList = gameState.getPlayerList();
        for (Player player : playerList) {
            Sprite sprite = player.getSprite();
            Sprite freezeSprite = player.getFreezeSprite();
            Sprite shieldSprite = player.getShieldSprite();

            sprite.draw(batch);
            freezeSprite.draw(batch);
            shieldSprite.setPosition(sprite.getX(), sprite.getY());
            shieldSprite.draw(batch);
        }

        // Render ActionTexts
        ActionTextSystem.render(batch, Gdx.graphics.getDeltaTime());

        batch.end();

        // Update HUD and draw on top of the game
        Player currPlayer = gameState.getCurrentPlayer();
        currPlayerLabel.setText(currPlayer.getPlayerProfile().getName() + "'s Turn");
        scoreLabel.setText("Score: " + currPlayer.getScore());
        starsLabel.setText("Stars: " + currPlayer.getStars());
        moneyLabel.setText("Money: $" + currPlayer.getMoney());
        levelLabel.setText("Knowledge Level: " + currPlayer.getLevel());

        roundLabel.setText("Round: " + gameState.getRound());

        hudStage.act(Gdx.graphics.getDeltaTime());
        hudStage.draw();

        /*
         * Game logic section
         */

        if (!currPlayer.canMove() && !currPlayer.canRoll()) {
            nextTurnButton.setVisible(true);
        }
        else {
            nextTurnButton.setVisible(false);
        }

        // Camera logic - handle WASD input
        if (cameraMoveUp || cameraMoveRight || cameraMoveDown || cameraMoveLeft) {
            newCameraX += 8f * (cameraMoveRight ? 1 : 0) + -8f * (cameraMoveLeft ? 1 : 0);
            newCameraY += 8f * (cameraMoveUp ? 1 : 0) + -8f * (cameraMoveDown ? 1 : 0);
            // Recalculate angle
            newCameraAngle = MathUtils.atan2(newCameraY - camera.position.y, newCameraX - camera.position.x);
        }

        // Move camera towards new position unless it's already close enough
        if (!Utility.epsilonEqual(camera.position.x, newCameraX, 16f)) {
            camera.translate(MathUtils.cos(newCameraAngle) * 15f, 0);
        }
        if (!Utility.epsilonEqual(camera.position.y, newCameraY, 16f)) {
            camera.translate(0, MathUtils.sin(newCameraAngle) * 15f);
        }
        // Handle zoom
        if (cameraZoomIn) camera.zoom -= 0.025f;
        if (cameraZoomOut) camera.zoom += 0.025f;
        camera.zoom += zoomVel;
        zoomVel *= 0.5f;

        if (camera.zoom < 0.5f) camera.zoom = 0.5f; // Max zoom in
        else if (camera.zoom > 4f) camera.zoom = 4f; // Max zoom out

        camera.update();
    }

    /**
     * Resizes the screen.
     * @param width New screen width.
     * @param height New screen height.
     */
    @Override
    public void resize(int width, int height) {
        hudStage.getViewport().update(width, height, true);
    }

    /**
     * Disposes of the screen's resources
     */
    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
        hudStage.dispose();
    }

    /**
     * Creates/recreates the buttons used to activate active Items for the current Player.
     * Call every time the current Player changes or an Item was used.
     */
    private void updateItemButtons() {
        // Update item activation buttons TODO do we want items to be usable on the game board? What about passive items?
        for (TextButton button : itemButtons) {
            button.addAction(Actions.removeActor());
        }
        itemButtons.clear();

        if (!gameState.getCurrentPlayer().canMove()) {
            return;
        }

        // To prevent excess item buttons, we allow stacking of items. This map is used to search for duplicates
        HashMap<String, Map.Entry<TextButton, Integer>> buttonMap = new HashMap<>();

        float x = 0;
        for (Item item : gameState.getCurrentPlayer().getItems()) {
            if (item.isPassive()) continue; // Skip passive items

            // Check if it already exists in the map. If so, just increment the existing button label
            if (buttonMap.containsKey(item.getName())) {
                Map.Entry<TextButton, Integer> entry = buttonMap.get(item.getName());
                TextButton button = entry.getKey();
                Integer count = entry.getValue();
                count++;
                button.setText("Use " + item.getName() + " (" + count + ")");

                buttonMap.put(item.getName(), new AbstractMap.SimpleEntry<TextButton, Integer>(button, count));

                continue;
            }

            TextButton button = new TextButton("Use " + item.getName() + " (1)", skin);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    Player player = gameState.getCurrentPlayer();
                    boolean delete = item.use(gameState.getCurrentPlayer(), gameState, hudStage);

                    if (delete) {
                        gameState.getCurrentPlayer().removeItem(item.getName());
                        ActionTextSystem.addText(item.getName() + " activated", player.getSprite().getX(), player.getSprite().getY() + 50, 0.5f);
                    }
                    checkRollButton();
                    updateItemButtons();
                }
            });
            button.setX(x);
            x += button.getWidth();
            itemButtons.add(button);
            hudStage.addActor(button);

            buttonMap.put(item.getName(), new AbstractMap.SimpleEntry<TextButton, Integer>(button, 1));
        }
    }

    /**
     * Checks if the roll button should be visible, based on if the player can roll
     */
    private void checkRollButton() {
        if (!gameState.getCurrentPlayer().canRoll()) {
            rollButton.setVisible(false);
            rollButton.setText("Roll");

            for (Button button : itemButtons) {
                button.setVisible(false);
            }
        }
        else if (gameState.getCurrentPlayer().getMultiDice()) rollButton.setText("Roll (Multidice enabled)");
    }

    /**
     * Sets the GameState. Must be set before switching screen to GameBoard.
     * @param gameState GameState to load
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        // Roll button only visible if the current player has rolls left
        rollButton.setVisible(gameState.getCurrentPlayer().canRoll());
        rollLabel.setVisible(!gameState.getCurrentPlayer().canRoll());

        camera.zoom = 1f; // Set to default
    }

    /**
     * Finish the current turn of the GameState and go to the next one.
     */
    public void turnChange() {
        rollButton.setVisible(true);
        rollLabel.setVisible(false);
        gameState.nextTurn();

        // Check for end of game
        if (gameState.isGameOver()) {
            SoundSystem.getInstance().playSound("success_bell.mp3");

            Dialog goToEnd = new Dialog("End Game", skin) {
                @Override
                protected void result(Object object) {
                    if ((Boolean) object) {
                        endEvent.notifyObservers(gameState);
                    }
                }
            };
            goToEnd.text("Game is over.");
            goToEnd.button("Continue", true);
            goToEnd.show(hudStage);

            return;
        }

        moveCameraPlayer();
        // Update items to the new player
        updateItemButtons();
    }


    /**
     * Returns the active game state.
     * @return Active game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Assigns an observer to listen for the event to pause the game.
     * @param ob Observer to listen for the event to pause the game.
     */
    public void addPauseListener(Observer<PlayerProfile> ob) { pauseEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to open the shop screen.
     * @param ob Observer to listen for the event to open the shop screen.
     */
    public void addShopListener(Observer<Array<Item>> ob) { shopEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to end the game.
     * @param ob Observer to listen for the event to end the game.
     */
    public void addEndListener(Observer<GameState> ob) { endEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to open the agility test minigame.
     * @param ob Observer to listen for the event to open the agility test minigame.
     */
    public void addAgilityTestListener(Observer<Void> ob) { agilityTestEvent.addObserver(ob); }


    public void addSaveGameListener(Observer<String> ob) {
        saveGameEvent.addObserver(ob);
    }
}
