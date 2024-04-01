package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Items.*;
import com.mygdx.game.Node.Node;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    static private AssetManager asset;
    static private GameContext gameContext;

    @BeforeAll
    static void setUp() {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();
    }

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
    void useShieldTrue() {

        // Create player with a shield
        Player player = new Player(profile, assets, 0, 0, 0, null, 0, null, false, false, true, null);

        // Use player's shield
        boolean used = player.useShield();
        assertTrue(used);  // Verify that the shield was used and is active

    }

    @Test
    void useShieldFalse() {

        // Create player without a shield
        Player player = new Player(profile, assets, 0, 0, 0, null, 0, null, false, false, false, null);

        // Attempt to use a shield
        boolean used = player.useShield();
        assertFalse(used);  // Verify that the shield was unable to be used and is not active

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
        PlayerProfile profile2 = new PlayerProfile(name, lifetimeScore, highScore, knowledgeLevel);
        GameState gs = new GameState(List.of(profile, profile2), assets, 0, false);
        Player player1 = gs.getCurrentPlayer();
        gs.nextTurn();
        Player player2 = gs.getCurrentPlayer();

        // Check if Player tile can be moved to another valid tile
        String currNode = player1.getCurrentTile();
        String node2 = player2.getCurrentTile();
        player1.setCurrentTile(node2, gs.getNodeMap());
        assertEquals(node2, player1.getCurrentTile());
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

        Player player = new Player(profile, assets);    // Create basic player with no items
        assertEquals(0, player.getDieRoll());  // Verify die roll value was initialized correctly

    }

    @Test
    void canMove() {

        Player player = new Player(profile, assets);  // Create basic player with no items
        assertTrue(player.canMove());                 // Verify moves remaining was initialized correctly

    }

    @Test
    void canRoll() {

        Player player = new Player(profile, assets);  // Create basic player with no items
        assertTrue(player.canRoll());                 // Verify rolls remaining was initialized correctly

    }

    @Test
    void setRollsLeft() {

        Player player = new Player(profile, assets);  // Create basic player with no items

        // Set the number of rolls remaining to a positive value
        player.setRollsLeft(5);
        assertTrue(player.canRoll());  // Verify rolls remaining was set correctly (player has rolls remaining)

    }

    @Test
    void setNoRollsLeft() {

        Player player = new Player(profile, assets);  // Create basic player with no items

        // Set the number of rolls remaining to zero
        player.setRollsLeft(0);
        assertFalse(player.canRoll());  // Verify rolls remaining was set correctly (player has no rolls remaining)

    }

    @Test
    void getReachablePaths() {
        GameState gs = new GameState(List.of(profile), assets, 0, false);
        Player player = gs.getCurrentPlayer();
        assertEquals(0, player.getReachablePaths().size());

        // Set it to something and check
        ArrayList<ArrayList<String>> paths = player.getReachablePaths();
        paths.add(new ArrayList<String>(Collections.singleton("test")));
        assertEquals(1, player.getReachablePaths().size());
    }

    @Test
    void endTurn() {
        GameState gs = new GameState(List.of(profile), assets, 0, false);
        Player player = gs.getCurrentPlayer();

        // Roll and select a random node to move to
        player.rollDie(gs.getNodeMap());
        ArrayList<String> path = player.getReachablePaths().get(0);
        player.move(path.get(path.size() - 1), gs.getNodeMap(), Mockito.mock(SpriteBatch.class), gs.getHardMode());
        player.endTurn(gs.getNodeMap());
        assertEquals(0, player.getDieRoll());
        assertTrue(player.canMove());
        assertTrue(player.canRoll());
        // Check for incorrectly colored nodes
        for (Node node : gs.getNodeMap().values()) {
            assertEquals(Color.WHITE, node.getSprite().getColor());
        }
    }

    @Test
    void startTurn() {
        GameState gs = new GameState(List.of(profile), assets, 0, false);
        Player player = gs.getCurrentPlayer();

        // Simulate full turn movement and start a new turn
        player.rollDie(gs.getNodeMap());
        ArrayList<String> path = player.getReachablePaths().get(0);
        player.move(path.get(path.size() - 1), gs.getNodeMap(), Mockito.mock(SpriteBatch.class), gs.getHardMode());
        player.endTurn(gs.getNodeMap());
        player.startTurn(gs.getNodeMap());
        // Check if previous path was highlighted
        for (String nodeID : path) {
            assertEquals(Color.GRAY, gs.getNodeMap().get(nodeID).getSprite().getColor());
        }
    }

    @Test
    void rollDie() {
        GameState gs = new GameState(List.of(profile), assets, 0, false);
        Player player = gs.getPlayerList().get(0);

        // Simulate roll and check if a reachable path exists
        assertNotEquals(0, player.rollDie(gs.getNodeMap()));
        assertFalse(player.canRoll());
        assertNotEquals(0, player.getReachablePaths().size());
        // Check colors
        for (ArrayList<String> path : player.getReachablePaths()) {
            Node last = gs.getNodeMap().get(path.get(path.size() - 1));
            assertEquals(Color.GREEN, last.getSprite().getColor());
        }


        // Check Multidice usage
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            do {
                player.endTurn(gs.getNodeMap());
                player.setUseMultiDice(true);
                player.rollDie(gs.getNodeMap());
            } while (player.getDieRoll() < 5);
        });
    }

    @Test
    void move() {
        GameState gs = new GameState(List.of(profile), assets, 0, false);
        Player player = gs.getPlayerList().get(0);
        player.rollDie(gs.getNodeMap());
        // Check against the impossible, going where you are already
        assertThrows(IllegalArgumentException.class, () -> {
            player.move(player.getCurrentTile(), gs.getNodeMap(), Mockito.mock(SpriteBatch.class), false);
        });
        // Check against valid movement
        ArrayList<String> validPath = player.getReachablePaths().get(0);
        player.move(validPath.get(validPath.size() - 1), gs.getNodeMap(), Mockito.mock(SpriteBatch.class), false);
        assertEquals(validPath.get(validPath.size() - 1), player.getCurrentTile());
    }

    @Test
    void getSprite() {
        Player player = new Player(profile, assets);
        assertNotNull(player.getSprite());
    }

    @Test
    void getFreezeSprite() {
        Player player = new Player(profile, assets);
        assertNotNull(player.getFreezeSprite());
    }

    @Test
    void getShieldSprite() {
        Player player = new Player(profile, assets);
        assertNotNull(player.getShieldSprite());
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