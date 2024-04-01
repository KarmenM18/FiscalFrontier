package com.mygdx.game;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.ActionTextSystem;
import com.mygdx.game.Config;
import com.mygdx.game.TestGame;
import com.ray3k.stripe.FreeTypeSkinLoader;
import org.mockito.Mockito;

/**
 * Create a version of the game suitable for unit testing. Call in BeforeAll of a unit test.
 */
public class GameContext {
    static private AssetManager asset;

    /**
     * Create necessary context to run tests
     */
    public GameContext() {
        HeadlessApplicationConfiguration GDXConfig = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new TestGame(), GDXConfig);
        Gdx.gl = Mockito.mock(GL20.class); // Mock gl to allow headless texture loading

        Config config = Config.getInstance();

        asset = new AssetManager();

        // Support TTF fonts
        asset.setLoader(Skin.class, new FreeTypeSkinLoader(asset.getFileHandleResolver()));

        // Load assets
        asset.load(config.getUiPath(), Skin.class);
        asset.load(config.getTilePath(), Texture.class);
        asset.load(config.getStarTilePath(), Texture.class);
        asset.load(config.getEventTilePath(), Texture.class);
        asset.load(config.getPenaltyTilePath(), Texture.class);
        asset.load(config.getPlayerPath(), Texture.class);
        asset.load(config.getPlayerFreezePath(), Texture.class);
        asset.load(config.getBackgroundPath(), Texture.class);
        asset.load(config.getPlayerShieldPath(), Texture.class);
        asset.load(config.getMapArrowPath(), Texture.class);
        asset.load(config.getAgilityTilePath(), Texture.class);
        asset.load("background.jpeg", Texture.class);
        asset.finishLoading();

        // Setup ActionTextSystem
        ActionTextSystem.initSkin(asset.get(config.getUiPath(), Skin.class));
    }

    /**
     * @return the generated AssetManager
     */
    public AssetManager getAssetManager() { return asset; }
}
