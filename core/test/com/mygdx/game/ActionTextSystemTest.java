package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ActionTextSystemTest {
    static GameContext gameContext;
    static AssetManager asset;
    static Skin skin;
    static Field skinF;
    static Field textsF;

    @BeforeAll
    static void setUp() throws NoSuchFieldException {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();
        skin = asset.get(Config.getInstance().getUiPath(), Skin.class);

        // Make private fields visible for testing purposes
        skinF = ActionTextSystem.class.getDeclaredField("skin");
        skinF.setAccessible(true); // Make the field accessible
        textsF = ActionTextSystem.class.getDeclaredField("texts");
        textsF.setAccessible(true);
    }

    @AfterEach
    void tearDown() throws IllegalAccessException {
        // Remove actionTexts to isolate tests
        LinkedList<ActionText> list = (LinkedList<ActionText>)textsF.get(ActionTextSystem.getInstance());
        list.clear();
    }

    @Test
    void getInstance() {
        assertNotNull(ActionTextSystem.getInstance());
    }

    @Test
    void initSkin() throws IllegalAccessException {
        ActionTextSystem.initSkin(skin);
        Skin privSkin = (Skin)skinF.get(ActionTextSystem.getInstance());
        assert(privSkin == skin);
    }

    @Test
    void addText() throws IllegalAccessException {
        ActionTextSystem.addText("test", 0, 0, 0.5f);
        LinkedList<ActionText> list = (LinkedList<ActionText>)textsF.get(ActionTextSystem.getInstance());
        assertEquals(list.size(), 1);
    }

    @Test
    void addText2() throws NoSuchFieldException, IllegalAccessException {
        ActionTextSystem.addText("test", 0, 0, 0.5f, Color.FIREBRICK);
        LinkedList<ActionText> list = (LinkedList<ActionText>)textsF.get(ActionTextSystem.getInstance());
        assertEquals(list.size(), 1);
        // Check for color change
        Field labelF = ActionText.class.getDeclaredField("label");
        labelF.setAccessible(true); // Make the field accessible
        Label label = (Label)labelF.get(list.get(0));
        assertEquals(label.getColor(), Color.FIREBRICK);

        // Try to add another one
        ActionTextSystem.addText("test2", 0, 0, 0.5f, Color.GREEN);
        // Check existing and new
        assertEquals(label.getColor(), Color.FIREBRICK);
        label = (Label)labelF.get(list.get(0));
        assertEquals(label.getColor(), Color.GREEN);
    }

    @Test
    void render() throws IllegalAccessException {
        LinkedList<ActionText> list = (LinkedList<ActionText>)textsF.get(ActionTextSystem.getInstance());
        ActionTextSystem.addText("test", 0, 0, 0.5f);
        ActionText text1 = list.get(0);
        ActionTextSystem.addText("test2", 50, 50, 5f);
        ActionText text2 = list.get(0);
        ActionTextSystem.addText("test2", 100, 100, 50f, Color.BLACK);
        ActionText text3 = list.get(0);

        // Check for proper deletion
        for (double i = 0; i < 60; i += 1.0) {
            ActionTextSystem.render(Mockito.mock(Batch.class), 1.0f);
            if (i < 0.5f) assertFalse(text1.expired());
            else assertTrue(list.size() < 3);
            if (i < 5f) assertFalse(text2.expired());
            else assertTrue(list.size() < 2);
            if (i < 50f) assertFalse(text3.expired());
            else assertTrue(list.isEmpty());
        }
    }
}