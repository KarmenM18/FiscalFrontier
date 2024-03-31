package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * ActionText is used to report events occurring in the game. It is rendered directly to the screen and fades out naturally.
 */
public class ActionText {
    private Label label;
    private float timeLeft; // They stick on the screen for a certain amount of time

    /**
     * Constructor
     *
     * @param text text to write to the screen
     * @param x x position
     * @param y y position
     * @param lifespan how long the text should stay on the screen before beginning to fade away
     * @param skin Skin to create label with
     */
    public ActionText(String text, float x, float y, float lifespan, Skin skin) {
        label = new Label(text, skin, "menu");
        label.setPosition(x, y);
        this.timeLeft = lifespan;
    }

    /**
     * Process this ActionText. Will update position and alpha, then draw on the screen.
     *
     * @param batch batch to draw with
     * @param deltaTime time passed since last render
     */
    public void render(Batch batch, float deltaTime) {
        timeLeft -= deltaTime;

        if (timeLeft <= 0) {
            // Slowly climb and fade away
            label.setY(label.getY() + deltaTime * 40f);
            Color color = label.getColor();
            color.a -= deltaTime * 0.5f;
            if (color.a < 0.0) color.a = 0.0f;
        }

        label.draw(batch, 1.0f);
    }

    /**
     * @return true only if the Label is invisible (alpha = 0)
     */
    public boolean expired() {
        return Utility.epsilonEqual(label.getColor().a, 0f, 0.001f);
    }

    /**
     * Set the color of the text Label.
     *
     * @param color LibGDX Color
     */
    public void setColor(Color color) {
        label.setColor(color);
    }
}
