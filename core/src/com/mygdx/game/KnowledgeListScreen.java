package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;
import java.util.LinkedList;

/**
 * Shows all knowledge that the current player has learned based on player's current knowledge level
 * @author ecatil3
 * @version 1.0
 */
public class KnowledgeListScreen extends GameScreen{
    private Observable<Void> pauseScreenEvent = new Observable<>();

    private Table background;
    private Table table;
    private ScrollPane scroller;
    private LinkedList<String> playerKnowledge = new LinkedList<>();

    /**
     * Knoledge Screen Constructor / initialization
     * @param batch SpriteBatch from libgdx
     * @param assets AssetManager from libgdx
     */
    public KnowledgeListScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        //Listening for return to pause screen
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    //Removing tables to "clear" the screen
                    table.remove();
                    scroller.remove();
                    background.remove();
                    pauseScreenEvent.notifyObservers(null); // Moving back to pause screen
                }
                else {return false;}
                return true;
            }
        });
    }

    /**
     * Updates the list of the current play's obtained knowledge
     * @param knowledge LinkList holding all player's current knowledge list
     */
    public void setPlayerKnowledge (LinkedList<String> knowledge) {this.playerKnowledge = knowledge;}

    /**
     * @param ob Observer listener to get back to pause screen
     */
    public void addBackToPause(Observer<Void> ob) {pauseScreenEvent.addObserver(ob);}

    /**
     * Updates the screen to show just the player's learned knowledge
     */
    public void updateTable () {

        table = new Table();

        for (int i = 0; i < playerKnowledge.size(); i++) {
            String s = playerKnowledge.get(i);
            Label text = new Label((i+1) +": "+ s, skin);
            text.setAlignment(Align.left);
            text.setWrap(true);
            table.add(text).left().fillX().expandX();
            table.row();
        }

        scroller = new ScrollPane(table, skin);

        background = new Table();
        background.setFillParent(true);
        background.add(scroller).fill().expand();

        this.stage.addActor(background);
    }
}