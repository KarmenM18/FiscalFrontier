package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

/**
 * Responsible for playing sounds in the game. Implements the singleton pattern.
 *
 * @author Franck Limtung (flimtung)
 */
public class SoundSystem {

    /** All game sounds. */
    private HashMap<String, Sound> soundsMap = new HashMap<>();
    /** Game's background music. */
    private Music music;

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
        music = Gdx.audio.newMusic(Gdx.files.internal(Config.getInstance().getSoundsFolder() + "music2.mp3"));
        music.setLooping(true);
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
        music.setVolume(0.5f);
        music.play();
    }

    /**
     * Stop the music
     */
    public void stopMusic() {
        music.stop();
    }

    /**
     * Play a specific sound file. Will automatically attempt to load the file if needed.
     *
     * @param name name of the sound file
     */
    public void playSound(String name) {
        playSound(name,1.0f);
    }

    /**
     * more refined playSound with volume
     * @param name
     * @param vol
     */
    public void playSound(String name, float vol) {
        if (soundsMap.get(name) == null) {
            // Load the sound
            loadSound(name);
        }
        soundsMap.get(name).play(vol);
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