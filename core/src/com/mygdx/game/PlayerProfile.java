package com.mygdx.game;

import java.util.ArrayList;

/**
 * Contains necessary information for viewing and updating Player information
 * <br><br>
 * Methods include displaying and setting player name, tips, lifetime score, high score, knowledge level and XP.
 */
public class PlayerProfile {
    String name;
    ArrayList<Integer> tips = new ArrayList<>();
    int knowledgeLevel;
    int xp; // are we still using XP as a metric in the game?
    int lifetimeScore;
    int highScore;

    /**
     * Constructor for class PlayerProfile
     * @param playerName name of the player to view and update information on
     */
    public PlayerProfile (String playerName) {
        this.name = playerName;
    }

    /**
     * Purpose: gets the name of the Player
     * @return name the name of the player
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
     * Purpose: gets the Player's list of tips
     * @return tips the list of tips that the player has unlocked so far
     */
    public ArrayList<Integer> getTips() {
        return tips;
    }

    /**
     * Purpose: adds a new tip to the Player's list
     */
    public void addTip(int tipNum) {
        tips.add(tipNum);
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
     * @return highScore of PLayer
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
     * Purpose: sets the Player's knowledgeLevel
     */
    public void setKnowledgeLevel(int newLevel) {
        this.knowledgeLevel = newLevel;
    }

    /**
     * Purpose: increments the Player's existing XP score with newly unlocked XP
     */
    public void addXP (int newXP) {
        xp = xp + newXP;
    }

}