package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

public class InstructorDashboard extends GameScreen {

    /** Table storing all GUI buttons. */
    private Table table;
    /** Button to view all student information. */
    private TextButton viewStudentsButton;
    /** Button to enter manage class mode. */
    private TextButton manageStudentsButton;
    /** Button to return to the main menu screen. */
    private TextButton returnButton;
    /** Event displays all student information. */
    private Observable<Void> viewStudentsEvent = new Observable<Void>();
    /** Event enters manage class mode. */
    private Observable<Void> manageStudentsEvent = new Observable<Void>();
    /** Event returns to main menu. */
    private Observable<Void> returnEvent = new Observable<Void>();

    private MainMenuScreen mainMenuScreen;

    private Game game;


    public InstructorDashboard(SpriteBatch batch, AssetManager assets) {

        super(batch, assets);

        // Setup GUI
        table = new Table();
        stage.addActor(table);

        // Initialize buttons
        viewStudentsButton = new TextButton("View Students", skin);
        manageStudentsButton = new TextButton("Manage Students", skin);
        returnButton = new TextButton("Return to Main Menu", skin);

        // Display buttons and layout GUI
        table.setFillParent(true); // Size table to stage
        table.add(viewStudentsButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(manageStudentsButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(returnButton).fillX();

    }

}
