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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
	private EndScreen endScreen;
	private HighScoreScreen highScoreScreen;
	private NewGameScreen newGameScreen;

	private AssetManager assets = new AssetManager();
	private SaveSystem saveSystem = new SaveSystem();

	private ProfileManager profileManager;
	
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
		assets.load("background2.jpg", Texture.class);
		assets.finishLoading(); // Make sure assets are loaded before continuing;

		// Load screens
		mainMenuScreen = new MainMenuScreen(batch, assets);
		gameBoard = new GameBoard(batch, assets);
		pauseScreen = new PauseScreen(batch, assets);
		shopScreen = new ShopScreen(batch, assets);
		knowledgeListScreen = new KnowledgeListScreen(batch, assets);
		saveScreen = new SaveScreen(batch, assets);

		ClassLoader CL = getClass().getClassLoader();
		profileManager = new ProfileManager("studentInformation.json", "highScoreTable.json", "lifetimeScoreTable.json");
		instructorDashboardScreen = new InstructorDashboardScreen(batch, assets, this.profileManager);
		manageStudentsScreen = new ManageStudentsScreen(batch, assets, this.profileManager);
		endScreen = new EndScreen(batch, assets);
		highScoreScreen = new HighScoreScreen(batch, assets, this.profileManager);
		newGameScreen = new NewGameScreen(batch, assets, profileManager);

		// Set starting screen
		setScreen(mainMenuScreen);

		// Set GameBoard observers
		gameBoard.addShopListener(v -> {
			shopScreen.setCurrentPlayer(gameBoard.getGameState().getCurrentPlayer());
			shopScreen.setStocksAvailable(gameBoard.getGameState().getAllStocks());
			shopScreen.updateScreen();
			setScreen(shopScreen);
		});
		gameBoard.addPauseListener(currentPlayer -> setScreen(pauseScreen));
		gameBoard.addEndListener(gameState -> {
			endScreen.setGameState(gameState);
			setScreen(endScreen);
		});

		knowledgeListScreen.addBackToPause(v -> setScreen(pauseScreen));

		// Set PauseScreen observers
		pauseScreen.addSaveGameListener(saveName -> saveGameState(gameBoard.getGameState(), saveName));
		pauseScreen.addMenuListener(v -> {
			setScreen(mainMenuScreen);
		});
		pauseScreen.addBoardListener(v -> setScreen(gameBoard));
		pauseScreen.addKnowledgeEventListener(v -> {
			knowledgeListScreen.setPlayerKnowledge(gameBoard.getGameState().getCurrentPlayer().getPlayerProfile().getLearned());
			knowledgeListScreen.updateTable();
			setScreen(knowledgeListScreen);
		});

		// Set EndScreen observers
		endScreen.addMenuListener(v -> setScreen(mainMenuScreen));
		endScreen.addDeleteSavesListener(id -> {
			// Delete all saves related to the completed game
            try {
                saveScreen.deleteByID(id, saveSystem);
            } catch (FileNotFoundException e) {
                Utility.showErrorDialog("Error; failed to open saves folder.", endScreen.stage, endScreen.skin);
            }
        });
		endScreen.addUpdateScoreListener(profile -> {
			// Modify the PlayerProfile based on what happened in the game
			// TODO: Handle renames and removals of profiles
			profileManager.updateHighScore(profile.getName(), profile.getHighScore());
			profileManager.addLifetimeScore(profile.getName(), profile.getLifetimeScore());
			// TODO: must check that game profile's lifetime score is synchronized with profile manager in the case of several save files
		});

		// Set HighScoreScreen observers
		highScoreScreen.addMenuListener(v -> setScreen(mainMenuScreen));

		// Set NewGameScreen observers
		newGameScreen.addMenuListener(v -> setScreen(mainMenuScreen));
		newGameScreen.addCreateGameStateListener(profileBoolPair -> {
			GameState newGame;
			try {
				newGame = new GameState(profileBoolPair.getKey(), assets, saveScreen.getUniqueID(saveSystem), profileBoolPair.getValue());
			} catch (FileNotFoundException e) {
				Utility.showErrorDialog("Error; saves folder not found", mainMenuScreen.stage, mainMenuScreen.skin);
				return;
			}
			gameBoard.setGameState(newGame);
			setScreen(gameBoard);
		});

		// Set ShopScreen observers
		shopScreen.addBoardListener(v -> {
			setScreen(gameBoard);
		});

		// Set SaveScreen observers
		saveScreen.addMenuListener(v -> setScreen(mainMenuScreen));
		saveScreen.addLoadSaveListener(savePath -> {
			GameState gs = loadGameState(savePath);
			gameBoard.setGameState(gs);
			setScreen(gameBoard);
		});

		// Set MainMenuScreen observers
		mainMenuScreen.addStartGameListener(v -> setScreen(newGameScreen));
		mainMenuScreen.addContinueGameListener(v -> {
			GameState gs;
			// TODO: load LAST save. Should check modified time of save files
			// TODO inform the user that there is no save to continue from
		});

		// Set InstructorDashboardScreen observers
		mainMenuScreen.addLoadGameListener(v -> {
			setScreen(saveScreen);
		});
		mainMenuScreen.addHighScoreListener(v -> {
			highScoreScreen.loadTables();  // Update high score tables to display most current information
			setScreen(highScoreScreen);
		});
		mainMenuScreen.addInstructorDashboardListener(v -> {
			setScreen(instructorDashboardScreen);  // Open instructor dashboard from main menu
		});
		instructorDashboardScreen.addMenuListener(v -> {
			setScreen(mainMenuScreen);  // Return to main menu
		});
		instructorDashboardScreen.addManageStudentsListener(v -> {
			setScreen(manageStudentsScreen);       // Enter manage students mode in instructor dashboard
		});

		// Set ManageStudentsScreen observers
		manageStudentsScreen.addBackListener(v -> {
			setScreen(instructorDashboardScreen);  // Enter manage students mode in instructor dashboard
		});
		manageStudentsScreen.addAddStudentListener(studentName -> {

			// Add new student to database
			profileManager.addStudent(studentName);

			// Reload dashboard screens to reflect changes
			instructorDashboardScreen.loadDashboard();
			manageStudentsScreen.loadDashboard();

		});
		manageStudentsScreen.addEditStudentListener(inputString -> {

			// Unpack student information
			String[] studentData = inputString.replace("[", "").replace("]", "").split(", ");

			String currentName = studentData[0];
			String newName = studentData[1];
			int newKnowledgeLevel = Integer.parseInt(studentData[2]);

			// Make changes to student database
			this.profileManager.changeKnowledgeLevel(currentName, newKnowledgeLevel);  // FIXME: Since players stored as references, may be better to just use the getProfile() and PlayerProfile getters/setters directly?
			this.profileManager.renameStudent(currentName, newName);

			// Reload dashboard screens to reflect changes
			manageStudentsScreen.loadDashboard();
			instructorDashboardScreen.loadDashboard();

		});
		manageStudentsScreen.addRemoveStudentListener(studentName -> {

			// Remove student from student database
			profileManager.removeStudent(studentName);

			// Reload dashboard screens to reflect changes
			manageStudentsScreen.loadDashboard();
			instructorDashboardScreen.loadDashboard();

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

	/**
	 * Save a GameState to file.
	 *
	 * @param gs the GameState to save
	 */
	public void saveGameState(GameState gs, String saveName) {
		saveSystem.saveGameState(gs, saveName);
	}

	/**
	 * Load a GameState from file
	 *
	 * @param path the path where the serialized JSON is located
	 */
	public GameState loadGameState(String path) {
		return saveSystem.readGameState(path, assets);
	}
}
