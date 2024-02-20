/*
 * Player Profile Class which is used for player handling outside of games
 * - Name
 * - Lifetime score
 */
package com.mygdx.game;

import java.io.Serializable;

public class PlayerProfile implements Serializable {
    String name;
    int lifetimeScore;

    public PlayerProfile(String name) {
        this.name = name;
        lifetimeScore = 0;
    }
}
