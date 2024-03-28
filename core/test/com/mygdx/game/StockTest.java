package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @BeforeEach
    void setUp() {
        HeadlessApplicationConfiguration GDXConfig = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new TestGame(), GDXConfig);
        Gdx.gl = Mockito.mock(GL20.class); // Mock gl to allow headless texture loading

        Config config = Config.getInstance();
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