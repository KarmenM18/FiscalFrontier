package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Map;


public class StarNode extends Node {
    int starCost = 10;
    boolean hasStar = true;
    Texture starTexture;

    public StarNode(int mapX, int mapY, boolean north, boolean east, boolean south, boolean west, Map<String, Node> map, AssetManager assets) {
        super(mapX, mapY, north, east, south, west, map, assets);
        checkStar();
    }

    public StarNode(int mapX, int mapY, AssetManager assets) {
        super(mapX, mapY, assets);
        checkStar();
    }
    private StarNode() {}

    @Override
    public void loadTextures(AssetManager assets) {
        Config config = Config.getInstance();
        tileTexture = assets.get(config.getTilePath());
        starTexture = assets.get(config.getStarTilePath());
        checkStar();
    }

    @Override
    public void activate(Player player, SpriteBatch batch) {
        if (hasStar) {
            if(player.getMoney() < starCost){
                this.hasStar = true;
                checkStar();
                //TODO make sure to remove this line
                System.out.println("you don't get any star!");
            }else {
                // TODO setup purchase logic
                player.addStar();
                this.hasStar = false;
                checkStar();
            }
        }
    }

    /**
     * Changes texture to a regular tile if the star was grabbed.
     */
    public void checkStar() {
        if (hasStar) {
            sprite.setTexture(starTexture);
        }
        else {
            sprite.setTexture(tileTexture);
        }
    }
}
