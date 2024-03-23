package com.mygdx.game;

import com.badlogic.gdx.Input;
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
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.util.List;

/**
 * Screen to display the high score list in the menu
 */
public class HighScoreScreen extends GameScreen {
    private Observable<Void> menuEvent = new Observable<Void>();

    private Table table;
    private Label title;
    private Button menuButton;
    private Table scoreTable; // Displays the stats of each player in the game

    private List<PlayerProfile> profileList;

    /**
     * Constructor.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public HighScoreScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        title = new Label("High Score Screen", skin);
        stage.addActor(title);

        // Setup GUI
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        menuButton = new TextButton("Back", skin);
        scoreTable = new Table();

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
     */
    public void setProfileList(List<PlayerProfile> profileList) {
        this.profileList = profileList;

        table.clear();

        table.add(menuButton).fillX();
    }

    public void addMenuListener(Observer<Void> ob) { menuEvent.addObserver(ob); }
}