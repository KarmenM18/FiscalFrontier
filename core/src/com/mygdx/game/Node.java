/**
 * The Node class. A node is a clickable tile on the board.
 * It is linked to other nodes in four possible directions.
 */
package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.Serializable;
import java.util.*;

public class Node implements Serializable {
    protected String nodeID;
    protected Sprite sprite; // Sprite to render node on the board
    protected Texture tileTexture;
    protected String north = null;
    protected String east = null;
    protected String south = null;
    protected String west = null;
    protected int x, y;

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
    }

    protected Node() {}

    public void loadTextures(AssetManager assets) {
        Config config = Config.getInstance();
        tileTexture = assets.get(config.getTilePath());
        sprite.setTexture(tileTexture);
    }

    /**
     * Runs when the Node is landed on.
     */
    public void activate(Player player) {
    }

    /**
     * Get the path from this Node to Node {@code b}.
     *
     * @param b the target node
     * @return the list of nodes between this node and {@code b}, or {@code null}
     */
    public List<Node> getPath(Node b) {
        // TODO DFS ALGORITHM
        return null;
    }

    /**
     * Get all Nodes reachable from this Node, exactly {@code distance} nodes away from it.
     *
     * @param distance EXACT distance from this node to match
     * @return the list of reachable nodes
     */
    public ArrayList<String> getReachable(int distance, Map<String, Node> nodeMap) {
        ArrayList<String> foundNodes = new ArrayList<>();
        getReachableRecur(this, null, 0, distance, foundNodes, nodeMap);

        return foundNodes;
    }

    /**
     * Recursive helper function for getReachable. Depth First Search.
     *
     * @param curr the current node
     * @param prev the previous node (going backwards is disallowed)
     * @param distance the current distance from the starting node
     * @param target the target distance from the starting node
     * @param found the list of valid reachable nodes found
     */
    private void getReachableRecur(Node curr, String prev, int distance, int target, List<String> found, Map<String, Node> map) {
        if (distance > target) {
            // Logic error, we would run forever
            throw new IllegalArgumentException("Distance exceeds target");
        }
        if (distance == target) {
            found.add(curr.getID());
            return;
        }

        // Try all directions except null and prev
        if (curr.north != null && !curr.north.equals(prev)) {
            getReachableRecur(map.get(curr.north), curr.getID(), distance + 1, target, found, map);
        }
        if (curr.east != null && !curr.east.equals(prev)) {
            getReachableRecur(map.get(curr.east), curr.getID(), distance + 1, target, found, map);
        }
        if (curr.south != null && !curr.south.equals(prev)) {
            getReachableRecur(map.get(curr.south), curr.getID(), distance + 1, target, found, map);
        }
        if (curr.west != null && !curr.west.equals(prev)) {
            getReachableRecur(map.get(curr.west), curr.getID(), distance + 1, target, found, map);
        }
    }


    /**
     * Nodes are set green when they are reachable for the current player
     */
    public void setGreen() { sprite.setColor(Color.GREEN); }

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

    public void setNorth(String n) { north = n; }
    public void setEast(String n) { east = n; }
    public void setSouth(String n) { south = n; }
    public void setWest(String n) { west = n; }

    public int getXPos() { return x; }
    public int getYPos() { return y; }
}
