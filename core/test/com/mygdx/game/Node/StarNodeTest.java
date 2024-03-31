package com.mygdx.game.Node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ray3k.stripe.FreeTypeSkinLoader;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;

class StarNodeTest {
    private AssetManager asset;
    private GameContext gameContext;
    private PlayerProfile profile;

    private StarNode starNode;
    private Player p;
    private boolean hasStar;


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
    void activateTest() {
        //TODO make test
    }

    @org.junit.jupiter.api.Test
    void starModTest() {
        //TODO make test
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