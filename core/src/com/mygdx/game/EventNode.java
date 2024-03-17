package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;
import com.mygdx.game.Observer.Observable;

import java.util.Map;

public class EventNode extends Node {
    // Observable must be transient as the Observer contains a reference to the GameState, creating a loop
    // Recreate observers when deserializing
    transient protected Observable<Void> callEventNode = new Observable<Void>();
    transient protected Observable<Integer> globalEvent = new Observable<Integer>();
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

    /**
     * coule be used with item too
     * @param player
     * @param batch
     */
    @Override
    public void activate(Player player, SpriteBatch batch) {
        globalEvent.notifyObservers(penaltyAmount);
    }
    public void addEventListener(Observer<Integer> ob) {globalEvent.addObserver(ob); }

}
