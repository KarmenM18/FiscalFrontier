package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Items.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    /** Player's assets (sprites, etc.) */
    static private AssetManager assets;
    /** Game skin. Required for testing items. */
    static private Skin skin;
    /** Test student profile to create player from. */
    static private PlayerProfile profile;
    /** Name of student profile used to test. */
    static String name;
    /** Knowledge level of student profile used to test. */
    static int knowledgeLevel;
    /** Individual game high score of student profile used to test. */
    static int highScore;
    /** Lifetime score of student profile used to test. */
    static int lifetimeScore;


    @BeforeAll
    static void setUpClass() {

        // Load player's assets
        GameContext gameContext = new GameContext();  // Create a game context for unit testing
        assets = gameContext.getAssetManager();
        Config config = Config.getInstance();
        skin = assets.get(config.getUiPath(), Skin.class);

        // Define attributes of student profile used to test player creation
        name = "Test";
        highScore = 80;
        lifetimeScore = 550;
        knowledgeLevel = 4;

    }

    @BeforeEach
    void createProfile() {

        // Create/reset the test student profile to create player from
        profile = new PlayerProfile(name, lifetimeScore, highScore, knowledgeLevel);

    }



    @Test
    void endTurn() {
    }

    @Test
    void startTurn() {
    }

    @Test
    void rollDie() {
    }

    @Test
    void move() {
    }

    @Test
    void getPlayerProfile() {

        Player player = new Player(profile, assets);       // Create basic player from test profile
        assertEquals(profile, player.getPlayerProfile());  // Verify that the correct profile reference is returned

    }

    @Test
    void getMoney() {

        // Create player with a specified amount of money
        int money = 350;
        Player player = new Player(profile, assets, 0, money, 0, null, 0, null, false, false, false, null);

        assertEquals(money, player.getMoney());   // Verify that the method returns the initialized value

    }


    @Test
    void setMoney() {

        Player player = new Player(profile, assets);  // Create basic player from test profile

        // Set the player's money to a new value
        int newValue = 54321;
        player.setMoney(newValue);

        assertEquals(newValue, player.getMoney());  // Verify that the correct attribute and correct value were set

    }


    @Test
    void getLevel() {

        Player player = new Player(profile, assets);      // Create basic player from test profile
        assertEquals(knowledgeLevel, player.getLevel());  // Verify that the knowledge level is referenced correctly.

    }


    @Test
    void setLevel() {

        Player player = new Player(profile, assets);  // Create basic player from test profile

        // Set new knowledge level
        int newLevel = 8;
        player.setLevel(newLevel);

        // Verify that knowledge level was updated correctly
        assertEquals(newLevel, player.getLevel());            // Verify that the knowledge level is stored correctly in the player
        assertEquals(newLevel, profile.getKnowledgeLevel());  // Verify that the updates were made in the student profile

    }


    @Test
    void setLevelAboveMax() {

        Player player = new Player(profile, assets);  // Create basic player from test profile

        // Attempt to set knowledge level to an invalid value: higher than the maximum allowed level
        assertThrows(IllegalArgumentException.class, () ->{player.setLevel(999);});

    }

    @Test
    void setLevelNegative() {

        Player player = new Player(profile, assets);  // Create basic player from test profile

        // Attempt to set knowledge level to an invalid value: negative integer
        assertThrows(IllegalArgumentException.class, () ->{player.setLevel(-1);});

    }

    @Test
    void levelUp() {

        Player player = new Player(profile, assets);  // Create basic player from test profile
        player.levelUp();  // Level up player

        // Verify that knowledge level was incremented correctly
        assertEquals(knowledgeLevel+1, player.getLevel());            // Verify that the knowledge level is stored correctly in the player
        assertEquals(knowledgeLevel+1, profile.getKnowledgeLevel());  // Verify that the updates were made in the student profile

    }

    @Test
    void levelUpFromZero() {

        // Create player with a knowledge level of 0
        Player player = new Player(profile, assets);
        player.setLevel(0);

        // Test the boundary condition of leveling up a player with a knowledge level of 0
        player.levelUp();

        assertEquals(1, player.getLevel());            // Verify that the knowledge level is stored correctly in the player
        assertEquals(1, profile.getKnowledgeLevel());  // Verify that the updates were made in the student profile

    }

    @Test
    void levelUpAboveMax() {

        // Create player with level set to the maximum
        int maxLevel = Config.getInstance().getMaxLevel();
        Player player = new Player(profile, assets);
        player.setLevel(maxLevel);

        // Attempt to level up player
        player.levelUp();

        // Verify that the knowledge level was not updated
        assertEquals(maxLevel, player.getLevel());            // Verify that the knowledge level is stored correctly in the player
        assertEquals(maxLevel, profile.getKnowledgeLevel());  // Verify that the updates were made in the student profile

    }

    @Test
    void getStars() {

        // Create player with a specified number of stars
        int stars = 7;
        Player player = new Player(profile, assets, stars, 0, 0, null, 0, null, false, false, false, null);

        assertEquals(stars, player.getStars());   // Verify that the method returns the initialized value

    }

    @Test
    void setStars() {

        Player player = new Player(profile, assets);  // Create basic player from test profile

        // Set stars
        int stars = 12;
        player.setStars(stars);

        assertEquals(stars, player.getStars());  // Verify that the stars have been updated

    }



    @Test
    void getMultiDice() {

        // Create player with multidice
        boolean hasMultidice = true;
        Player player = new Player(profile, assets, 0, 0, 0, null, 0, null, false, hasMultidice, false, null);

        assertEquals(hasMultidice, player.getMultiDice());   // Verify that the method returns the initialized value

    }

    @Test
    void setUseMultiDice() {

        // Create player without multidice
        Player player = new Player(profile, assets, 0, 0, 0, null, 0, null, false, false, false, null);

        // Set multidice to true
        player.setUseMultiDice(true);
        assertTrue(player.getMultiDice());  // Verify that multidice was set correctly

    }


    @Test
    void getHasShield() {

        // Create player with a shield
        boolean hasShield = true;
        Player player = new Player(profile, assets, 0, 0, 0, null, 0, null, false, false, hasShield, null);

        assertEquals(hasShield, player.getHasShield());   // Verify that the method returns the initialized value

    }


    @Test
    void setHasShield() {

        // Create player without shield
        Player player = new Player(profile, assets, 0, 0, 0, null, 0, null, false, false, false, null);

        // Set shield to true
        player.setHasShield(true);
        assertTrue(player.getHasShield());  // Verify that shield was set correctly

    }


    @Test
    void isFrozen() {

        // Create player that is frozen
        boolean frozen = true;
        Player player = new Player(profile, assets, 0, 0, 0, null, 0, null, frozen, false, false, null);

        assertEquals(frozen, player.isFrozen());   // Verify that the method returns the initialized value

    }


    @Test
    void setFrozen() {

        // Create player that is not frozen
        Player player = new Player(profile, assets, 0, 0, 0, null, 0, null, false, false, false, null);

        // Set frozen to true
        player.setFrozen(true);
        assertTrue(player.isFrozen());  // Verify that the frozen attribute was set correctly

    }


    @Test
    void calculateScore() {

        // Create player with specified stars, money, and investments
        int stars = 7;
        int money = 350;
        int investments = 200;
        Player player = new Player(profile, assets, stars, money, investments, null, 0, null, false, false, false, null);

        // Calculate player's score
        player.calculateScore();

        // Verify score was calculated and returned correctly
        int expectedScore = 1000 * stars + money + investments;
        assertEquals(expectedScore, player.getScore());

    }


    @Test
    void getCurrentTile() {

        // Create player with a specified position on the game board
        String nodeID = "TestID";
        Player player = new Player(profile, assets, 0, 0, 0, null, 0, null, false, false, false, nodeID);

        assertEquals(nodeID, player.getCurrentTile());  // Verify that the method returns the initialized value

    }

    @Test
    void setCurrentTile() {

    }

    @Test
    void getItems() {

        // Create an item inventory
        ArrayList<Item> inventory = new ArrayList<Item>();

        Bike bike = new Bike(skin);
        FreezeItem freeze = new FreezeItem(skin);
        MultiDice multiDice = new MultiDice(skin);
        Shield shield = new Shield(skin);

        inventory.add(bike);  // Items added using the methods from ArrayList
        inventory.add(freeze);
        inventory.add(multiDice);
        inventory.add(shield);

        // Create player with an existing item inventory
        Player player = new Player(profile, assets, 0, 0, 0, null, 0, inventory, false, false, false, null);

        // Verify that inventory was initialized correctly
        assertEquals(4, player.getItems().size());  // All items loaded
        assertEquals(multiDice, player.getItems().get(2));   // Check that a one of the items was created successfully

    }

    @Test
    void addItem() {

        Player player = new Player(profile, assets);  // Create basic player with no items

        // Add items to the inventory
        Bike bike = new Bike(skin);
        FreezeItem freeze = new FreezeItem(skin);
        MultiDice multiDice = new MultiDice(skin);
        Shield shield = new Shield(skin);

        player.addItem(bike);  // Items added using methods from Player
        player.addItem(freeze);
        player.addItem(multiDice);
        player.addItem(shield);

        // Verify that all items were added successfully
        assertEquals(4, player.getItems().size());
        assertEquals(multiDice, player.getItems().get(2));

    }

    @Test
    void removeItem() {

        // Create an item inventory
        ArrayList<Item> inventory = new ArrayList<Item>();

        Bike bike = new Bike(skin);
        FreezeItem freeze = new FreezeItem(skin);
        MultiDice multiDice = new MultiDice(skin);
        Shield shield = new Shield(skin);

        inventory.add(bike);  // Items added using the methods from ArrayList
        inventory.add(freeze);
        inventory.add(multiDice);
        inventory.add(shield);

        // Create player with an existing item inventory
        Player player = new Player(profile, assets, 0, 0, 0, null, 0, inventory, false, false, false, null);

        // Remove an item
        player.removeItem(multiDice.getName());

        // Verify that the item was removed successfully
        assertEquals(3, player.getItems().size());
        assertNotEquals(multiDice, player.getItems().get(2));

    }

    @Test
    void removeNonExistentItem() {

        Player player = new Player(profile, assets);  // Create basic player with no items
        player.addItem(new Bike(skin));
        player.addItem(new FreezeItem(skin));

        // Attempt to remove an item that the player does not have
        assertThrows(IllegalArgumentException.class, () ->{player.removeItem("MultiDice");});

    }

    @Test
    void getDieRoll() {

    }

    @Test
    void canMove() {
    }

    @Test
    void canRoll() {
    }

    @Test
    void setRollsLeft() {
    }

    @Test
    void getReachablePaths() {
    }

    @Test
    void getSprite() {
    }

    @Test
    void getFreezeSprite() {
    }

    @Test
    void getShieldSprite() {
    }


    @Test
    void useShield() {
    }

    @Test
    void getCurrentInvestments() {
    }

    @Test
    void addInvestments() {
    }

    @Test
    void removeInvestment() {
    }

    @Test
    void updateInvestment() {
    }

    @Test
    void getInvestments() {
    }
}