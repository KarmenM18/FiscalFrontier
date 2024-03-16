package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.util.LinkedList;

public class KnowledgeListScreen extends GameScreen{
    private Observable<Void> pauseScreenEvent = new Observable<>();

    private Label title;
    private Table background;
    private Table table;
    private ScrollPane scroller;
    private LinkedList<String> playerKnowledge = new LinkedList<>();

    public KnowledgeListScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        title = new Label("learned Knowledge", skin);
        this.stage.addActor(title);

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
                else {
                    return false;
                }

                return true;
            }
        });
    }

    public void setPlayerKnowledge (LinkedList<String> knowledge) {this.playerKnowledge = knowledge;}
    public void addBackToPause(Observer<Void> ob) {pauseScreenEvent.addObserver(ob);}

    //Updates the screen to show just the player's learned knowledge base
    public void updateTable () {

        table = new Table();
        for (int i = 0; i < playerKnowledge.size(); i++) {
            String s = playerKnowledge.get(i);
            Label text = new Label((i+1) +": "+ s, skin);
            text.setAlignment(Align.top);
            text.setWrap(true);
            table.add(text).fillX().expandX();
            table.row();
        }

        scroller = new ScrollPane(table);

        background = new Table();
        background.setFillParent(true);
        background.add(scroller).fill().expand();

        this.stage.addActor(background);
    }
}
