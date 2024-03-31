package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

/**
 * Class to play game sounds
 * Singleton Pattern
 */
public class SoundSystem {
    private HashMap<String, Sound> soundsMap = new HashMap<>();
    private Music music;
    private Music agileMusic;

    /**
     * Contains the static Singleton.
     */
    private static class SoundSystemHolder {
        private static final SoundSystem instance = new SoundSystem();
    }

    /**
     * Initializer of SoundSystem
     */
    private SoundSystem() {
        // Load music
        music = Gdx.audio.newMusic(Gdx.files.internal(Config.getInstance().getSoundsFolder() + "music.mp3"));
        music.setLooping(true);
        // TODO
        //agileMusic = Gdx.audio.newMusic(Gdx.files.internal(Config.getInstance().getSoundsFolder() + "agileMusic.mp3"));
        //agileMusic.setLooping(true);
    }

    /**
     * Accessor for the static Singleton.
     *
     * @return the SoundSystem object
     */
    public static SoundSystem getInstance() {
        return SoundSystem.SoundSystemHolder.instance;
    }

    /**
     * Load sound while automatically prepending the sounds folder
     *
     * @param name The sound file name
     */
    private void loadSound(String name) {
        soundsMap.put(name, Gdx.audio.newSound(Gdx.files.internal(Config.getInstance().getSoundsFolder() + name)));
    }

    /**
     * Reset the playback position and play the music
     */
    public void playMusic() {
        music.setPosition(0);
        music.play();
    }

    /**
     * Stop the music
     */
    public void stopMusic() {
        music.stop();
    }

    public void playAgilityMusic() {
        // TODO
        //agileMusic.play();
    }

    public void stopAgilityMusic() {
        // TODO
        //agileMusic.stop();
    }

    /**
     * Play a specific sound file. Will automatically attempt to load the file if needed.
     *
     * @param name name of the sound file
     */
    public void playSound(String name) {
        if (soundsMap.get(name) == null) {
            // Load the sound
            loadSound(name);
        }
        soundsMap.get(name).play();
    }

    /**
     * Dispose of loaded sound files when no longer needed
     */
    public void dispose() {
        music.dispose();
        for (Sound sound : soundsMap.values()) {
            sound.dispose();
        }
    }
}