/**
 * TODO Just a dummy class for now
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Item {
    protected String name;
    protected boolean passive; // Passive items cannot be activated, they just affect the Player in some way
    transient protected Dialog usedItemDialog; // Transient; must be rebuilt by calling loadTextures after deserialization

    /**
     * Standard constructor
     *
     * @param name the Item's name
     * @param passive whether the Item is passive or active
     * @param skin skin to use to style the dialog box
     */
    public Item(String name, boolean passive, Skin skin) {
        this.name = name;
        this.passive = passive;
        loadTextures(skin);
    }

    /**
     * Constructor with omitted passive parameter. Will default to not passive
     *
     * @param name the Item's name
     * @param skin skin to use to style the dialog box
     */
    public Item(String name, Skin skin) {
        this(name, false, skin);
    }

    /**
     * No-arg constructor for deserialization
     */
    protected Item() {}

    public void loadTextures(Skin skin) {
        usedItemDialog = new Dialog("Used Item", skin); // ChangeListener does nothing, as the user just has to press ok
        usedItemDialog.text("You used \"" + getName() + "\".");
        usedItemDialog.button("Ok");
    }

    /**
     * Overridable use function
     * The item will return true if it should be removed
     *
     * @param stage Stage to render onto
     * @return True if the item should be removed, false if otherwise
     */
    public boolean use(Player player, GameState state, Stage stage) {
        usedItemDialog.show(stage);
        return true;
    }

    public String getName() { return name; }

    public boolean isPassive() { return passive; }
}
