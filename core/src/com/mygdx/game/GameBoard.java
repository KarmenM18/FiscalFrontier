/*
* Class used for the game board screen.
* - Rolling
* - Taking Turns
* - Shop
* - Events
 */

package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.util.*;
import java.util.List;

public class GameBoard extends GameScreen {
    // Observables are used to inform about events to subscribed Observers. The Observer Pattern
    private Observable<PlayerProfile> pauseEvent = new Observable<PlayerProfile>();
    private Observable<Void> shopEvent = new Observable<Void>();

    private Texture background;
    private InputMultiplexer inputMultiplexer;
    private InputAdapter clickListener;
    private OrthographicCamera camera;

    private Stage hudStage; // The stage for the HUD of the game
    private Table hudTable;
    private TextButton pauseButton;
    private TextButton rollButton;
    private TextButton nextTurnButton;
    private TextButton shopButton;
    private Label currPlayerLabel;
    private Label moneyLabel;
    private Label scoreLabel;
    private Label starsLabel;
    private Label rollLabel;

    transient private ArrayList<Item> playerItems; // Transient as it will be regenerated when the state is deserialized
    transient private ArrayList<TextButton> itemButtons;

    private GameState gameState;
    private int width = 1920;
    private int height = 1080;
    private float newCameraX;
    private float newCameraY;
    private float newCameraAngle;
    // Camera control variables
    private boolean cameraMoveUp = false;
    private boolean cameraMoveRight = false;
    private boolean cameraMoveDown = false;
    private boolean cameraMoveLeft = false;

    public GameBoard(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        itemButtons = new ArrayList<>();

        assets.load("gameboard.png", Texture.class);
        assets.finishLoading();

        // Initialize background
        background = assets.get("gameboard.png");
        Image backgroundImage = new Image(background);
        backgroundImage.setSize(width, height);
        stage.addActor(backgroundImage);

        // Setup keyboard shortcuts
        stage.addListener(new InputListener() {
           @Override
           public boolean keyDown(InputEvent event, int keycode) {
               if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.P) {
                   // Go to pause screen by notifying observer in MainGame, the Observer Pattern
                   pauseEvent.notifyObservers(null);
               }
               else if (keycode == Input.Keys.I) {
                   // Go to shop screen
                   shopEvent.notifyObservers(null);
               }
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
               else return false;

               return true;
           }
        });

