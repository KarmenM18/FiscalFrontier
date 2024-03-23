/*
TODO: Documentation
 */

package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.util.ArrayList;

public class ShopScreen extends GameScreen {
    private Observable<Void> boardEvent = new Observable<Void>();

    private Player currentPlayer;

    private PlayerProfile currentPlayer;
    private Label title;

    /**
     * Constructor.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public ShopScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        title = new Label("ShopScreen", skin);
        stage.addActor(title);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.S) {
                    boardEvent.notifyObservers(null);
                }
                else {
                    return false;
                }

                return true;
            }
        });
    }

    /*
    TODO: create exhaustive final list of all possible store items, link it here
     */

    /**
     * PURPOSE: to show all the available items that the store offers to a Player
     * @param player that's currently accessing the store
     * @return list of items that are available to purchase
     */
    public ArrayList<Item> showItems(Player player) {
        return currentPlayer.getPlayerItems(player);
    }

    /**
     * PURPOSE: display the amount of coins that the Player has so far
     * @param player who's currently accessing the ShopScreen
     * @return number of coins that the Player has
     */
    public int showCoins(Player player) {
        return currentPlayer.viewTotalCoins(player);
    }

    /**
     * PURPOSE: display the items that the Player has unlocked so far
     * @param player who's currently accessing the ShopScreen
     * @return list of items that the Player owns
     */
    public ArrayList<Item> showCurrentOwnedItems(Player player) {
//        for (int i = 0; i < player.getItems().size(); i++) {
//            System.out.println("Item " + (i+1) + ": " + player.getItems().get(i).getName());
//        }
        return currentPlayer.getPlayerItems(player);
    }

    public void showInvestmentsMade() {

    }

    public void showAvailableInvestments() {


    }

    public void checkOut() {

    }


    public void addBoardListener(Observer<Void> ob) { boardEvent.addObserver(ob); }

    /**
     * Updates to the current player's information
     * @param player PlayerProfile to access player's items, stocks and current funds
     */
    public void setCurrentPlayer(Player player) {this.currentPlayer = player;}
}