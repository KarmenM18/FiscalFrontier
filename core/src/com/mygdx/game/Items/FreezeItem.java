package com.mygdx.game.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.GameState;
import com.mygdx.game.Player;
import com.mygdx.game.SoundSystem;

import java.util.ArrayList;

/**
 * This Item allows a Player to freeze another Player for a turn
 * @author Franck Limtung (flimtung)
 */
public class FreezeItem extends Item {
    transient GameState gameState; // Transient, only set when use() is activated and used in the dialog

    public FreezeItem(Skin skin) {
        super("FreezeItem", false, skin);
        this.price = 100;
        this.description = "Used to freeze a player for one turn.";
    }

    @Override
    public boolean use(Player player, GameState gameState, Stage stage) {
        this.gameState = gameState;

        for (int i = 0; i < gameState.getPlayerList().size(); i++) {
            Player otherPlayer = gameState.getPlayerList().get(i);
            if (otherPlayer == player) continue; // Skip self
            usedItemDialog.button(otherPlayer.getPlayerProfile().getName(), Integer.valueOf(i)); // Will call result of usedItemDialog with the player index
        }

        usedItemDialog.show(stage);

        return true;
    }

    @Override
    public void loadTextures(Skin skin) {
        usedItemDialog = new Dialog("Choice", skin) {
            @Override
            protected void result(Object object) {
                gameState.getPlayerList().get((int)object).setFrozen(true);
            }
        };
        usedItemDialog.text("Which player do you want to Freeze?");
    }


    private FreezeItem() {}
}