        // Add node click listener
        clickListener = new InputAdapter() {
           @Override
           public boolean touchDown(int screenX, int screenY, int pointer, int button) {
               Player currPlayer = gameState.getCurrentPlayer();
               if (!currPlayer.canMove()) return false; // Player can't move anymore, don't do anything with the event

               // Get position of the click
               Vector3 touchPoint = new Vector3(screenX, screenY, 0);
               camera.unproject(touchPoint);

               Map<String, Node> nodeMap = gameState.getNodeMap();
               for (ArrayList<String> path : currPlayer.getReachablePaths()) {
                   String nodeID = path.get(path.size() - 1);
                   Sprite sprite = nodeMap.get(nodeID).getSprite();
                   if (sprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
                       // Player selected a reachable node, we update their position and activate the node
                       currPlayer.move(nodeID, nodeMap, batch);
                       moveCameraPlayer();

                       return true;
                   }
               }

               return false;
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
        starsLabel = new Label("starsLabel", skin);
        moneyLabel = new Label("moneyLabel", skin);
        rollLabel = new Label("rollLabel", skin);
        rollLabel.setVisible(false);

        // Initialize HUD
        hudTable = new Table();
        hudTable.setBackground(skin.getDrawable("window"));
        hudTable.add(pauseButton);
        hudTable.add(currPlayerLabel).padLeft(5).uniform();
        hudTable.add(scoreLabel).padLeft(5).uniform();
        hudTable.add(starsLabel).padLeft(5).uniform();
        hudTable.add(moneyLabel).padLeft(5).uniform();
        hudTable.add(rollButton).padLeft(5).uniform();
        hudTable.add(rollLabel).padLeft(5).uniform();
        hudTable.add(nextTurnButton).expandX().right();
        // Put the hud table into another table to align it properly with the top of the screen
        Table t = new Table();
        t.setBounds(0, (float) (hudStage.getHeight() * .94), hudStage.getWidth(), (float) (hudStage.getHeight() *.1));
        t.add(hudTable).width(hudStage.getWidth());

        hudStage.addActor(t);

        // Add listeners
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseEvent.notifyObservers(null);
            }
        });
        //TODO change to automatic next turn
        nextTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                rollButton.setVisible(true);
                rollLabel.setVisible(false);
                gameState.nextTurn();
                moveCameraPlayer();

                // Update items to the new player
                updateItemButtons();
            }
        });
        rollButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Player currPlayer = gameState.getCurrentPlayer();
                if (!currPlayer.canRoll()) return;

                rollLabel.setText(gameState.getCurrentPlayer().rollDie(gameState.getNodeMap()));
                rollLabel.setVisible(true);

                // Disable button if the player cannot roll anymore
                checkRollButton();
            }
        });

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
        newCameraX = currPlayerSprite.getX();
        newCameraY = currPlayerSprite.getY();
        // Get angle between current camera position and new one.
        newCameraAngle = MathUtils.atan2(newCameraY - camera.position.y, newCameraX - camera.position.x);
    }

    @Override
    public void show() {
        if (gameState == null) {
            throw new IllegalStateException("Switched to GameBoard without a GameState set");
        }

        moveCameraPlayer();
        updateItemButtons();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

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

        // Draw nodes
        Map<String, Node> nodeMap = gameState.getNodeMap();
        for (Node node : nodeMap.values()) {
            Sprite sprite = node.getSprite();
            sprite.draw(batch);
        }

        // Render player sprites
        List<Player> playerList = gameState.getPlayerList();
        for (Player player : playerList) {
            Sprite sprite = player.getSprite();
            sprite.draw(batch);
        }
        batch.end();

        // Update HUD and draw on top of the game
        Player currPlayer = gameState.getCurrentPlayer();
        currPlayerLabel.setText(currPlayer.getPlayerProfile().getName() + "'s Turn");
        scoreLabel.setText("Score: " + currPlayer.getScore());
        starsLabel.setText("Stars: " + currPlayer.getStars());
        moneyLabel.setText("Money: $" + currPlayer.getMoney());

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
            newCameraX += 4f * (cameraMoveRight ? 1 : 0) + -4f * (cameraMoveLeft ? 1 : 0);
            newCameraY += 4f * (cameraMoveUp ? 1 : 0) + -4f * (cameraMoveDown ? 1 : 0);
            // Recalculate angle
            newCameraAngle = MathUtils.atan2(newCameraY - camera.position.y, newCameraX - camera.position.x);
        }

        // Move camera towards new position unless it's already close enough
        if (!Utility.epsilonEqual(camera.position.x, newCameraX, 10f)) {
            camera.translate(MathUtils.cos(newCameraAngle) * 5f, 0);
        }
        if (!Utility.epsilonEqual(camera.position.y, newCameraY, 10f)) {
            camera.translate(0, MathUtils.sin(newCameraAngle) * 5f);
        }
        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        hudStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
        hudStage.dispose();
    }

    public void updateItemButtons() {
        // Update item activation buttons TODO do we want items to be usable on the game board? What about passive items?
        for (TextButton button : itemButtons) {
            button.addAction(Actions.removeActor());
        }
        itemButtons.clear();

        float x = 0;
        for (Item item : gameState.getCurrentPlayer().getItems()) {
            if (item.isPassive()) continue; // Skip passive items

            TextButton button = new TextButton("Use " + item.getName(), skin);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    boolean delete = item.use(gameState.getCurrentPlayer(), gameState, hudStage);
                    if (delete) gameState.getCurrentPlayer().removeItem(item.getName());
                    checkRollButton();
                    updateItemButtons();
                }
            });
            button.setX(x);
            x += button.getWidth();
            itemButtons.add(button);
            hudStage.addActor(button);
        }
    }

    /**
     * Checks if the roll button should be visible, based on the if the player can roll
     */
    public void checkRollButton() {
        if (!gameState.getCurrentPlayer().canRoll()) {
            rollButton.setVisible(false);
            // Disable items as well TODO: intended behavior?
            for (Button button : itemButtons) {
                button.setVisible(false);
            }
        }
    }

    /**
     * Setter for GameState.
     * Must be set before switching screen to GameBoard.
     * @param gameState GameState to load
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        // Roll button only visible if the current player has rolls left
        rollButton.setVisible(gameState.getCurrentPlayer().canRoll());
        rollLabel.setVisible(!gameState.getCurrentPlayer().canRoll());
    }

    public GameState getGameState() {
        return gameState;
    }

    public void addPauseListener(Observer<PlayerProfile> ob) { pauseEvent.addObserver(ob); }
    public void addShopListener(Observer<Void> ob) { shopEvent.addObserver(ob); }
}
