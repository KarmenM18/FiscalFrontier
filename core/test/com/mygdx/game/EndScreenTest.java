package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class EndScreenTest {
    static GameContext gameContext;
    static AssetManager asset;

    @BeforeAll
    static void setUp() throws NoSuchFieldException {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();
    }

    static PlayerProfile resultProfile;

    @Test
    void setGameState() {
        // Check if Profiles are updated by the screen
        EndScreen screen = new EndScreen(Mockito.mock(SpriteBatch.class), asset);
        PlayerProfile profile = new PlayerProfile("test",0,125,0);
        screen.addUpdateScoreListener(p -> resultProfile = p);
        screen.addDeleteSavesListener(id -> assertEquals(1234, id));

        GameState state = new GameState(Collections.singletonList(profile), asset, 1234, false);
        state.getCurrentPlayer().setMoney(1000);
        state.getCurrentPlayer().setStars(2);
        state.getCurrentPlayer().getPlayerProfile().setKnowledgeLevel(5);
        state.getCurrentPlayer().calculateScore();
        screen.setGameState(state);
        assertEquals(3000, resultProfile.getHighScore());
        assertEquals(3000, resultProfile.getLifetimeScore());
        assertEquals(5, resultProfile.getKnowledgeLevel());
    }
}