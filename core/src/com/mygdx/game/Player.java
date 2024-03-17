package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Items.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

// TODO: Add a useItem() method, which uses the use() method of the item class.
// TODO: Add polling for item use -- in GameBoard?

/**
 * Represents a player in a particular game state.
 * <br><br>
 * Contains methods to get and modify the player's current game data, including as their stars, money, score, items,
 * and position on the game board. Any information about the player which is consistent across multiple game states,
 * such as their name and high score, is stored in their persistent player profile, which can be access through the
 * {@link #getPlayerProfile()} method.
 * @see PlayerProfile
 *
 * @author Joelene Hales
 * @author Franck Limtung
 */
public class Player implements Serializable {
    /**
     * Player's profile. Links the in-game player to their persistent, out-of-game profile.
     */
    private PlayerProfile profile;
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
     * Player's frozen state. When they are frozen, they skip their next turn.
     */
    private boolean frozen;

    /**
     * ID of the node where the player is on the game board.
     */
    private String currentTile;

    /**
     * Player's Sprite to render on the board
     */
    private Sprite sprite;

    /**
     * Board movement variables
     */
    private int dieRoll;
    private int rollsLeft;
    private int maxRolls;
    private int movesLeft;
    private int maxMoves;
    private ArrayList<ArrayList<String>> reachablePaths;
    /**
     * The previous tile the player was on. Used to disallow going backwards.
     */
    private ArrayList<String> previousPath;

    /**
     * Constructor creates a player with existing game data.
     *
     * @param profile     Player's profile. Links the player to their persistent, out-of-game profile.
     * @param money       Player's money
     * @param stars       Player's stars
     * @param score       Player's score
     * @param items       Player's item inventory
     * @param currentTile Player's current tile ID
     */
    public Player(PlayerProfile profile, AssetManager assets, int money, int stars, int score, ArrayList<Item> items, String currentTile) {

        // Initialize all player attributes
        this.profile = profile;
        this.stars = stars;
        this.money = money;
        this.score = score;
        this.items = items;
        this.frozen = false;
        this.currentTile = currentTile;
        this.dieRoll = 0;
        this.maxMoves = 1;
        this.movesLeft = maxMoves;
        this.maxRolls = 1;
        this.rollsLeft = maxRolls;
        this.reachablePaths = new ArrayList<>();
        this.previousPath = new ArrayList<>();

        Config config = Config.getInstance();
        sprite = new Sprite((Texture) assets.get(profile.getSpritePath()));
        loadTextures(assets);
        sprite.setSize(100, 100);
        sprite.setPosition(0, 0);
    }

    /**
     * Constructor creates a player with no existing game data. Sets all current game data to their initial values for
     * a new game state. Used when adding a players in a new game.
     *
     * @param profile Player profile. Links the in-game player with their out-of-game profile
     */
    public Player(PlayerProfile profile, AssetManager assets) {
        this(profile, assets, 0, 0, 0, new ArrayList<Item>(), null);
    }

    /**
     * Private no-arg constructor for serialization
     */
    private Player() {}

    public void loadTextures(AssetManager assets) {
        sprite.setTexture(assets.get(profile.getSpritePath()));
        // Load textures for all items
        Config config = Config.getInstance();
        Skin skin = assets.get(config.getUiPath(), Skin.class);
        for (Item item : items) {
            item.loadTextures(skin);
        }
    }

    /**
     * Used to refresh Player attributes
     * Run at the end of the Player's turn.
     *
     * @param nodeMap Map of nodes
     */
    public void endTurn(Map<String, Node> nodeMap) {
        movesLeft = maxMoves;
        rollsLeft = maxRolls;
        // Clear reachable node colors
        for (ArrayList<String> reachablePath : reachablePaths) {
            nodeMap.get(reachablePath.get(reachablePath.size() - 1)).setNoColor();
        }
        // Set previous path tiles back to normal
        for (String prevID : previousPath) {
            nodeMap.get(prevID).setNoColor();
        }

        reachablePaths.clear();
        dieRoll = 0;
    }

