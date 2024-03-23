package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.mock.audio.MockAudio;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.backends.headless.mock.input.MockInput;
import com.badlogic.gdx.graphics.Texture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SaveSystemTest {
    private SaveSystem saver;
    private AssetManager asset;

    @BeforeEach
    void setUp() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new TestGame(), config);

        saver = new SaveSystem();
        asset = new AssetManager();
        asset.load(Config.getInstance().getPlayerPath(), Texture.class);
        asset.finishLoading();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveGameState() {
        PlayerProfile testProfile = new PlayerProfile("TestUser");
        GameState gs = new GameState(Collections.singletonList(testProfile), asset);
        saver.saveGameState(gs, "testSave");
    }

    @Test
    void readGameState() {
        GameState deserialized = saver.readGameState("testSave", asset);
        assertEquals(deserialized.getCurrentPlayer().getPlayerProfile().getName(), "TestUser");
    }
}