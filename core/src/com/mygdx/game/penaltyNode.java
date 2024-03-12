package com.mygdx.game;

public class penaltyNode extends Node{
    int penaltyAmount = 0;
    public penaltyNode(int id, int x, int y, boolean star) {
        super(id, x, y, star);
    }

    @Override
    public void activate(Player p) {
        if(p.money >= penaltyAmount){
            //maybe add logic on the penalty graphic
            p.money -= penaltyAmount;
        }else{
            //maybe add logic for showing penalty not applied due to not enough mooney
            //return
        }
    }
}
