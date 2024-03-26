package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveSystem {
    Json json = new Json();

    public SaveSystem() {
        // Textures don't support serialization. We skip textures and reconstruct them after loading
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

    /**
     * Serialize a GameState object to a file.
     * We cannot properly serialize certain LibGDX classes, so those must be reconstructed when deserializing.
     *
     * @param gs the GameState object to serialize
     * @param path the path to save the file. If a file with the same name exists, it will increment an ID for the save file
     */
    public void saveGameState(GameState gs, String path) {
        try {
            Config config = Config.getInstance();
            String JSONed = json.prettyPrint(gs);

            // Keep increasing the save number until we find an empty slot
            int saveNumber = 1;
            ClassLoader CL = getClass().getClassLoader();
            File saveFolder = new File(CL.getResource("saves").getFile());
            while (Utility.fileExists(saveFolder+File.separator + path + "_" + saveNumber + ".json")) saveNumber++;
            Files.writeString(Paths.get(saveFolder+File.separator + path + "_" + saveNumber + ".json"), JSONed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserialize a GameState object from a JSON file.
     *
     * @param savePath the path to the JSON file
     * @param assets an AssetManager loaded with all the assets required by the GameState and its objects
     * @return a GameState object
     */
    public GameState readGameState(String savePath, AssetManager assets) {
        try {
            Config config = Config.getInstance();
            ClassLoader CL = getClass().getClassLoader();
            File saveFolder = new File(CL.getResource("saves").getFile());
            String gsString = Files.readString(Path.of(saveFolder+File.separator + savePath));
            GameState gs = json.fromJson(GameState.class, gsString);
            gs.loadTextures(assets);
            return gs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
