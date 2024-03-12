package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Map;

public class PenaltyNode extends Node{
    protected int penaltyAmount = 5;
    protected Texture penaltyTexture;

    public PenaltyNode(String id, int x, int y, String north, String east, String south, String west, Map<String, Node> map, AssetManager assets) {
        super(id, x, y, north, east, south, west, map, assets);
    }

    public PenaltyNode(String id, int x, int y, AssetManager assets) {
        super(id, x, y, assets);
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
