package com.mygdx.game.Node;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Player;

import java.util.Map;

public class NormalNode extends Node {
    protected int baseMoney = 25;

    public NormalNode(int mapX, int mapY, boolean north, boolean east, boolean south, boolean west, Map<String, Node> map, AssetManager assets) {
        super(mapX, mapY, north, east, south, west, assets);
    }

    public NormalNode(int mapX, int mapY, AssetManager assets) {
        super(mapX, mapY, assets);
    }

    /**
     * necessary for serialization
     */
    private NormalNode() {}

    @Override
    public void activate(Player player, SpriteBatch batch, boolean hardmode) {
        //maybe add logic for showing the adding money graphic??
        player.setMoney(player.getMoney() + baseMoney);
    }
    public int getBaseMoney(){
        return baseMoney;
    }
}
