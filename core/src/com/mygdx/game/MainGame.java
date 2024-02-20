/*
* Main class of the game
* Contains the various screens of the game, as well as the AssetManager and the Config
 */

package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainGame extends Game {
	SpriteBatch batch;
	private GameBoard gameBoard;
	private MainMenuScreen mainMenuScreen;
	private AssetManager assets = new AssetManager();
	private Config config = new Config();

	List<PlayerProfile> profileList; // List of player profiles
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		assets.load(config.uiPath, Skin.class);
		assets.finishLoading(); // Load assets before continuing;

		// Load screens
		mainMenuScreen = new MainMenuScreen(this, assets, config);
		gameBoard = new GameBoard(this, assets, config);

		// Load players from save if possible
		if (Utility.fileExists(config.playerSavePath)) {
			profileList = (List<PlayerProfile>)Utility.loadObject(config.playerSavePath);
		} else {
			profileList = new ArrayList<PlayerProfile>();
			profileList.add(new PlayerProfile("Player 1"));
			profileList.add(new PlayerProfile("Player 2"));
			profileList.add(new PlayerProfile("Player 3"));
			profileList.add(new PlayerProfile("Player 4"));
		}

		// Set starting screen
		setScreen(mainMenuScreen);
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
	 * @return Get MainMenuScreen object
	 */
	public MainMenuScreen getMainMenuScreen() {
		return mainMenuScreen;
	}

	/**
	 * @return Get GameBoard object
	 */
	public GameBoard getGameBoard() {
		return gameBoard;
	}

	// Saving stuff TODO: Save manager
	/**
	 * Save player profile list to file
	 */
	public void saveProfiles() {
		Utility.saveObject(profileList, config.playerSavePath);
	}
	/**
	 * Save a GameState to file
	 */
	public void saveGameState(GameState gs) {
		Utility.saveObject(gs, config.gameStateSavePath);
	}
	/**
	 * Load a GameState from file
	 */
	public GameState loadGameState(String filename) {
		try {
			return (GameState) Utility.loadObject(filename);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
