/*
 * TODO Documentation
 */
package com.mygdx.game;


import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Random;
import com.badlogic.gdx.utils.Json;
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
    /**
     * TODO: remove knowledge level since level is now discrete
     */
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
    public PlayerProfile(String name) {

        this(name, 0, 0, 1);

        // Generate random knowledge level TODO: Is this intended?
        Random rn = new Random();
        this.knowledgeLevel = rn.nextInt(13)+1;

    }

    /**
     * Private no-arg constructor for serialization
     */
    private PlayerProfile() {}

    public void addXP(int xp) {
        // TODO level curve
        while (xp >= 1000) {
            xp -= 1000;
            knowledgeLevel++;
        }
    }

    public void addScore(int score) {
        this.lifetimeScore += score;
        if (score > highScore) highScore = score;
    }

    public void updateKnowledgeLevel () {
        this.knowledgeLevel++;
        updateKnowledgeBase();
    }

    public String getName() {
        return name;
    }

    public int getLifetimeScore() {
        return lifetimeScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public String getSpritePath() { return spritePath; }

    private void updateKnowledgeBase () {

        //Player levels up every 3 rounds for a total of 13 levels
        //Each knowledge level grants 5 tips. Total of 65 "tips"
        //As the player levels up, more and more complicated tips are granted
        int line = this.knowledgeLevel * 5;

        //Reading from file
        try {
            Scanner catalog = new Scanner(new File("external/knowledge catalog.txt"));
            for (int i = 0; i < line; i++) { //Getting all knowledge up to line
                if (!catalog.hasNextLine()) break; //No more lines to read

                this.learned.add(catalog.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String getRandomTip () {

        int randTip = (int) (Math.random() * this.learned.size());
        try {
            return this.learned.get(randTip);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }


    public LinkedList<String> getLearned () {return this.learned;}


    /**
     * Returns the number of tips unlocked.
     * @return Number of tips unlocked
     */
    public int getTipCount() {
        return this.learned.size();
    }

}