package com.mygdx.game.Items;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.*;
import com.mygdx.game.Node.Node;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BikeTest {
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
        GameState state = new GameState(List.of(profile), asset, 0, false);
        Bike bike = new Bike(skin);
        Player currPlayer = state.getCurrentPlayer();
        bike.use(state.getCurrentPlayer(), state, Mockito.mock(Stage.class));
        assertFalse(currPlayer.canRoll());
        // Check that all nodes are accessible excluding the player's current node
        assertEquals(state.getNodeMap().size() - 1, currPlayer.getReachablePaths().size());
    }
}