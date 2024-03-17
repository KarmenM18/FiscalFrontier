package com.mygdx.game.Node;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Node.Node;
import com.mygdx.game.Player;

import java.util.Map;

public class NormalNode extends Node {
    protected int baseMoney = 5;

    public NormalNode(int mapX, int mapY, boolean north, boolean east, boolean south, boolean west, Map<String, Node> map, AssetManager assets) {
        super(mapX, mapY, north, east, south, west, map, assets);
    }

    public NormalNode(int mapX, int mapY, AssetManager assets) {
        super(mapX, mapY, assets);
    }

    private NormalNode() {}

    @Override
    public void activate(Player player, SpriteBatch batch) {
        //maybe add logic for showing the adding money graphic??
        player.setMoney(player.getMoney() + baseMoney);
    }
}
