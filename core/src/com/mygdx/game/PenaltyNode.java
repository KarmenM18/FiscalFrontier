package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Map;

public class PenaltyNode extends Node {
    protected int penaltyAmount = 5;
    protected Texture penaltyTexture;

    public PenaltyNode(int mapX, int mapY, boolean north, boolean east, boolean south, boolean west, Map<String, Node> map, AssetManager assets) {
        super(mapX, mapY, north, east, south, west, map, assets);
    }

    public PenaltyNode(int mapX, int mapY, AssetManager assets) {
        super(mapX, mapY, assets);
    }

    private PenaltyNode() {}

    @Override
    public void loadTextures(AssetManager assets) {
        Config config = Config.getInstance();
        tileTexture = assets.get(config.getTilePath());
        penaltyTexture = assets.get(config.getPenaltyTilePath());
        sprite.setTexture(penaltyTexture);
    }

    @Override
    public void activate(Player player, SpriteBatch batch) {
        if(player.getMoney() >= penaltyAmount){
            //maybe add logic on the penalty graphic
            player.setMoney(player.getMoney() - penaltyAmount);
        } else {
            //maybe add logic for showing penalty not applied due to not enough mooney
            //return
        }
    }
}
