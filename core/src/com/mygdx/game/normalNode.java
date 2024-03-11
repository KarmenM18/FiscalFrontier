package com.mygdx.game;

public class normalNode extends Node{
    int baseMoney = 5;

    public normalNode(int id, int x, int y, boolean star) {
        super(id, x, y, star);
    }

    @Override
    public void activate(Player p) {
        //maybe add logic for showing the node graphic??
        p.money += baseMoney;
    }
}
