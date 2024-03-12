package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen extends ScreenAdapter {
    protected AssetManager assets;
    protected Config config;
    protected Skin skin;
    protected Stage stage;

    /**
     * Constructor.
     *
     * @param batch SpriteBatch to initialize the Stage with
     * @param assets AssetManager to load assets with
     */
    public GameScreen(SpriteBatch batch, AssetManager assets) {
        Config config = Config.getInstance();
        this.assets = assets;
        this.skin = assets.get(config.getUiPath(), Skin.class);
        stage = new Stage(new ScreenViewport(), batch);
    }

    /**
     * Runs immediately when screen is changed to this.
     * By default, it will just set LibGDX's InputProcessor to this screen's Stage.
     * It should be overridden by child classes if they need to perform additional functions.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Runs every frame.
     * It should be overridden by child classes if they need to perform additional functions.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    /**
     * Called when the screen needs to dispose resources.
     * Must be overridden by child classes if they create other LibGDX objects.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
