package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ActionTextTest {
    static GameContext gameContext;
    static AssetManager asset;
    static Field labelF;
    static Skin skin;

    @BeforeAll
    static void setUp() throws NoSuchFieldException {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();
        skin = asset.get(Config.getInstance().getUiPath(), Skin.class);

        // Make the label visible for testing purposes
        labelF = ActionText.class.getDeclaredField("label");
        labelF.setAccessible(true); // Make the field accessible
    }

    @Test
    void render() throws IllegalAccessException {
        ActionText text = new ActionText("TestText", 0, 0, 1.0f, skin);
        Label label = (Label)labelF.get(text);

        assertFalse(text.expired());
        // Simulate render, while making sure nothing weird is happening
        for (int i = 1; i < 10; i++) {
            text.render(Mockito.mock(Batch.class), 0.1f);
            assertFalse(text.expired());
            assertEquals(1.0, label.getColor().a);
            assertEquals(0, label.getY());
            assertEquals(0, label.getX());
        }
        while (label.getColor().a > 0.0) {
            text.render(Mockito.mock(Batch.class), 0.1f);
            assertEquals(0, label.getX());
            assertTrue(label.getY() >= 0);
        }

        assertTrue(text.expired());
        assertEquals(0, label.getColor().a);
    }

    @Test
    void expired() throws IllegalAccessException {
        // Standard test
        ActionText text = new ActionText("TestText", 0, 0, 100f, skin);
        assertFalse(text.expired());
        text.render(Mockito.mock(Batch.class), 10f);
        assertFalse(text.expired());
        text.render(Mockito.mock(Batch.class), 1000f);
        assertTrue(text.expired());

        // Instant expiration test
        ActionText instant = new ActionText("test2", 0, 0, 0f, skin);
        instant.render(Mockito.mock(Batch.class), 0.0001f);
        Label label = (Label)labelF.get(text);
        assertTrue(label.getY() > 0);
    }

    @Test
    void setColor() throws IllegalAccessException {
        ActionText text = new ActionText("test", 0, 0, 1f, skin);
        Label label = (Label)labelF.get(text);
        assertEquals(label.getColor(), Color.WHITE);
        text.setColor(Color.YELLOW);
        assertEquals(label.getColor(), Color.YELLOW);
    }
}