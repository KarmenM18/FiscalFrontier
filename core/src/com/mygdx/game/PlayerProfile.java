/*
 * TODO Documentation
 */
package com.mygdx.game;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Random;

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

    public PlayerProfile(String name) {

        Random rn = new Random();
        this.name = name;
        lifetimeScore = 0;
        knowledgeLevel = rn.nextInt(13)+1;
        highScore = 0;

        this.updateKnowledgeBase();
        Config config = Config.getInstance();
        spritePath = config.getPlayerPath();
    }

    /**
     * No-arg constructor for deserialization purposes.
     */
    private PlayerProfile() {}

    /**
     * Add xp to the Player.
     *
     * @param xp amount of xp to add
     */
    public void addXP(int xp) {
        // TODO level curve
        while (xp >= 1000) {
            xp -= 1000;
            knowledgeLevel++;
        }
    }

    /**
     * Add score to the Player.
     *
     * @param score the amount of score to add
     */
    public void addScore(int score) {
        this.lifetimeScore += score;
        if (score > highScore) highScore = score;
    }

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
     * @return the Player's lifetime score
     */
    public int getLifetimeScore() {
        return lifetimeScore;
    }

    /**
     * @return the Player's maximum score in a single game
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * @return the Player's knowledge level
     */
    public int getKnowledgeLevel() {
        return knowledgeLevel;
    }

    /**
     * @return the file path of the Player's Sprite
     */
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
}