package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Manage students mode screen in instructor dashboard.
 * <br></br>
 * Includes methods to add new students and edit and remove existing students.
 *
 * @author Joelene Hales
 */
public class ManageStudentsScreen extends GameScreen {

    /** Table storing all GUI buttons. */
    private Table table;
    /** Button to exit manage students mode.  */
    private TextButton backButton;
    /** Button to add a new student. */
    private TextButton addStudentButton;
    /** Buttons to select each student to edit or remove. */
    private ArrayList<TextButton> studentButtons = new ArrayList<TextButton>();
    /** Button to edit a student. */
    private TextButton editStudentButton;
    /** Button to remove a student. */
    private TextButton removeStudentButton;
    /** Event exits manage students mode and returns to the instructor dashboard. */
    private Observable<Void> instructorDashboardEvent = new Observable<Void>();
    /** Dialog prompts to enter student name when adding a new student. */
    private Dialog addStudentDialog;
    /** Dialog prompts to enter new name and knowledge level when editing a student's information.. */
    private Dialog editStudentDialog;
    /** Dialog prompts for confirmation to delete a student. */
    private Dialog removeStudentDialog;
    /** List of student profiles */
    //private ArrayList<PlayerProfile> studentProfiles;
    /** Object responsible for storing and managing student profiles. */
    private ProfileManager profileManager;
    /** Event adds a new student to the database. */
    private Observable<String> addStudentEvent = new Observable<String>();
    /** Event edits an existing student's name and knowledge level in the database. */
    private Observable<String> editStudentEvent = new Observable<String>();
    /** Event removes a student from the database. */
    private Observable<String> removeStudentEvent = new Observable<String>();


    public ManageStudentsScreen(SpriteBatch batch, AssetManager assets, ProfileManager profileManager) {

        super(batch, assets);
        this.profileManager = profileManager;

        this.loadDashboard();

    }

    /** Add listeners for each event. Handled by the MainGame screen manager. */
    void addBackListener(Observer<Void> ob) { instructorDashboardEvent.addObserver(ob); }
    void addAddStudentListener(Observer<String> ob) { addStudentEvent.addObserver(ob); }
    void addEditStudentListener(Observer<String> ob) { editStudentEvent.addObserver(ob); }
    void addRemoveStudentListener(Observer<String> ob) { removeStudentEvent.addObserver(ob); }


