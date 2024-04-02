package com.mygdx.game.Items;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.GameState;
import com.mygdx.game.Node.Node;
import com.mygdx.game.Player;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Bike allows the player to go to any tile on the map in one move.
 * @author Franck Limtung (flimtung)
 */
public class Bike extends Item {
    /**
     * Initializer
     *
     * @param skin Skin to initialize Item with
     */
    public Bike(Skin skin) {
        super("Bike", false, skin);
        this.price = 200;
        this.description = "Used to go anywhere on the board.";
    }

    /**
     * Activate the Bike on a Player
     *
     * @param player    Player to use Bike on
     * @param gameState GameState containing tiles
     * @param stage     Stage to render onto
     * @return true, indicating that the Bike was consumed
     */
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
