package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class AgilityTestScreenTest {
    static GameContext gameContext;
    static AssetManager asset;

    @BeforeAll
    static void setUp() {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();
    }

    @Test
    void show() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        AgilityTestScreen screen = new AgilityTestScreen(Mockito.mock(SpriteBatch.class), asset);
        screen.show();

        // Check timer
        Field tableF = AgilityTestScreen.class.getDeclaredField("inputTable");
        tableF.setAccessible(true); // Make the field accessible
        Table inputTable = (Table)tableF.get(screen);
        assertFalse(inputTable.isVisible());
        Thread.sleep(4000);
        assertTrue(inputTable.isVisible());
    }

    @Test
    void setHardMode() throws NoSuchFieldException, IllegalAccessException {
        Field boolF = AgilityTestScreen.class.getDeclaredField("hardMode");
        boolF.setAccessible(true); // Make the field accessible

        AgilityTestScreen screen = new AgilityTestScreen(Mockito.mock(SpriteBatch.class), asset);
        assertFalse((boolean)boolF.get(screen));

        screen.setHardMode(true);
        assertTrue((boolean)boolF.get(screen));

        screen.setHardMode(false);
        assertFalse((boolean)boolF.get(screen));
    }
}