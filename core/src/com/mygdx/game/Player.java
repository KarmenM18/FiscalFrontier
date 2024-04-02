package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Node.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static java.lang.Math.abs;


/**
 * Represents a player in a particular game state.
 * <br><br>
 * Contains methods to get and modify the player's current game data, including as their stars, money, investments, score,
 * items, and position on the game board. Any information about the player which is consistent across multiple game states,
 * such as their name, level, and high score, is stored in their persistent student profile.
 * @see PlayerProfile
 *
 * @author Joelene Hales (jhales5)
 * @author Franck Limtung (flimtung)
 * @author Earl Castillo (ecastil3)
 */
public class Player implements Serializable {

    /* Student data  */

    /** Player's student profile. Links the in-game player to their persistent, out-of-game profile. */
    private PlayerProfile profile;

    private int level;


    /* In-game attributes: Score and money */

    /** Player's score. */
    private int score;
    /** Player's stars. */
    private int stars;
    /** Player's money. */
    private int money;
    /** Player's investment account. */
    private int investments;
    /** Player's stocks owned. */
    private ArrayList<ArrayList<Stock>> stocks;


    /* In-game attributes: Items */

    /** Player's item inventory. */
    private ArrayList<Item> items;

    /**
     * Indicates if the player has the multidice item. <br><br>
     * When the player has this item, they are able to roll the dice multiple times on one turn.
     */
    private boolean useMultiDice;
    /**
     * Indicates if the player has the shield item. <br><br>
     * When the player has this item, they may protect themselves from the effect of the next item used against them.
     */
    private boolean hasShield = false;
    /**
     * Player's frozen state. <br><br>
     * When the player is frozen, they must skip their next turn, unless they possess a shield item.
     */
    private boolean frozen;


    /* Board movement */

    /** Number rolled on the die on a given turn. */
    private int dieRoll;
    /** Number of die rolls remaining on a given turn. */
    private int rollsLeft;
    /** Maximum number of times a player can roll the die on a given turn. */
    private int maxRolls;
    /** Board movements remaining on a given turn. */
    private int movesLeft;
    /** Maximum number of board movements a player is able to make on their turn. */
    private int maxMoves;
    /** Node ID of player's current position on the game board. */
    private String currentTile;
    /** All paths reachable from the player's current position on the game board. */
    private ArrayList<ArrayList<String>> reachablePaths;
    /**
     * The previous game board tile the player was on. <br><br>
     * Used to disallow players from going backwards.
     */
    private ArrayList<String> previousPath;

    /* Sprites */

    /** Player's sprite representation on the game board. */
    private Sprite sprite;
    /** Sprite rendered over the player when they are frozen. */
    private Sprite freezeSprite;
    /** Sprite rendered over the player when they have a shield. */
    private Sprite shieldSprite;


    /**
     * Constructor creates a player with existing game data.
     *
     * @param profile       Player's profile. Links the player to their persistent, out-of-game profile.
     * @param stars         Player's stars
     * @param money         Player's money
     * @param investments   Player's investment account
     * @param stocks        Player's stocks owned
     * @param score         Player's score
     * @param items         Player's item inventory
     * @param frozen        Player's frozen state
     * @param hasMultidice  If the player has a multidice item
     * @param hasShield     If the player has a shield item
     * @param currentTile   Player's current tile ID
     */
    public Player(PlayerProfile profile, AssetManager assets, int stars, int money, int investments, ArrayList<ArrayList<Stock>> stocks, int score, ArrayList<Item> items, boolean frozen, boolean hasMultidice, boolean hasShield, String currentTile) {

        /* Initialize all player attributes */

        // Student data
        this.profile = profile;
        this.level = this.profile.getKnowledgeLevel();

        // Score and money
        this.stars = stars;
        this.money = money;
        this.score = score;
        this.investments = investments;

        if (Objects.isNull(this.stocks)) {  // Initialize stock list if none given
            stocks = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                stocks.add(new ArrayList<>());
            }
        }
        this.stocks = stocks;



        // Items
        if (Objects.isNull(items)) {  // Initialize item inventory if none given
            items = new ArrayList<Item>();
        }
        this.items = items;

        this.frozen = frozen;
        this.useMultiDice = hasMultidice;
        this.hasShield = hasShield;

