package com.mygdx.game.Items;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.GameState;
import com.mygdx.game.Player;

/**
 * Creates a Book item to be added to the ShopScreen.
 * @author Franck Limtung (flimtung)
 * @author Karmen Minhas (kminhas7)
 * @author Kevin Chen (kchen546)
 */

public class Book extends Item{

    /**
     * Constructor - sets the price of a Book and sets the associated Item description for the user to view.
     * NOTE: inherits constructor attributes from Item class
     * @param skin used to style the associated dialog box
     */
    public Book(Skin skin) {
        super("Book", false, skin);
        this.price = 50;
        this.description = "Power Up: provides the necessary knowledge to boost player up by 1 level.";

    }

    /**
     * Increments the player's level by 1 after purchasing the Book from the shop
     * @param player - player that is currently accessing the shop
     * @param gameState - the current game state
     * @param stage - stage to render onto
     * @return true - if the Player has been successfully levelled up after purchasing the Book, false otherwise
     */
    public boolean use(Player player, GameState gameState, Stage stage) {
        player.levelUp();
        return true;
    }
    /**
     * necessary for serialization
     */
    private Book(){}
}
