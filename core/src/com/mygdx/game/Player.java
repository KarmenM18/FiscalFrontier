package com.mygdx.game;

import java.io.Serializable;
import java.util.ArrayList;

// TODO: Add a useItem() method, which uses the use() method of the item class.
// TODO: Add polling for item use -- in GameBoard?

/**
 * Represents a player in a particular game state.
 * <br><br>
 * Contains methods to get and modify the player's current game data, including as their stars, money, score, items,
 * and position on the game board. Any information about the player which is consistent across multiple game states,
 * such as their name and high score, is stored in their persistent player profile, which can be access through the
 * {@link getPlayerProfile} method.
 * @see PlayerProfile
 *
 * @author Joelene Hales
 */
public class Player implements Serializable {

    /**
     * Player's profile. Links the in-game player to their persistent, out-of-game profile.
     */
    private final PlayerProfile profile;
    /**
     * Player's score.
     */
    private int score;
    /**
     * Player's stars.
     */
    private int stars;
    /**
     * Player's money.
     */
    private int money;
    /**
     * Player's item inventory.
     */
    private ArrayList<Item> items;
    /**
     * Player's current position on the game board.
     */
    private Node currentTile;


    /**
     * Constructor creates a player with existing game data.
     *
     * @param profile     Player's profile. Links the player to their persistent, out-of-game profile.
     * @param money       Player's money
     * @param stars       Player's stars
     * @param score       Player's score
     * @param items       Player's item inventory
     * @param currentTile Player's current position on the game board
     */
    public Player(PlayerProfile profile, int money, int stars, int score, ArrayList<Item> items, Node currentTile) {

        // Initialize all player attributes
        this.profile = profile;
        this.stars = stars;
        this.money = money;
        this.score = score;
        this.items = items;
        this.currentTile = currentTile;

    }

    /**
     * Constructor creates a player with no existing game data. Sets all current game data to their initial values for
     * a new game state. Used when adding a players in a new game.
     *
     * @param profile Player profile. Links the in-game player with their out-of-game profile
     */
    public Player(PlayerProfile profile)  {
        this(profile, 0, 0, 0, new ArrayList<Item>(), null);
    }

    /**
     * Returns the player's out-of-game profile.
     *
     * @return Player's out-of-game profile
     */
    public PlayerProfile getPlayerProfile() {
        return this.profile;
    }


    /**
     * Returns the player's money.
     *
     * @return Player's money
     */
    public int getMoney() {
        return this.money;
    }


    /**
     * Sets the player's money to the given amount.
     *
     * @param amount New value for player's money
     */
    public void setMoney(int amount) {
        this.money = amount;
    }


    /**
     * Returns the player's stars.
     *
     * @return Player's stars
     */
    public int getStars() {
        return this.stars;
    }


    /**
     * Sets the player's stars to the given amount.
     *
     * @param numStars New value for player's stars
     */
    public void setStars(int numStars) {
        this.stars = numStars;
    }


    // TODO: Confirm score formula

    /**
     * Calculates and sets the player's score.
     * The player's score is calculated from their stars and money using the formula:
     * Score = 5 * stars + money
     */
    public void calculateScore() {
        this.score = 5 * this.stars + this.money;
    }

    /**
     * Returns the player's score.
     *
     * @return Player's score
     */
    public int getScore() {
        return this.score;
    }


    /**
     * Returns the player's current position on the game board.
     *
     * @return Player's current position on the game board.
     */
    public Node getCurrentTile() {
        return this.currentTile;
    }


    /**
     * Updates the player's current position on the game board.
     *
     * @param newTile Player's new position on the game board
     */
    public void setCurrentTile(Node newTile) {
        this.currentTile = newTile;
    }


    /**
     * Adds an item to the player's inventory.
     *
     * @param item Item to add
     */
    public void addItem(Item item) {
        this.items.add(item);
    }


    /**
     * Finds the index of the item with the given name in the player's item inventory.
     *
     * @param itemName Name of item to find
     * @return Index of the item in the player's inventory, or -1 if the player does not have the item
     */
    private int findItem(String itemName) {

        // Search for item in the player's inventory by name
        for (int index = 0; index < this.items.size(); index++) {  // Iterate through inventory items

            if (this.items.get(index).getName().equals(itemName)) {  // Item found
                return index;
            }
        }

        return -1;  // Checked entire inventory and item not found
    }


    /**
     * Removes an item from the player's inventory.
     *
     * @param itemName Name of item to remove
     * @return Item removed
     * @throws IllegalArgumentException If the player does not have an item with the given name
     */
    public Item removeItem(String itemName) throws IllegalArgumentException {

        Item removedItem;                // Stores the item removed
        int index = findItem(itemName);  // Find index of item to remove in the player's inventory

        if (index == -1) {  // Player does not have the item
            throw new IllegalArgumentException("Player does not have an item with the given name.");
        }
        else {  // Item was found
            removedItem = this.items.get(index);  // Retrieve the item to be removed
            this.items.remove(index);             // Remove item from inventory
        }

        return removedItem;

    }

}

