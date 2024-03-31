package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ray3k.stripe.FreeTypeSkinLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SaveSystemTest {
    static private SaveSystem saver;
    static private AssetManager asset;
    static private GameContext gameContext;

    @BeforeAll
    static void setUp() {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();
        saver = new SaveSystem();
    }

    @Test
    void saveAndReadGameState() {
        PlayerProfile testProfile = new PlayerProfile("TestUser");
        GameState gs = new GameState(Collections.singletonList(testProfile), asset, 0, false);
        saver.saveGameState(gs, "testSave");
        Path saveFile = Paths.get("saves/testSave_1.json");
        assertTrue(Files.exists(saveFile));

        GameState deserialized = saver.readGameState("testSave_1.json", asset);
        // Delete save file
        try {
            Files.delete(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        deserialized.loadTextures(asset);

        // Compare the Players
        Player deserPlayer = deserialized.getCurrentPlayer();
        Player origPlayer = gs.getCurrentPlayer();
        assertEquals(deserPlayer.getPlayerProfile().getName(), origPlayer.getPlayerProfile().getName());
        assertEquals(deserPlayer.getScore(), origPlayer.getScore());
        assertEquals(deserPlayer.getStars(), origPlayer.getStars());
        assertEquals(deserPlayer.getMoney(), origPlayer.getMoney());
        assertEquals(deserPlayer.getItems().size(), origPlayer.getItems().size());
        assertEquals(deserPlayer.isFrozen(), origPlayer.isFrozen());
        assertEquals(deserPlayer.getCurrentTile(), origPlayer.getCurrentTile());
        assertEquals(deserPlayer.getDieRoll(), origPlayer.getDieRoll());
        assertEquals(deserPlayer.canMove(), origPlayer.canMove());
        assertEquals(deserPlayer.canRoll(), origPlayer.canRoll());
        assertEquals(deserPlayer.getLevel(), origPlayer.getLevel());

        // Compare the GameStates
        assertEquals(deserialized.getPlayerList().size(), gs.getPlayerList().size());
        assertEquals(deserialized.getNodeMap().size(), gs.getNodeMap().size());
    }
}