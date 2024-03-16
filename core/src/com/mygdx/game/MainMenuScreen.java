/*
 * Class to show options menu before going into the main game baord screen
 * - Continue button
 * - Play / New Game button
 * - Load Game button
 * - Teacher Panel button
 */

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

public class MainMenuScreen extends GameScreen {
    private Observable<Void> startGameEvent = new Observable<Void>();
    private Observable<Void> continueGameEvent = new Observable<Void>();
    private Observable<Void> loadGameScreenEvent = new Observable<Void>();

    private Table table;
    private TextButton quitButton;
    private TextButton playButton;
    private TextButton continueButton;
    private TextButton loadGameButton;
    private Dialog confirmQuitDialog;

    public MainMenuScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        // Setup GUI
        table = new Table();
        stage.addActor(table);

        // Initialize buttons
        quitButton = new TextButton("Quit", skin);
        playButton = new TextButton("New Game", skin);
        continueButton = new TextButton("Continue Game", skin);
        loadGameButton = new TextButton("Load Game", skin);
        // Initialize confirm to quit dialog box
        confirmQuitDialog = new Dialog("Confirm Quit", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    // Save players before quitting TODO IMPLEMENT SAVING
                    // main.saveProfiles();
                    Gdx.app.exit();
                }
            }
        };
        confirmQuitDialog.text("Are you sure you want to quit?");
        confirmQuitDialog.button("Yes", true);
        confirmQuitDialog.button("No", false);

        // Layout GUI
        table.setFillParent(true); // Size table to stage
        table.add(playButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(continueButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(loadGameButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(quitButton).fillX();


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
                startGameEvent.notifyObservers(null);
            }
        });
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                continueGameEvent.notifyObservers(null);
            }
        });
        loadGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadGameScreenEvent.notifyObservers(null);
            }
        });

    }

    void addStartGameListener(Observer<Void> ob) { startGameEvent.addObserver(ob); }
    void addContinueGameListener(Observer<Void> ob) { continueGameEvent.addObserver(ob); }
    void addLoadGameListener(Observer<Void> ob) { loadGameScreenEvent.addObserver(ob); }
}
