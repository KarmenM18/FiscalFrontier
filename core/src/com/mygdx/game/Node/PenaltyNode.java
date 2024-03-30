package com.mygdx.game.Node;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Config;
import com.mygdx.game.Node.Node;
import com.mygdx.game.Player;

import java.util.Map;

public class PenaltyNode extends Node {
    protected int penaltyAmount = 50;
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
     * TODO hardmode lose star and go negative
     * easy mode no lose star and no negative
     * @param player
     * @param batch
     */
    @Override
    public void activate(Player player, SpriteBatch batch, boolean hardmode) {
        if (player.useShield()) return;

        if(!hardmode){
            player.setMoney(player.getMoney() - penaltyAmount);

            if (player.getMoney() < 0) {
                player.setMoney(0);
            }
        }else {
            if (player.getStars() > 0) {
                player.setStars(player.getStars() - 1);
            } else {
                player.setMoney(player.getMoney() - penaltyAmount);
            }
        }
    }
}
