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

/**
 * Initial screen loaded when entering instructor dashboard, displays student metrics.
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


    public InstructorDashboardScreen(SpriteBatch batch, AssetManager assets) {

        super(batch, assets);

        // Setup GUI
        table = new Table();
        stage.addActor(table);

        // Initialize buttons
        manageStudentsButton = new TextButton("Manage Students", skin);
        returnButton = new TextButton("Return to Main Menu", skin);

        // Display buttons and layout GUI
        table.setFillParent(true); // Size table to stage
        table.add(manageStudentsButton).fillX();
        table.row().pad(10, 0, 10, 0);
        table.add(returnButton).fillX();

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
