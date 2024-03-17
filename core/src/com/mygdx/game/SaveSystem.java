package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveSystem {
    Json json = new Json();

    public SaveSystem() {
        // Textures don't support serialization. We must not write textures, and then reconstruct them after loading
        json.setSerializer(Texture.class, new Json.Serializer<Texture>() {
            @Override
            public void write(Json json, Texture object, Class knownType) {
                json.writeValue(null);
            }
            @Override
            public Texture read(Json json, JsonValue jsonValue, Class aClass) {
                return null;
            }
        });
    }

    public void saveGameState(GameState gs) {
        try {
            Config config = Config.getInstance();
            String JSONed = json.prettyPrint(gs);

            // Keep increasing the save number until we find an empty slot
            int saveNumber = 1;
            while (Utility.fileExists(config.getGameStateSavePath() + "_" + saveNumber + ".json")) saveNumber++;
            Files.writeString(Paths.get(config.getGameStateSavePath() + "_" + saveNumber + ".json"), JSONed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GameState readGameState(String savePath, AssetManager assets) {
        try {
            Config config = Config.getInstance();
            String gsString = Files.readString(Path.of(savePath));
            GameState gs = json.fromJson(GameState.class, gsString);
            gs.loadTextures(assets);
            return gs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
