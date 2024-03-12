package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

public class ShopScreen extends GameScreen {
    private Observable<Void> boardEvent = new Observable<Void>();

    private Label title;

    /**
     * Constructor.
     *
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public ShopScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        title = new Label("ShopScreen", skin);
        stage.addActor(title);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.S) {
                    boardEvent.notifyObservers(null);
                }
                else {
                    return false;
                }

                return true;
            }
        });
    }

    public void addBoardListener(Observer<Void> ob) { boardEvent.addObserver(ob); }
}
