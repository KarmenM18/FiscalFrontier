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

    Texture img;
    // TODO: Have a separate class to store game state
    int currTurn; // The current active player turn


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

        assets.load("badlogic.jpg", Texture.class);
        assets.finishLoading();

        img = assets.get("badlogic.jpg");
        Image imgActor =  new Image(img);
        stage.addActor(imgActor);

        // Setup keyboard shortcuts
        stage.addListener(new InputListener() {
           @Override
           public boolean keyDown(InputEvent event, int keycode) {
               if (keycode == Input.Keys.ESCAPE) {
                   main.setScreen(main.getMainMenuScreen());
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
        hudTable.setFillParent(true);
        hudTable.top().left();
        hudTable.add(menuButton);
        hudTable.add(currPlayerLabel).padLeft(10).uniform();
        hudTable.add(scoreLabel).padLeft(10).uniform();
        hudTable.add(starsLabel).padLeft(10).uniform();
        hudTable.add(moneyLabel).padLeft(10).uniform();
        hudTable.add(nextTurnButton).expandX().right();
        hudStage.addActor(hudTable);

        // Add listeners
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.setScreen(main.getMainMenuScreen());
            }
        });
        nextTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currTurn = (currTurn + 1) % main.playerList.size();
            }
        });

        // Setup input multiplexer so hud and main stage work at the same time
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(hudStage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // Update HUD and draw on top of the game
        Player currPlayer = main.playerList.get(currTurn);
        currPlayerLabel.setText(currPlayer.name + "'s Turn");
        scoreLabel.setText("Score: " + currPlayer.score);
        starsLabel.setText("Stars: " + currPlayer.stars);
        moneyLabel.setText("Money: $" + currPlayer.money);

        hudStage.draw();
    }

    @Override
    public void dispose() {
        img.dispose();
        stage.dispose();
        hudStage.dispose();
    }
}
