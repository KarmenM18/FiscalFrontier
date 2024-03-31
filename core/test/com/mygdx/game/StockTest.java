package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {
    static private AssetManager asset;
    static private GameContext gameContext;

    static private Stock testStock;

    @BeforeAll
    static void setUp() {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();

        testStock = new Stock("TEST", 100, "TEST Stock", 10, 10, 100, 100, 10, 5, 5);
        assertNotNull(testStock);
    }

    @Test
    void dividendPay() {
        double pay = testStock.dividendPay();
        assertTrue(pay >= 0); //Making sure the pay is not a negative number
    }

    @Test
    void getPrice() {
        assertTrue(testStock.getPrice() >= 0); //Making sure the price of stock is not a negative number
    }

    @Test
    void divPay (){
        double divPay = testStock.getDivPay();
        //Making sure the dividend pay % isn't a negative number
        assertTrue(divPay >= 0);
    }

    @Test
    void getDescription() {
        String stockDiscription = testStock.getDescription();
        assertNotEquals("", stockDiscription);
    }

    @Test
    void getTickerName() {
        String stockName = testStock.getTickerName();
        assertNotEquals("", testStock.getTickerName());
    }

    @Test
    void getPriceChange() {
        double priceChange = testStock.getPriceChange();
        //Making sure the change doesn't exceed 100% or below -100%
        assertTrue(priceChange > -101 && priceChange < 101);
    }

    @Test
    void getDivPayChange() {
        double divPayChange = testStock.getDivPayChange();
        //Making sure the % dividend pay change is between -50% and 30%
        assertTrue(divPayChange >= -50 && divPayChange <= 30);

    }

    @Test
    void updatePrice() {
        Stock newStock = new Stock("newStock", 100, "New Test Stock",
                10, 10, 100, 100, 10, 5, 5); //Creating duplicate
        newStock.updatePrice(); //Changed price and dividend pay

        //Making sure the new stock doesn't equal the testStock
        assertTrue(testStock.getPriceChange() != newStock.getPriceChange());
        assertTrue(testStock.dividendPay() != newStock.dividendPay());
    }
}