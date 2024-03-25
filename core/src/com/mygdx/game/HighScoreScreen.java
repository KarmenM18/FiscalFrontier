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
        table.top().left();
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