/**
 * Stores a game state. Used by the GameBoard. Can be saved and loaded.
 * At the end of a game, the players profiles should be updated with anything relevant stored here.
 */
package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Items.Bike;
import com.mygdx.game.Items.FreezeItem;
import com.mygdx.game.Items.MultiDice;
import com.mygdx.game.Items.Shield;

import java.io.Serializable;
import java.util.*;

public class GameState implements Serializable {
    private List<Player> playerList;
    private int currPlayerTurn;
    private int turnNumber;
    private HashMap<String, Node> nodeMap;
    transient private AssetManager assetMan;
    private final int maxStar = 3;
    private final int minStar = 1;
    private int currentStar;

    /**
     * Constructor
     * Will throw error if the profileList is null or empty
     *
     * @param profileList The profile list of players in the game
     */
    public GameState(List<PlayerProfile> profileList, AssetManager assets) {
        assetMan = assets;
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

            Config config = Config.getInstance();
            Skin skin = assets.get(config.getUiPath(), Skin.class);
            // TODO TESTING, 25% CHANCE PER ADDITIONAL RANDOM ITEM
            for (int i = 0; i == 0; i = Utility.getRandom(0, 3)) {
               player.addItem(new Bike(skin));
               player.addItem(new MultiDice(skin));
               player.addItem(new Shield(skin));
            }
            for (int i = 0; i == 0; i = Utility.getRandom(0, 3)) {
                player.addItem(new FreezeItem(skin));
            }
        }

        // Setup nodes
        nodeMap = new HashMap<String, Node>();
        nodeMap.put("0,0", new NormalNode(0, 0, false, false, false, true, nodeMap, assets));
        nodeMap.put("1,0", new NormalNode(1, 0, false, false, false, true, nodeMap, assets));
        nodeMap.put("2,0", new NormalNode(2, 0, false, true, false, true, nodeMap, assets));
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
        createEventNode(2, 1, false, false, true, false);

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
        assetMan = assets;
        for (Node node : nodeMap.values()) {
            // Restore Node Observers
            if (node instanceof EventNode) {
                ((EventNode)node).addEventListener(penaltyAmount -> globalEvent(penaltyAmount));
            }
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
        removeStar(nodeMap);
        checkStar(nodeMap);
        currPlayerTurn = (currPlayerTurn + 1) % playerList.size();
        if (getCurrentPlayer().isFrozen()) {
            // TODO Inform that player was frozen
            getCurrentPlayer().setFrozen(false);
            // Skip turn
            currPlayerTurn = (currPlayerTurn + 1) % playerList.size();
            turnNumber++;
        }
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
    public void globalEvent(int penaltyAmount){
        //TODO adjust Money penalty logic for hardmode
        for (Player p : getPlayerList()){
            if(p.getHasShield()){
                //do nothing
            }else if(p.getStars() > 0){
                p.setStars(p.getStars() - 1);
            }else if(p.getMoney() > 0){
                p.setMoney(p.getMoney() - penaltyAmount);
            }
        }
    }

    /**
     * Runs when there is less star than per turn limit
     * per turn limit is rand based on minStar and maxStar
     * @param nodeMap
     */
    public void checkStar(HashMap<String, Node> nodeMap){
        //should only run when map has
        //not getting the right current node
        currentStar = 0;
        for (HashMap.Entry<String, Node> node : nodeMap.entrySet()) {
            if (node.getValue() instanceof StarNode) {
                currentStar++;
            }
        }
        System.out.println(currentStar);//for testing
        int starLimit = Utility.getRandom(minStar, maxStar); //setting random number of max star every turn
        if(currentStar < starLimit){ //only add new star if currentStar is less than maxStar
            List<Node> normalNodeList = new ArrayList<Node>();
            for (HashMap.Entry<String, Node> node : nodeMap.entrySet()) {
                Node node1 = node.getValue();
                if (node1 instanceof NormalNode) {
                    normalNodeList.add(node1);
                    Utility.shuffle(normalNodeList);
                }
            }
            Node randNode = normalNodeList.get(Utility.getRandom(0, normalNodeList.size() - 1));
            String randKey = randNode.getID();
            int x = randNode.getMapX();
            int y = randNode.getMapY();
            boolean north = randNode.getNorth();
            boolean south = randNode.getSouth();
            boolean west = randNode.getWest();
            boolean east = randNode.getEast();
            StarNode newStar = new StarNode(x, y, north, east, south, west, nodeMap, assetMan);
            newStar.hasStar = true;
            newStar.checkStar();
            nodeMap.remove(randKey);
            nodeMap.put(randKey, newStar);
        }
    }
    public void removeStar(HashMap<String, Node> nodeMap){
        Node currentNode = nodeMap.get(getCurrentPlayer().getCurrentTile());
        if(currentNode instanceof StarNode){
            if(!((StarNode) currentNode).hasStar){
                String currentKey = currentNode.getID();
                int x = currentNode.getMapX();
                int y = currentNode.getMapY();
                boolean north = currentNode.getNorth();
                boolean south = currentNode.getSouth();
                boolean west = currentNode.getWest();
                boolean east = currentNode.getEast();
                nodeMap.remove(currentKey);
                NormalNode newNormal = new NormalNode(x, y, north, east, south, west, nodeMap, assetMan);
                nodeMap.put(currentKey, newNormal);
            }
        }
    }

    public void createEventNode(int x, int y, boolean north, boolean east, boolean south, boolean west) {
        EventNode eventNode = new EventNode(x, y, false, false, true, false, nodeMap, assetMan);
        eventNode.addEventListener(penaltyValue -> globalEvent(penaltyValue));
        nodeMap.put(x + "," + y, eventNode);
    }
}
