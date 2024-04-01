package com.mygdx.game.Node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ray3k.stripe.FreeTypeSkinLoader;
import org.mockito.Mockito;


import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class StarNodeTest {
    private AssetManager asset;
    private GameContext gameContext;
    private PlayerProfile profile;

    private StarNode starNode;
    private Player p;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();

        profile = new PlayerProfile("test",0,0,0);
        starNode = new StarNode(0,0, asset);

        p = new Player(profile,asset);
        p.setMoney(0);
    }

    @org.junit.jupiter.api.Test
    void activateTest() throws NoSuchFieldException, IllegalAccessException {
        Field costField = StarNode.class.getDeclaredField("starCost");
        costField.setAccessible(true);
        // Try normal mode
        starNode.activate(p, Mockito.mock(SpriteBatch.class), Mockito.mock(Stage.class),
                asset.get(Config.getInstance().getUiPath()), Mockito.mock(GameBoard.class), false);
        int starCost = (Integer)costField.get(starNode);
        assertEquals(100, starCost);
        // Hard mode
        starNode.activate(p, Mockito.mock(SpriteBatch.class), Mockito.mock(Stage.class),
                asset.get(Config.getInstance().getUiPath()), Mockito.mock(GameBoard.class), true);
        starCost = (Integer)costField.get(starNode);
        assertEquals(250, starCost);
    }

    @org.junit.jupiter.api.Test
    void starModTest() {
        int prevMoney = p.getMoney();
        starNode.activate(p, Mockito.mock(SpriteBatch.class), Mockito.mock(Stage.class),
                asset.get(Config.getInstance().getUiPath()), Mockito.mock(GameBoard.class), false);
        assertEquals(0, p.getStars());
        starNode.starMod(p);
        assertEquals(1, p.getStars());
        assertEquals(prevMoney - 100, p.getMoney());

        // Hard mode
        prevMoney = p.getMoney();
        starNode.activate(p, Mockito.mock(SpriteBatch.class), Mockito.mock(Stage.class),
                asset.get(Config.getInstance().getUiPath()), Mockito.mock(GameBoard.class), true);
        starNode.starMod(p);
        assertEquals(2, p.getStars());
        assertEquals(prevMoney - 250, p.getMoney());
    }

    @org.junit.jupiter.api.Test
    void checkStarTest() {
        starNode.hasStar = true;
        starNode.checkStar();
        assertTrue(starNode.starSprite.getColor().a == 1.0);
        starNode.hasStar = false;
        starNode.checkStar();
        assertTrue(starNode.starSprite.getColor().a == 0.0);
    }
}