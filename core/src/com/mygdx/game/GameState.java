/**
 * Stores a game state. Used by the GameBoard. Can be saved and loaded.
 * At the end of a game, the players profiles should be updated with anything relevant stored here.
 */
package com.mygdx.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    List<Player> playerList;
    int currPlayerTurn = 0;
    int turnNumber = 0;

    /**
     * Constructor
     * Will throw error if profileList is null or empty
     *
     * @param profileList The profile list of players in the game
     */
    public GameState(List<PlayerProfile> profileList) {
        if (profileList == null || profileList.isEmpty()) {
            throw new IllegalArgumentException("Player list cannot be empty");
        }
        this.playerList = new ArrayList<Player>();

        for (PlayerProfile playerProfile : profileList) {
            this.playerList.add(new Player(playerProfile));
        }
    }
}