    public void startTurn(Map<String, Node> nodeMap) {
        // Set previous path tiles gray
        for (String prevID : previousPath) {
            nodeMap.get(prevID).setGray();
        }
    }

    public int rollDie(Map<String, Node> nodeMap) {
        dieRoll = Utility.getRandom(1, 4);

        // Get previous node if a previous path exists or just use null
        String previousNode = null;
        if (previousPath.size() >= 2) {
            previousNode = previousPath.get(previousPath.size() - 2);
        }
        // Get reachable nodes and color them
        reachablePaths = nodeMap.get(currentTile).getReachable(dieRoll, previousNode, nodeMap);
        for (ArrayList<String> reachablePath : reachablePaths) {
            nodeMap.get(reachablePath.get(reachablePath.size() - 1)).setGreen();
        }

        rollsLeft--;

        return dieRoll;
    }

    /**
     * Move the player to the specified tileID.
     * Calls the node's activation function.
     * 
     * @param tileID ID of the tile in the Node map
     * @param nodeMap map of all board nodes
     * @param batch SpriteBatch for rendering operations
     * @throws IllegalArgumentException if the nodeID doesn't exist in nodeMap
     */
    public void move(String tileID, Map<String, Node> nodeMap, SpriteBatch batch) {
        // Check if a path exists to the given tileID
        ArrayList<String> validPath = null;
        for (ArrayList<String> path : reachablePaths) {
            if (path.get(path.size() - 1).equals(tileID)) {
                // TODO: Selects first valid path. if there are multiple, it's possible the player would want to choose
                //  the path themselves.
                validPath = path;
                break;
            }
        }
        if (validPath == null) {
            throw new IllegalArgumentException("Invalid movement; Node not in reachable list.");
        }

        // Set green tiles back to normal and clear reachable tile paths
        for (ArrayList<String> reachablePath : reachablePaths) {
            nodeMap.get(reachablePath.get(reachablePath.size() - 1)).setNoColor();
        }

        // Remove previous path
        for (String pathID : previousPath) {
            nodeMap.get(pathID).setNoColor();
        }

        previousPath = validPath; // Set previousPath to the new one
        // Color path gray
        for (String pathID : previousPath) {
            nodeMap.get(pathID).setGray();
        }

        setCurrentTile(tileID, nodeMap);
        
        // Call the node's activation function
        nodeMap.get(tileID).activate(this, batch);

        reachablePaths.clear();
        dieRoll = 0;
        movesLeft--;
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

    /**
     * Give a single star to the player
     */
    public void addStar() {
        this.stars++;
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
    public String getCurrentTile() {
        return this.currentTile;
    }


    /**
     * Updates the player's current position on the game board.
     * Updates the Player's Sprite position.
     *
     * @param newTileID Player's new position on the game board
     * @param nodeMap Map of all tiles on the board
     */
    public void setCurrentTile(String newTileID, Map<String, Node> nodeMap) {
        this.currentTile = newTileID;
        Node newTile = nodeMap.get(newTileID);
        this.sprite.setPosition(newTile.getXPos(), newTile.getYPos());
    }

    /**
     * Get all the player's items
     *
     * @return List of Items
     */
    public ArrayList<Item> getItems() {
        return items;
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

    /**
     * Get current value of Player's roll.
     *
     * @return Roll value
     */
    public int getDieRoll() { return dieRoll; }


    /**
     * Check if Player has moves left.
     *
     * @return True or false
     */
    public boolean canMove() { return movesLeft > 0; }

    /**
     * Check if Player has rolls left.
     *
     * @return True or false
     */
    public boolean canRoll() { return rollsLeft > 0; }

    public void setRollsLeft(int value) { rollsLeft = value; }

    /**
     * Get the list of current reachable tile paths.
     *
     * @return List of Node IDs
     */
    public ArrayList<ArrayList<String>> getReachablePaths() { return reachablePaths; }

    /**
     * Get Sprite
     *
     * @return Sprite
     */
    public Sprite getSprite() { return sprite; }

    /**
     * FreezeItem the Player for a turn. Their next turn is skipped
     */
    public void setFrozen(boolean t) { frozen = t; }
    public boolean isFrozen() { return frozen; }
}
