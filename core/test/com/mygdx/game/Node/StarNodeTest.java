package com.mygdx.game.Node;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Player;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Config;



import static org.junit.jupiter.api.Assertions.*;

class StarNodeTest {
    private AssetManager assets;
    private StarNode star;
    private Player p;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        star = new StarNode(0,0,true,false,false,false,null,assets);
        p.setMoney(5);
        p.setStars(1);
    }


    @org.junit.jupiter.api.Test
    void checkStarTest() {
        Config config = Config.getInstance();
        star.hasStar = true;
        star.checkStar();
        assertEquals(star.sprite.getTexture(), assets.get(config.getStarTilePath()));
        star.hasStar = false;
        star.checkStar();
        assertEquals(star.sprite.getTexture(), assets.get(config.getTilePath()));
    }

}