    /**
     * Displays all students and initializes buttons.
     */
    public void loadDashboard() {

        ArrayList<PlayerProfile> studentProfiles = this.profileManager.getStudentProfiles();

        // Clear any previously loaded data
        stage.clear();
        this.table = new Table();

        // Setup GUI
        stage.addActor(table);
        table.setFillParent(true);  // Size table to stage


        // Initialize buttons
        for (PlayerProfile student : studentProfiles) {  // Create a button to select each existing student
            TextButton studentButton = new TextButton(student.getName(), skin);
            studentButtons.add(studentButton);
        }
        addStudentButton = new TextButton("Add Student", skin);
        editStudentButton = new TextButton("Edit Student", skin);
        removeStudentButton = new TextButton("Remove Student", skin);
        backButton = new TextButton("Back", skin);

        editStudentButton.setTouchable(Touchable.disabled);    // Disable until a student is selected
        removeStudentButton.setTouchable(Touchable.disabled);  // TODO: Should somehow make these grayed out, so it doesn't look like you can click them


        // Initialize dialog prompt to add a new student
        addStudentDialog = new Dialog("Add Student Dialog", skin);

        addStudentDialog.text("Enter student name:");
        addStudentDialog.getContentTable().row();

        TextField studentNameInput = new TextField("", skin);
        addStudentDialog.getContentTable().add(studentNameInput);
        addStudentDialog.getContentTable().row();

        Label addSubtext = new Label("", skin);  // Placeholder for text displayed when action completed successfully or an invalid input is entered
        addStudentDialog.getContentTable().add(addSubtext);

        TextButton addStudentConfirm = new TextButton("Confirm", skin);
        TextButton addStudentCancel = new TextButton("Back", skin);

        addStudentDialog.getButtonTable().add(addStudentConfirm);
        addStudentDialog.getButtonTable().add(addStudentCancel);

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

                    addStudentEvent.notifyObservers(studentNameInput.getText());
                    addSubtext.setText("Student added successfully.");  // Display confirmation of action

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
        editStudentDialog = new Dialog("Edit Student Dialog", skin);

        editStudentDialog.text("Edit student information");
        editStudentDialog.getContentTable().row();

        editStudentDialog.text("Name:");
        TextField editNameInput = new TextField("", skin);
        editStudentDialog.getContentTable().add(editNameInput);
        editStudentDialog.getContentTable().row();

        editStudentDialog.text("Knowledge Level:");
        TextField editKnowledgeLevelInput = new TextField("", skin);
        editStudentDialog.getContentTable().add(editKnowledgeLevelInput);
        editStudentDialog.getContentTable().row();

        Label editSubtext = new Label("", skin);  // Placeholder for text displayed when action completed successfully or if an invalid input is entered
        editStudentDialog.getContentTable().add(editSubtext);

        TextButton editStudentConfirm = new TextButton("Confirm", skin);
        TextButton editStudentCancel = new TextButton("Back", skin);

        editStudentDialog.getButtonTable().add(editStudentConfirm);
        editStudentDialog.getButtonTable().add(editStudentCancel);

        editStudentCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                editStudentDialog.hide();
            }
        });


        // Initialize dialog to confirm deleting a student
        removeStudentDialog = new Dialog("Remove Student Dialog", skin);

        removeStudentDialog.text("Are you sure you would like to delete this player?");  // TODO: Include the name of the player deleting
        removeStudentDialog.getContentTable().row();

        Label removeSubtext = new Label("", skin);  // Placeholder for text displayed when action completed successfully or an invalid input is entered
        removeStudentDialog.getContentTable().add(removeSubtext);

        TextButton removeStudentConfirm = new TextButton("Confirm", skin);
        TextButton removeStudentCancel = new TextButton("Cancel", skin);

        removeStudentDialog.getButtonTable().add(removeStudentConfirm);
        removeStudentDialog.getButtonTable().add(removeStudentCancel);

        removeStudentCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                removeStudentDialog.hide();
            }
        });


        // Display buttons and layout GUI
        for (TextButton button : studentButtons) {
            table.add(button).fillX();
            table.row().pad(10, 0, 10, 0);
        }
        table.add(addStudentButton).pad(10, 10, 10, 10);
        table.add(editStudentButton).pad(10, 10, 10, 10);
        table.add(removeStudentButton).pad(10, 10, 10, 10);
        table.add(backButton).pad(10, 10, 10, 10);


        // Add button listeners
        addStudentButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addStudentDialog.show(stage);  // Prompt to enter name of new student
            }
        });
        editStudentButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                editStudentDialog.show(stage);  // Prompt to enter new information for selected student
            }
        });
        removeStudentButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                removeStudentDialog.show(stage);  // Prompt for confirmation
            }
        });
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                instructorDashboardEvent.notifyObservers(null);
            }
        });


        for (TextButton button : studentButtons) {

            // Retrieve selected student's information
            PlayerProfile student = this.profileManager.getProfile(button.getText().toString());
            String studentName = student.getName();
            int knowledgeLevel = student.getKnowledgeLevel();

            button.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {

                    // Allow edit and remove buttons to be clicked once student is selected
                    editStudentButton.setTouchable(Touchable.enabled);
                    removeStudentButton.setTouchable(Touchable.enabled);

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

                            // Handle empty inputs
                            if (newName.isEmpty()) {
                                newName = studentName;
                            }
                            if (newKnowledgeLevel.isEmpty()) {
                                newKnowledgeLevel = Integer.toString(knowledgeLevel);
                            }

                            try {

                                // Ensure an integer was entered for knowledge level
                                int inputCheck = Integer.parseInt(newKnowledgeLevel);

                                // Ensure a different student with the same name does not already exist
                                if (!(newName.equals(studentName))) {
                                    for (PlayerProfile student : studentProfiles) {
                                        if (student.getName().equals(newName)) {
                                            throw new IllegalArgumentException("Student with this name already exists.");
                                        }
                                    }
                                }

                                String[] studentData = {studentName, newName, newKnowledgeLevel};
                                System.out.println(Arrays.toString(studentData));
                                editStudentEvent.notifyObservers(Arrays.toString(studentData));

                                editSubtext.setText("Student edited successfully.");  // Display confirmation of action

                            }
                            catch (NumberFormatException e) {
                                editSubtext.setText("Invalid knowledge level.");  // Display error message
                            }
                            catch (IllegalArgumentException e) {
                                editSubtext.setText(e.getMessage());  // Display error message
                            }

                        }
                    });

                    removeStudentConfirm.addListener(new ChangeListener() {

                        // TODO: Ensure player being removed is not in a game

                        @Override
                        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                            removeStudentEvent.notifyObservers(studentName);
                            removeSubtext.setText("Student removed successfully.");  // Display confirmation of action
                        }
                    });
                }

            });

        }

    }

}
