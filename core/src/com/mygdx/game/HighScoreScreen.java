package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.steer.behaviors.Alignment;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Screen to display the high score list in the menu
 */
public class HighScoreScreen extends GameScreen {
    private Observable<Void> menuEvent = new Observable<Void>();

    private Table table;

    private Table highScoreTable;

    private Table lifetimeHighScoreTable;


    private Label title;

    private Label highScoreTitle;

    private Label lifetimeHighScoreTitle;

    private Button menuButton;
    private Table scoreTable; // Displays the stats of each player in the game


    private ProfileManager profileManager;

    private List<PlayerProfile> profileList;

    /**
     * Constructor.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public HighScoreScreen(SpriteBatch batch, AssetManager assets, ProfileManager profileManager) {

        super(batch, assets);

        // Clear any previously loaded data
        stage.clear();
        this.table = new Table();
        this.highScoreTable = new Table();           // Highest individual game scores
        this.lifetimeHighScoreTable = new Table();  // Highest overall lifetime scores

        // Setup GUI
        stage.addActor(this.table);
        this.table.setFillParent(true);  // Size full table to stage
        this.table.defaults().pad(10);
        this.highScoreTable.defaults().pad(10);
        this.lifetimeHighScoreTable.defaults().pad(10);

        // Add screen title
        this.title = new Label("High Score Screen", skin);
        this.title.setAlignment(Align.center);
        this.table.add(this.title).colspan(2).fillX();
        this.table.row();

        // Add high score table headers
        this.highScoreTitle = new Label("Individual Game Score", skin);
        this.highScoreTitle.setAlignment(Align.center);
        this.highScoreTable.add(this.highScoreTitle).colspan(3).fillX();
        this.highScoreTable.row();

        this.highScoreTable.add(new Label("Ranking", skin));
        this.highScoreTable.add(new Label("Student Name", skin));
        this.highScoreTable.add(new Label("Score", skin));
        this.highScoreTable.row();

        this.lifetimeHighScoreTitle = new Label("Lifetime Score", skin);
        this.lifetimeHighScoreTitle.setAlignment(Align.center);
        this.lifetimeHighScoreTable.add(this.lifetimeHighScoreTitle).colspan(3).fillX();
        this.lifetimeHighScoreTable.row();

        this.lifetimeHighScoreTable.add(new Label("Ranking", skin));
        this.lifetimeHighScoreTable.add(new Label("Student Name", skin));
        this.lifetimeHighScoreTable.add(new Label("Score", skin));
        this.lifetimeHighScoreTable.row();


        // Get high score lists
        this.profileManager = profileManager;
        ArrayList<PlayerProfile> highScoreList = this.profileManager.getHighScoreList();
        ArrayList<PlayerProfile> lifetimeHighScoreList = this.profileManager.getLifetimeHighScoreList();

        // Display each individual game high score
        int highScoreRanking = 1;
        for (PlayerProfile studentProfile : highScoreList) {

            Label rankingLabel = new Label(Integer.toString(highScoreRanking), skin);
            Label studentNameLabel = new Label(studentProfile.getName(), skin);
            Label highScoreLabel = new Label(Integer.toString(studentProfile.getHighScore()), skin);

            this.highScoreTable.add(rankingLabel);
            this.highScoreTable.add(studentNameLabel);
            this.highScoreTable.add(highScoreLabel);
            this.highScoreTable.row();

            highScoreRanking++;

        }

        // Display each overall lifetime high score
        int lifetimeScoreRanking = 1;
        for (PlayerProfile studentProfile : lifetimeHighScoreList) {

            Label rankingLabel = new Label(Integer.toString(lifetimeScoreRanking), skin);
            Label studentNameLabel = new Label(studentProfile.getName(), skin);
            Label highScoreLabel = new Label(Integer.toString(studentProfile.getHighScore()), skin);

            this.lifetimeHighScoreTable.add(rankingLabel);
            this.lifetimeHighScoreTable.add(studentNameLabel);
            this.lifetimeHighScoreTable.add(highScoreLabel);
            this.lifetimeHighScoreTable.row();

            lifetimeScoreRanking++;

        }

        // Display each high score table
        this.table.add(this.highScoreTable);
        this.table.add(this.lifetimeHighScoreTable);
        this.table.row();

        // Create and display buttons
        this.menuButton = new TextButton("Back", skin);
        this.table.add(this.menuButton);

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
     * Update the list of profiles to generate high score table from
     *
     * @param profiles list of profiles to copy and use
     */
    public void setProfileList(List<PlayerProfile> profiles) {
        this.profileList = new ArrayList<>(profiles);

        // Setup score table
        scoreTable.clear();

        // Add column headers to the table
        scoreTable.add(new Label("#", skin)).padRight(5);
        scoreTable.add(new Label("Name", skin)).padLeft(50).padRight(50);
        scoreTable.add(new Label("High Score", skin)).padRight(25);
        scoreTable.row();

        profileList.sort((o1, o2) -> o1.getHighScore() - o2.getHighScore()); // Sort profile list by high score
        for (int i = 0; i < profileList.size(); i++) {
            scoreTable.add(new Label((i + 1) + ".", skin)).padRight(5);
            scoreTable.add(new Label(profileList.get(i).getName(), skin)).padLeft(50).padRight(50);
            scoreTable.add(new Label(String.valueOf(profileList.get(i).getHighScore()), skin)).padRight(25);
            scoreTable.row();
        }

        // Layout GUI
        table.clear();
        table.add(menuButton).top().left();
        table.row();
        table.add(scoreTable).center().expand();
    }

    public void addMenuListener(Observer<Void> ob) { menuEvent.addObserver(ob); }
}