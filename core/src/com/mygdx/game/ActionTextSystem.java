package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.LinkedList;


/**
 * Class to put moving text on the screen to report actions
 * Singleton Pattern
 */
public class ActionTextSystem {
    private static Skin skin;
    private static LinkedList<ActionText> texts = new LinkedList<ActionText>();

    /**
     * Contains the static Singleton.
     */
    private static class ActionTextSystemHolder {
        private static final ActionTextSystem instance = new ActionTextSystem();
    }

    /**
     * Initializer of ActionTextSystem
     */
    private ActionTextSystem() {
    }

    /**
     * Accessor for the static Singleton.
     *
     * @return the ActionTextSystem object
     */
    public static ActionTextSystem getInstance() {
        return ActionTextSystem.ActionTextSystemHolder.instance;
    }

    /**
     * Set the Skin used to create Labels with
     *
     * @param newSkin LibGDX Skin to use
     */
    public static void initSkin(Skin newSkin) {
        skin = newSkin;
    }

    /**
     * Add a new ActionText to the list.
     *
     * @param text text of the ActionText
     * @param x x position
     * @param y y position
     * @param lifespan lifespan in terms of LibGDX delta time
     */
    public static void addText(String text, float x, float y, float lifespan) {
        texts.add(new ActionText(text, x, y, lifespan, skin));
    }

    /**
     * Add a new ActionText to the list, and set the color
     *
     * @param text text of the ActionText
     * @param x x position
     * @param y y position
     * @param lifespan lifespan in terms of LibGDX delta time
     * @param color Color to write text
     */
    public static void addText(String text, float x, float y, float lifespan, Color color) {
        texts.addFirst(new ActionText(text, x, y, lifespan, skin));
        texts.getFirst().setColor(color);
    }

    /**
     * Process all ActionTexts currently active
     *
     * @param batch Batch to draw with
     * @param deltaTime time passed since last render
     */
    public static void render(Batch batch, float deltaTime) {
        for (ActionText text : texts) {
            text.render(batch, deltaTime);
        }

        // Delete expired ActionTexts
        texts.removeIf(text -> text.expired());
    }


}