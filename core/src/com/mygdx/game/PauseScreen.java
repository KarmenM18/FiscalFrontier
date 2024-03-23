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

public class PauseScreen extends GameScreen {
    private Observable<String> saveGameEvent = new Observable<String>();
    private Observable<Void> menuEvent = new Observable<Void>();
    private Observable<Void> boardEvent = new Observable<Void>();
    private Observable<Void> knowledgeEvent = new Observable<Void>();

    private Table table;
    private Label title;
    private Dialog confirmMenuDialog;
    private Dialog saveGameDialog;
    private Button menuButton;
    private Button resumeButton;
    private Button saveGameButton;
    private Button viewKnowledgeButton;

    /**
     * Constructor.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public PauseScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        title = new Label("PauseScreen", skin);
        stage.addActor(title);

        // Setup GUI
        table = new Table();
        stage.addActor(table);
        saveGameButton = new TextButton("Save Current Game", skin);
        viewKnowledgeButton = new TextButton("View Knowledge Catalog", skin);
        resumeButton = new TextButton("Resume Game", skin);
        menuButton = new TextButton("Exit To Main Menu", skin);

        // Layout GUI
        table.setFillParent(true); // Size table to stage
        table.add(saveGameButton).fillX();
        table.row();
        table.add(viewKnowledgeButton).fillX();
        table.row();
        table.add(resumeButton).fillX();
        table.row();
        table.add(menuButton).fillX();

        // Set shortcuts
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    menuEvent.notifyObservers(null);
                }
                else if (keycode == Input.Keys.P) {
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
        confirmMenuDialog.text("Are you sure you want to exit to menu? Game progress will NOT be saved.");
        confirmMenuDialog.button("Yes", true);
        confirmMenuDialog.button("No", false);

        // Setup save game box
        TextField saveNameInput = new TextField("", skin);
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
        saveGameDialog.getContentTable().add(saveNameInput);
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

    public void addSaveGameListener(Observer<String> ob) { saveGameEvent.addObserver(ob); }
    public void addMenuListener(Observer<Void> ob) { menuEvent.addObserver(ob); }
    public void addBoardListener(Observer<Void> ob) { boardEvent.addObserver(ob); }
    public void addKnowledgeEventListener(Observer<Void> ob) { knowledgeEvent.addObserver(ob);}
}