package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

/**
 * Screen displayed at the end of a game.
 *
 * @author Franck Limtung (flimtung)
 * @author Kevin Chen (kchen546)
 */
public class EndScreen extends GameScreen {

    /** Event returns to main menu. */
    private Observable<Void> menuEvent = new Observable<>();
    /** Event delete all saves for the game. */
    private Observable<Integer> deleteSavesEvent = new Observable<>();
    /** Event updates lifetime and highscore of the player. */
    private Observable<PlayerProfile> updateScoreEvent = new Observable<>();

    private Table table;
    /** Button to return to main menu. */
    private Button menuButton;
    /** Displays the stats of each player in the game. */
    private Table scoreTable;
    /** Set when the screen is switched. */
    private GameState finalGameState = null;


    /**
     * Constructor initializes the ending game screen.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public EndScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        // Setup GUI
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        menuButton = new TextButton("Exit To Main Menu", skin);
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
     * Set the GameState to read and end.
     *
     * @param gs a GameState
     */
    public void setGameState(GameState gs) {

        this.finalGameState = gs;

        if (finalGameState == null) throw new IllegalStateException();

        // Setup score table
        scoreTable.clear();

        // Add column headers to the table
        scoreTable.add(new Label("Name", skin)).padLeft(50).padRight(50);
        scoreTable.add(new Label("Score", skin)).padRight(25);
        scoreTable.add(new Label("Stars", skin)).padRight(25);
        scoreTable.row();

        // Add players to table and get the winner - we sort the table by score
        // TODO: If we are weighting other things than score, this is where that logic should go

        finalGameState.getPlayerList().sort((o1, o2) -> o2.getScore() - o1.getScore());
        Player winner = finalGameState.getPlayerList().get(0);
        for (Player player : finalGameState.getPlayerList()) {
            scoreTable.add(new Label(player.getPlayerProfile().getName(), skin)).padLeft(50).padRight(50);
            scoreTable.add(new Label(String.valueOf(player.getScore()), skin)).padRight(25);
            scoreTable.add(new Label(String.valueOf(player.getStars()), skin)).padRight(25);
            scoreTable.row();
        }

        // Layout GUI
        table.clear();
        table.row().pad(50);
        table.add(new Label("Game over! " + winner.getPlayerProfile().getName() + " won with " + winner.getScore() + " total score!", skin)).center().top();
        table.row().pad(25);
        table.add(scoreTable).center().expand();
        table.row().uniform();
        table.add(menuButton);

        // Update Player Profiles
        for (Player player : finalGameState.getPlayerList()) {
            PlayerProfile profile = player.getPlayerProfile();

            profile.setKnowledgeLevel(player.getLevel());
            profile.setLifetimeScore(profile.getLifetimeScore() + player.getScore());
            if (profile.getHighScore() < player.getScore()) {
                profile.setHighScore(player.getScore());
            }

            updateScoreEvent.notifyObservers(profile);
        }

        // Delete all saves for the game by matching the ID
        deleteSavesEvent.notifyObservers(finalGameState.getID());
    }

    /**
     * Assigns an observer to listen for the event to return to main menu.
     * @param ob Observer to listen for the event to return to main menu.
     */
    public void addMenuListener(Observer<Void> ob) { menuEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to delete save file.
     * @param ob Observer to listen for the event to delete save file.
     */
    public void addDeleteSavesListener(Observer<Integer> ob) { deleteSavesEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to update players' high scores and lifetime scores.
     * @param ob Observer to listen for the event to update players' high scores and lifetime scores.
     */
    public void addUpdateScoreListener(Observer<PlayerProfile> ob) { updateScoreEvent.addObserver(ob); }
}