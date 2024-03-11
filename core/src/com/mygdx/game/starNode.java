package com.mygdx.game;

public class starNode extends Node {
    int starCost = 10;

    public starNode(int id, int x, int y, boolean star) {
        super(id, x, y, star);
    }

    @Override
    public void activate(Player p) {
        if(p.money < starCost){
            //logic for showing player can't buy star
        }else{
            //logic for checking if player wants to buy star
        }
    }

}
