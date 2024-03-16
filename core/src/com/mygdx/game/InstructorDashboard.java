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

    public InstructorDashboard(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        // TODO: Finish loading assets

        // Setup GUI
        table = new Table();
        stage.addActor(table);

        // Initialize buttons
        viewStudentsButton = new TextButton("View Students", skin);
        manageStudentsButton = new TextButton("Manage Students", skin);
        returnButton = new TextButton("Return to Main Menu", skin);

    }


}
