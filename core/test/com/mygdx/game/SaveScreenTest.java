package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaveScreenTest {
    static GameContext gameContext;
    static AssetManager asset;
    static SaveSystem saver;
    static Skin skin;
    static String filename;

    @BeforeAll
    static void setUp() throws IOException {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();
        saver = new SaveSystem();
        skin = asset.get(Config.getInstance().getUiPath());
        File saveFolder = new File("saves");
        for (File file: saveFolder.listFiles()) {
            Files.deleteIfExists(file.toPath());
        }
    }

    @Test
    void getUniqueID() throws IOException {
        PlayerProfile profile = new PlayerProfile("test", 0, 0, 0);
        SaveScreen sScreen = new SaveScreen(Mockito.mock(SpriteBatch.class), asset);
        // Check for bad ids
        for (int i = 0; i < 50; i++) {
            GameState g = new GameState(List.of(profile), asset, i, false);
            saver.saveGameState(g, "testG");
            assertTrue(sScreen.getUniqueID(saver) > i);
        }
        // Cleanup
        for (int i = 1; i <= 50; i++) {
            Files.deleteIfExists(Path.of("saves/testG_" + i + ".json"));
        }
    }

    @Test
    void loadLatestSave() throws IOException {
        SaveScreen sScreen = new SaveScreen(Mockito.mock(SpriteBatch.class), asset);
        sScreen.addLoadSaveListener(str -> filename = str);
        filename = "unchanged";
        // Try with no saves
        sScreen.loadLatestSave(Mockito.mock(Stage.class), skin);
        assertEquals("unchanged", filename);

        // Add some saves
        PlayerProfile profile = new PlayerProfile("test", 0, 0, 0);
        for (int i = 0; i < 10; i++) {
            GameState g = new GameState(List.of(profile), asset, i, false);
            saver.saveGameState(g, "testG");
            assertTrue(sScreen.getUniqueID(saver) > i);
        }
        // Check if we get the latest one
        sScreen.loadLatestSave(Mockito.mock(Stage.class), skin);
        assertEquals("testG_10.json", filename);

        // Cleanup
        for (int i = 1; i <= 10; i++) {
            Files.deleteIfExists(Path.of("saves/testG_" + i + ".json"));
        }
    }

    @Test
    void deleteByID() throws IOException {
        PlayerProfile profile = new PlayerProfile("test", 0, 0, 0);
        SaveScreen sScreen = new SaveScreen(Mockito.mock(SpriteBatch.class), asset);

        for (int i = 0; i < 5; i++) {
            GameState g = new GameState(List.of(profile), asset, 0, false);
            saver.saveGameState(g, "testG");
        }
        for (int i = 0; i < 5; i++) {
            GameState g = new GameState(List.of(profile), asset, 1, false);
            saver.saveGameState(g, "testG");
        }
        // Only delete second half
        sScreen.deleteByID(1, saver);
        File saveFolder = new File("saves");
        assertEquals(5, saveFolder.listFiles().length);

        // Cleanup
        for (int i = 1; i <= 10; i++) {
            Files.deleteIfExists(Path.of("saves/testG_" + i + ".json"));
        }
    }
}