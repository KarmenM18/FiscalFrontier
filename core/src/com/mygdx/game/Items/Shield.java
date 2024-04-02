package com.mygdx.game.Items;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.ActionTextSystem;
import com.mygdx.game.GameState;
import com.mygdx.game.Player;
import com.mygdx.game.SoundSystem;

public class Shield extends Item{
    public Shield(Skin skin){
        super("Shield", false, skin);
        this.price = 150;
        this.description = "Immunity: protects player from next penalty.";
    }

    public boolean use(Player player, GameState gameState, Stage stage) {
        if (player.getHasShield()) {
            SoundSystem.getInstance().playSound("error.wav");
            ActionTextSystem.addText("Shield already active", player.getSprite().getX(), player.getSprite().getY() + 25, 0.5f);
            return false;
        }
        player.setHasShield(true);
        return true;
    }
    /**
     * necessary for serialization
     */
    private Shield(){}
}
