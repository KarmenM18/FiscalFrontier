/*
* Player Class to encompass all player attributes
* - Score / Money
* - Stars
* - Items
* - Current Knowledge level
 */

package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a player in the game.
 */
public class Player implements Serializable {
    private PlayerProfile profile;
    private String name;
    private int score;
    private int stars;
    private int money;
    private Sprite sprite; // Sprite to render to the board
    private String spritePath;
    private String currNode;
    private int dieRoll;
    private int movesLeft;
    private int maxMovesLeft;
    private int rollsLeft;
    private int maxRollsLeft;
    private List<String> reachableNodes; // Nodes reachable from the current player's position, calculated based on their roll.


    /**
     * Constructor with only name required
     *
     * @param profile Player profile, used to link in-game player with their out-of-game profile
     */
    public Player(PlayerProfile profile, AssetManager assets) {
        this(profile, assets, 0, 0, 0);
    }

    /**
     * Constructor for Player class
     *
     * @param profile Player profile, used to link in-game player with their out-of-game profile
     * @param money Player money
     * @param score Player score
     * @param stars Player stars
     */
    public Player(PlayerProfile profile, AssetManager assets, int money, int score, int stars) {
        Config config = Config.getInstance();
        this.profile = profile;
        this.name = profile.getName();
        this.score = score;
        this.stars = stars;
        this.money = money;
        this.currNode = null;
        this.dieRoll = 0;
        this.movesLeft = 1;
        this.maxMovesLeft = 1;
        this.rollsLeft = 1;
        this.maxRollsLeft = 1;
        reachableNodes = new ArrayList<>();

        this.spritePath = config.getPlayerPath();
        sprite = new Sprite((Texture)assets.get(spritePath));
        loadTextures(assets);
        sprite.setSize(50, 50);
        sprite.setPosition(0, 0);
    }

    private Player() {}

    public void loadTextures(AssetManager assets) {
        sprite.setTexture(assets.get(spritePath));
    }

    public void nextTurn(Map<String, Node> nodeMap) {
        movesLeft = maxMovesLeft;
        rollsLeft = maxRollsLeft;
        // Clear reachable nodes
        for (String reachable : reachableNodes) {
            nodeMap.get(reachable).setNoColor();
        }
        reachableNodes.clear();
        dieRoll = 0;
    }

    public int rollDie(Map<String, Node> nodeMap) {
        dieRoll = Utility.getRandom(1, 6);
        // Get reachable nodes and color them
        reachableNodes = nodeMap.get(currNode).getReachable(dieRoll, nodeMap);
        for (String reachable : reachableNodes) {
            nodeMap.get(reachable).setGreen();
        }

        rollsLeft--;

        return dieRoll;
    }

    public void move(String nodeID, Map<String, Node> nodeMap) {
        if (!reachableNodes.contains(nodeID)) {
            throw new IllegalArgumentException("Invalid movement; Node not in reachable list.");
        }

        setNode(nodeID, nodeMap);
        nodeMap.get(nodeID).activate(this);

        // Clear reachable nodes
        for (String reachable : reachableNodes) {
            nodeMap.get(reachable).setNoColor();
        }
        reachableNodes.clear();
        dieRoll = 0;
        movesLeft--;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addStar() {
        stars++;
    }

    public int getStars() {
        return stars;
    }

    public int getMoney() {
        return money;
    }

    public Sprite getSprite() { return sprite; }

    public String getNode() { return currNode; }

    public void setNode(String nodeID, Map<String, Node> nodeMap) {
        currNode = nodeID;
        sprite.setPosition(nodeMap.get(nodeID).getXPos(), nodeMap.get(nodeID).getYPos());
    }

    public int getDieRoll() { return dieRoll; }

    public boolean canMove() { return movesLeft > 0; }
    public boolean canRoll() { return rollsLeft > 0; }

    public List<String> getReachableNodes() {
        return reachableNodes;
    }
}
