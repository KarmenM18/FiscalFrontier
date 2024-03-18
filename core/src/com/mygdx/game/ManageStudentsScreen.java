package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

/**
 * Manage students mode screen in instructor dashboard.
 */
public class ManageStudentsScreen extends GameScreen {

    /**
     * Table storing all GUI buttons.
     */
    private Table table;
    /**
     * Button to exit manage students mode.
     */
    private TextButton backButton;
    /**
     * Button to add a new student.
     */
    private TextButton addStudentButton;
    /**
     * Event exits manage students mode and returns to the instructor dashboard.
     */
    private Observable<Void> instructorDashboardEvent = new Observable<Void>();

    private Dialog addStudentDialog;


    public ManageStudentsScreen(SpriteBatch batch, AssetManager assets) {

        super(batch, assets);

        // Setup GUI
        table = new Table();
        stage.addActor(table);

        // Initialize buttons
        addStudentButton = new TextButton("Add", skin);
        backButton = new TextButton("Back", skin);


        // Initialize add student dialog prompt
        addStudentDialog = new Dialog("Add Student Dialog", skin);

        addStudentDialog.text("Enter student name:");
        addStudentDialog.getContentTable().row();

        TextField studentNameInput = new TextField("", skin);
        addStudentDialog.getContentTable().add(studentNameInput);

        TextButton addStudentConfirm = new TextButton("Confirm", skin);
        TextButton addStudentCancel = new TextButton("Cancel", skin);

        addStudentDialog.getButtonTable().add(addStudentConfirm);
        addStudentDialog.getButtonTable().add(addStudentCancel);

        addStudentConfirm.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {

                String studentName = studentNameInput.getText();
                // TODO: Verify student does not already exist
                // TODO: Display confirmation message when successful

            }
        });
        addStudentCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                addStudentDialog.hide();
            }
        });


        // Display buttons and layout GUI
        table.setFillParent(true); // Size table to stage
        table.add(addStudentButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(backButton).fillX();


        // Add button listeners
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                instructorDashboardEvent.notifyObservers(null);
            }
        });
        addStudentButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addStudentDialog.show(stage);
            }
        });

    }

    /** Add listeners for each event handled by the MainGame screen manager. */
    void addBackListener(Observer<Void> ob) { instructorDashboardEvent.addObserver(ob); }

}
