package com.mygdx.game;

/**
 * Contains general configuration stuff for the game
 * Singleton Pattern
 */
public class Config {
    // Private constructor to prevent instantiation
    private Config() {}

    /**
     * Contains the static Singleton.
     */
    private static class ConfigHolder {
        private static final Config instance = new Config();
    }

    /**
     * Accessor for the static Singleton.
     *
     * @return the Config object
     */
    public static Config getInstance() {
        return ConfigHolder.instance;
    }

    //private String uiPath = "ui/flat-earth-ui.json"; // Path of UI skin file
    private String uiPath = "ui/clean-crispy/skin/clean-crispy-ui.json"; // Path of UI skin file
    //private String uiPath = "ui/8bit/uiskin.json"; // Path of UI skin file
    private String tilePath = "tileGemini.jpeg";
    private String starTilePath = "starTile.jpg";
    private String penaltyTilePath = "penalty-node1.png";
    private String eventTilePath = "eventNode.png";
    private String playerPath = "player-alt.png";
    private String saveFolder = "saves";
    private int maxPlayers = 5;
    private int minPlayers = 2;
    private int maxRounds = 26; // The highest reachable round before the game ends

    public String getUiPath() {
        return uiPath;
    }

    public String getTilePath() {
        return tilePath;
    }

    public String getStarTilePath() {
        return starTilePath;
    }

    public String getPenaltyTilePath() {
        return penaltyTilePath;
    }
    public String getEventTilePath(){
        return eventTilePath;
    }

    public String getPlayerPath() {
        return playerPath;
    }

    public int getMaxRounds() { return maxRounds; }

    public int getMaxPlayers() { return maxPlayers; }
    public int getMinPlayers() {
        return minPlayers;
    }

    public String getSaveFolder() {
        return saveFolder;
    }
}
