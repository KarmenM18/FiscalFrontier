package com.mygdx.game.Items;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.GameState;
import com.mygdx.game.Player;

public class Shield extends Item{
    public Shield(Skin skin){
        super("Shield", false, skin);
    }
    public boolean use(Player player, GameState gameState, Stage stage) {
        player.setHasShield(true);
        return true;
    }
}
