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
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Screen displayed at the beginning of a new game. Includes difficulty and player selection.
 *
 * @author Franck Limtung (flimtung)
 * @author Kevin Chen (kchen546)
 */
public class NewGameScreen extends GameScreen {

    /** Event returns to main menu. */
    private Observable<Void> menuEvent = new Observable<>();
    /** Event creates a new game state with the selected player list and switches to the game board. */
    private Observable<Map.Entry<ArrayList<PlayerProfile>, Boolean>> createGameEvent = new Observable<>();
    /**  Responsible for storing and managing student profiles. */
    private ProfileManager profileManager;
    /** Stores all UI elements. */
    private Table table;
    /** Left column of UI elements. */
    private Table leftTable;
    /** Right column of UI elements */
    private Table rightTable;
    /** Selected players' student profiles. */
    HashMap<String, PlayerProfile> profilesMap;
    /** List of selected players' names. */
    private Array<String> selectedPlayerNames;
    /** LibGDX list widget displays selected players. */
    private List<String> selectedPlayersBox;
    /** Button to remove the currently selected player from the player list. */
    private TextButton deleteSelectedButton;
    /** Label above player selection box. */
    private Label selectPlayerLabel;
    /** List of names of all players that may be selected. Does not include the currently selected players. */
    private Array<String> selectablePlayerNames;
    /** LibGDX widget to select players. */
    private SelectBox<String> playerSelector;
    /** Button to begin the game. */
    private TextButton startButton;
    /** Button to return to main menu. */
    private TextButton returnButton;
    /** Checkbox to select hard mode. */
    private CheckBox hardModeCheckBox;


    /**
     * Constructor initializes the screen.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     * @param profileManager ProfileManager to get player profiles from.
     */
    public NewGameScreen(SpriteBatch batch, AssetManager assets, ProfileManager profileManager) {
        super(batch, assets);
        this.profileManager = profileManager;
        profilesMap = new HashMap<String, PlayerProfile>();
        selectedPlayerNames = new Array<String>();
        selectablePlayerNames = new Array<String>();

        // Setup keyboard shortcuts
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    menuEvent.notifyObservers(null);
                }
                else if (keycode == Input.Keys.FORWARD_DEL) {
                    // Delete the currently selected Player
                    deleteSelectedButton.fire(new ChangeListener.ChangeEvent());
                }
                else return false;

                return true;
            }
        });

        // Initialize GUI elements
        table = new Table();
        table.setFillParent(true);
        leftTable = new Table(); // Used for selecting profiles
        rightTable = new Table(); // Used to select hard mode and start the game

        selectedPlayersBox = new List<String>(skin);

        selectPlayerLabel = new Label("Select players:", skin);
        playerSelector = new SelectBox<String>(skin);
        playerSelector.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Ignore blank element, indicating no selection
                if (playerSelector.getSelectedIndex() == 0) return;

                // Check for too many players
                if (selectedPlayerNames.size >= Config.getInstance().getMaxPlayers()) {
                    Utility.showErrorDialog("Already at the maximum number of players.\n", stage, skin);
                }
                else {
                    String selectedPlayer = playerSelector.getSelected();
                    selectedPlayerNames.add(selectedPlayer);
                    selectedPlayersBox.setItems(selectedPlayerNames);

                    selectablePlayerNames.removeValue(selectedPlayer, false);
                    playerSelector.setItems(selectablePlayerNames);
                    playerSelector.setSelectedIndex(0); // Set to default blank element

                    deleteSelectedButton.setVisible(true);
                }
            }
        });

        deleteSelectedButton = new TextButton("Remove Player", skin);
        deleteSelectedButton.setVisible(false);
        deleteSelectedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectedPlayerNames.size < 1) return;

                // Re-add name to selectables
                selectablePlayerNames.add(selectedPlayersBox.getSelected());
                playerSelector.setItems(selectablePlayerNames);

                // Remove the currently selected player from the list
                selectedPlayerNames.removeIndex(selectedPlayersBox.getSelectedIndex());
                selectedPlayersBox.setItems(selectedPlayerNames);

                // Only show delete button if there are players to remove
                if (selectedPlayerNames.size < 1) {
                    deleteSelectedButton.setVisible(false);
                }
            }
        });

        startButton = new TextButton("Start Game", skin);
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int minPlayers = Config.getInstance().getMinPlayers();
                if (selectedPlayerNames.size < minPlayers) {
                    Utility.showErrorDialog("Not enough players. There must be at least " + minPlayers + " players to start a game.", stage, skin);
                    return;
                }

                ArrayList<PlayerProfile> profiles = new ArrayList<>();
                for (String playerName : selectedPlayerNames) {
                    profiles.add(profilesMap.get(playerName));
                }

                // Call MainGame to create the new GameState and switch screen to GameBoard
                createGameEvent.notifyObservers(new AbstractMap.SimpleEntry<ArrayList<PlayerProfile>, Boolean>(profiles, hardModeCheckBox.isChecked()));
            }
        });

        hardModeCheckBox = new CheckBox("Hard mode", skin);

        returnButton = new TextButton("Return to Main Menu", skin);
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuEvent.notifyObservers(null);
            }
        });


        // Layout GUI
        table.add(leftTable).left().grow();
        table.add(rightTable).right().grow();
        leftTable.row().pad(5, 25, 5, 25);
        leftTable.add(selectPlayerLabel).center();
        leftTable.row().pad(25);
        leftTable.add(playerSelector).growX();
        leftTable.row().pad(25);
        leftTable.add(selectedPlayersBox).grow();
        leftTable.row();
        leftTable.add(deleteSelectedButton).fill().pad(0, 25, 0, 25);
        rightTable.row().pad(25);
        rightTable.add(startButton).growX().height(100);
        rightTable.row();
        rightTable.add(hardModeCheckBox);
        rightTable.row();
        rightTable.add(returnButton).padTop(100);

        stage.addActor(table);

    }


    /**
     * Displays the screen.
     */
    @Override
    public void show() {
        // Add list of player profiles to the selector
        selectablePlayerNames.clear();
        profilesMap.clear();
        selectablePlayerNames.add("Select Players"); // Add value which indicates "nothing selected"
        for (PlayerProfile profile : profileManager.getStudentProfiles()) {
            assert !profile.getName().equals("Select Players");

            profilesMap.put(profile.getName(), profile);
            selectablePlayerNames.add(profile.getName());
        }
        playerSelector.setItems(selectablePlayerNames);
        playerSelector.setSelectedIndex(0); // Set to default blank element

        // Clear list of active profiles
        selectedPlayerNames.clear();
        selectedPlayersBox.setItems(selectedPlayerNames);

        Gdx.input.setInputProcessor(stage);
    }


    /**
     * Assigns an observer to listen for the event to return to main menu.
     * @param ob Observer to listen for the event to return to main menu.
     */
    public void addMenuListener(Observer<Void> ob) { menuEvent.addObserver(ob); }

    /**
     * Assigns an observer to listen for the event to create a new game state and switch to gameboard.
     * @param ob Observer to listen for the event to create a new game state and switch to gameboard.
     */
    public void addCreateGameStateListener(Observer<Map.Entry<ArrayList<PlayerProfile>, Boolean>> ob) { createGameEvent.addObserver(ob); }

}