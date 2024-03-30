package com.mygdx.game.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.*;
import com.ray3k.stripe.FreeTypeSkinLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    static private AssetManager asset;
    static private GameContext gameContext;

    @BeforeAll
    static void setUp() {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();
    }

    @Test
    void use() {
        Item item = new Item("testItem", asset.get(Config.getInstance().getUiPath()));
        Player player = Mockito.mock(Player.class);
        GameState state = Mockito.mock(GameState.class);
        Stage stage = Mockito.mock(Stage.class);

        boolean result = item.use(player, state, stage);
        assertTrue(result);
    }

    @Test
    void getName() {
        Item item = new Item("testItem", asset.get(Config.getInstance().getUiPath()));
        Player player = Mockito.mock(Player.class);
        GameState state = Mockito.mock(GameState.class);
        Stage stage = Mockito.mock(Stage.class);

        assertEquals("testItem", item.getName());
    }

    @Test
    void isPassive() {
        Item item = new Item("testItem", asset.get(Config.getInstance().getUiPath()));
        Player player = Mockito.mock(Player.class);
        GameState state = Mockito.mock(GameState.class);
        Stage stage = Mockito.mock(Stage.class);

        assertFalse(item.isPassive());

        Item passiveItem = new Item("testPassiveItem", true, asset.get(Config.getInstance().getUiPath()));
        assertTrue(passiveItem.isPassive());
    }

    @Test
    void loadTextures() {
        Item item = new Item("testItem", asset.get(Config.getInstance().getUiPath()));
        Player player = Mockito.mock(Player.class);
        GameState state = Mockito.mock(GameState.class);
        Stage stage = Mockito.mock(Stage.class);

        item.loadTextures(asset.get(Config.getInstance().getUiPath()));
        assertNotNull(item.usedItemDialog);
    }
}