/**
 * Stores a game state. Used by the GameBoard. Can be saved and loaded.
 * At the end of a game, the players profiles should be updated with anything relevant stored here.
 */
package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GameState implements Serializable {
    private List<Player> playerList;
    private int currPlayerTurn;
    private int turnNumber;
    private EventNode eventNode;
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
        nodeMap.put("0,0", new NormalNode(0, 0, false, false, false, true, nodeMap, assets));
        nodeMap.put("1,0", new NormalNode(1, 0, false, false, false, true, nodeMap, assets));
        nodeMap.put("2,0", new NormalNode(2, 0, false, true, false, true, nodeMap, assets));
        nodeMap.put("2,1", new EventNode(2, 1, false, false, true, false, nodeMap, assets));
        nodeMap.put("2,2", new NormalNode(2, 2, false, false, true, false, nodeMap, assets));
        nodeMap.put("3,0", new PenaltyNode(3, 0, false, true, false, false, nodeMap, assets));
        nodeMap.put("4,0", new NormalNode(4, 0, true, false, false, false, nodeMap, assets));
        nodeMap.put("4,1", new NormalNode(4, 1, true, false, false, false, nodeMap, assets));
        nodeMap.put("3,2", new NormalNode(3, 2, false, false, false, true, nodeMap, assets));
        nodeMap.put("4,2", new StarNode(4, 2, false, false, false, true, nodeMap, assets));
        nodeMap.put("-1,0", new NormalNode(-1, 0, true, false, false, false, nodeMap, assets));
        nodeMap.put("-1,1", new StarNode(-1, 1, true, false, false, false, nodeMap, assets));
        nodeMap.put("-1,2", new NormalNode(-1, 2, false, true, false, false, nodeMap, assets));
        nodeMap.put("0,2", new PenaltyNode(0, 2, false, true, false, false, nodeMap, assets));
        nodeMap.put("1,2", new NormalNode(1, 2, false, true, false, false, nodeMap, assets));

        eventNode.addEventListener(v -> globalEventMode(eventNode.penaltyAmount));

        // Set starting nodes - player cannot start on a special node, only a plain node
        // They also can't start on the same node as another player
        ArrayList<String> nodeIDs = new ArrayList<>();
        for (Node node : nodeMap.values()) {
            if (node.getClass().equals(NormalNode.class)) nodeIDs.add(node.getID());
        }
        Utility.shuffle(nodeIDs);

        assert(nodeIDs.size() >= playerList.size());
        for (Player player : playerList) {
            player.setCurrentTile(nodeIDs.get(nodeIDs.size() - 1), nodeMap);
            nodeIDs.remove(nodeIDs.size() - 1);
        }
    }

    /**
     * No-arg constructor for serialization
     */
    private GameState() {}

    public void loadTextures(AssetManager assets) {
        for (Node node : nodeMap.values()) {
            node.loadTextures(assets);
        }

        for (Player player : playerList) {
            player.loadTextures(assets);
        }
    }

    /**
     * End the current Player's turn, and start the next Player's turn
     */
    public void nextTurn() {
        // Wipe Player's calculated turn values
        getCurrentPlayer().endTurn(nodeMap);
        currPlayerTurn = (currPlayerTurn + 1) % playerList.size();
        getCurrentPlayer().startTurn(nodeMap);
        turnNumber++;
    }

    /**
     * Get current Player. Based on the current turn.
     *
     * @return Player
     */
    public Player getCurrentPlayer() { return playerList.get(currPlayerTurn); }

    /**
     * @return List of Players
     */
    public List<Player> getPlayerList() { return playerList; }


    /**
     * @return Map of nodes
     */
    public Map<String, Node> getNodeMap() { return nodeMap; }

    /**
     * global event for event node, reduce all player's money
     * @param penaltyAmount
     */
    public void globalEventMode(int penaltyAmount){
        for (Player p : getPlayerList()){
            p.setMoney(p.getMoney() - penaltyAmount);
        }
    }



}
