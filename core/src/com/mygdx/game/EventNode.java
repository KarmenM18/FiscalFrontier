package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Observer.Observable;

import java.util.Map;

public class EventNode extends Node {
    protected Observable<Void> callEventNode = new Observable<Void>();
    protected int penaltyAmount = 5;
    protected Texture eventTexture;

    public EventNode(int mapX, int mapY, boolean north, boolean east, boolean south, boolean west, Map<String, Node> map, AssetManager assets) {
        super(mapX, mapY, north, east, south, west, map, assets);
    }

    public EventNode(int mapX, int mapY, AssetManager assets) {
        super(mapX, mapY, assets);
    }

    private EventNode() {}

    @Override
    public void loadTextures(AssetManager assets) {
        Config config = Config.getInstance();
        eventTexture = assets.get(config.getEventTilePath());
        sprite.setTexture(eventTexture);
    }

    @Override
    public void activate(Player player, SpriteBatch batch) {
        //GameBoard.globalEventNode(penaltyAmount, player);
        //TODO try to call the globalEvent method from Gameboard
    }
}
