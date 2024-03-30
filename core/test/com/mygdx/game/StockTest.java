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

    @BeforeAll
    static void setUp() {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();
    }

    @Test
    void dividendPay() {
    }

    @Test
    void getPrice() {
    }

    @Test
    void getDivPay() {
    }

    @Test
    void getDescription() {
    }

    @Test
    void getTickerName() {
    }

    @Test
    void getPriceChange() {
    }

    @Test
    void getDivPayChange() {
    }

    @Test
    void updatePrice() {
    }
}