/*
 * TODO Documentation
 */
package com.mygdx.game;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

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
        this.name = name;
        lifetimeScore = 0;
        knowledgeLevel = 0;
        highScore = 0;

        Config config = Config.getInstance();
        spritePath = config.getPlayerPath();
    }

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
            Scanner catalog = new Scanner(new File("knowledge catalog.txt"));
            for (int i = 0; i < line; i++) { //Getting all knowledge up to line
                if (!catalog.hasNextLine()) break; //No more lines to read

                this.learned.add(catalog.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}