package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.game.Observer.Observable;
import com.mygdx.game.Observer.Observer;

public class TutorialScreen extends GameScreen{
    private Observable<Void> mainmenu = new Observable<>();

    //UI for TutorialScreen

    /**
     * @param batch  SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public TutorialScreen(SpriteBatch batch, AssetManager assets) {
        super(batch, assets);

        //Adding listener to go back to menu screen
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.ESCAPE) mainmenu.notifyObservers(null);
                else return false;
                return true;
            }
        });

    }

    public void addBackToMenu(Observer<Void> ob) {mainmenu.addObserver(ob);}
}
