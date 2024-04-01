package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    static GameContext gameContext;
    static AssetManager asset;

    @BeforeAll
    static void setUp() {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();
    }

    @Test
    void show() {
        GameBoard board = new GameBoard(Mockito.mock(SpriteBatch.class), asset);
        PlayerProfile profile = new PlayerProfile("test",0,0,0);
        GameState state = new GameState(Collections.singletonList(profile), asset, 0, false);
        // Check that it fails to show if there is no gameState set
        assertThrows(IllegalStateException.class, () -> board.show());
        board.setGameState(state);

        assertDoesNotThrow(() -> board.show());
    }

    @Test
    void turnChange() {
        GameBoard board = new GameBoard(Mockito.mock(SpriteBatch.class), asset);
        PlayerProfile profile = new PlayerProfile("test",0,0,0);
        GameState state = new GameState(Collections.singletonList(profile), asset, 0, false);
        board.setGameState(state);

        // Simulate turns until the end of the game
        for (int i = 0; i < 25; i++) {
            board.turnChange();
            assertFalse(board.getGameState().isGameOver());
        }
        board.turnChange();
        assertTrue(board.getGameState().isGameOver());
    }
}