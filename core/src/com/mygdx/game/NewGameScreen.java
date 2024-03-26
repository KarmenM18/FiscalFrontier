package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.util.ArrayList;

/**
 * This screen allows selection of the Players for a game and the difficulty
 */
public class NewGameScreen extends GameScreen {

    /** Event returns to main menu. */
    private Observable<Void> menuEvent = new Observable<Void>();
    /** Object responsible for storing and managing student profiles and high scores. */
    private ProfileManager profileManager;


    /**
     * Constructor initializes the high score screen's assets.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public NewGameScreen(SpriteBatch batch, AssetManager assets, ProfileManager profileManager) {

        super(batch, assets);
        this.profileManager = profileManager;

    }

    
    /**
     * Loads student high score data and displays the high score tables.
     */
    public void loadTables() {

        // Clear any previously loaded data
        stage.clear();
        Table table = new Table();                   // Stores all GUI elements. Used to layout the screen
        Table highScoreTable = new Table();          // Highest individual game scores
        Table lifetimeHighScoreTable = new Table();  // Highest overall lifetime scores

        // Setup GUI
        stage.addActor(table);
        table.setFillParent(true);  // Size full table to stage
        table.defaults().pad(10);
        highScoreTable.defaults().pad(10);
        lifetimeHighScoreTable.defaults().pad(10);

        // Add screen title
        Label title = new Label("High Score Screen", skin);
        title.setAlignment(Align.center);
        table.add(title).colspan(2).fillX();
        table.row();

        // Add high score table headers
        Label highScoreTitle = new Label("Individual Game Score", skin);
        highScoreTitle.setAlignment(Align.center);
        highScoreTable.add(highScoreTitle).colspan(3).fillX();
        highScoreTable.row();

        highScoreTable.add(new Label("Ranking", skin));
        highScoreTable.add(new Label("Name", skin));
        highScoreTable.add(new Label("Score", skin));
        highScoreTable.row();

        Label lifetimeHighScoreTitle = new Label("Lifetime Score", skin);
        lifetimeHighScoreTitle.setAlignment(Align.center);
        lifetimeHighScoreTable.add(lifetimeHighScoreTitle).colspan(3).fillX();
        lifetimeHighScoreTable.row();

        lifetimeHighScoreTable.add(new Label("Ranking", skin));
        lifetimeHighScoreTable.add(new Label("Name", skin));
        lifetimeHighScoreTable.add(new Label("Score", skin));
        lifetimeHighScoreTable.row();

        // Load high score lists
        ArrayList<PlayerProfile> highScoreList = this.profileManager.getHighScoreList();
        ArrayList<PlayerProfile> lifetimeHighScoreList = this.profileManager.getLifetimeHighScoreList();

        // Display each individual game high score
        int highScoreRanking = 1;
        for (PlayerProfile studentProfile : highScoreList) {

            Label rankingLabel = new Label(Integer.toString(highScoreRanking), skin);
            Label studentNameLabel = new Label(studentProfile.getName(), skin);
            Label highScoreLabel = new Label(Integer.toString(studentProfile.getHighScore()), skin);

            highScoreTable.add(rankingLabel);
            highScoreTable.add(studentNameLabel);
            highScoreTable.add(highScoreLabel);
            highScoreTable.row();

            highScoreRanking++;

        }

        // Display each overall lifetime high score
        int lifetimeScoreRanking = 1;
        for (PlayerProfile studentProfile : lifetimeHighScoreList) {

            Label rankingLabel = new Label(Integer.toString(lifetimeScoreRanking), skin);
            Label studentNameLabel = new Label(studentProfile.getName(), skin);
            Label highScoreLabel = new Label(Integer.toString(studentProfile.getHighScore()), skin);

            lifetimeHighScoreTable.add(rankingLabel);
            lifetimeHighScoreTable.add(studentNameLabel);
            lifetimeHighScoreTable.add(highScoreLabel);
            lifetimeHighScoreTable.row();

            lifetimeScoreRanking++;

        }

        // Display each high score table
        table.add(highScoreTable);
        table.add(lifetimeHighScoreTable);
        table.row();

        // Create and display buttons
        TextButton menuButton = new TextButton("Back", skin);
        table.add(menuButton);

        // Set shortcuts
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    menuEvent.notifyObservers(null);
                }
                else {
                    return false;
                }

                return true;
            }
        });

        // Add button listeners
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuEvent.notifyObservers(null);
            }
        });

    }


    /**
     * Assigns an observer to listen for the event to return to main menu.
     * @param ob Observer
     */
    public void addMenuListener(Observer<Void> ob) { menuEvent.addObserver(ob); }
}