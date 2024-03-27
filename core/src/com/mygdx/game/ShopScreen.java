/*
TODO: Documentation
 */

package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.text.DecimalFormat;

public class ShopScreen extends GameScreen {
    private Observable<Void> boardEvent = new Observable<Void>();

    private Player currentPlayer;
    private Stock [] stocksAvailable;

    //For GUI
    private Table investments;
    private Table investmentsList;
    private Table background;
    private Table playerInfo;
    private ScrollPane scroller;
    private Button buyButtons[];
    private Button sellButtons[];
    private final DecimalFormat decformat;

    /**
     * Constructor
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public ShopScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);


        this.decformat = new DecimalFormat("0.00");
        this.buyButtons = new Button[6];
        this.sellButtons = new Button[6];

        //Initializing buttons
        for (int i = 0; i < 6; ++i) {
            buyButtons[i] = new TextButton("Buy Stock", skin);
            sellButtons[i] = new TextButton("Sell Stock", skin);
        }

        setListeners();

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
        showPlayerInfo();

        background = new Table();
        background.setFillParent(true);
        background.add(investmentsList).top().center();
        background.row();
        background.add(playerInfo).right();
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
            Label ticker = new Label("Ticker Name: " + stocksAvailable[i].getTickerName() + " Qty Owned: " + currentPlayer.getCurrentInvestments().get(i).size(), skin);
            Label stockDescription = new Label("Description of stock: \n" + this.stocksAvailable[i].getDescription(), skin);
            Label stockPrice = new Label("Stock Price: " + this.stocksAvailable[i].getPrice(), skin);
            Label stockPriceChange = new Label("Stock Price Change: " + this.decformat.format(this.stocksAvailable[i].getPriceChange()) + "%", skin);
            Label stockDivPayChange = new Label("Dividend Pay Change: " + this.decformat.format(this.stocksAvailable[i].getDivPayChange()) + "%", skin);
            Label lineBreak = new Label("-------------------------------------------------------------------------------------------------------------------", skin);
            Label space = new Label("     ", skin);

            ticker.setFontScale(2);

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
            space.setAlignment(Align.left);


            investments.add(ticker).width(500).height(80).left();
            investments.add(buyButtons[i]).width(180).height(80).left();
            investments.add(sellButtons[i]).width(180).height(80).left();
            investments.row();
            investments.add(space).width(600).height(30).left();
            investments.row();
            investments.add(stockDescription).width(600).left();
            investments.row();
            investments.add(space).width(600).height(30).left();
            investments.row();
            investments.add(stockPrice).width(500).height(30).left();
            investments.row();
            investments.add(stockPriceChange).width(500).height(30).left();
            investments.row();
            investments.add(stockDivPayChange).width(500).height(30).left();
            investments.row();
            investments.add(lineBreak).width(500).height(30).left();
            investments.row();
        }

        scroller = new ScrollPane(investments, skin);
        scroller.setHeight(500);
        scroller.setScrollBarPositions(false,true);
        scroller.setScrollbarsVisible(true);

        investmentsList = new Table();
        investmentsList.setHeight(800);
        investmentsList.add(scroller).left().expandX();
    }

    private void showPlayerInfo() {
        playerInfo = new Table();

        Label playerName = new Label(currentPlayer.getPlayerProfile().getName(), skin);
        Label playerMoney = new Label("Current Cash Available: " + currentPlayer.getMoney(), skin);
        Label playerInvestment = new Label("Investment Account: " + currentPlayer.getInvestments() , skin);

        playerName.setWrap(true);
        playerMoney.setWrap(true);
        playerInvestment.setWrap(true);

        //Adding to table
        playerInfo.add(playerName).width(400).height(30).center();
        playerInfo.add(playerMoney).width(600).height(30).left();
        playerInfo.add(playerInvestment).width(400).height(30).left();
    }

    /**
     * Updates to the current player's information
     * @param player PlayerProfile to access player's items, stocks and current funds
     */
    public void setCurrentPlayer(Player player) {this.currentPlayer = player;}

    /**
     * @param stocksAvailable list of updated stocks to show in the shop screen
     */
    public void setStocksAvailable (Stock [] stocksAvailable) {this.stocksAvailable = stocksAvailable;}

    /**
     * Add the bought stock to the player's owned list
     * @param num the index of what stock is being bought
     */
    private void buyStock (int num) {
        this.currentPlayer.addInvestments(this.stocksAvailable[num], num);
        this.background.remove();
        this.playerInfo.remove();
        updateScreen();
    }

    /**
     * Removing the sold stock from the player's investments
     * @param num the index of what stock is being sold
     */
    private void sellStock(int num) {
        this.currentPlayer.removeInvestment(this.stocksAvailable[num], num);
        this.background.remove();
        this.playerInfo.remove();
        updateScreen();
    }

    /**
     * Sets the button listeners in the shop screen
     */
    private void setListeners () {

        //Adding listeners for Safe Growth Stock
        buyButtons[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                buyStock(0);
            }
        });
        sellButtons[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                sellStock(0);
            }
        });

        //Adding listeners to Medium Risk Growth Stock Buttons
        buyButtons[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                buyStock(1);
            }
        });
        sellButtons[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                sellStock(1);
            }
        });

        //Adding listeners to High Risk Growth Stock Buttons
        buyButtons[2].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                buyStock(2);
            }
        });
        sellButtons[2].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                sellStock(2);
            }
        });

        //Adding listeners to Safe Risk Dividends Stock
        buyButtons[3].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                buyStock(3);
            }
        });
        sellButtons[3].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                sellStock(3);
            }
        });

        //Adding listeners to Medium Risk Dividend Stocks
        buyButtons[4].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                buyStock(4);
            }
        });
        sellButtons[4].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                sellStock(4);
            }
        });

        //Adding listeners to High Risk Dividend Stock Buttons
        buyButtons[5].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                buyStock(5);
            }
        });
        sellButtons[5].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                sellStock(5);
            }
        });
    }

    public void addBoardListener(Observer<Void> ob) { boardEvent.addObserver(ob); }
}