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

	List<Player> playerList; // List of players
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		assets.load(config.uiPath, Skin.class);
		assets.finishLoading(); // Load assets before continuing;

		// Load screens
		mainMenuScreen = new MainMenuScreen(this, assets, config);
		gameBoard = new GameBoard(this, assets, config);

		// Load players from save if possible
		File f = new File(config.playerSavePath);
		if (f.exists() && f.isFile()) {
			playerList = (List<Player>)Utility.loadObject(config.playerSavePath);
		} else {
			playerList = new ArrayList<Player>();
			playerList.add(new Player("Player 1", Utility.getRandom(0, 1000), Utility.getRandom(0, 1000), Utility.getRandom(0, 1000)));
			playerList.add(new Player("Player 2", Utility.getRandom(0, 1000), Utility.getRandom(0, 1000), Utility.getRandom(0, 1000)));
			playerList.add(new Player("Player 3", Utility.getRandom(0, 1000), Utility.getRandom(0, 1000), Utility.getRandom(0, 1000)));
			playerList.add(new Player("Player 4", Utility.getRandom(0, 1000), Utility.getRandom(0, 1000), Utility.getRandom(0, 1000)));
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
	public Screen getMainMenuScreen() {
		return mainMenuScreen;
	}

	/**
	 * @return Get GameBoard object
	 */
	public Screen getGameBoard() {
		return gameBoard;
	}

	/**
	 * Save player list to file
	 */
	public void savePlayers() {
		Utility.saveObject(playerList, config.playerSavePath);
	}
}
