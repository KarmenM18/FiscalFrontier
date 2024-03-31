package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.Items.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    /** Player's assets (sprites, etc.) */
    static private AssetManager assets;
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
    }

    @Test
    void setStars() {
    }

    @Test
    void setUseMultiDice() {
    }

    @Test
    void getMultiDice() {
    }

    @Test
    void setHasShield() {
    }

    @Test
    void getHasShield() {
    }

    @Test
    void calculateScore() {
    }

    @Test
    void getScore() {
    }

    @Test
    void getCurrentTile() {
    }

    @Test
    void setCurrentTile() {
    }

    @Test
    void getItems() {
    }

    @Test
    void addItem() {
    }

    @Test
    void removeItem() {
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
    void setFrozen() {
    }

    @Test
    void isFrozen() {
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