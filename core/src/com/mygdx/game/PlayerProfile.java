package com.mygdx.game;


import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.badlogic.gdx.utils.Json;
import com.mygdx.game.Items.Item;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.Serializable;

/**
 * Represents a player when not on the game board.
 */
public class PlayerProfile {

    private String name;
    private int lifetimeScore;
    private int highScore;
    private int knowledgeLevel;
    private LinkedList<String> learned = new LinkedList<>(); //Storing all the learned knowledge from a knowledge catalog
    private String spritePath; // Path of the sprite to use when rendering the Player

    /**
     * Constructor used to load an existing player profile.
     *
     * @param name
     * @param lifetimeScore
     * @param highScore
     * @param knowledgeLevel
     */
    public PlayerProfile(String name, int lifetimeScore, int highScore, int knowledgeLevel) {

        // Initialize all profile fields
        this.name = name;
        this.lifetimeScore = lifetimeScore;
        this.highScore = highScore;
        this.knowledgeLevel = knowledgeLevel;

        // Load knowledge base based on knowledge level
        this.updateKnowledgeBase();

        Config config = Config.getInstance();
        spritePath = config.getPlayerPath();
    }

    /**
     * Constructor creates a new player profile for a student.
     *
     * @param name Student's name
     */
    public PlayerProfile(String name) {this(name, 0, 0, 1);}

    /**
     * Private no-arg constructor for serialization
     */
    private PlayerProfile() {}

    /**
     * Increases the player's knowledge level and updates
     * their learned knowledge list
     */
    public void updateKnowledgeLevel () {
        this.knowledgeLevel++;
        updateKnowledgeBase();
    }

    /**
     * @return the Player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Purpose: sets the name of the Player
     */
    public void setName(String pName) {
        this.name = pName;
    }

    /**
     * Purpose: gets the Player's lifetime score
     * @return lifetimeScore of Player
     */
    public int getLifetimeScore() {
        return lifetimeScore;
    }

    /**
     * Purpose: sets the Player's lifetime score
     */
    public void setLifetimeScore(int newScore) {
        this.lifetimeScore = newScore;
    }

    /**
     * Purpose: gets the Player's highest score unlocked
     * @return the Player's maximum score in a single game
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * Purpose: sets the Player's new high score
     */
    public void setHighScore(int newScore) {
        this.highScore = newScore;
    }

    /**
     * Purpose: gets the Player's knowledge level
     * @return knowledgeLevel of Player
     */
    public int getKnowledgeLevel() {
        return knowledgeLevel;
    }

    /**
     * Purpose: sets the Player's knowledgeLevel and updates the knowledge base.
     */
    public void setKnowledgeLevel(int newLevel) {
        this.knowledgeLevel = newLevel;
        this.updateKnowledgeBase();
    }

     /** @return the file path of the Player's Sprite
     */
    public String getSpritePath() { return spritePath; }

    /**
     * Updates the player's learned knowledge
     */
    private void updateKnowledgeBase () {

        //Player levels up every 3 rounds for a total of 13 levels
        //Each knowledge level grants 5 tips. Total of 65 "tips"
        //As the player levels up, more and more complicated tips are granted
        int line = this.knowledgeLevel * 5;
        this.learned = new LinkedList<>(); //Resetting list

        //Reading from file
        try {

            // Define path to assets directory, where knowledge catalog is stored
            String catalogPathname = "external/knowledge catalog.txt";
            String currentDirectory = System.getProperty("user.dir");

            // Specify absolute path if running from unit tests folder
            if (currentDirectory.endsWith("core")) {
                catalogPathname = currentDirectory.substring(0, currentDirectory.length()-4) + "assets/" + catalogPathname;
            }

            // Open file
            Scanner catalog = new Scanner(new File(catalogPathname));

            for (int i = 0; i < line; i++) { //Getting all knowledge up to line
                if (!catalog.hasNextLine()) break; //No more lines to read

                this.learned.add(catalog.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return LinkedList of all knowledge learned
     */
    public LinkedList<String> getLearned () {return this.learned;}

    /**
     * Returns the number of tips unlocked.
     * @return Number of tips unlocked
     */
    public int getTipCount() {
        return this.knowledgeLevel*5;
    }

}