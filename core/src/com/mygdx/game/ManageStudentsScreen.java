package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Manage students mode in the instructor dashboard. Provides a user interface for instructors to manage their class.
 * <br><br>
 * Includes options to add, edit, or remove student profiles. Any changes made will be written to the student database
 * and synced across the application via the profile manager.
 * <br><br>
 *
 * <b>Adding student profiles:</b>
 * New student profiles can be created by selecting "Add Student." A dialog box will prompt the instructor to enter a
 * name for the new student. The student's name must not be taken by an existing student. The new student profile will be
 * created with a knowledge level of 1 and both high scores set to 0.
 * <br>
 *
 * <b>Editing student profiles:</b>
 * Instructors can change the name or knowledge level of existing students by clicking on the student they wish to edit
 * and selecting "Edit Student." A dialog box will prompt the instructor to enter a new name and/or knowledge level for
 * the selected student. If a new name is entered, it must not be taken by an existing student. If a knowledge level is
 * entered, it must be a positive integer.
 * <br>
 *
 * <b>Removing student profiles:</b>
 * Existing student profiles can be deleted by clicking on the student they wish to remove and selecting "Remove
 * Student." A dialog box will prompt the instructor for confirmation to delete the selected student.
 * <br>
 *
 * @see PlayerProfile
 * @see ProfileManager
 * @see InstructorDashboardScreen
 *
 * @author Joelene Hales
 */
public class ManageStudentsScreen extends GameScreen {

    /** Event exits manage students mode and returns to the instructor dashboard. */
    private final Observable<Void> instructorDashboardEvent = new Observable<Void>();
    /** Event adds a new student to the database. */
    private final Observable<String> addStudentEvent = new Observable<String>();
    /** Event edits an existing student's name and knowledge level in the database. */
    private final Observable<String> editStudentEvent = new Observable<String>();
    /** Event removes a student from the database. */
    private final Observable<String> removeStudentEvent = new Observable<String>();


    /** Responsible for storing and managing student profiles. */
    private final ProfileManager profileManager;
    /** List of all student profiles. */
    private ArrayList<PlayerProfile> studentProfiles;


    /** Buttons to select each student to be edited or removed. */
    private ArrayList<TextButton> studentButtons = new ArrayList<TextButton>();
    /** Button to edit the name and knowledge level of an existing student.<br><br>
     *  Must first select an existing student to enable. */
    private TextButton editStudentButton;
    /** Button to remove an existing student.<br><br>
     *  Must first select an existing student to enable. */
    private TextButton removeStudentButton;
    /** Button to confirm and submit edited student information. */
    private TextButton editStudentConfirm;
    /** Button to confirm and submit removing a student. */
    private TextButton removeStudentConfirm;


    /** Width of dialog prompts. */
    private static int dialogWidth;
    /** Height of dialog prompts. */
    private static int dialogHeight;
    /** Maximum width of text within dialog prompts. */
    private static int textWidth;


    /** Dialog prompts to enter student name when adding a new student. */
    private Dialog addStudentDialog;
    /** Dialog prompts to enter new name and knowledge level when editing a student's information. */
    private Dialog editStudentDialog;
    /** Dialog prompts for confirmation to delete a student. */
    private Dialog removeStudentDialog;


    /** Text input field used to enter a new name for an existing student. */
    private TextField editNameInput;
    /** Text input field used to enter a new knowledge level for an existing student. */
    private TextField editKnowledgeLevelInput;
    /** Displays error when invalid input has been entered when editing student information. */
    private Label editSubtext;
    /** Text displayed in dialog prompt for confirmation to delete student. */
    private Label removeStudentNameText;
    /** Displays error when student was unable to be removed. */
    private Label removeSubtext;


    /**
     * Constructor initializes the manage students screen's assets.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     * @param profileManager ProfileManager responsible for storing and managing student profiles and high scores
     */
    public ManageStudentsScreen(SpriteBatch batch, AssetManager assets, ProfileManager profileManager) {

        super(batch, assets);
        this.profileManager = profileManager;

        // Define width and height of dialog boxes and associated text
        dialogWidth = 650;
        dialogHeight = 350;
        textWidth = dialogWidth - 75;

        this.loadDashboard();  // Display buttons

    }


