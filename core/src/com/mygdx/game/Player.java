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
    PlayerProfile profile;
    int score;
    int stars;
    int money;

    /**
     * Constructor with only name required
     *
     * @param profile Player profile, used to link in-game player with their out-of-game profile
     */
    public Player(PlayerProfile profile) {
        this(profile, 0, 0, 0);
    }

    /**
     * Constructor for Player class
     *
     * @param profile Player profile, used to link in-game player with their out-of-game profile
     * @param money Player money
     * @param score Player score
     * @param stars Player stars
     */
    public Player(PlayerProfile profile, int money, int score, int stars) {
        this.profile = profile;
        this.score = score;
        this.stars = stars;
        this.money = money;
    }
}
