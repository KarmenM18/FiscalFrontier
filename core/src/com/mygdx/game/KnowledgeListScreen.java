package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

import java.util.LinkedList;

public class KnowledgeListScreen extends GameScreen{
    private Observable<Void> pauseScreenEvent = new Observable<>();

    private Label title;
    private LinkedList<String> playerKnowledge = new LinkedList<>();

    public KnowledgeListScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        title = new Label("Current Unlocked Knowledge", skin);
        stage.addActor(title);

        LinkedList<Label> texts = new LinkedList<>();
        for (int i = 0; i < playerKnowledge.size(); i++) {
            texts.add(new Label(playerKnowledge.get(i),skin));
        }

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
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
}
