/**
 * The Node class. A node is a clickable tile on the board.
 * It is linked to other nodes in four possible directions.
 */
package com.mygdx.game.Node;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Config;
import com.mygdx.game.GameBoard;
import com.mygdx.game.Player;


import java.io.Serializable;
import java.util.*;

/**
 * Represents a Node on the game board. 
 */
public abstract class Node implements Serializable {
    protected String nodeID; // ID of the node in the Node Map
    protected Sprite sprite; // Sprite to render node on the board
    protected Texture tileTexture; // Texture of the board tile
    protected int x, y;
    protected int separateDist = 150; // Distance between nodes on the map

    // Enabled directions. These control which adjacent nodes are accessible from this node
    protected boolean north = false;
    protected boolean east = false;
    protected boolean south = false;
    protected boolean west = false;


    /**
     * Constructor for Node.
     *
     * @param mapX   X position
     * @param mapY   Y position
     * @param north  indicates if there is a node North on the map
     * @param east   indicates if there's a node East on the map
     * @param south  indicates if there's a node South on the map
     * @param west   indicates if there's a node West on the map
     * @param assets AssetManager to use
     */
    public Node(int mapX, int mapY, boolean north, boolean east, boolean south, boolean west, AssetManager assets) {
        this(mapX, mapY, assets);

        // Set available directions
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    /**
     * Simpler constructor which assumes there are no linked nodes.
     *
     * @param mapX X position
     * @param mapY Y position
     * @param assets AssetManager to use
     */
    public Node(int mapX, int mapY, AssetManager assets) {
        this.nodeID = String.valueOf(mapX) + "," + String.valueOf(mapY);
        this.x = mapX;
        this.y = mapY;

        // Setup sprite
        Config config = Config.getInstance();
        tileTexture = assets.get(config.getTilePath());
        sprite = new Sprite(tileTexture);
        sprite.setSize(100, 100);
        sprite.setPosition(x * separateDist, y * separateDist);
        loadTextures(assets);
    }

    /**
     * No-arg constructor for serialization
     */
    protected Node() {}

    /**
     * Load/reload textures. Required after deserialization.
     *
     * @param assets AssetManager to use
     */
    public void loadTextures(AssetManager assets) {
        Config config = Config.getInstance();
        tileTexture = assets.get(config.getTilePath());
        sprite.setTexture(tileTexture);
    }

    /**
     * Runs when the Node is landed on.
     *
     * @param player the Player who landed on the Node
     * @param batch the SpriteBatch to draw on
     * @param hardmode whether the gameState is in hard mode
     */
    public void activate(Player player, SpriteBatch batch, boolean hardmode) {
    }

    /**
     * Run by the GameBoard when the Node is landed on. Used to allow interaction with the GameBoard.
     * To delay passing of a turn, this function can return true to indicate that it will control starting the next turn.
     *
     * @param player the Player who landed on the Node
     * @param batch the SpriteBatch to draw on
     * @param stage the GameBoard's Stage
     * @param skin the GameBoard's Skin
     * @param board the GameBoard executing the function
     * @param hardmode whether the gameState is in hard mode
     * @return true if the Node will handle changing the turn, false otherwise
     */
    public boolean activate(Player player, SpriteBatch batch, Stage stage, Skin skin, GameBoard board, boolean hardmode) {
        return false; // Returns false if the GameBoard should start the next turn automatically
    }

    /**
     * Draw the Node.
     *
     * @param batch the Batch to draw with
     */
    public void draw(Batch batch) {
        sprite.draw(batch);
    }

    /**
     * Get all Nodes reachable from this Node, exactly {@code distance} nodes away from it.
     * Returns the whole path from the current node to the reachable node.
     *
     * @param distance EXACT distance from this node to match
     * @param prevNodeID previous node taken. Used to prevent going backwards
     * @param nodeMap Map of nodes
     * @return the list of paths to reachable nodes
     */
    public ArrayList<ArrayList<String>> getReachable(int distance, String prevNodeID, Map<String, Node> nodeMap) {
        ArrayList<ArrayList<String>> foundNodes = new ArrayList<>();
        if (distance < 1) return foundNodes; // Nothing in reach

        ArrayList<String> path = new ArrayList<>(List.of(nodeID));
        // Try all directions by adding / removing 1 from the x and y coordinates
        String northID = String.valueOf(x) + "," + String.valueOf(y + 1);
        String eastID = String.valueOf(x + 1) + "," + String.valueOf(y);
        String southID = String.valueOf(x) + "," + String.valueOf(y - 1);
        String westID = String.valueOf(x - 1) + "," + String.valueOf(y);
        if (north && !northID.equals(prevNodeID)) {
            getReachableRecur(nodeMap.get(northID), path, 1, distance, foundNodes, nodeMap);
        }
        if (east && !eastID.equals(prevNodeID)) {
            getReachableRecur(nodeMap.get(eastID), path, 1, distance, foundNodes, nodeMap);
        }
        if (south && !southID.equals(prevNodeID)) {
            getReachableRecur(nodeMap.get(southID), path, 1, distance, foundNodes, nodeMap);
        }
        if (west && !westID.equals(prevNodeID)) {
            getReachableRecur(nodeMap.get(westID), path, 1, distance, foundNodes, nodeMap);
        }

        return foundNodes;
    }

    /**
     * Recursive helper function for getReachable. Depth First Search.
     *
     * @param curr the current node
     * @param path the path of nodes from the starting node to here (going backwards is disallowed)
     * @param distance the current distance from the starting node
     * @param target the target distance from the starting node
     * @param found the list of valid reachable nodes found
     */
    private void getReachableRecur(Node curr, ArrayList<String> path, int distance, int target, ArrayList<ArrayList<String>> found, Map<String, Node> map) {

        ArrayList<String> newPath = new ArrayList<>(path);
        newPath.add(curr.getID());

        if (distance > target) {
            // Logic error, we would run forever
            throw new IllegalArgumentException("Distance exceeds target");
        }
        if (distance == target) {
            found.add(newPath);
            return;
        }

        int currX = curr.getMapX();
        int currY = curr.getMapY();
        String northID = currX + "," + (currY + 1);
        String eastID = (currX + 1) + "," + currY;
        String southID = currX + "," + (currY - 1);
        String westID = (currX - 1) + "," + currY;
        if (curr.getNorth() && !northID.equals(path.get(path.size() - 1))) {
            getReachableRecur(map.get(northID), newPath, distance + 1, target, found, map);
        }
        if (curr.getEast() && !eastID.equals(path.get(path.size() - 1))) {
            getReachableRecur(map.get(eastID), newPath, distance + 1, target, found, map);
        }
        if (curr.getSouth() && !southID.equals(path.get(path.size() - 1))) {
            getReachableRecur(map.get(southID), newPath, distance + 1, target, found, map);
        }
        if (curr.getWest() && !westID.equals(path.get(path.size() - 1))) {
            getReachableRecur(map.get(westID), newPath, distance + 1, target, found, map);
        }
    }


    /**
     * Nodes are set green when they are reachable for the current player
     */
    public void setGreen() { sprite.setColor(Color.GREEN); }

    /**
     * Nodes are set gray when they were on the previous path taken by the player
     */
    public void setGray() { sprite.setColor(Color.GRAY); }

    /**
     * After movement, all reachable nodes must have their original color restored.
     */
    public void setNoColor() { sprite.setColor(Color.WHITE); }

    public String getID() { return nodeID; }

    /**
     * Getter to allow rendering of the Node's Sprite
     *
     * @return Node's Sprite
     */
    public Sprite getSprite() {
        return sprite;
    }

    // Getters and Setters for Node links
    public void setNorth(boolean e) { north = e; }
    public void setEast(boolean e) { east = e; }
    public void setSouth(boolean e) { south = e; }
    public void setWest(boolean e) { west = e; }
    public boolean getNorth() { return north; }
    public boolean getEast() { return east; }
    public boolean getSouth() { return south; }
    public boolean getWest() { return west; }

    // Getters for position

    // Position scaled to node
    public int getXPos() { return x * separateDist; }
    public int getYPos() { return y * separateDist; }
    public int getMapX() { return x; }
    public int getMapY() { return y; }
}
