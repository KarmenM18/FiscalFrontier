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

class NormalNodeTest {
    private AssetManager asset;
    private PlayerProfile profile;
    private NormalNode normalNode;
    private Player p;
    private int baseMoney;
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
        normalNode = new NormalNode(0,0, asset);

        baseMoney = normalNode.getBaseMoney();

        p = new Player(profile,asset);
        p.setMoney(0);
    }

    @Test
    void activate() {
        normalNode.activate(p,null,true);
        assertEquals(baseMoney, p.getMoney());
    }
}