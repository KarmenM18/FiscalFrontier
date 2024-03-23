package com.mygdx.game.Items;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
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

    @Test
    void use() {
        Item item = new Item("testItem", Mockito.mock(Skin.class));
        Player player = Mockito.mock(Player.class);
        GameState state = Mockito.mock(GameState.class);
        Stage stage = Mockito.mock(Stage.class);

        boolean result = item.use(player, state, stage);
        assertTrue(result);
    }

    @Test
    void getName() {
        Item item = new Item("testItem", Mockito.mock(Skin.class));
        Player player = Mockito.mock(Player.class);
        GameState state = Mockito.mock(GameState.class);
        Stage stage = Mockito.mock(Stage.class);

        assertEquals("testItem", item.getName());
    }
}