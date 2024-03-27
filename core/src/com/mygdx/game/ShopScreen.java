/*
TODO: Documentation
 */

package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.util.ArrayList;
import java.util.LinkedList;

public class ShopScreen extends GameScreen {
    private Observable<Void> boardEvent = new Observable<Void>();

    private Player currentPlayer;
    private Label title;
    private double shoppingKart;
    private Stock [] stocksAvailable;

    //For GUI
    private Table investments;
    private Table investmentsList;
    private Table background;
    private Table playerInfo;
    private ScrollPane scroller;

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
                    playerInfo.remove();
                    investments.remove();
                    investmentsList.remove();
                    scroller.remove();
                    background.remove();

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
    /*public ArrayList<Item> showItems(Player player) {
        return currentPlayer.getPlayerItems(player);
    }*/

    /**
     * PURPOSE: display the amount of coins that the Player has so far
     * @param player who's currently accessing the ShopScreen
     * @return number of coins that the Player has
     */
    /*public int showCoins(Player player) {
        return currentPlayer.viewTotalCoins(player);
    }*/

    /*public ArrayList<Item> showCurrentOwnedItems(Player player) {
       for (int i = 0; i < player.getItems().size(); i++) {
           System.out.println("Item " + (i + 1) + ": " + player.getItems().get(i).getName());
       }
       return currentPlayer.getPlayerItems(player);
    }*/

    public void updateScreen() {
        showAvailableInvestments();

        background = new Table();
        background.setFillParent(true);
        background.add(investmentsList).fill();
        this.stage.addActor(background);
    }

    /**
     * Method to show the current information of stocks that are available to buy
     * to the user.
     */
    private void showAvailableInvestments() {
        System.out.println("Entered Investments");
        investments = new Table();

        //UI implementation for all stocks
        for (int i = 0; i < 6; i++) {
            Label ticker = new Label("Ticker Name: " + stocksAvailable[i].getTickerName(), skin);
            Label stockDescription = new Label(this.stocksAvailable[i].getDescription(), skin);
            Label stockPrice = new Label("Stock Price: " + this.stocksAvailable[i].getPrice(), skin);
            Label stockPriceChange = new Label("Stock Price Change Since Last Update: " + this.stocksAvailable[i].getPriceChange() + "%", skin);
            Label stockDivPayChange = new Label("Dividend Pay Change Since Last Update: " + this.stocksAvailable[i].getDivPayChange() + "%", skin);

            ticker.setFontScale(3);

            ticker.setWrap(true);
            stockDescription.setWrap(true);
            stockPrice.setWrap(true);
            stockPriceChange.setWrap(true);
            stockDivPayChange.setWrap(true);

            ticker.setAlignment(Align.center);
            stockDescription.setAlignment(Align.left);
            stockPrice.setAlignment(Align.left);
            stockPriceChange.setAlignment(Align.left);
            stockDivPayChange.setAlignment(Align.left);


            investments.add(ticker).width(500).height(50).left();
            investments.row();
            investments.add(stockPrice).width(500).height(30).left();
            investments.row();
            investments.add(stockPriceChange).width(500).height(30).left();
            investments.row();
            investments.add(stockDivPayChange).width(500).height(30).left();
            investments.row();
            investments.add(stockDescription).width(500).height(300).left();
            investments.row();
        }

        scroller = new ScrollPane(investments, skin);
        scroller.layout();
        scroller.setScrollBarPositions(false,true);
        scroller.setScrollbarsVisible(true);

        investmentsList = new Table();
        investmentsList.add(scroller).left().fillY().expandX();
    }

    private void showPlayerInfo() {
        playerInfo = new Table();
    }

    /**
     * Method to move to check out screen to verify if the player would like
     * to purchase a break-down of selected items
     */
    public void checkOut() {

    }

    public void addBoardListener(Observer<Void> ob) { boardEvent.addObserver(ob); }

    /**
     * Updates to the current player's information
     * @param player PlayerProfile to access player's items, stocks and current funds
     */
    public void setCurrentPlayer(Player player) {this.currentPlayer = player;}

    public void setStocksAvailable (Stock [] stocksAvailable) {this.stocksAvailable = stocksAvailable;}
}