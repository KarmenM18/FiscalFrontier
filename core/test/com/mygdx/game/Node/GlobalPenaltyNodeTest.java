package com.mygdx.game.Node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.*;
import com.mygdx.game.Observer.Observer;
import com.ray3k.stripe.FreeTypeSkinLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class GlobalPenaltyNodeTest {
    private PlayerProfile profile;
    private GameState state;
    private GameContext gameContext;
    private AssetManager asset;
    private GlobalPenaltyNode globalPenaltyNode;
    private Player p;
    private boolean easy = false;
    private boolean hard = true;
    private int iniStar = 1;
    private int penalty;

    @BeforeEach
    void setUp() {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();

        profile = new PlayerProfile("test",0,0,0);
        globalPenaltyNode = new GlobalPenaltyNode(0,0, asset);

        penalty = globalPenaltyNode.penaltyAmount;

        p = new Player(profile,asset);
        p.setMoney(penalty);
        p.setStars(iniStar);
        p.setHasShield(true);
    }

    @Test
    //TODO figure out how to get GameState to activate the logic
    void activateTestEasy() {
        boolean mode = easy;
        //test shield
        assertTrue(p.getHasShield());
        globalPenaltyNode.activate(p,null,mode);
        assertFalse(p.getHasShield());

        //test money
        p.setMoney(penalty);
        globalPenaltyNode.activate(p,null,mode);
        assertEquals(0, p.getMoney());

        //test non-negative
        globalPenaltyNode.activate(p,null,mode);
        assertFalse(p.getMoney() < 0);
    }

    @Test
    void activateTestHard() {
        boolean mode = hard;
        //test shield
        assertTrue(p.getHasShield());
        globalPenaltyNode.activate(p,null,mode);
        assertFalse(p.getHasShield());

        //test star
        assertEquals(iniStar, p.getStars());
        globalPenaltyNode.activate(p,null,mode);
        assertEquals(0, p.getStars());

        //test money
        assertEquals(penalty, p.getMoney());

        //test negative
        globalPenaltyNode.activate(p,null,mode);
        assertTrue(p.getMoney() < 0);
    }

}