        // Board movement
        this.currentTile = currentTile;
        this.dieRoll = 0;
        this.maxMoves = 1;
        this.movesLeft = maxMoves;
        this.maxRolls = 1;
        this.rollsLeft = maxRolls;
        this.reachablePaths = new ArrayList<>();
        this.previousPath = new ArrayList<>();

        // Load player's sprite
        sprite = new Sprite((Texture) assets.get(profile.getSpritePath()));
        sprite.setSize(100, 100);
        sprite.setPosition(0, 0);

        freezeSprite = new Sprite((Texture) assets.get(Config.getInstance().getPlayerFreezePath()));
        freezeSprite.setSize(100, 100);
        freezeSprite.setAlpha(0.0f); // Starts invisible
        shieldSprite = new Sprite((Texture) assets.get(Config.getInstance().getPlayerShieldPath()));
        shieldSprite.setSize(35, 35);
        shieldSprite.setAlpha(0.0f);

        loadTextures(assets);
    }

    /**
     * Constructor creates a player with no existing game data. Sets all current game data to their initial values for
     * a new game state. Used when adding a players in a new game.
     *
     * @param profile Player profile. Links the in-game player with their out-of-game profile
     */
    public Player(PlayerProfile profile, AssetManager assets) {

        this(profile, assets, 0, 500, 0, null, 0, null, false, false, false, null);
    }

    /**
     * Private no-arg constructor for serialization
     */
    private Player() {}


    /**
     * Loads the player's sprites. Includes any sprites for the player's items.
     *
     * @param assets Used to load assets
     */
    public void loadTextures(AssetManager assets) {
        sprite.setTexture(assets.get(profile.getSpritePath()));
        freezeSprite.setTexture(assets.get(Config.getInstance().getPlayerFreezePath()));
        shieldSprite.setTexture(assets.get(Config.getInstance().getPlayerShieldPath()));

        // Load textures for all items
        Config config = Config.getInstance();
        Skin skin = assets.get(config.getUiPath(), Skin.class);
        for (Item item : items) {
            item.loadTextures(skin);
        }
    }


    /**
     * Refreshes the player's attributes. Run at the end of the player's turn.
     *
     * @param nodeMap Map of nodes.
     */
    public void endTurn(Map<String, Node> nodeMap) {

        // Reset die roll and remaining moves and rolls for next turn
        dieRoll = 0;
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

    }

    /**
     * Stores the player's path from the previous turns. Run at the beginning of the player's turn.
     *
     * @param nodeMap Map of all board nodes.
     */
    public void startTurn(Map<String, Node> nodeMap) {

        // Set previous path tiles gray
        for (String prevID : previousPath) {
            nodeMap.get(prevID).setGray();
        }
    }


    /**
     * Rolls the die for the player's turn. <br><br>
     * If the player used the multidice item, the die is rolled twice.
     * Reachable nodes (at the distance of the number rolled) are colored green.
     *
     * @param nodeMap Map of all board nodes.
     * @return Number rolled by the die.
     */
    public int rollDie(Map<String, Node> nodeMap) {

        // Roll die
        dieRoll = Utility.getRandom(1, 4);
        if(useMultiDice){  // Check if multidice was used
            dieRoll += Utility.getRandom(1, 4);
            useMultiDice = false;
        }

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
     * Move the player to the specified position on the game board, a calls the node's activation function. <br><br>
     * Positions are specified by their node ID.
     * 
     * @param tileID Node ID of position in the node map.
     * @param nodeMap Map of all board nodes.
     * @param batch SpriteBatch for rendering operations.
     * @throws IllegalArgumentException If the given node ID does not exist in the node map.
     */
    public void move(String tileID, Map<String, Node> nodeMap, SpriteBatch batch, boolean hardmode) {

        // Check if a path exists to the given tileID
        ArrayList<String> validPath = null;
        for (ArrayList<String> path : reachablePaths) {
            if (path.get(path.size() - 1).equals(tileID)) {
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

        // Set previousPath to the new one
        previousPath = validPath;

        for (String pathID : previousPath) {  // Color path gray
            nodeMap.get(pathID).setGray();
        }

        setCurrentTile(tileID, nodeMap);
        
        // Call the node's activation function
        nodeMap.get(tileID).activate(this, batch, hardmode);

        reachablePaths.clear();  // Reset reachable paths and die roll for next turn
        dieRoll = 0;
        movesLeft--;  // Decrement the moves remaining for the turn

    }


    /**
     * Returns the player's out-of-game profile.
     *
     * @return Player's out-of-game profile
     */
    public PlayerProfile getPlayerProfile() {return this.profile;}


    /**
     * Returns the player's money.
     *
     * @return Player's money
     */
    public int getMoney() {return this.money;}


    /**
     * Sets the player's money to the given amount.
     *
     * @param amount New value for player's money.
     */
    public void setMoney(int amount) {
        //int prevMoney = money;
        this.money = amount;

        /*
        int difference = money - prevMoney;
        if (difference == 0) return;
        else if (prevMoney > money) ActionTextSystem.addText("-$" + Integer.toString(abs(difference)), sprite.getX(), sprite.getY() + 50, 0.5f, Color.RED);
        else ActionTextSystem.addText("+$" + Integer.toString(difference), sprite.getX(), sprite.getY() + 50, 0.5f, Color.GREEN);
        */
    }


    /**
     * Returns the player's level. Corresponds to the knowledge level in the student's profile.
     *
     * @return Player's level.
     */
    public int getLevel(){
        return this.profile.getKnowledgeLevel();
    }


    /**
     * Checks if a given level is valid. A level is considered valid if it is a positive integer and is less than the
     * maximum level.
     * @param level Level to check
     * @return True if the level is valid, false if otherwise
     */
    private boolean validLevel(int level) {
        if (level < 0 || level > Config.getInstance().getMaxLevel()) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Sets the player's level to the given value.  <br><br>
     * Includes checking if the new level for the player is valid. A level is considered valid if it is a positive
     * integer and is less than the maximum level.
     *
     * @param level Desired level
     * @throws IllegalArgumentException If the level is invalid
     */
    public void setLevel(int level) throws IllegalArgumentException {

        if (!validLevel(level)) {  // Check that the new level is valid
            throw new IllegalArgumentException();
        }

        this.level = level;
        this.profile.setKnowledgeLevel(level);

    }

    /**
     * Increases the student's knowledge level by one and updates the student's knowledge database. <br><br>
     * Includes checking if the new level for the student is valid. A level is considered valid if it is a positive integer
     * and is less than the maximum level.
     */
    public void levelUp(){
        if(validLevel(level + 1)){  // Check that player's new level will be a valid level
            this.level++;
            this.profile.updateKnowledgeLevel();
        }

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
     * @param numStars New value for player's stars.
     */
    public void setStars(int numStars) {
        this.stars = numStars;
    }


    /**
     * Set whether the player has a multidice item or not.
     *
     * @param use True if the player has a multidice item, false if otherwise.
     */
    public void setUseMultiDice(boolean use){
        this.useMultiDice = use;
    }

    /**
     * Returns an indicator if the player has a multidice item.
     *
     * @return True if the player has a multidice item, false if otherwise
     */
    public boolean getMultiDice() { return useMultiDice; }


    /**
     * Set whether the player has a shield item or not
     *
     * @param has True if the player has a shield item, false if otherwise
     */
    public void setHasShield(boolean has){
        this.hasShield = has;

        if (has) {
            SoundSystem.getInstance().playSound("shield.mp3");
            shieldSprite.setPosition(sprite.getX() - 10, sprite.getY() - 10);
            shieldSprite.setAlpha(0.8f);
        }
        else {
            SoundSystem.getInstance().playSound("shieldDrop.mp3");
            shieldSprite.setAlpha(0);
        }
    }

    /**
     * Returns an indicator if the player has a shield item.
     *
     * @return True if the player has a shield item, false if otherwise.
     */
    public boolean getHasShield(){
        return hasShield;
    }


    /**
     * Calculates and sets the player's score. <br><br>
     * The player's score is calculated from their stars and money using the formula:
     * Score = 1000 * stars + money + investments
     *
     * @return Player's score
     */
    public void calculateScore() {
        this.score = 1000 * this.stars + this.money + this.investments;
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
     * Updates the player's current position on the game board. Includes moving the player's sprite.
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
     * Get current value of the player's die roll.
     *
     * @return Die roll value
     */
    public int getDieRoll() { return dieRoll; }


    /**
     * Check if Player has moves remaining for the current turn.
     *
     * @return True if the player has at least one move remaining, false if otherwise
     */
    public boolean canMove() { return movesLeft > 0; }


    /**
     * Check if Player has die rolls remaining for the current turn.
     *
     * @return True if the player has at least one die roll remaining, false if otherwise.
     */
    public boolean canRoll() { return rollsLeft > 0; }


    /**
     * Sets the number of die rolls remaining for the given turn.
     *
     * @param value Number of die rolls remaining for the given turn.
     */
    public void setRollsLeft(int value) { rollsLeft = value; }


    /**
     * Get the list of current reachable tile paths.
     *
     * @return List of Node IDs
     */
    public ArrayList<ArrayList<String>> getReachablePaths() { return reachablePaths; }


    /**
     * Get the player's sprite.
     *
     * @return Player's sprite.
     */
    public Sprite getSprite() { return sprite; }


    /**
     * Get the sprite for the player when frozen.
     *
     * @return Player's sprite when frozen
     */
    public Sprite getFreezeSprite() {
        return freezeSprite;
    }

    /**
     * Get the sprite for the shield item.
     *
     * @return Sprite for the shield item
     */
    public Sprite getShieldSprite() {
        return shieldSprite;
    }


    /**
     * Sets the player's frozen state. <br><br>
     * When a player is frozen, they must skip their next turn, unless they possess a shield item.
     *
     * @param frozen True to freeze the player
     */
    public void setFrozen(boolean frozen) {

        if (frozen) {
            if (useShield()) {  // Consume shield item to prevent freezing
                return;
            }

            // Freeze player
            this.frozen = true;  // Set attribute

            // Play sound effect
            SoundSystem.getInstance().playSound("coldsnap.wav");
            freezeSprite.setPosition(sprite.getX(), sprite.getY());
            freezeSprite.setAlpha(0.8f);
        }
        else {
            freezeSprite.setAlpha(0);
            this.frozen = false; // Unfreeze player
        }
    }

    /**
     * Check if the player is frozen. <br><br>
     * When a player is frozen, they must skip their next turn, unless they possess a shield item.
     *
     * @return True if the player is frozen, false if otherwise.
     */
    public boolean isFrozen() { return this.frozen; }


    /**
     * Call this function when the shield could be used. <br><br>
     * If the shield is active, it will return true.
     *
     * @return True if the shield is active or false otherwise
     */
    public boolean useShield() {
        if (hasShield) {
            ActionTextSystem.addText("Used Shield", sprite.getX(), sprite.getY() + 50, 0.5f);
            setHasShield(false);
            return true;
        }

        return false;
    }

    /**
     * Get all stocks that the player has invested in
     *
     * @return All stocks that the player has invested in
     */
    public ArrayList<ArrayList<Stock>> getCurrentInvestments() {
        return this.stocks;
    }


    /**
     * Adds a new stock to the player's stock portfolio and updates the player's money and investment account.
     *
     * @param newInvestment The new Stock to add to the list
     * @param stockNum Stock type from the 6 available stocks to pick from
     */
    public void addInvestments(Stock newInvestment, int stockNum) {

        //Making sure that the user can afford the stock
        if (this.money >= newInvestment.getPrice()) {
            this.stocks.get(stockNum).add(newInvestment);
            this.investments += newInvestment.getPrice();
            this.money -= newInvestment.getPrice();

            SoundSystem.getInstance().playSound("buy.wav");
        }
    }


    /**
     * Removes a stock from the player's list.
     *
     * @param stock The current price of the stock to remove
     * @param stockNum what type of stock
     */
    public void removeInvestment(Stock stock, int stockNum) {

        //Checking to make sure that the player has at least one stock of selected stock type
        if (!this.stocks.get(stockNum).isEmpty()) {
            this.stocks.get(stockNum).remove(0);
            this.investments -= stock.getPrice();
            this.money += stock.getPrice();

            SoundSystem.getInstance().playSound("buy.wav");
        }
    }


    /**
     * Updates the list of stocks. Used to simulate stock market dynamics
     *
     * @param updatedStocks Updated list of stocks.
     */
    public void updateInvestment(Stock [] updatedStocks) {
        this.investments = 0;

        //Multiplies the current stock price with quantity of that the player owns for the given stock price
        for (int i = 0; i < this.stocks.size(); i++) {
            this.investments += updatedStocks[i].getPrice() * this.stocks.get(i).size();
        }
    }


    /**
     * Returns the total value of the player's investment account
     *
     * @return Total value of investment account
     */
    public int getInvestments() {return this.investments;}
}
