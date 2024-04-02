package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;


/**
 * The main menu screen. Provides options to start a new game, load an existing game, open the instructor dashboard,
 * view the game tutorial, view the high score table, and quit the application.
 *
 * @author Franck Limtung (flimtung)
 * @author Joelene Hales (jhales5)
 * @author Earl Castillo (ecastil3)
 * @author Kevin Chen (kchen546)
 */
public class MainMenuScreen extends GameScreen {

    /* Events */

    /**
     * Event to start a new game. *
     * @see NewGameScreen
     */
    private Observable<Void> startGameEvent = new Observable<Void>();
    /** Event to resume the most recently saved game state. */
    private Observable<Void> continueGameEvent = new Observable<Void>();
    /** Event to load and resume a previously saved game state. */
    private Observable<Void> loadGameScreenEvent = new Observable<Void>();
    /**
     * Event opens the instructor dashboard
     * @see InstructorDashboardScreen
     */
    private Observable<Void> instructorDashboardEvent = new Observable<Void>();
    /**
     * Event opens the high score table.
     * @see HighScoreScreen
     */
    private Observable<Void> highScoreScreenEvent = new Observable<Void>();
    /** Event enables debug mode.*/
    private Observable<Void> debugEvent = new Observable<>();
    /** Event opens the tutorial screen. */
    private Observable<Void> tutorialScreenEvent = new Observable<>();

    /* Display */

    /** Background animation */
    private Texture backgroundAni;
    /** Screen width. */
    private int width = 1920;
    /** Screen height. */
    private int height = 1080;
    /** Container for all UI elements */
    private Table background;
    /** Contains each button */
    private Table table;
    /** Container for developper information*/
    private Table devs;

    /** Button to quit the application. */
    private TextButton quitButton;
    /** Displays confirmation before exiting the application*/
    private Dialog confirmQuitDialog;
    /** Button to begin a new game.*/
    private TextButton playButton;
    /** Button to resume the most recently saved game. */
    private TextButton continueButton;
    /** Button to load a saved game state. */
    private TextButton loadGameButton;
    /** Button to open the instructor dashboard*/
    private TextButton instructorDashboardButton;
    /** Prompts for password to enter instructor dashboard. */
    private Dialog instructorPasswordDialog;
    /** Button to view high score list. */
    private TextButton highScoreButton;
    /** Button to open game tutorial screen. */
    private TextButton tutorialButton;
    /** Hidden button to enter debug mode. */
    private Button debugButton;
    /** Prompts for password to enter debug mode*/
    private Dialog debugDialog;




    /**
     * Constructor initializes the main menu screen.
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


    /* Event listeners */

    /**
     * Assigns an observer to listen for the event to start a new game.
     * @param ob Observer to listen for the event to start a new game.
     */
    public void addStartGameListener(Observer<Void> ob) { startGameEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to continue the most recently saved game state.
     * @param ob Observer to listen for the event to continue the most recently saved game state.
     */
    public void addContinueGameListener(Observer<Void> ob) { continueGameEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to open the instructor dashboard.
     * @param ob Observer to listen for the event to open the instructor dashboard.
     */
    public void addInstructorDashboardListener(Observer<Void> ob) { instructorDashboardEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to load and resume saved game state.
     * @param ob Observer to listen for the event to load and resume a saved game state.
     */
    public void addLoadGameListener(Observer<Void> ob) { loadGameScreenEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to view high score list screen.
     * @param ob Observer to listen for the event to view high score list screen.
     */
    public void addHighScoreListener(Observer<Void> ob) { highScoreScreenEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to view the game's tutorial.
     * @param ob Observer to listen for the event to view the game tutorial.
     */
    public void addTutorialScreenListener(Observer<Void> ob) {tutorialScreenEvent.addObserver(ob);}

    /**
     * Assigns an observer to listen for the event to enter debug mode.
     * @param ob Observer to listen for the event to enter debug mode.
     */
    public void addDebugListener(Observer<Void> ob) { debugEvent.addObserver(ob); }
}
