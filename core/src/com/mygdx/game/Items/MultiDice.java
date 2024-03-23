package com.mygdx.game.Items;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.GameState;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Player;

public class MultiDice extends Item {
    public MultiDice(Skin skin){
        super("MultiDice", false, skin);
    }
    public boolean use(Player player, GameState gameState, Stage stage) {
        player.setUseMutliDice(true);
        return true;
    }
    /**
     * necessary for serialization
     */
    private MultiDice(){}
}
