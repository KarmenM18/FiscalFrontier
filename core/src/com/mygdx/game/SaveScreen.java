package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Screen displayed when loading a saved game state.
 * <br><br>
 * Displays a list of saved game states, with buttons that allow users to load or delete the save.
 *
 * @author Franck Limtung (flimtung)
 * @author Kevin Chen (kchen546)
 */
public class SaveScreen extends GameScreen {

    /** Event loads a selected saved game state. */
    private Observable<String> loadSaveEvent = new Observable<String>(); // Should bind to MainGame and load the save with the specified name
    /** Event returns to main menu. */
    private Observable<Void> menuEvent = new Observable<Void>();

    /** Contains all UI elements. */
    private Table table;
    /** Allows screen to scroll. */
    private ScrollPane scrollPane;
    /** Button to return to main menu. */
    private Button menuButton;

    /**
     * Constructor initializes the screen's GUI.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public SaveScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

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


    /**
     * Attempts to find saves and displays an error message if no saved game states could be found. Called before
     * displaying the screen.
     */
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
     * Generate an unused ID number to use to create a new GameStates.
     *
     * @param sSystem Used to deserialize JSON files
     * @return Unique integer ID
     */
    public int getUniqueID(SaveSystem sSystem) throws FileNotFoundException {
        File[] fileList = getFileList();

        HashSet<Integer> foundIDs = new HashSet<>();
        for (File file : fileList) {
            if (file.isFile()) {
                // Match all strings starting with a valid save filename and ending with .json
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
     * Get the last save played.
     *
     * @param stage Stage to show possible error dialogs on
     * @param skin Skin to draw possible error dialogs
     * @throws FileNotFoundException if the saves folder isn't found
     */
    public void loadLatestSave(Stage stage, Skin skin) throws FileNotFoundException {
        File[] fileList = getFileList();

        // Get the latest save by modified time
        File latestModifiedFile = null;
        long lastModifiedTime = Long.MIN_VALUE;

        for (File file : fileList) {
            if (file.isFile()) {
                // Match all strings starting with a valid save filename and ending with .json
                Config config = Config.getInstance();
                Pattern pattern = Pattern.compile("^.*_.*\\.json$");
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.matches()) {
                    if (file.lastModified() > lastModifiedTime) {
                        lastModifiedTime = file.lastModified();
                        latestModifiedFile = file;
                    }
                }
            }
        }

        if (latestModifiedFile == null) {
            Utility.showErrorDialog("No saves to load.", stage, skin);
        }
        else {
            loadSaveEvent.notifyObservers(latestModifiedFile.getName());
        }
    }

    /**
     * Remove all GameStates with a given ID. Used when the game ends.
     *
     * @param id The ID of the game state
     * @param sSystem The SaveSystem object used to get game states.
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
     *
     * @throws FileNotFoundException If the save folder wasn't found. It will not be thrown if the folder is just empty.
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
     * Get the list of all saved game states in the save folder.
     *
     * @return an array of Files
     * @throws FileNotFoundException If the save folder wasn't found. It will not be thrown if the folder is just empty.
     */
    private File[] getFileList() throws FileNotFoundException {
        File saveFolder = new File("saves");
        File[] fileList = saveFolder.listFiles();
        if (fileList == null) throw new FileNotFoundException();

        return fileList;
    }


    /**
     * Assigns an observer to listen for the event to load the selected game save state.
     * @param ob Observer to listen for the event to load the selected game save state.
     */
    public void addLoadSaveListener(Observer<String> ob) { loadSaveEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to return to main menu.
     * @param ob Observer to listen for the event to return to main menu.
     */
    public void addMenuListener(Observer<Void> ob) { menuEvent.addObserver(ob); }

}