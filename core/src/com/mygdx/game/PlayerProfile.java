/*
 * Player Profile Class which is used for player handling outside of the game board.
 */
package com.mygdx.game;

public class PlayerProfile {
    private String name;
    private int lifetimeScore;
    private int highScore;
    private int knowledgeLevel;
    private int xp;

    public PlayerProfile(String name) {
        this.name = name;
        lifetimeScore = 0;
        knowledgeLevel = 0;
        xp = 0;
        highScore = 0;
    }

    private PlayerProfile() {}

    public void addXP(int xp) {
        this.xp += xp;
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
}
