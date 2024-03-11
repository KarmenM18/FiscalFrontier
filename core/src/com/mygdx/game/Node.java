package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Node {
    protected int nodeID;
    protected int x;
    protected int y;
    protected boolean hasStar;
    protected int baseMoney;
    protected int penaltyAmount;
    protected int starCost;

    protected Node[] nodes;
    SpriteBatch batch;

    protected Node north = null;
    protected Node east = null;
    protected Node west = null;
    protected Node south = null;

    public Node(int id, int x, int y, boolean star){
        nodeID = id;
        this.x = x;
        this.y = y;
        hasStar = star;
    }
    public abstract void activate(Player p);

    //Pathing, not implement for now
    //public Node[] getPath(Node b){
      //  return nodes;
    //}
}
