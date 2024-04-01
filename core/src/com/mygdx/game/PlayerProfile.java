package com.mygdx.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * Represents a player when not on the game board.
 * <br><br>
 * Stores all the student's persistent information, including their highest achieved individual game score, overall
 * lifetime score (the cumulative score from all played games), their knowledge level, and the tips they have unlocked.
 *
 * @author Karmen Minhas (kminhas7)
 * @author Joelene Hales (jhales5)
 */
public class PlayerProfile {

    /** Student's name. */
    private String name;
    /** Student's lifetime score (cumulative score from all played game) */
    private int lifetimeScore;
    /** Student's highest achieved individual game score. */
    private int highScore;
    /** Student's knowledge level. */
    private int knowledgeLevel;
    /** Stores all the learned knowledge from the knowledge catalog. Based on the student's knowledge level. */
    private LinkedList<String> learned = new LinkedList<>();
    /** Path of the sprite to use when rendering the Player */
    private String spritePath;

    /**
     * Constructor used to load an existing player profile.
     *
     * @param name Student's name
     * @param lifetimeScore Student's lifetime score
     * @param highScore Student's high score
     * @param knowledgeLevel Student's knowledge level
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
     * Increases the player's knowledge level and updates their learned knowledge base.
     */
    public void updateKnowledgeLevel () {
        this.knowledgeLevel++;
        updateKnowledgeBase();
    }

    /**
     * Returns the studen't name.
     * @return Student's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Purpose: sets the name of the student.
     * @param sName New name for student
     */
    public void setName(String sName) {
        this.name = sName;
    }

    /**
     * Purpose: gets the student's lifetime score (cumulative score from all played games)
     * @return Student's lifetime score
     */
    public int getLifetimeScore() {
        return this.lifetimeScore;
    }

    /**
     * Sets the student's lifetime score
     * @param newScore Student's lifetime score
     */
    public void setLifetimeScore(int newScore) {
        this.lifetimeScore = newScore;
    }

    /**
     * Purpose: gets the slayer's highest score unlocked
     * @return the slayer's maximum score in a single game
     */
    public int getHighScore() {
        return this.highScore;
    }

    /**
     * Purpose: sets the student's new high score
     * @param newScore New high score
     */
    public void setHighScore(int newScore) {
        this.highScore = newScore;
    }

    /**
     * Purpose: gets the student's knowledge level
     * @return Student's knowledge level
     */
    public int getKnowledgeLevel() {
        return knowledgeLevel;
    }

    /**
     * Purpose: sets the student's knowledge level and updates the knowledge base.
     * @param newLevel New knowledge level
     */
    public void setKnowledgeLevel(int newLevel) {
        this.knowledgeLevel = newLevel;
        this.updateKnowledgeBase();
    }

     /**
      * Returns the filepath of the player's sprite.
      * @return File path of the player's Sprite
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
     * Returns a list of the student's knowledge base.
     * @return List of the student's knowledge base.
     */
    public LinkedList<String> getLearned () {return this.learned;}

    /**
     * Returns the number of tips unlocked.
     * @return Number of tips unlocked.
     */
    public int getTipCount() {
        return this.knowledgeLevel*5;
    }

}