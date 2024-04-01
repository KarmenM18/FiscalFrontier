package com.mygdx.game.Items;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ShieldTest {
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
        PlayerProfile profile = new PlayerProfile("test", 0, 0, 0);
        Player p = new Player(profile, asset);
        Shield shield = new Shield(skin);

        // Test against an already active shield
        p.setHasShield(true);
        assertFalse(shield.use(p, Mockito.mock(GameState.class), Mockito.mock(Stage.class)));
        // Test enabling shield
        p.setHasShield(false);
        assertTrue(shield.use(p, Mockito.mock(GameState.class), Mockito.mock(Stage.class)));
        assertTrue(p.getHasShield());
    }
}