package com.mygdx.game.Node;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Config;
import com.mygdx.game.GameBoard;
import com.mygdx.game.Node.Node;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Player;
import com.mygdx.game.Utility;
import sun.nio.ch.Util;

import java.awt.*;
import java.util.Map;


public class StarNode extends Node {
    int starCost;
    public boolean hasStar = true;
    private Observable<Void> starEvent = new Observable<Void>();
    private Dialog buyStarDialog;
    private Dialog starDialog;

    Texture starTexture;


    public StarNode(int mapX, int mapY, boolean north, boolean east, boolean south, boolean west, Map<String, Node> map, AssetManager assets) {
        super(mapX, mapY, north, east, south, west, map, assets);
        checkStar();
    }

    public StarNode(int mapX, int mapY, AssetManager assets) {
        super(mapX, mapY, assets);
        checkStar();
    }
    /**
     * necessary for serialization
     */
    private StarNode() {}

    @Override
    public void loadTextures(AssetManager assets) {
        Config config = Config.getInstance();
        tileTexture = assets.get(config.getTilePath());
        starTexture = assets.get(config.getStarTilePath());
        checkStar();
    }

    @Override
    public void activate(Player player, SpriteBatch batch, boolean hardmode) {

    }

    /**
     * activate the star purchase dialog,
     * TODO add a delay to move cam until player confirms action
     * @param player the Player who landed on the Node
     * @param batch the SpriteBatch to draw on
     * @param stage the GameBoard's Stage
     * @param skin the GameBoard's Skin
     * @param board the GameBoard executing the function
     * @param hardmode whether the gameState is in hard mode
     * @return true if the Node will handle changing the turn, false otherwise
     */
    public boolean activate(Player player, SpriteBatch batch, Stage stage, Skin skin, GameBoard board, boolean hardmode) {
        if(!hardmode){
            starCost = 100;
        }else {
            starCost = 250;
        }
        if(hasStar){
            if(player.getMoney() >= starCost){
                buyStarDialog = new Dialog("Buying Star", skin) {
                    @Override
                    protected void result(Object object) {
                        if ((Boolean) object) {
                            starMod(player, true, hardmode);
                        }else{
                            starMod(player, false, hardmode);
                        }

                        // Inform GameState to change the turn
                        board.turnChange();
                    }
                };
                //TODO add Y/N keyboard shortcut for purchase confirmation
                buyStarDialog.text("Do you want to buy the star?");
                buyStarDialog.button("Yes", true);
                buyStarDialog.button("No", false);
                buyStarDialog.show(stage);
                //TODO dialog for denied purchase
            }else{
                starDialog = new Dialog("Can't buy Star", skin) {
                    @Override
                    protected void result(Object object) {
                        if ((Boolean) object) {
                            // Inform GameState to change the turn
                            board.turnChange();
                        }
                    }
                };
                starDialog.text("Sorry you don't have enough money to buy a star");
                starDialog.button("Ok", true);
                starDialog.show(stage);
                //TODO dialog for purchasing
            }
        }

        return true;
    }

    /**
     * hardmode higher price
     * @param player
     * @param buy
     * @param hardmode
     */
    private void starMod(Player player, boolean buy, boolean hardmode){
        if(hardmode){

        }
        if(buy){
            this.hasStar = false;
            player.addStar();
            player.setMoney(player.getMoney() - starCost);
            checkStar();
        }else{
            this.hasStar = true;
            checkStar();
        }
    }
    /**
     * Changes texture to a regular tile if the star was grabbed.
     */
    public void checkStar() {
        if (hasStar) {
            sprite.setTexture(starTexture);
        }
        else {
            sprite.setTexture(tileTexture);
        }
    }
}
