package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Map;

public class NormalNode extends Node {
    protected int baseMoney = 5;

    public NormalNode(String id, int x, int y, String north, String east, String south, String west, Map<String, Node> map, AssetManager assets) {
        super(id, x, y, north, east, south, west, map, assets);
    }

    public NormalNode(String id, int x, int y, AssetManager assets) {
        super(id, x, y, assets);
    }

    private NormalNode() {}

    @Override
    public void activate(Player player, SpriteBatch batch) {
        //maybe add logic for showing the node graphic??
        player.setMoney(player.getMoney() + baseMoney);
    }
}
