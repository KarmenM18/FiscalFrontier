package com.mygdx.game.Node;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.*;
import com.mygdx.game.Observer.Observable;

import java.util.Map;


public class StarNode extends Node {
    int starCost;
    public boolean hasStar = true;
    private Observable<Void> starEvent = new Observable<Void>();
    private Dialog buyStarDialog;
    private Dialog starDialog;

    transient protected Sprite starSprite; // Visible if there is a star on the node


    public StarNode(int mapX, int mapY, boolean north, boolean east, boolean south, boolean west, Map<String, Node> map, AssetManager assets) {
        super(mapX, mapY, north, east, south, west, assets);
        loadTextures(assets);
    }

    public StarNode(int mapX, int mapY, AssetManager assets) {
        super(mapX, mapY, assets);
        loadTextures(assets);
    }
    /**
     * necessary for serialization
     */
    private StarNode() {}

    @Override
    public void loadTextures(AssetManager assets) {
        super.loadTextures(assets);

        starSprite = new Sprite((Texture) assets.get(Config.getInstance().getStarTilePath()));
        starSprite.setTexture(assets.get(Config.getInstance().getStarTilePath()));
        starSprite.setPosition(sprite.getX() + 12.5f, sprite.getY() + 12.5f);
        starSprite.setSize(75, 75);

        checkStar();
    }

    /**
     * activate the star purchase dialog,
     *
     * @param player the Player who landed on the Node
     * @param batch the SpriteBatch to draw on
     * @param stage the GameBoard's Stage
     * @param skin the GameBoard's Skin
     * @param board the GameBoard executing the function
     * @param hardmode whether the gameState is in hard mode
     * @return true if the Node will handle changing the turn, false otherwise
     */
    public boolean activate(Player player, SpriteBatch batch, Stage stage, Skin skin, GameBoard board, boolean hardmode) {
        if(!hardmode){
            starCost = 100;
        }else {
            starCost = 250;
        }
        if(hasStar){
            if(player.getMoney() >= starCost){
                buyStarDialog = new Dialog("Buying Star", skin) {
                    @Override
                    protected void result(Object object) {
                        if ((Boolean) object) {
                            starMod(player);
                        }

                        // Inform GameState to change the turn
                        board.turnChange();
                    }
                };
                //TODO add Y/N keyboard shortcut for purchase confirmation
                buyStarDialog.text("Do you want to buy the star?");
                buyStarDialog.button("Yes", true);
                buyStarDialog.button("No", false);
                buyStarDialog.show(stage);
                //TODO dialog for denied purchase
            }else{
                starDialog = new Dialog("Can't buy Star", skin) {
                    @Override
                    protected void result(Object object) {
                        if ((Boolean) object) {
                            // Inform GameState to change the turn
                            board.turnChange();
                        }
                    }
                };
                starDialog.text("Sorry you don't have enough money to buy a star");
                starDialog.button("Ok", true);
                starDialog.show(stage);
                //TODO dialog for purchasing
            }
        }

        return true;
    }

    /**
     * Draw the star on top of the sprite.
     *
     * @param batch the Batch to draw with
     */
    @Override
    public void draw(Batch batch) {
        sprite.draw(batch);
        starSprite.draw(batch);
    }

    /**
     * Perform the purchasing of a star by a player
     *
     * @param player Player who is buying
     */
    protected void starMod(Player player){
            this.hasStar = false;
            player.setStars( player.getStars() + 1 );
            player.setMoney(player.getMoney() - starCost);
            checkStar();

            // Feedback
            SoundSystem.getInstance().playSound("gainedStar.mp3", 0.35f);
            ActionTextSystem.addText("+1 Star", player.getSprite().getX(), player.getSprite().getY() + 50, 0.5f);
    }
    /**
     * Hide star if it was grabbed.
     */
    public void checkStar() {
        if (hasStar) {
            starSprite.setAlpha(1.0f);
        }
        else {
            starSprite.setAlpha(0.0f);
        }
    }
}
