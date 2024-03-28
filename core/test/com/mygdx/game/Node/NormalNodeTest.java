package com.mygdx.game.Node;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Config;
import com.mygdx.game.Player;
import com.mygdx.game.PlayerProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalNodeTest {
    private AssetManager asset = new AssetManager();
    private PlayerProfile profile = new PlayerProfile("test",0,0,0);
    private NormalNode nodeNormal = new NormalNode(0,0,asset);
    private Player p = new Player(profile,asset);
    private int gain = nodeNormal.getBaseMoney();
    @BeforeEach
    void setUp() {
        p.setMoney(0);
        Config config = Config.getInstance();
        asset.load(config.getUiPath(), Skin.class);
        asset.load(config.getTilePath(), Texture.class);
        asset.load(config.getStarTilePath(), Texture.class);
        asset.load(config.getEventTilePath(), Texture.class);
        asset.load(config.getPenaltyTilePath(), Texture.class);
        asset.load(config.getPlayerPath(), Texture.class);
        asset.load("background.jpeg", Texture.class);
        asset.finishLoading();
    }

    @Test
    void activate() {
        nodeNormal.activate(p,null,true);
        assertEquals(gain, p.getMoney());
    }
}