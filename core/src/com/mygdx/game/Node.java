/**
 * The Node class. A node is a clickable tile on the board.
 * It is linked to other nodes in four possible directions.
 */
package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.Serializable;
import java.util.*;

public abstract class Node implements Serializable {
    protected String nodeID; // ID of the node in the Node Map
    protected Sprite sprite; // Sprite to render node on the board
    protected Texture tileTexture; // Texture of the board tile
    protected int x, y;

    // IDs of adjacent nodes
    protected String north = null;
    protected String east = null;
    protected String south = null;
    protected String west = null;

    /**
     * Constructor for Node
     */
    public Node(String id, int x, int y, String north, String east, String south, String west, Map<String, Node> map, AssetManager assets) {
        this(id, x, y, assets);

        // Set linkage between nodes
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
        if (north != null) { map.get(north).setSouth(id); }
        if (east != null) { map.get(east).setWest(id); }
        if (south != null) { map.get(south).setNorth(id); }
        if (west != null) { map.get(west).setEast(id); }
    }

    /**
     * Simpler constructor for Node
     */
    public Node(String id, int x, int y, AssetManager assets) {
        this.nodeID = id;
        this.x = x;
        this.y = y;

        // Setup sprite
        Config config = Config.getInstance();
        tileTexture = assets.get(config.getTilePath());
        sprite = new Sprite(tileTexture);
        sprite.setSize(50, 50);
        sprite.setPosition(x, y);
        loadTextures(assets);
    }

    /**
     * No-arg constructor for serialization
     */
    protected Node() {}

    public void loadTextures(AssetManager assets) {
        Config config = Config.getInstance();
        tileTexture = assets.get(config.getTilePath());
        sprite.setTexture(tileTexture);
    }

    /**
     * Runs when the Node is landed on.
     */
    public void activate(Player player, SpriteBatch batch) {
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

        // Try all directions except null and the previous node
        ArrayList<String> path = new ArrayList<>(List.of(nodeID));
        if (north != null && !north.equals(prevNodeID)) {
            getReachableRecur(nodeMap.get(north), path, 1, distance, foundNodes, nodeMap);
        }
        if (east != null && !east.equals(prevNodeID)) {
            getReachableRecur(nodeMap.get(east), path, 1, distance, foundNodes, nodeMap);
        }
        if (south != null && !south.equals(prevNodeID)) {
            getReachableRecur(nodeMap.get(south), path, 1, distance, foundNodes, nodeMap);
        }
        if (west != null && !west.equals(prevNodeID)) {
            getReachableRecur(nodeMap.get(west), path, 1, distance, foundNodes, nodeMap);
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

        // Try all directions except null and the previous node
        if (curr.north != null && !curr.north.equals(path.getLast())) {
            getReachableRecur(map.get(curr.north), newPath, distance + 1, target, found, map);
        }
        if (curr.east != null && !curr.east.equals(path.getLast())) {
            getReachableRecur(map.get(curr.east), newPath, distance + 1, target, found, map);
        }
        if (curr.south != null && !curr.south.equals(path.getLast())) {
            getReachableRecur(map.get(curr.south), newPath, distance + 1, target, found, map);
        }
        if (curr.west != null && !curr.west.equals(path.getLast())) {
            getReachableRecur(map.get(curr.west), newPath, distance + 1, target, found, map);
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

    // Setters for Node links
    public void setNorth(String n) { north = n; }
    public void setEast(String n) { east = n; }
    public void setSouth(String n) { south = n; }
    public void setWest(String n) { west = n; }

    // Getters for position
    public int getXPos() { return x; }
    public int getYPos() { return y; }
}
