package com.mygdx.game.Node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class PenaltyNodeTest {
    private PlayerProfile profile;
    private AssetManager asset;
    private PenaltyNode penaltyNode;
    private Player p;
    private boolean easy = false;
    private boolean hard = true;
    private int iniStar = 1;
    private int penalty;

    @BeforeEach
    void setUp() {
        HeadlessApplicationConfiguration GDXConfig = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new TestGame(), GDXConfig);
        Gdx.gl = Mockito.mock(GL20.class);
        asset = new AssetManager();

        Config config = Config.getInstance();
        asset.load(config.getUiPath(), Skin.class);
        asset.load(config.getTilePath(), Texture.class);
        asset.load(config.getStarTilePath(), Texture.class);
        asset.load(config.getEventTilePath(), Texture.class);
        asset.load(config.getPenaltyTilePath(), Texture.class);
        asset.load(config.getPlayerPath(), Texture.class);
        asset.load("background.jpeg", Texture.class);
        asset.finishLoading();

        profile = new PlayerProfile("test",0,0,0);
        penaltyNode = new PenaltyNode(0,0, asset);

        penalty = penaltyNode.penaltyAmount;

        p = new Player(profile,asset);
        p.setMoney(penalty);
        p.setStars(iniStar);
        p.setHasShield(true);
    }

    @Test
    void activateTestEasy() {
        boolean mode = easy;

        //testing shield
        assertTrue(p.getHasShield());
        penaltyNode.activate(p,null,mode);
        assertFalse(p.getHasShield());

        //testing money
        assertEquals(penalty, p.getMoney());
        penaltyNode.activate(p,null,mode);
        assertEquals(0, p.getMoney());

        //testing non-negative
        penaltyNode.activate(p,null,mode);
        assertFalse(p.getMoney() < 0);
    }
    @Test
    void activateTestHard() {
        boolean mode = hard;

        //testing shield
        assertTrue(p.getHasShield());
        penaltyNode.activate(p,null,mode);
        assertFalse(p.getHasShield());

        //testing star
        assertEquals(iniStar, p.getStars());
        penaltyNode.activate(p,null,mode);
        assertEquals(iniStar - 1, p.getStars());

        //testing money
        assertEquals(penalty, p.getMoney());
        penaltyNode.activate(p,null,mode);
        assertEquals(0, p.getMoney());

        //testing negative
        penaltyNode.activate(p,null,mode);
        assertTrue(p.getMoney() < 0);
    }
}