package com.mygdx.game.Node;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Config;
import com.mygdx.game.Node.Node;
import com.mygdx.game.Player;

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

    /**
     * necessary for serialization
     */
    private PenaltyNode() {}

    @Override
    public void loadTextures(AssetManager assets) {
        Config config = Config.getInstance();
        tileTexture = assets.get(config.getTilePath());
        penaltyTexture = assets.get(config.getPenaltyTilePath());
        sprite.setTexture(penaltyTexture);
    }


    /**
     * TODO need to discuss easy/hard mode difference 
     * @param player
     * @param batch
     */
    @Override
    public void activate(Player player, SpriteBatch batch) {
        if(player.getHasShield()){
            //do nothing
        }else if(player.getMoney() >= penaltyAmount){
            //maybe add logic on the penalty graphic
            player.setMoney(player.getMoney() - penaltyAmount);
        }else if(player.getStars() > 0){
            player.setStars(player.getStars() - 1);
        }
    }
}
