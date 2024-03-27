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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SaveScreen extends GameScreen {
    private Observable<String> loadSaveEvent = new Observable<String>(); // Should bind to MainGame and load the save with the specified name
    private Observable<Void> menuEvent = new Observable<Void>(); // Go back to the main menu

    private ScrollPane scrollPane; // Used so we can have a scrollable list of saves
    private Table table;
    private Label title;
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
        try {
            findSaves();
        } catch (FileNotFoundException e) {
            Utility.showErrorDialog("Error; saves folder not found", stage, skin);
        }
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Used to get an unused ID number when creating new GameStates.
     *
     * @param sSystem a SaveSystem to deserialize JSONs with
     * @return unique integer ID
     */
    public int getUniqueID(SaveSystem sSystem) throws FileNotFoundException {
        File[] fileList = getFileList();

        HashSet<Integer> foundIDs = new HashSet<>();
        for (File file : fileList) {
            if (file.isFile()) {
                // Match all strings starting with a valid save filename and ending with .json
                Config config = Config.getInstance();
                Pattern pattern = Pattern.compile("^.*_.*\\.json$");
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.matches()) {
                    // Get ID
                    GameState gs = sSystem.readGameState(file.getName(), assets);
                    foundIDs.add(gs.getID());
                }
            }
        }

        // Generate random ID until it's unique
        int randID;
        do {
            randID = Utility.getRandom(0, Integer.MAX_VALUE - 1);
        } while (foundIDs.contains(randID));

        return randID;
    }

    /**
     * Remove all GameStates with a given ID. Used when the game ends.
     * @param id the ID value to match
     * @param sSystem the SaveSystem to use to get GameStates.
     */
    public void deleteByID(int id, SaveSystem sSystem) throws FileNotFoundException {
        File[] fileList = getFileList();

        for (File file : fileList) {
            if (file.isFile()) {
                // Match all strings starting with a valid save filename and ending with .json
                Config config = Config.getInstance();
                Pattern pattern = Pattern.compile("^.*_.*\\.json$");
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.matches()) {
                    // Get ID
                    GameState gs = sSystem.readGameState(file.getName(), assets);
                    // Delete if we find a match
                    if (gs.getID() == id) {
                        try {
                            Files.delete(Path.of(file.getPath()));
                        } catch (IOException e) {
                            Utility.showErrorDialog("Error; failed to delete a save associated with this game.", stage, skin);
                        }
                    }
                }
            }
        }
    }

    /**
     * Find new saves in the saves folder
     */
    private void findSaves() throws FileNotFoundException {
        // Remove old buttons and create new ones as needed
        table.clear();
        table.add(menuButton);
        table.row().pad(10, 0, 10, 0);

        int saveNum = 1;
        File[] fileList = getFileList();

        for (File file : fileList) {
            if (file.isFile()) {
                // Match all strings starting with a valid save filename and ending with .json
                Config config = Config.getInstance();
                Pattern pattern = Pattern.compile("^.*_.*\\.json$");
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
                            try {
                                findSaves();
                            } catch (FileNotFoundException e) {
                                Utility.showErrorDialog("Error; saves folder not found", stage, skin);
                            }
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

    /**
     * Get the list of saves in the save folder.
     *
     * @return an array of Files
     * @throws FileNotFoundException if the save folder wasn't found. It will not throw if the folder is just empty
     */
    private File[] getFileList() throws FileNotFoundException {
        ClassLoader CL = getClass().getClassLoader();
        File saveFolder = new File(CL.getResource(Config.getInstance().getSaveFolder()).getFile());
        File[] fileList = saveFolder.listFiles();
        if (fileList == null) throw new FileNotFoundException();

        return fileList;
    }

    public void addLoadSaveListener(Observer<String> ob) { loadSaveEvent.addObserver(ob); }
    public void addMenuListener(Observer<Void> ob) { menuEvent.addObserver(ob); }
}