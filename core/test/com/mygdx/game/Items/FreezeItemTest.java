package com.mygdx.game.Items;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FreezeItemTest {
    static GameContext gameContext;
    static AssetManager asset;
    static Skin skin;

    @BeforeAll
    static void setUp() {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();
        skin = asset.get(Config.getInstance().getUiPath());
    }

    @Test
    void use() {
        PlayerProfile profile = new PlayerProfile("test profile", 0, 0, 0);
        // Check buttons with only one player
        GameState state = new GameState(List.of(profile), asset, 0, false);
        FreezeItem freeze = new FreezeItem(skin);
        freeze.use(state.getCurrentPlayer(), state, Mockito.mock(Stage.class));
        assertEquals(0, freeze.usedItemDialog.getButtonTable().getChildren().size);

        // Check with more players
        PlayerProfile p2 = new PlayerProfile("test2", 0, 0, 0);
        PlayerProfile p3 = new PlayerProfile("test3", 0, 0, 0);
        state = new GameState(List.of(profile, p2, p3), asset, 0, false);
        freeze.use(state.getCurrentPlayer(), state, Mockito.mock(Stage.class));
        assertEquals(2, freeze.usedItemDialog.getButtonTable().getChildren().size);
        // Check effect
        freeze.usedItemDialog.getButtonTable().getChild(0).fire(new ChangeListener.ChangeEvent());
        assertTrue(state.getPlayerList().get(1).isFrozen());
    }
}