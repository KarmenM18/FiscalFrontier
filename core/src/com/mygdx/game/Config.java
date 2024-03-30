package com.mygdx.game;

/**
 * Contains general configuration stuff for the game
 * Singleton Pattern
 */
public class Config {

    private int maxPlayers = 5;
    private int minPlayers = 2;
    private int maxRounds = 26; // The highest reachable round before the game ends
    private int maxLevel = 13;
    private String uiPath = "ui/clean-crispy/skin/clean-crispy-ui.json"; // Path of UI skin file
    private String tilePath = "kenny_block_pack/PNG/Double (128px)/tileGrass.png";
    private String starTilePath = "star.png";
    private String penaltyTilePath = "penalty-node1.png";
    private String eventTilePath = "eventNode.png";
    private String playerPath = "player-alt.png";
    private String saveFolder = "saves";
    private String debugPassword = "noclip";
    private String soundsFolder = "sounds/";
    private String backgroundPath = "background.jpg";
    private String playerFreezePath = "kenny_block_pack/PNG/Double (128px)/detail_snow.png";
    private String playerShieldPath = "shield.png";
    private String mapArrowPath = "arrow.png";

    public int getMaxLevel() {
        return maxLevel;
    }

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

    public String getDebugPassword() { return debugPassword; }

    public String getSoundsFolder() {
        return soundsFolder;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public String getPlayerFreezePath() {
        return playerFreezePath;
    }

    public String getPlayerShieldPath() {
        return playerShieldPath;
    }

    public String getMapArrowPath() {
        return mapArrowPath;
    }

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

    // Private constructor to prevent instantiation
    private Config() {}
}
