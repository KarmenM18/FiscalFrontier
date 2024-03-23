/*
* Main class of the game
* Contains the various screens of the game, as well as the AssetManager and the Config
 */

package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.Observer.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * The main class of the game. Handles interaction with the saving system and the switching of screens.
 */
public class MainGame extends Game {
	SpriteBatch batch;
	private GameBoard gameBoard;
	private PauseScreen pauseScreen;
	private ShopScreen shopScreen;
	private MainMenuScreen mainMenuScreen;
	private KnowledgeListScreen knowledgeListScreen;
	private SaveScreen saveScreen;
	private InstructorDashboardScreen instructorDashboardScreen;
	private ManageStudentsScreen manageStudentsScreen;
	private AssetManager assets = new AssetManager();
	private SaveSystem saveSystem = new SaveSystem();

	private ProfileManager profileManager;

	List<PlayerProfile> profileList; // List of player profiles
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		// Load assets
		Config config = Config.getInstance();
		assets.load(config.getUiPath(), Skin.class);
		assets.load(config.getTilePath(), Texture.class);
		assets.load(config.getStarTilePath(), Texture.class);
		assets.load(config.getEventTilePath(), Texture.class);
		assets.load(config.getPenaltyTilePath(), Texture.class);
		assets.load(config.getPlayerPath(), Texture.class);
		assets.load("background.jpeg", Texture.class);
		assets.finishLoading(); // Make sure assets are loaded before continuing;

		// Load screens
		mainMenuScreen = new MainMenuScreen(batch, assets);
		gameBoard = new GameBoard(batch, assets);
		pauseScreen = new PauseScreen(batch, assets);
		shopScreen = new ShopScreen(batch, assets);
		knowledgeListScreen = new KnowledgeListScreen(batch, assets);
		saveScreen = new SaveScreen(batch, assets);
		profileManager = new ProfileManager("studentInformation.json");
		instructorDashboardScreen = new InstructorDashboardScreen(batch, assets, this.profileManager);
		manageStudentsScreen = new ManageStudentsScreen(batch, assets, this.profileManager);

		// Load players from save if possible
		if (Utility.fileExists(config.getPlayerSavePath())) {
			profileList = (List<PlayerProfile>)Utility.loadObject(config.getPlayerSavePath());
		} else {
			profileList = new ArrayList<PlayerProfile>();
			profileList.add(new PlayerProfile("Player 1"));
			profileList.add(new PlayerProfile("Player 2"));
			profileList.add(new PlayerProfile("Player 3"));
			profileList.add(new PlayerProfile("Player 4"));
		}

		// Set starting screen
		setScreen(mainMenuScreen);

		// Set gameBoard observers
		gameBoard.addShopListener(v -> setScreen(shopScreen));
		gameBoard.addPauseListener(currentPlayer -> {
			//TODO Send Current Player Info To Screen
			setScreen(pauseScreen);
		});

		knowledgeListScreen.addBackToPause(v -> setScreen(pauseScreen));

		// Set PauseScreen observers
		pauseScreen.addSaveGameListener(v -> saveGameState(gameBoard.getGameState()));
		pauseScreen.addMenuListener(v -> {
			setScreen(mainMenuScreen);
		});
		pauseScreen.addBoardListener(v -> setScreen(gameBoard));
		pauseScreen.addKnowledgeEventListener(v -> {
			knowledgeListScreen.setPlayerKnowledge(gameBoard.getGameState().getCurrentPlayer().getPlayerProfile().getLearned());
			knowledgeListScreen.updateTable();
			setScreen(knowledgeListScreen);
		});

		// Set ShopScreen observers
		shopScreen.addBoardListener(v -> setScreen(gameBoard));
		// Set SaveScreen observers
		saveScreen.addMenuListener(v -> setScreen(mainMenuScreen));
		saveScreen.addLoadSaveListener(savePath -> {
			GameState gs = loadGameState(savePath);
			gameBoard.setGameState(gs);
			setScreen(gameBoard);
		});
		// Set MainMenuScreen observers
		mainMenuScreen.addStartGameListener(v -> {
			// Create new game with all players in it TODO player selection
			GameState newGame = new GameState(profileList, assets);
			gameBoard.setGameState(newGame);
			setScreen(gameBoard);
		});
		mainMenuScreen.addContinueGameListener(v -> {
			GameState gs;
			// TODO: load LAST save
			if (Utility.fileExists(config.getGameStateSavePath())) {
				gs = loadGameState(config.getGameStateSavePath());
				gameBoard.setGameState(gs);
				setScreen(gameBoard);
			}
			// TODO inform the user that there is no save to continue from
		});
		// Set InstructorDashboardScreen observers
		mainMenuScreen.addLoadGameListener(v -> {
			setScreen(saveScreen);
		});
		mainMenuScreen.addInstructorDashboardListener(v -> {
			setScreen(instructorDashboardScreen);  // Open instructor dashboard from main menu
		});
		instructorDashboardScreen.addMenuListener(v -> {
			setScreen(mainMenuScreen);  // Return to main menu
		});
		instructorDashboardScreen.addManageStudentsListener(v -> {
			setScreen(manageStudentsScreen);  // Enter manage students mode in instructor dashboard
		});
		// Set ManageStudentsScreen observers
		manageStudentsScreen.addBackListener(v -> {
			setScreen(instructorDashboardScreen);  // Enter manage students mode in instructor dashboard
		});
		manageStudentsScreen.addAddStudentListener(studentName -> {
			try {
				profileManager.addStudent(studentName);
				instructorDashboardScreen.loadDashboard();  // Reload data to reflect changes
			}
			catch (IllegalArgumentException e) {
				System.out.println("Unable to add student.");  // FIXME: Make this a dialog box
			}

		});
	}

	@Override
	public void render() {
		super.render(); // Render current screen
	}
	
	@Override
	public void dispose() {
		mainMenuScreen.dispose();
		gameBoard.dispose();
		batch.dispose();
		assets.dispose();
		super.dispose();
	}

	// Saving stuff TODO: Save manager
	/**
	 * Save player profile list to file
	 */
	public void saveProfiles() {
		Config config = Config.getInstance();
		Utility.saveObject(profileList, config.getPlayerSavePath());
	}

	/**
	 * Save a GameState to file.
	 *
	 * @param gs the GameState to save
	 */
	public void saveGameState(GameState gs) {
		saveSystem.saveGameState(gs, Config.getInstance().getGameStateSavePath());
	}

	/**
	 * Load a GameState from file
	 *
	 * @param path the path of where the serialized JSON is located
	 */
	public GameState loadGameState(String path) {
		return saveSystem.readGameState(path, assets);
	}
}
