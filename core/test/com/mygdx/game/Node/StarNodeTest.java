package com.mygdx.game.Node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Player;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Config;
import com.mygdx.game.PlayerProfile;
import com.mygdx.game.TestGame;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;

class StarNodeTest {
    private AssetManager asset;
    private PlayerProfile profile;

    private StarNode starNode;
    private Player p;
    private boolean hasStar;
    private Config config;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        HeadlessApplicationConfiguration GDXConfig = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new TestGame(), GDXConfig);
        Gdx.gl = Mockito.mock(GL20.class);
        asset = new AssetManager();

        config = Config.getInstance();
        asset.load(config.getUiPath(), Skin.class);
        asset.load(config.getTilePath(), Texture.class);
        asset.load(config.getStarTilePath(), Texture.class);
        asset.load(config.getEventTilePath(), Texture.class);
        asset.load(config.getPenaltyTilePath(), Texture.class);
        asset.load(config.getPlayerPath(), Texture.class);
        asset.load("background.jpeg", Texture.class);
        asset.finishLoading();

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
        assertEquals(starNode.sprite.getTexture(), asset.get(config.getStarTilePath()));
        starNode.hasStar = false;
        starNode.checkStar();
        assertEquals(starNode.sprite.getTexture(), asset.get(config.getTilePath()));
    }
}