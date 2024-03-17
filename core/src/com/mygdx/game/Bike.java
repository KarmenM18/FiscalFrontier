package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.Arrays;

//TODO make sure to auto next turn after use
public class Bike extends Item {
    public Bike(Skin skin) {
        super("Bike", false, skin);
    }

    @Override
    public boolean use(Player player, GameState gameState, Stage stage) {
        for (String key : gameState.getNodeMap().keySet()) {
            // Add all tiles as possible tiles for the player to go to, except their current tile
            if (player.getCurrentTile().equals(key)) continue;
            player.getReachablePaths().add(new ArrayList<String>(Arrays.asList(player.getCurrentTile(), key)));
            Node currNode = gameState.getNodeMap().get(key);
            currNode.setGreen();
        }
        player.setRollsLeft(0);

        return true;
    }

    private Bike() {}
}
