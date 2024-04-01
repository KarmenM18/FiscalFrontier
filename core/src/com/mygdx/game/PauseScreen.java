package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

/**
 * Screen displayed when the game is paused.
 * <br><br>
 * Includes options to save the current game state, view the learned knowledge base of the player whose turn it is,
 * resume gameplay, or return to main menu.
 *
 * @author Franck Limtung (flimtung)
 * @author Kevin Chen (kchen546)
 * @author Earl Castillo (ecastil3)
 */
public class PauseScreen extends GameScreen {

    /** Event saves the current game state. */
    private Observable<String> saveGameEvent = new Observable<String>();
    /** Event returns to main menu. */
    private Observable<Void> menuEvent = new Observable<Void>();
    /** Event returns to gameboard and resumes gameplay. */
    private Observable<Void> boardEvent = new Observable<Void>();
    /** Event displays the player's learned knowledge base. */
    private Observable<Void> knowledgeEvent = new Observable<Void>();

    /** Contains all UI elements. */
    private Table table;
    /** Dialog box prompts for confirmation to return to main menu. */
    private Dialog confirmMenuDialog;
    /** Dialog box prompts to enter name for saved game state. */
    private Dialog saveGameDialog;
    /** Button to return main menu. */
    private Button menuButton;
    /** Button to return gameboard and resume gameplay. */
    private Button resumeButton;
    /** Button to save the current game state. */
    private Button saveGameButton;
    /** Button to view player's learned knowledge base. */
    private Button viewKnowledgeButton;


    /**
     * Constructor initializes the pause screen's attributes.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public PauseScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        // Setup GUI
        table = new Table();
        stage.addActor(table);

        table.row().pad(25);
        Label title = new Label("Pause", skin, "menu");
        title.setAlignment(Align.center);
        table.add(title).colspan(2).fillX();
        table.row();

        saveGameButton = new TextButton("Save Game", skin);
        viewKnowledgeButton = new TextButton("View Knowledge Catalog", skin);
        resumeButton = new TextButton("Resume Game", skin);
        menuButton = new TextButton("Exit To Main Menu", skin);

        // Layout GUI
        table.setFillParent(true); // Size table to stage
        table.add(resumeButton).fillX();
        table.row();
        table.add(saveGameButton).fillX();
        table.row();
        table.add(viewKnowledgeButton).fillX();
        table.row();
        table.add(menuButton).fillX();

        // Set shortcuts
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.P) {
                    confirmMenuDialog.hide();
                    saveGameDialog.hide();
                    boardEvent.notifyObservers(null);
                }
                else {
                    return false;
                }

                return true;
            }
        });

        // Setup confirm to menu box
        confirmMenuDialog = new Dialog("Confirm Menu", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    menuEvent.notifyObservers(null); // Change screen to menu
                }
            }
        };
        confirmMenuDialog.text("Are you sure you want to exit to menu?\n Game progress will NOT be saved automatically.");
        confirmMenuDialog.button("Yes", true);
        confirmMenuDialog.button("No", false);

        // Setup save game box
        TextField saveNameInput = new TextField("NewGame", skin);
        saveGameDialog = new Dialog("Save Menu", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    String filename = saveNameInput.getText();
                    saveGameEvent.notifyObservers(filename);
                }
            }
        };
        saveGameDialog.text("Enter save name:");
        saveGameDialog.getContentTable().row();
        saveGameDialog.getContentTable().add(saveNameInput).fill();
        saveGameDialog.button("Continue", true);


        // Add button listeners
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmMenuDialog.show(stage);
            }
        });
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmMenuDialog.hide();
                saveGameDialog.hide();
                boardEvent.notifyObservers(null);
            }
        });
        saveGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveGameDialog.show(stage);
            }
        });
        viewKnowledgeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                knowledgeEvent.notifyObservers(null); //Moving to show knowledge screen
            }
        });
    }


    /**
     * Assigns an observer to listen for the event to save the current game state.
     * @param ob Observer to listen for the event to save the current game state.
     */
    public void addSaveGameListener(Observer<String> ob) { saveGameEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to return to main menu.
     * @param ob Observer to listen for the event to return to main menu.
     */
    public void addMenuListener(Observer<Void> ob) { menuEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to resume the game.
     * @param ob Observer to listen for the event to resume the game.
     */
    public void addBoardListener(Observer<Void> ob) { boardEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to view the player's learned knowledge base.
     * @param ob Observer to listen for the event to view the player's learned knowledge base.
     */
    public void addKnowledgeEventListener(Observer<Void> ob) { knowledgeEvent.addObserver(ob);}
}