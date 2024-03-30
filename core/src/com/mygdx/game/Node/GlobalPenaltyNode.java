package com.mygdx.game.Node;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Config;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;
import com.mygdx.game.Player;

import java.util.Map;

public class GlobalPenaltyNode extends Node {
    // Observable must be transient as the Observer contains a reference to the GameState, creating a loop
    // Recreate observers when deserializing
    transient protected Observable<Void> callEventNode = new Observable<Void>();
    transient protected Observable<Integer> globalEvent = new Observable<Integer>();
    protected int penaltyAmount = 50;
    protected Texture eventTexture;

    public GlobalPenaltyNode(int mapX, int mapY, boolean north, boolean east, boolean south, boolean west, Map<String, Node> map, AssetManager assets) {
        super(mapX, mapY, north, east, south, west, map, assets);
    }


    public GlobalPenaltyNode(int mapX, int mapY, AssetManager assets) {
        super(mapX, mapY, assets);
    }

    /**
     * necessary for serialization
     */
    private GlobalPenaltyNode() {}

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
    public void activate(Player player, SpriteBatch batch, boolean hardmode) {
        //setting penalty amount to be double, actual activation is in GameState
        if(hardmode){
            penaltyAmount *= 2;
        }
        //notify observer in GameState
        globalEvent.notifyObservers(penaltyAmount);
    }
    public void addEventListener(Observer<Integer> ob) {globalEvent.addObserver(ob); }

    //TODO decide where the star selling should go, shop/sellnode or something else
    //Hardmode everyone lose star if they have money
    /*
    public void sellStar(Player p, int starToMoney, int starsToSell){
        if(p.getStars() <= 0){
            //show dialogue for player has no star
        }else{
            int moneyGained = starToMoney * starsToSell;
            p.setStars(p.getStars() - starsToSell);
            p.setMoney(p.getMoney() + moneyGained);
        }
    }
    */
}
