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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * The main menu screen. Handles all related functions including loading a save game and starting a new game.
 */
public class MainMenuScreen extends GameScreen {
    private Observable<Void> startGameEvent = new Observable<Void>();
    private Observable<Void> continueGameEvent = new Observable<Void>();
    private Observable<Void> instructorDashboardEvent = new Observable<Void>();
    private Observable<Void> loadGameScreenEvent = new Observable<Void>();
    private Observable<Void> highScoreScreenEvent = new Observable<Void>();
    private Observable<Void> debugEvent = new Observable<>();
    private Observable<Void> tutorialScreenEvent = new Observable<>();

    private Texture backgroundAni;
    private int width = 1920;
    private int height = 1080;
    private Table table;
    private Table devs;
    private Table background;
    private TextButton quitButton;
    private TextButton playButton;
    private TextButton continueButton;
    private TextButton instructorDashboardButton;
    private TextButton loadGameButton;
    private TextButton highScoreButton;
    private TextButton tutorialButton;
    private Button debugButton;

    private Dialog confirmQuitDialog;
    private Dialog instructorPasswordDialog;
    private Dialog debugDialog;


    /**
     * Constructor.
     *
     * @param batch the SpriteBatch used for rendering
     * @param assets the AssetManager loaded with all necessary assets
     */
    public MainMenuScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);
        int textWidth = 100;

        // Setup GUI
        background = new Table();
        background.setFillParent(true);
        table = new Table();
        devs = new Table();

        //UI for creator bio
        Label earl = new Label("Name: Earl Castillo\nEmail: ecastil3@uwo.ca", skin);
        Label joelene = new Label("Name: Joelene Hales\nEmail: jhales5@uwo.ca", skin);
        Label kevin = new Label("Name: Kevin Chen\nEmail: kchen546@uwo.ca", skin);
        Label frank = new Label("Name: Franck Limtung\nEmail: flimtung@uwo.ca", skin);
        Label karmen = new Label("Name: Karmen Minhas\nEmail: kminhas7@uwo.ca",skin);
        Label course = new Label("Created as part of CS2212 Final Project at Western University", skin);
        Label term = new Label("Term Created: Winter Term 2024", skin);
        Label about = new Label("About This Project: ", skin, "menu");
        Label blank = new Label(" ", skin);
        Label title = new Label("Navigating the Fiscal Frontier", skin, "menu");
        title.setColor(0.27f, 0.79f, 0.53f, 1);
        title.setFontScale(2);
        devs.add(about).width(textWidth).center();
        devs.row();
        devs.add(blank);
        devs.row();
        devs.add(course).width(textWidth).left();
        devs.row();
        devs.add(term).width(textWidth).left();
        devs.row();
        devs.add(earl).left();
        devs.add(blank);
        devs.add(karmen).left();
        devs.add(blank);
        devs.add(joelene).left();
        devs.add(blank);
        devs.add(frank).left();
        devs.add(blank);
        devs.add(kevin).left();
        for (Actor actor : devs.getChildren().toArray()) {
            Label label = (Label)actor;
            label.setColor(1, 1, 1, 1);
        }

        // Initialize buttons
        quitButton = new TextButton("Quit", skin);
        playButton = new TextButton("New Game", skin);
        continueButton = new TextButton("Continue Game", skin);
        loadGameButton = new TextButton("Load Game", skin);
        highScoreButton = new TextButton("High Scores", skin);
        instructorDashboardButton = new TextButton("Instructor Dashboard", skin);
        tutorialButton = new TextButton("Game Tutorial", skin);
        debugButton = new Button(skin);
        debugButton.setSize(75, 75);
        debugButton.setPosition(1700, 100);
        debugButton.setColor(1, 1, 1 ,0.2f);

        // Initialize confirm to quit dialog box
        confirmQuitDialog = new Dialog("Confirm Quit", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    Gdx.app.exit(); // Quit the game
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
        instructorPasswordDialog.getContentTable().add(password).width(300);

        Label invalidPasswordText = new Label("", skin);  // Placeholder for text displayed if incorrect password is etered
        instructorPasswordDialog.getContentTable().row();
        instructorPasswordDialog.getContentTable().add(invalidPasswordText);

        TextButton passwordConfirm = new TextButton("Confirm", skin);
        TextButton passwordCancel = new TextButton("Cancel", skin);

        instructorPasswordDialog.getButtonTable().add(passwordConfirm);
        instructorPasswordDialog.getButtonTable().add(passwordCancel);

        passwordConfirm.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {

            String inputPassword = password.getText();

            if (inputPassword.equals(Config.getInstance().getInstructorPassword())) {  // Correct password entered
                instructorPasswordDialog.hide();
                instructorDashboardEvent.notifyObservers(null);
            }
            else {  // Incorrect password entered
                invalidPasswordText.setText("Incorrect password. Please try again.");  // Display error message
            }
            }
        });

        passwordCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                password.setText("");  // Reset any entered text
                instructorPasswordDialog.hide();
            }
        });

        assets.load("stock2.jpg", Texture.class);
        assets.finishLoading();

        backgroundAni = assets.get("stock2.jpg");
        Image backgroundImage = new Image(backgroundAni);
        backgroundImage.setSize(width, height);
        stage.addActor(backgroundImage);

        // Layout GUI
        table.row().pad(10, 0, 10, 0);
        table.add(playButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(continueButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(loadGameButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(highScoreButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(instructorDashboardButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(tutorialButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(quitButton).fillX();

        //Adding to background
        background.add(title).top().center().expandY();
        background.row();
        background.add(table).center().expandY();
        background.row();
        background.add(devs).bottom().expandY();

        stage.addActor(background);
        stage.addActor(debugButton);

        // Add button listeners
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
        highScoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                highScoreScreenEvent.notifyObservers(null);
            }
        });
        tutorialButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tutorialScreenEvent.notifyObservers(null);
            }
        });

        // Setup debug button
        debugButton.addListener(new ChangeListener() {
            @Override
            public void changed(com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent event, Actor actor) {
                debugDialog.show(stage);
            }
        });
        // Initialize debug password prompt dialog
        debugDialog = new Dialog("DEBUG", skin);
        debugDialog.text("ENTER PASSWORD:");
        TextField debugPassField = new TextField("", skin);
        debugDialog.getContentTable().add(debugPassField);
        TextButton debugContinueButton = new TextButton("Continue", skin);
        debugContinueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                // Check if the password matches the debug password
                String inputPassword = debugPassField.getText();
                if (inputPassword.equals(Config.getInstance().getDebugPassword())) {
                    debugButton.setVisible(false);
                    debugEvent.notifyObservers(null);
                    debugDialog.hide();
                }
                else {
                    Utility.showErrorDialog("Incorrect password. Please try again.", stage, skin);
                }
            }
        });
        debugDialog.button("Cancel");
        debugDialog.getButtonTable().add(debugContinueButton);
    }

    // Listener setters
    public void addStartGameListener(Observer<Void> ob) { startGameEvent.addObserver(ob); }
    public void addContinueGameListener(Observer<Void> ob) { continueGameEvent.addObserver(ob); }
    public void addInstructorDashboardListener(Observer<Void> ob) { instructorDashboardEvent.addObserver(ob); }
    public void addLoadGameListener(Observer<Void> ob) { loadGameScreenEvent.addObserver(ob); }
    public void addHighScoreListener(Observer<Void> ob) { highScoreScreenEvent.addObserver(ob); }
    public void addTutorialScreenListener(Observer<Void> ob) {tutorialScreenEvent.addObserver(ob);}
    public void addDebugListener(Observer<Void> ob) { debugEvent.addObserver(ob); }
}
