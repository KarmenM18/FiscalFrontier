package com.mygdx.game.Node;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Config;
import com.mygdx.game.GameBoard;
import com.mygdx.game.Player;

import java.util.Map;

/**
 * On landing, the Player has a choice to start an Agility Test game.
 */
public class AgilityTestNode extends Node {
    Sprite symbol;

    public AgilityTestNode(int mapX, int mapY, boolean north, boolean east, boolean south, boolean west, Map<String, Node> map, AssetManager assets) {
        super(mapX, mapY, north, east, south, west, assets);
        loadTextures(assets);
    }


    public AgilityTestNode(int mapX, int mapY, AssetManager assets) {
        super(mapX, mapY, assets);
        loadTextures(assets);
    }

    /**
     * necessary for serialization
     */
    private AgilityTestNode() {}

    @Override
    public void loadTextures(AssetManager assets) {
        super.loadTextures(assets);

        symbol = new Sprite((Texture) assets.get(Config.getInstance().getAgilityTilePath()));
        symbol.setTexture(assets.get(Config.getInstance().getAgilityTilePath()));
        symbol.setPosition(sprite.getX() + 12.5f, sprite.getY() + 12.5f);
        symbol.setSize(75, 75);
    }

    /**
     * Prompt the Player to start a game, if they have enough money
     *
     * @param player the Player who landed on the Node
     * @param batch the SpriteBatch to draw on
     * @param stage the GameBoard's Stage
     * @param skin the GameBoard's Skin
     * @param board the GameBoard executing the function
     * @param hardmode whether the gameState is in hard mode
     * @return true if the Node will handle changing the turn, false otherwise
     */
    @Override
    public boolean activate(Player player, SpriteBatch batch, Stage stage, Skin skin, GameBoard board, boolean hardmode) {
        if(player.getMoney() >= 100) {
            Dialog playDialog = new Dialog("Play Game", skin) {
                @Override
                protected void result(Object object) {
                    if ((Boolean) object) {
                        player.setMoney(player.getMoney() - 100);
                        board.agilityTestEvent.notifyObservers(null);
                    }
                    else {
                        board.turnChange();
                    }
                }
            };
            playDialog.text("Do you want to play the Financial Agility Game ($100)?");
            playDialog.button("Skip", false);
            playDialog.button("Play", true);
            playDialog.show(stage);
        }
        else {
            Dialog cannotAfford = new Dialog("Can't Play Game", skin) {
                @Override
                protected void result(Object object) {
                    if ((Boolean) object) {
                        // Inform GameState to change the turn
                        board.turnChange();
                    }
                }
            };
            cannotAfford.text("You can't afford to play the game ($100).");
            cannotAfford.button("Ok", true);
            cannotAfford.show(stage);
        }

        return true;
    }

    /**
     * Draw the symbol on top of the sprite.
     *
     * @param batch the Batch to draw with
     */
    @Override
    public void draw(Batch batch) {
        sprite.draw(batch);
        symbol.draw(batch);
    }
}
