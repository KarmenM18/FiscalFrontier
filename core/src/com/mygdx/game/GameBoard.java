/*
* Class used for GameBoard events during player turns
* - Rolling
* - Taking Turns
* - Shop
* - Events
 */

package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameBoard extends ScreenAdapter {
    private MainGame main;
    private AssetManager assets;
    private Config config;
    private Skin skin;
    private InputMultiplexer inputMultiplexer;

    private Stage stage;
    private Stage hudStage; // The stage for the HUD of the game
    private Table hudTable;
    private TextButton menuButton;
    private TextButton nextTurnButton;
    private Label currPlayerLabel;
    private Label moneyLabel;
    private Label scoreLabel;
    private Label starsLabel;
    private Dialog confirmMenuDialog;

    private Texture background;

    private GameState gameState;


    /**
     * Constructor for the GameBoard class
     *
     * @param main reference to the shared MainGame object
     * @param assets reference to the shared AssetManager
     * @param config reference to the shared Config
     */
    public GameBoard(MainGame main, AssetManager assets, Config config) {
        this.main = main;
        this.assets = assets;
        this.config = config;
        this.skin = assets.get(config.uiPath, Skin.class);
        stage = new Stage(new ScreenViewport(), main.batch);
        hudStage = new Stage(new ScreenViewport(), main.batch);
        initializeHUD();

        assets.load("background.jpeg", Texture.class);
        assets.finishLoading();

        // Initialize background
        background = assets.get("background.jpeg");
        Image backgroundImage = new Image(background);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(backgroundImage);

        // Setup keyboard shortcuts
        stage.addListener(new InputListener() {
           @Override
           public boolean keyDown(InputEvent event, int keycode) {
               if (keycode == Input.Keys.ESCAPE) {
                   confirmMenuDialog.show(hudStage);
                   return true;
               }
               return false;
           }
        });
    }

    /**
     * Load and layout HUD elements
     */
    private void initializeHUD() {
        // Initialize buttons and labels
        menuButton = new TextButton("Menu", skin);
        nextTurnButton = new TextButton("Next Turn", skin);
        currPlayerLabel = new Label("currPlayerLabel", skin);
        scoreLabel = new Label("scoreLabel", skin);
        starsLabel = new Label("starsLabel", skin);
        moneyLabel = new Label("moneyLabel", skin);
        // Initialize HUD
        hudTable = new Table();
        hudTable.setBackground(skin.getDrawable("window"));
        hudTable.add(menuButton);
        hudTable.add(currPlayerLabel).padLeft(10).uniform();
        hudTable.add(scoreLabel).padLeft(10).uniform();
        hudTable.add(starsLabel).padLeft(10).uniform();
        hudTable.add(moneyLabel).padLeft(10).uniform();
        hudTable.add(nextTurnButton).expandX().right();
        Table table = new Table();
        table.setBounds(0, (float) (hudStage.getHeight() * .94), hudStage.getWidth(), (float) (hudStage.getHeight() *.1));
        table.add(hudTable).width(hudStage.getWidth());

        hudStage.addActor(table);

        // Initialize confirm to menu box
        confirmMenuDialog = new Dialog("Confirm Menu", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    enterMenu();
                }
            }
        };
        confirmMenuDialog.text("Are you sure you want to exit to menu? Game progress will be saved.");
        confirmMenuDialog.button("Yes", true);
        confirmMenuDialog.button("No", false);

        // Add listeners
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmMenuDialog.show(hudStage);
            }
        });
        nextTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameState.currPlayerTurn = (gameState.currPlayerTurn + 1) % gameState.playerList.size();
                gameState.turnNumber++;
            }
        });

        // Setup input multiplexer so hud and main stage work at the same time
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(hudStage);
    }

    /**
     * Setter for GameState.
     * Must be set before switching screen to GameBoard.
     * @param gameState GameState to load
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Save the GameState and leave to the menu
     */
    private void enterMenu() {
        main.saveGameState(gameState);
        main.setScreen(main.getMainMenuScreen());
    }

    @Override
    public void show() {
        if (gameState == null) {
            throw new IllegalStateException("Switched to GameBoard without a GameState set");
        }

        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // Update HUD and draw on top of the game
        Player currPlayer = gameState.playerList.get(gameState.currPlayerTurn);
        currPlayerLabel.setText(currPlayer.profile.name + "'s Turn");
        scoreLabel.setText("Score: " + currPlayer.score);
        starsLabel.setText("Stars: " + currPlayer.stars);
        moneyLabel.setText("Money: $" + currPlayer.money);

        hudStage.act(Gdx.graphics.getDeltaTime());
        hudStage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
        hudStage.dispose();
    }
}
