package com.mygdx.game.Items;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.GameState;
import com.mygdx.game.Player;

public class Book extends Item{
    /**
     * TODO decide if this should be an useable item (this if book is item)
     * or in-effect right when player gets it (this will go to shop then)
     *
     */
    public Book(Skin skin) {
        super("Book", false, skin);
    }
    public boolean use(Player player, GameState gameState, Stage stage) {
        player.levelUp();
        return true;
    }
    /**
     * necessary for serialization
     */
    private Book(){}
}
