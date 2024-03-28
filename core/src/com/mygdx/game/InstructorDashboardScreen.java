package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;
import java.util.ArrayList;

/**
 * Instructor dashboard screen displays metrics to track student progress.
 * <br><br>
 * Metrics include the student's highest achieved individual game score, overall lifetime score (the cumulative score
 * from all played games), their knowledge level, and the number of tips they have unlocked. From this dashboard,
 * instructors can enter Manage Students mode to add, edit, and remove student profiles.
 * @see ManageStudentsScreen
 * @see PlayerProfile
 *
 * @author Joelene Hales
 */
public class InstructorDashboardScreen extends GameScreen{

    /** Event returns to main menu. */
    private final Observable<Void> menuEvent = new Observable<Void>();
    /** Event enters manage students mode. */
    private final Observable<Void> manageStudentsEvent = new Observable<Void>();
    /** Object responsible for storing and managing student profiles. */
    private ProfileManager profileManager;


    /**
     * Constructor initializes the instructor dashboard's assets.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     * @param profileManager ProfileManager responsible for storing and managing student profiles and high scores
     */
    public InstructorDashboardScreen(SpriteBatch batch, AssetManager assets, ProfileManager profileManager) {

        super(batch, assets);
        this.profileManager = profileManager;

        this.loadDashboard();   // Load student information and initialize buttons

    }

    /**
     * Initializes the dashboard GUI and displays all student information.
     */
    public void loadDashboard() {

        // Clear any previously loaded data
        stage.clear();
        Table table = new Table();          // Stores all GUI elements
        Table studentTable = new Table();   // Table displaying all student information
        Table buttonTable = new Table();    // Table containing buttons only, for nicer formatting

        // Setup GUI
        stage.addActor(table);
        table.setFillParent(true);  // Size table to stage
        table.defaults().pad(10);
        studentTable.defaults().pad(10);
        buttonTable.defaults().pad(10);

        // Add screen title
        Label title = new Label("Instructor Dashboard", skin, "menu");
        title.setAlignment(Align.center);
        table.add(title).colspan(5);
        table.row();

        // Add header to student information table
        studentTable.add(new Label("Student Name", skin));
        studentTable.add(new Label("High Score", skin));
        studentTable.add(new Label("Lifetime Score", skin));
        studentTable.add(new Label("Knowledge Level", skin));
        studentTable.add(new Label("Tips Unlocked", skin));
        studentTable.row();

        // Load student information
        ArrayList<PlayerProfile> studentProfiles = this.profileManager.getStudentProfiles();

        // Display student information
        for (PlayerProfile studentProfile : studentProfiles) {

            // Create text for each field
            Label studentName = new Label(studentProfile.getName(), skin);
            Label highScore = new Label(Integer.toString(studentProfile.getHighScore()), skin);
            Label lifetimeScore = new Label(Integer.toString(studentProfile.getLifetimeScore()), skin);
            Label knowledgeLevel = new Label(Integer.toString(studentProfile.getKnowledgeLevel()), skin);
            Label tipsUnlocked = new Label(Integer.toString(studentProfile.getTipCount()), skin);

            // Display each field
            studentTable.add(studentName);
            studentTable.add(highScore);
            studentTable.add(lifetimeScore);
            studentTable.add(knowledgeLevel);
            studentTable.add(tipsUnlocked);
            studentTable.row();

        }

        // Enable scrolling
        ScrollPane scroll = new ScrollPane(studentTable, skin);
        scroll.setScrollBarPositions(false, true);
        scroll.setScrollbarsOnTop(false);
        scroll.setScrollbarsVisible(true);
        table.add(scroll).colspan(5).fillX().expand();
        table.row();

        // Initialize buttons
        TextButton manageStudentsButton = new TextButton("Manage Students", skin);  // Enter manage students mode
        TextButton returnButton = new TextButton("Return to Main Menu", skin);      // Return to the main menu screen

        // Display buttons
        buttonTable.add(manageStudentsButton);
        buttonTable.add(returnButton);
        table.add(buttonTable).colspan(5).expandX();

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


    /**
     * Assigns an observer to listen for the event to return to main menu.
     * @param ob Observer
     */
    void addMenuListener(Observer<Void> ob) { menuEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to enter manage students mode.
     * @param ob Observer
     */
    void addManageStudentsListener(Observer<Void> ob) { manageStudentsEvent.addObserver(ob); }

}