    /**
     * Initializes the dashboard GUI.
     */
    public void loadDashboard() {

        // Clear any previously loaded data
        this.stage.clear();
        Table table = new Table();          // Stores all GUI elements
        Table studentTable = new Table();   // Displays all existing students
        Table buttonTable = new Table();    // Contains buttons for action options, for nicer formatting

        this.studentProfiles = this.profileManager.getStudentProfiles();  // List of all student profiles
        this.studentButtons = new ArrayList<TextButton>();

        // Setup GUI
        this.stage.addActor(table);
        table.setFillParent(true);  // Size table to stage
        table.defaults().pad(10);
        studentTable.defaults().pad(10);
        buttonTable.defaults().pad(10);

        // Add screen title
        Label title = new Label("Manage Students", this.skin);
        title.setAlignment(Align.center);
        table.add(title).colspan(5);
        table.row();

        Label subtitle = new Label("Select a student to edit or remove:", this.skin);
        title.setAlignment(Align.left);
        table.add(subtitle).colspan(3);
        table.row();

        // Initialize buttons
        /** Button to add a new student. */
        TextButton addStudentButton = new TextButton("Add Student", this.skin);         // Add a new student
        this.editStudentButton = new TextButton("Edit Student", this.skin);       // Edit existing student name and knowledge level
        this.removeStudentButton = new TextButton("Remove Student", this.skin);   // Remove existing student
        /** Button to exit manage students mode.  */
        TextButton backButton = new TextButton("Back", this.skin);                      // Return to instructor dashboard

        this.editStudentButton.setTouchable(Touchable.disabled);    // Disable clicking until a student is selected
        this.editStudentButton.setColor(Color.GRAY);
        this.removeStudentButton.setTouchable(Touchable.disabled);
        this.removeStudentButton.setColor(Color.GRAY);

        // Initialize dialog prompt to add a new student
        this.addStudentDialog = new Dialog("Add Student", this.skin);
        this.addStudentDialog.text("Enter student name:");
        this.addStudentDialog.getContentTable().row();

        TextField studentNameInput = new TextField("", skin);
        this.addStudentDialog.getContentTable().add(studentNameInput);
        this.addStudentDialog.getContentTable().row();

        Label addSubtext = new Label("", skin);  // Placeholder for text displayed when an invalid input is entered
        addSubtext.setWrap(true);
        addSubtext.setAlignment(Align.center);
        this.addStudentDialog.getContentTable().add(addSubtext).width(textWidth);

        TextButton addStudentConfirm = new TextButton("Confirm", skin);
        TextButton addStudentCancel = new TextButton("Back", skin);

        this.addStudentDialog.getButtonTable().add(addStudentConfirm);
        this.addStudentDialog.getButtonTable().add(addStudentCancel);

        addStudentConfirm.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {

                String studentName = studentNameInput.getText();   // Entered name for new student

                try {

                    // Ensure name is not null
                    if (studentName.isEmpty()) {
                        throw new IllegalArgumentException("Must enter a student name.");
                    }

                    // Ensure a  student with the same name does not already exist
                    for (PlayerProfile student : studentProfiles) {
                        if (student.getName().equals(studentName)) {
                            throw new IllegalArgumentException("Student with this name already exists.");
                        }
                    }

                    // Add the student to the database
                    addStudentEvent.notifyObservers(studentNameInput.getText());

                }
                catch (IllegalArgumentException e) {
                    addSubtext.setText(e.getMessage());  // Display error message
                }

            }
        });
        addStudentCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                addStudentDialog.hide();
            }
        });


        // Initialize dialog to edit student's name and knowledge level
        this.editStudentDialog = new Dialog("Edit Student", this.skin);

        this.editStudentDialog.text("Edit student information:").align(Align.left);
        this.editStudentDialog.getContentTable().row();

        this.editStudentDialog.text("Name:").align(Align.left);
        this.editNameInput = new TextField("", this.skin);
        this.editStudentDialog.getContentTable().add(this.editNameInput).width(200);
        this.editStudentDialog.getContentTable().row();

        this.editStudentDialog.text("Knowledge Level:").align(Align.left);
        this.editKnowledgeLevelInput = new TextField("", this.skin);
        this.editStudentDialog.getContentTable().add(this.editKnowledgeLevelInput);
        this.editStudentDialog.getContentTable().row();

        this.editSubtext = new Label("", this.skin);  // Placeholder for text displayed when  if an invalid input is entered
        this.editSubtext.setWrap(true);
        this.editSubtext.setAlignment(Align.center);
        this.editStudentDialog.getContentTable().add(this.editSubtext).colspan(2).width(textWidth);

        this.editStudentConfirm = new TextButton("Confirm", this.skin);
        TextButton editStudentCancel = new TextButton("Back", this.skin);

        this.editStudentDialog.getButtonTable().add(this.editStudentConfirm);
        this.editStudentDialog.getButtonTable().add(editStudentCancel);

        editStudentCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                editStudentDialog.hide();
            }
        });


        // Initialize dialog to confirm deleting a student
        this.removeStudentDialog = new Dialog("Remove Student", this.skin);
        Label removePrompt = new Label("Are you sure you would like to delete this student?\n", this.skin);
        removePrompt.setWrap(true);
        this.removeStudentDialog.getContentTable().add(removePrompt).width(textWidth);
        this.removeStudentDialog.getContentTable().row();
        removeStudentNameText = new Label("", this.skin);  // Placeholder for student's name in confirmation text
        removeStudentNameText.setAlignment(Align.center);
        removeStudentNameText.setWrap(true);
        this.removeStudentDialog.getContentTable().add(removeStudentNameText).width(textWidth);
        this.removeStudentDialog.getContentTable().row();

        this.removeSubtext = new Label("", this.skin);  // Placeholder for text displayed when  an invalid input is entered
        this.removeSubtext.setWrap(true);
        this.removeSubtext.setAlignment(Align.center);
        this.removeStudentDialog.getContentTable().add(this.removeSubtext).width(textWidth);

        this.removeStudentConfirm = new TextButton("Confirm", this.skin);
        TextButton removeStudentCancel = new TextButton("Cancel", this.skin);

        this.removeStudentDialog.getButtonTable().add(this.removeStudentConfirm);
        this.removeStudentDialog.getButtonTable().add(removeStudentCancel);

        removeStudentCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                removeStudentDialog.hide();
            }
        });


        // Create buttons to select each existing student to edit or remove
        for (PlayerProfile student : this.studentProfiles) {
            this.newStudentButton(student);
        }


        // Add button listeners
        addStudentButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                // Prompt to enter name of new student
                addStudentDialog.show(stage).setSize(dialogWidth, dialogHeight);
                addStudentDialog.setPosition(stage.getWidth()/2, stage.getHeight()/2, Align.center);  // Center in the middle of the screen

            }
        });
        this.editStudentButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                // Prompt to enter new information for selected student
                editStudentDialog.show(stage).setSize(dialogWidth, dialogHeight);
                editStudentDialog.setPosition(stage.getWidth()/2, stage.getHeight()/2, Align.center);  // Center in the middle of the screen
            }
        });
        this.removeStudentButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                // Prompt for confirmation before removing student
                removeStudentDialog.show(stage).setSize(dialogWidth, dialogHeight);
                removeStudentDialog.setPosition(stage.getWidth()/2, stage.getHeight()/2, Align.center);  // Center in the middle of the screen

            }
        });
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                instructorDashboardEvent.notifyObservers(null);  // Return to instructor dashboard
            }
        });


        // Display each student
        for (int i = 0; i < this.studentButtons.size(); i++) {

            TextButton button = this.studentButtons.get(i);
            studentTable.add(button);

            if ((i+1) % 3 == 0) {  // Line down every 3 students
                studentTable.row();
            }

        }

        // Enable scrolling
        ScrollPane scroll = new ScrollPane(studentTable, this.skin);
        scroll.setScrollBarPositions(false, true);
        scroll.setScrollbarsOnTop(false);
        scroll.setScrollbarsVisible(true);
        table.add(scroll).colspan(3).fillX().expand();
        table.row();

        // Display each action button
        buttonTable.add(addStudentButton);
        buttonTable.add(this.editStudentButton);
        buttonTable.add(this.removeStudentButton);
        buttonTable.add(backButton);
        table.add(buttonTable).colspan(3).expandX();

        // Setup keyboard shortcuts
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    instructorDashboardEvent.notifyObservers(null);  // Return to instructor dashboard
                    return true;
                }
                return false;
            }

        });

    }


    /**
     * Create a button on the dashboard linked to a student. Used to select the student to edit or remove.
     *
     * @param student Student's profile.
     */
    private void newStudentButton(PlayerProfile student) {

        // Retrieve student's current information
        String studentName = student.getName();
        int knowledgeLevel = student.getKnowledgeLevel();

        // Create button for the student
        String displayText =  studentName + "\n (Knowledge Level: " + Integer.toString(knowledgeLevel) + ")";  // Text displayed in the student's button
        TextButton button = new TextButton(displayText, this.skin);
        this.studentButtons.add(button);  // Add button to list of buttons

        // Link edit and remove buttons to the student when selected
        button.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                // Allow edit and remove buttons to be clicked once student is selected
                editStudentButton.setTouchable(Touchable.enabled);
                editStudentButton.setColor(Color.WHITE);
                removeStudentButton.setTouchable(Touchable.enabled);
                removeStudentButton.setColor(Color.WHITE);

                // Set remove student confirmation to display the name of the selected player
                removeStudentNameText.setText(studentName + "\n");

                // Fill edit information fields with student's current name and knowledge level
                editNameInput.setText(studentName);
                editKnowledgeLevelInput.setText(Integer.toString(knowledgeLevel));

                // Link edit and remove buttons to the selected student
                editStudentConfirm.addListener(new ChangeListener() {

                    @Override
                    public void changed(ChangeEvent event, Actor actor) {

                        // Unpack input
                        String newName = editNameInput.getText();
                        String newKnowledgeLevel = editKnowledgeLevelInput.getText();

                        try {

                            // Prevent errors caused by renaming a student currently in a saved game
                            if (inGame(studentName)) {
                                throw new IllegalArgumentException("Cannot edit a student that is currently in a game. Please try again once game has finished.");
                            }

                            // Handle empty inputs by replacing with current values
                            if (newName.isEmpty()) {
                                newName = studentName;
                            }
                            if (newKnowledgeLevel.isEmpty()) {
                                newKnowledgeLevel = Integer.toString(knowledgeLevel);
                            }


                            // Ensure a valid knowledge level was entered
                            int inputCheck = Integer.parseInt(newKnowledgeLevel);  // Integer value

                            if (inputCheck < 0) {  // Non-negative integer
                                throw new NumberFormatException();
                            }

                            // Ensure a different student with the same name does not already exist
                            if (!(newName.equals(studentName))) {
                                for (PlayerProfile student : studentProfiles) {
                                    if (student.getName().equals(newName)) {
                                        throw new IllegalArgumentException("Student with this name already exists.");
                                    }
                                }
                            }

                            // Update button to display new name
                            int index =  studentButtons.indexOf(button);
                            studentButtons.get(index).setText(newName);

                            // Update student database
                            String[] studentData = {studentName, newName, newKnowledgeLevel};
                            editStudentEvent.notifyObservers(Arrays.toString(studentData));

                            editSubtext.setText("");  // Clear any error messages that occurred

                        }
                        catch (NumberFormatException e) {
                            editSubtext.setText("Invalid knowledge level. Must be a non-negative integer.");  // Display error message
                        }
                        catch (IllegalArgumentException e) {
                            editSubtext.setText(e.getMessage());  // Display error message
                        }

                    }
                });

                removeStudentConfirm.addListener(new ChangeListener() {

                    @Override
                    public void changed(ChangeListener.ChangeEvent event, Actor actor) {

                        try {

                            // Prevent errors caused by renaming a student currently in a saved game
                            if (inGame(studentName)) {
                                throw new IllegalArgumentException("Cannot delete a student that is currently in a game. Please try again once game has finished.");
                            }

                            studentButtons.remove(button);                    // Remove button from dashboard
                            removeStudentEvent.notifyObservers(studentName);  // Remove student from database
                            removeSubtext.setText("");                        // Clear any error messages that occurred

                        }
                        catch (IllegalArgumentException e) {
                            removeSubtext.setText(e.getMessage());  // Display error message
                        }

                    }
                });
            }

        });

    }


    /**
     * Displays an action confirmation message.
     *
     * @param message Confirmation message to display.
     */
    public void showConfirmation(String message) {

        // Create dialog, set size and position
        Dialog actionConfirmation = new Dialog("Confirmation", this.skin);
        actionConfirmation.setSize(dialogWidth, dialogHeight);
        actionConfirmation.setPosition(this.stage.getWidth()/2, this.stage.getHeight()/2, Align.center);  // Center in the middle of the screen

        // Populate with given confirmation message
        actionConfirmation.text(message);
        actionConfirmation.getContentTable().row();

        // Add button to hide dialog box
        TextButton hideButton = new TextButton("Back", this.skin);
        actionConfirmation.getButtonTable().add(hideButton);

        actionConfirmation.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actionConfirmation.hide();
            }
        });

        this.stage.addActor(actionConfirmation);  // Display dialog

    }


    /**
     * Checks if the student is currently in a saved game.
     *
     * @param studentName Student's name
     * @return True if the student is currently in a game, false if otherwise
     */
    private boolean inGame(String studentName) {

        // Open folder of saves
        File saveFolder = new File("saves");
        File[] fileList = saveFolder.listFiles();

        if (fileList != null) {  // Saved games exist

            // Check each saved game
            SaveSystem saves = new SaveSystem();
            for (File file : fileList) {

                GameState gs = saves.readGameState(file.getName(), this.assets);  // Load game state

                // Check each player in the game save file
                for (Player player : gs.getPlayerList()) {
                    if (player.getPlayerProfile().getName().equals(studentName)) {  // Student found in game
                        return true;
                    }

                }
            }

        }

        return false;  // No saved games exist or checked all save files and student not found

    }



    /**
     * Assigns an observer to listen for the event to return to the instructor dashboard.
     * @param ob Observer.
     */
    public void addBackListener(Observer<Void> ob) { this.instructorDashboardEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to add a new student.
     * @param ob Observer.
     */
    public void addAddStudentListener(Observer<String> ob) { this.addStudentEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to edit an existing student.
     * @param ob Observer.
     */
    public void addEditStudentListener(Observer<String> ob) { this.editStudentEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to remove a student.
     * @param ob Observer.
     */
    public void addRemoveStudentListener(Observer<String> ob) { this.removeStudentEvent.addObserver(ob); }

}
