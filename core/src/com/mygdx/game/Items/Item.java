package com.mygdx.game.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.GameState;
import com.mygdx.game.Player;


/**
 * Represents a given Item present in the ShopScreen.
 * <br><br>
 * Contains methods to implement Item funtionality - this includes a check if an item has already been purchased by a
 * Player, displays a dialog box to show an Item's purchase status, gets the name, price, description of an Item, and
 * checks whether an item is passive or not. Items are linked and displayed to the ShopScreen, which contains the
 * interface for the user to navigate, view, and select items to purchase, while considering the amount of coins they
 * have to make a given purchase.
 */

public class Item {
    protected String name;
    protected boolean passive; // Passive items cannot be activated, they just affect the Player in some way
    transient protected Dialog usedItemDialog; // Transient; must be rebuilt by calling loadTextures after deserialization
    protected int price; // The cost of an item in the shop
    protected String description; // A brief description of the item

    /**
     * PURPOSE: Standard constructor
     * @param name the Item's name
     * @param passive whether the Item is passive or active
     * @param skin skin to use to style the dialog box
     */
    /*
    public Item(String name, boolean passive, Skin skin, int price, String description) {
        this.name = name;
        this.passive = passive;
        loadTextures(skin);
        this.price = price;
        this.description = description;
    }
    */
    public Item(String name, boolean passive, Skin skin) {
        this.name = name;
        this.passive = passive;
        loadTextures(skin);
    }

    /**
     * PURPOSE: Constructor with omitted passive parameter. Will default to not passive.
     * @param name the Item's name
     * @param skin skin to use to style the dialog box
     */
    public Item(String name, Skin skin) {

        this(name, false, skin);
    }

    /**
     * PURPOSE: No-arg constructor for deserialization
     */
    protected Item() {}

    /**
     * PURPOSE: Creates and presents graphical display box that appears when an item is activated
     * @param skin to style the dialog box that appears
     */
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

    /**
     * @return The name of the item selected
     */
    public String getName() {
        return name;
    }

    /**
     * @return The cost, in coins, that is needed to purchase the item
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return A brief description of what the item does, advantages, etc.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return True if the item is passive, False if not
     */
    public boolean isPassive() { return passive; }

    /**
     * PURPOSE: displays all information about a given Item
     * @return
     */
    public String displayItemInfo() {
        System.out.println(this.name + ": " + this.price);
        System.out.println(this.description);
        //System.out.println
        return null;
    }

    /**
     * @param player that is currently accessing the shop
     * @param state of the game during which the player chooses to visit the shop
     * @param stage
     * @return True if the item was added to cart, false otherwise
     */
    public boolean addToCart(Player player, GameState state, Stage stage) {
        if use(player, state, stage) = true { // item should be removed, meaning that it's already been added to cart
            return true;
        }
        else {
            return false;
        }
    }
}
