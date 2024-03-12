/**
 * Stores a game state. Used by the GameBoard. Can be saved and loaded.
 * At the end of a game, the players profiles should be updated with anything relevant stored here.
 */
package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GameState implements Serializable {
    private List<Player> playerList;
    private int currPlayerTurn;
    private int turnNumber;
    private HashMap<String, Node> nodeMap;

    /**
     * Constructor
     * Will throw error if the profileList is null or empty
     *
     * @param profileList The profile list of players in the game
     */
    public GameState(List<PlayerProfile> profileList, AssetManager assets) {
        if (profileList == null || profileList.isEmpty()) {
            throw new IllegalArgumentException("Player list cannot be empty");
        }
        this.playerList = new ArrayList<Player>();
        currPlayerTurn = 0;
        turnNumber = 0;

        for (PlayerProfile playerProfile : profileList) {
            Player player = new Player(playerProfile, assets);
            // Each player gets a different color
            player.getSprite().setColor(Utility.getRandom(0, 255) / 255f, Utility.getRandom(0, 255) / 255f, Utility.getRandom(0, 255) / 255f, 1);
            this.playerList.add(player);
        }

        // Setup nodes
        nodeMap = new HashMap<String, Node>();
        nodeMap.put("0", new Node("0", 50, 50, assets));
        nodeMap.put("10", new Node("10", 150, 50, null, null, null, "0", nodeMap, assets));
        nodeMap.put("20", new Node("20", 250, 50, null, null, null, "10", nodeMap, assets));
        nodeMap.put("21", new Node("21", 250, 150, null, null, "20", null, nodeMap, assets));
        nodeMap.put("22", new Node("22", 250, 250, null, null, "21", null, nodeMap, assets));
        nodeMap.put("30", new Node("30", 350, 50, null, null, null, "20", nodeMap, assets));
        nodeMap.put("40", new Node("40", 450, 50, null, null, null, "30", nodeMap, assets));
        nodeMap.put("41", new Node("41", 450, 150, null, null, "40", null, nodeMap, assets));
        nodeMap.put("50", new StarNode("50", 550, 50, null, null, null, "40", nodeMap, assets));
        nodeMap.put("32", new Node("32", 350, 250, null, null, null, "22", nodeMap, assets));
        nodeMap.put("42", new StarNode("42", 450, 250, null, null, "41", "32", nodeMap, assets));
        // Set starting nodes - player cannot start on a special node, only a plain node
        // They also can't start on the same node as another player
        ArrayList<String> nodeIDs = new ArrayList<>();
        for (Node node : nodeMap.values()) {
            if (node.getClass().equals(Node.class)) nodeIDs.add(node.getID());
        }
        Utility.shuffle(nodeIDs);

        assert(nodeIDs.size() >= playerList.size());
        for (Player player : playerList) {
            player.setNode(nodeIDs.getLast(), nodeMap);
            nodeIDs.removeLast();
        }
    }

    private GameState() {}

    public void loadTextures(AssetManager assets) {
        for (Node node : nodeMap.values()) {
            node.loadTextures(assets);
        }

        for (Player player : playerList) {
            player.loadTextures(assets);
        }
    }

    public void nextTurn() {
        // Wipe calculated turn values
        getCurrentPlayer().nextTurn(nodeMap);
        currPlayerTurn = (currPlayerTurn + 1) % playerList.size();
        turnNumber++;
    }

    public Player getCurrentPlayer() { return playerList.get(currPlayerTurn); }

    public List<Player> getPlayerList() { return playerList; }
    public Map<String, Node> getNodeMap() { return nodeMap; }
}
