/*
* Player Class to encompass all player attributes
* - Score / Money
* - Stars
* - Items
* - Current Knowledge level
 */

package com.mygdx.game;

import java.io.Serializable;

/**
 * Represents a player in the game.
 */
public class Player implements Serializable {
    String name;
    int score;
    int stars;
    int money;

    /**
     * Constructor with only name required
     *
     * @param name Player name
     */
    public Player(String name) {
        this(name, 0, 0, 0);
    }

    /**
     * Constructor for Player class
     *
     * @param name Player name
     * @param money Player money
     * @param score Player score
     * @param stars Player stars
     */
    public Player(String name, int money, int score, int stars) {
        this.name = name;
        this.score = score;
        this.stars = stars;
        this.money = money;
    }
}
