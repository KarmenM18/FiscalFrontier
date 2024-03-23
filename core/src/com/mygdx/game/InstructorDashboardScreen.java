package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.util.ArrayList;

/**
 * Initial screen loaded when entering instructor dashboard. Displays student metrics.
 *
 * @author Joelene Hales
 */
public class InstructorDashboardScreen extends GameScreen{

    /** Table storing all GUI buttons. */
    private Table table;
    /** Button to enter manage students mode. */
    private TextButton manageStudentsButton;
    /** Button to return to the main menu screen. */
    private TextButton returnButton;
    /** Event returns to main menu. */
    private Observable<Void> menuEvent = new Observable<Void>();
    /** Event enters manage students mode. */
    private Observable<Void> manageStudentsEvent = new Observable<Void>();

    private ProfileManager profileManager;


    public InstructorDashboardScreen(SpriteBatch batch, AssetManager assets, ProfileManager profileManager) {

        super(batch, assets);
        this.profileManager = profileManager;

        this.loadDashboard();   // Display student information and initialize buttons

    }

    /**
     * Displays all student information and initializes buttons.
     */
    public void loadDashboard() {

        // Clear any previously loaded data
        stage.clear();
        this.table = new Table();

        // Setup GUI
        stage.addActor(table);
        table.setFillParent(true);  // Size table to stage

        // Initialize buttons
        manageStudentsButton = new TextButton("Manage Students", skin);
        returnButton = new TextButton("Return to Main Menu", skin);

        // Add header to student information table
        Label header = new Label("Instructor Dashboard", skin);
        Label studentNameHeader = new Label("Student Name", skin);
        Label highScoreHeader = new Label("High Score", skin);
        Label lifetimeScoreHeader = new Label("Lifetime Score", skin);
        Label knowledgeLevelHeader = new Label("Knowledge Level", skin);
        Label tipsUnlockedHeader = new Label("Tips Unlocked", skin);

        table.add(header).pad(0, 0, 20, 0).uniform();
        table.row().pad(20, 0, 20, 0);

        table.add(studentNameHeader).pad(0, 0, 0, 10).uniform();
        table.add(highScoreHeader).pad(0, 10, 0, 10).uniform();
        table.add(lifetimeScoreHeader).pad(0, 10, 0, 10).uniform();
        table.add(knowledgeLevelHeader).pad(0, 10, 0, 10).uniform();
        table.add(tipsUnlockedHeader).pad(0, 10, 0, 10).uniform();
        table.row().pad(20, 0, 20, 0);

        // Display student information
        ArrayList<PlayerProfile> studentProfiles = this.profileManager.getStudentProfiles();
        for (PlayerProfile studentProfile : studentProfiles) {

            // Create text for each field
            Label studentName = new Label(studentProfile.getName(), skin);
            Label highScore = new Label(Integer.toString(studentProfile.getHighScore()), skin);
            Label lifetimeScore = new Label(Integer.toString(studentProfile.getLifetimeScore()), skin);
            Label knowledgeLevel = new Label(Integer.toString(studentProfile.getKnowledgeLevel()), skin);
            Label tipsUnlocked = new Label(Integer.toString(studentProfile.getTipCount()), skin);

            // Display each field
            table.add(studentName).pad(0, 0, 0, 10).uniform();
            table.add(highScore).pad(0, 10, 0, 10).uniform();
            table.add(lifetimeScore).pad(0, 10, 0, 10).uniform();
            table.add(knowledgeLevel).pad(0, 10, 0, 10).uniform();
            table.add(tipsUnlocked).pad(0, 10, 0, 10).uniform();
            table.row().pad(20, 0, 20, 0);  // Add an extra line

        }

        // Display buttons
        table.row().pad(100, 0, 0, 0);
        table.add(manageStudentsButton).padRight(10);
        table.add(returnButton);

        // Add button listeners
        manageStudentsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                manageStudentsEvent.notifyObservers(null);
            }
        });
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuEvent.notifyObservers(null);
            }
        });

    }

    /** Add listeners for each event handled by the MainGame screen manager. */
    void addMenuListener(Observer<Void> ob) { menuEvent.addObserver(ob); }
    void addManageStudentsListener(Observer<Void> ob) { manageStudentsEvent.addObserver(ob); }

}
