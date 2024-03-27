package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen extends ScreenAdapter {
    protected AssetManager assets;
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
        //this.skin.getFont("font").getData().setScale(1.7f); // Scale font size
        stage = new Stage(new FitViewport(1920, 1080), batch);
    }

    /**
     * Runs immediately when the active screen is changed to this.
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
     * Called when the screen is resized
     *
     *
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
