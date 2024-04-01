package com.mygdx.game.Items;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.GameState;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Player;

public class MultiDice extends Item {
    public MultiDice(Skin skin){
        super("MultiDice", false, skin);
        this.price = 50;
        this.description = "Doubles the amount of dice, increasing the maximum roll.";
    }
    public boolean use(Player player, GameState gameState, Stage stage) {
        player.setUseMultiDice(true);
        return true;
    }
    /**
     * necessary for serialization
     */
    private MultiDice(){}
}
