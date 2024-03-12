package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;


public class StarNode extends Node {
    Texture starTexture;
    boolean hasStar = true;

    public StarNode(String id, int x, int y, String north, String east, String south, String west, Map<String, Node> map, AssetManager assets) {
        super(id, x, y, north, east, south, west, map, assets);

        Config config = Config.getInstance();

        starTexture = assets.get(config.getStarTilePath());
        sprite.setTexture(starTexture);
    }

    private StarNode() {}

    @Override
    public void loadTextures(AssetManager assets) {
        Config config = Config.getInstance();
        tileTexture = assets.get(config.getTilePath());
        starTexture = assets.get(config.getStarTilePath());
        if (hasStar) {
            sprite.setTexture(starTexture);
        }
        else {
            sprite.setTexture(tileTexture);
        }
    }

    @Override
    public void activate(Player player) {
        if (hasStar) {
            // Add star to player and remove it from the tile
            player.addStar();
            sprite.setTexture(tileTexture);
            hasStar = false;
        }
    }
}
