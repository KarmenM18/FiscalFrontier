package com.mygdx.game;

import com.badlogic.gdx.Gdx;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SaveScreen extends GameScreen {
    private Observable<String> loadSaveEvent = new Observable<String>(); // Should bind to MainGame and load the save with the specified name
    private Observable<Void> menuEvent = new Observable<Void>(); // Go back to the main menu

    private ScrollPane scrollPane; // Used so we can have a scrollable list of saves
    private Table table;
    private Label title;
    private Dialog confirmMenuDialog;
    private Button menuButton;

    /**
     * Constructor.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public SaveScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        title = new Label("SaveScreen", skin);
        stage.addActor(title);

        // Setup GUI
        table = new Table();
        scrollPane = new ScrollPane(table);
        scrollPane.setScrollbarsVisible(true);
        scrollPane.setFillParent(true);
        menuButton = new TextButton("Back", skin);
        menuButton.align(Align.topLeft);

        // Layout GUI
        stage.addActor(menuButton);
        stage.addActor(scrollPane);

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

    @Override
    public void show() {
        findSaves();
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Find new saves in the saves folder
     */
    private void findSaves() {
        // Remove old buttons and create new ones as needed
        table.clear();

        int saveNum = 1;
        File saveFolder = new File(".");
        File[] fileList = saveFolder.listFiles();
        assert fileList != null;
        for (File file : fileList) {
            if (file.isFile()) {
                // Match all strings starting with Save filename and ending with .json
                Config config = Config.getInstance();
                Pattern pattern = Pattern.compile("^" + config.getGameStateSavePath() + ".*\\.json$");
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.matches()) {
                    Label saveLabel = new Label(file.getName(), skin);
                    TextButton loadButton = new TextButton("Load", skin);
                    loadButton.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            loadSaveEvent.notifyObservers(file.getName());
                        }
                    });
                    Button deleteButton = new TextButton("Delete", skin);
                    deleteButton.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            try {
                                Files.delete(Path.of(file.getPath()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            findSaves();
                        }
                    });

                    table.add(saveLabel);
                    table.add(loadButton);
                    table.add(deleteButton);
                    table.row().pad(10, 0, 10, 0);
                }
            }
        }
    }

    public void addLoadSaveListener(Observer<String> ob) { loadSaveEvent.addObserver(ob); }
    public void addMenuListener(Observer<Void> ob) { menuEvent.addObserver(ob); }
}