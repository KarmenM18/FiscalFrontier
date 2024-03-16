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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

public class MainMenuScreen extends GameScreen {
    private Observable<Void> startGameEvent = new Observable<Void>();
    private Observable<Void> continueGameEvent = new Observable<Void>();
    private Observable<Void> instructorDashboardEvent = new Observable<Void>();
    private Observable<Void> loadGameScreenEvent = new Observable<Void>();

    private Table table;
    private TextButton quitButton;
    private TextButton playButton;
    private TextButton continueButton;
    private TextButton instructorDashboardButton;
    private TextButton loadGameButton;
    private Dialog confirmQuitDialog;

    private Dialog instructorPasswordDialog;


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
        instructorDashboardButton = new TextButton("Instructor Dashboard", skin);

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


        // Initialize instructor dashboard password prompt dialog box
        instructorPasswordDialog = new Dialog("Instructor Dashboard Password", skin);

        instructorPasswordDialog.text("Enter password:");
        instructorPasswordDialog.getContentTable().row();

        TextField password = new TextField("", skin);
        instructorPasswordDialog.getContentTable().add(password);

        TextButton passwordConfirm = new TextButton("Confirm", skin);
        TextButton passwordCancel = new TextButton("Cancel", skin);

        instructorPasswordDialog.getButtonTable().add(passwordConfirm);
        instructorPasswordDialog.getButtonTable().add(passwordCancel);


        passwordConfirm.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {

                String inputPassword = password.getText();
                String correctPassword = "TEST";  // FIXME: Add a correct password

                if (inputPassword.equals(correctPassword)) {
                    instructorDashboardEvent.notifyObservers(null);}
                else {

                    // Display error message
                    // FIXME: Need this to only display once
                    instructorPasswordDialog.getContentTable().row();
                    instructorPasswordDialog.text("Incorrect password. Please try again.");

                }

            }
        });

        passwordCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                instructorPasswordDialog.hide();
            }
        });


        // Layout GUI
        table.setFillParent(true); // Size table to stage
        table.add(playButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(continueButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(loadGameButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(instructorDashboardButton).fillX();
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
        instructorDashboardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                instructorPasswordDialog.show(stage);
            }
        });

    }

    void addStartGameListener(Observer<Void> ob) { startGameEvent.addObserver(ob); }
    void addContinueGameListener(Observer<Void> ob) { continueGameEvent.addObserver(ob); }
    void addInstructorDashboardListener(Observer<Void> ob) { instructorDashboardEvent.addObserver(ob); }

    void addLoadGameListener(Observer<Void> ob) { loadGameScreenEvent.addObserver(ob); }
}
