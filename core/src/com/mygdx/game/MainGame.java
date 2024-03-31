package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ray3k.stripe.FreeTypeSkinLoader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The main class of the game. Handles interaction with the saving system and the switching of screens.
 */
public class MainGame extends Game {
	SpriteBatch batch;

	//All Screens needed for the game
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
	private TutorialScreen tutorialScreen;

	private AssetManager assets = new AssetManager();
	private SaveSystem saveSystem = new SaveSystem();

	private ProfileManager profileManager;

	private boolean debugMode = false; // Indicates that we are in debug mode.
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		// Load assets
		Config config = Config.getInstance();

		// Set renderClear color
		Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);

		// Support TTF fonts
		assets.setLoader(Skin.class, new FreeTypeSkinLoader(assets.getFileHandleResolver()));

		assets.load(config.getUiPath(), Skin.class);
		assets.load(config.getTilePath(), Texture.class);
		assets.load(config.getStarTilePath(), Texture.class);
		assets.load(config.getEventTilePath(), Texture.class);
		assets.load(config.getPenaltyTilePath(), Texture.class);
		assets.load(config.getPlayerPath(), Texture.class);
		assets.load(config.getPlayerFreezePath(), Texture.class);
		assets.load(config.getBackgroundPath(), Texture.class);
		assets.load(config.getPlayerShieldPath(), Texture.class);
		assets.load(config.getMapArrowPath(), Texture.class);
		assets.finishLoading(); // Make sure assets are loaded before continuing.

		// Setup ActionTextSystem
		ActionTextSystem.initSkin(assets.get(config.getUiPath(), Skin.class));

		// Load screens
		mainMenuScreen = new MainMenuScreen(batch, assets);
		gameBoard = new GameBoard(batch, assets);
		pauseScreen = new PauseScreen(batch, assets);
		shopScreen = new ShopScreen(batch, assets);
		knowledgeListScreen = new KnowledgeListScreen(batch, assets);
		saveScreen = new SaveScreen(batch, assets);
		tutorialScreen = new TutorialScreen(batch, assets);

		profileManager = new ProfileManager("studentInformation.json", "highScoreTable.json", "lifetimeScoreTable.json");
		instructorDashboardScreen = new InstructorDashboardScreen(batch, assets, this.profileManager);
		manageStudentsScreen = new ManageStudentsScreen(batch, assets, this.profileManager);
		endScreen = new EndScreen(batch, assets);
		highScoreScreen = new HighScoreScreen(batch, assets, this.profileManager);
		newGameScreen = new NewGameScreen(batch, assets, profileManager);

		//Set starting screen
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

		//For back to screen buttons
		knowledgeListScreen.addBackToPause(v -> setScreen(pauseScreen));
		tutorialScreen.addBackToMenu(v -> setScreen(mainMenuScreen));

		// Set PauseScreen observers
		pauseScreen.addSaveGameListener(saveName -> saveGameState(gameBoard.getGameState(), saveName));
		pauseScreen.addMenuListener(v -> {
			SoundSystem.getInstance().stopMusic();
			setScreen(mainMenuScreen);
		});
		pauseScreen.addBoardListener(v -> setScreen(gameBoard));
		pauseScreen.addKnowledgeEventListener(v -> {
			knowledgeListScreen.setPlayerKnowledge(gameBoard.getGameState().getCurrentPlayer().getPlayerProfile().getLearned());
			knowledgeListScreen.updateTable();
			setScreen(knowledgeListScreen);
		});

		// Set EndScreen observers
		endScreen.addMenuListener(v -> {
			SoundSystem.getInstance().stopMusic();
			setScreen(mainMenuScreen);
		});
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
			this.profileManager.changeKnowledgeLevel(profile.getName(), profile.getKnowledgeLevel());
			this.profileManager.updateHighScore(profile.getName(), profile.getHighScore());
			this.profileManager.addLifetimeScore(profile.getName(), profile.getLifetimeScore());

			// Reload all screens involving scores and levels to reflect changes
			this.reloadScoreScreens();

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
			newGame.setDebugMode(debugMode);
			gameBoard.setGameState(newGame);
			SoundSystem.getInstance().playMusic();

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
			gs.setDebugMode(debugMode);
			gameBoard.setGameState(gs);
			SoundSystem.getInstance().playMusic();

			setScreen(gameBoard);
		});

		// Set MainMenuScreen observers
		mainMenuScreen.addStartGameListener(v -> setScreen(newGameScreen));
		mainMenuScreen.addContinueGameListener(v -> {
            try {
                saveScreen.loadLatestSave(mainMenuScreen.stage, mainMenuScreen.skin);
            } catch (FileNotFoundException e) {
				Utility.showErrorDialog("Error; saves folder not found", mainMenuScreen.stage, mainMenuScreen.skin);
            }
        });
		//Adding tutorial screen
		mainMenuScreen.addTutorialScreenListener(v -> setScreen(tutorialScreen));
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
		mainMenuScreen.addDebugListener(v -> {
			debugMode = true;
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

			// Add new student to database
			profileManager.addStudent(studentName);

			// Reload all screens involving scores and levels to reflect changes
			this.reloadScoreScreens();

			// Display confirmation of action
			manageStudentsScreen.showConfirmation("Student added successfully!");

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

			// Reload all screens involving scores and levels to reflect changes
			this.reloadScoreScreens();

			// Display confirmation of action
			manageStudentsScreen.showConfirmation("Student edited successfully!");

		});
		manageStudentsScreen.addRemoveStudentListener(studentName -> {

			// Remove student from student database
			profileManager.removeStudent(studentName);

			// Reload all screens involving scores and levels to reflect changes
			this.reloadScoreScreens();

			// Display confirmation of action
			manageStudentsScreen.showConfirmation("Student removed successfully!");

		});
	}

	@Override
	public void render() {
		super.render(); // Render current screen

		// Render debug text and debugdraw if we are in debug mode
		if (debugMode) {
			Stage stage = ((GameScreen)getScreen()).stage;
			stage.setDebugAll(true);

			Batch debugBatch = stage.getBatch();
			BitmapFont font = assets.get(Config.getInstance().getUiPath(), Skin.class).getFont("font");
			font.setColor(Color.RED);

			debugBatch.begin();
			font.draw(debugBatch, "DEBUG MODE ENABLED", 1520, 1080);
			debugBatch.end();
		}
	}
	
	@Override
	public void dispose() {
		mainMenuScreen.dispose();
		gameBoard.dispose();
		pauseScreen.dispose();
		shopScreen.dispose();
		mainMenuScreen.dispose();
		knowledgeListScreen.dispose();
		saveScreen.dispose();
		instructorDashboardScreen.dispose();
		manageStudentsScreen.dispose();
		endScreen.dispose();
		highScoreScreen.dispose();
		newGameScreen.dispose();
		tutorialScreen.dispose();

		batch.dispose();
		assets.dispose();
		super.dispose();
	}

	/**
	 * Save a GameState to file and update the student database and high score tables.
	 *
	 * @param gs the GameState to save
	 */
	public void saveGameState(GameState gs, String saveName) {

		// Save game state
		saveSystem.saveGameState(gs, saveName);

		// Update the level of each player in the game
		for (Player player : gs.getPlayerList()) {
			String studentName = player.getPlayerProfile().getName();
			this.profileManager.changeKnowledgeLevel(studentName, player.getLevel());
		}

		// Reload all screens involving scores and levels to reflect changes
		this.reloadScoreScreens();

	}

	/**
	 * Load a GameState from file
	 *
	 * @param path the path where the serialized JSON is located
	 */
	public GameState loadGameState(String path) {
		return saveSystem.readGameState(path, assets);
	}


	/**
	 * Reloads all screens involving student levels and scores (the instructor dashboard and high score screens) to
	 * reflect the most updated data in the player profile manager.
	 */
	private void reloadScoreScreens() {

		this.instructorDashboardScreen.loadDashboard();
		this.manageStudentsScreen.loadDashboard();
		this.highScoreScreen.loadTables();

	}

}
