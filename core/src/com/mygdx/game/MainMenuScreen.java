/*
 * Class to show options menu before going into the main game baord screen
 * - Continue button
 * - Play / New Game button
 * - Load Game button
 * - Teacher Panel button
 */

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen extends ScreenAdapter {
    private MainGame main;
    private AssetManager assets;
    private Config config;
    private Skin skin;

    private Stage stage;
    private Table table;
    private TextButton quitButton;
    private TextButton playButton;
    private TextButton continueButton;
    private Dialog confirmQuitDialog;

    /**
     * @param main reference to the shared MainGame object
     * @param assets reference to the shared AssetManager
     * @param config reference to the shared Config
     */
    public MainMenuScreen(MainGame main, AssetManager assets, Config config) {
        this.main = main;
        this.assets = assets;
        this.config = config;
        this.skin = assets.get(config.uiPath, Skin.class);
        stage = new Stage(new ScreenViewport(), main.batch);
        table = new Table();

        // Initialize buttons
        quitButton = new TextButton("Quit", skin);
        playButton = new TextButton("New Game", skin);
        continueButton = new TextButton("Continue Game", skin);
        // Initialize confirm to quit dialog box
        confirmQuitDialog = new Dialog("Confirm Quit", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    // Save players before quitting
                    main.saveProfiles();
                    Gdx.app.exit();
                }
            }
        };
        confirmQuitDialog.text("Are you sure you want to quit?");
        confirmQuitDialog.button("Yes", true);
        confirmQuitDialog.button("No", false);

        // Layout UI
        table.setFillParent(true); // Size table to stage
        table.add(playButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(continueButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(quitButton).fillX();
        stage.addActor(table);

        // Add listeners
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmQuitDialog.show(stage);
            }
        });
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Create new game with all players in it
                GameState newGame = new GameState(main.profileList);
                main.getGameBoard().setGameState(newGame);
                main.setScreen(main.getGameBoard());
            }
        });
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameState gs;
                if (Utility.fileExists(config.gameStateSavePath)) {
                    gs = main.loadGameState(config.gameStateSavePath);
                    main.getGameBoard().setGameState(gs);
                    main.setScreen(main.getGameBoard());
                }
                // Else do nothing
            }
        });

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
