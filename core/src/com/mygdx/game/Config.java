package com.mygdx.game;

/**
 * Contains general configuration stuff for the game
 * Singleton Pattern
 */
public class Config {
    // Private constructor to prevent instantiation
    private Config() {}

    private static class ConfigHolder {
        private static final Config instance = new Config();
    }

    // Accessor
    public static Config getInstance() {
        return ConfigHolder.instance;
    }

    private String uiPath = "ui/flat-earth-ui.json"; // Path of UI skin file
    private String playerSavePath = "players.ser";
    private String gameStateSavePath = "gameState";
    private String tilePath = "tileGemini.jpeg";
    private String starTilePath = "starTile.jpg";
    private String penaltyTilePath = "penalty-node1.png";
    private String eventTilePath = "eventNode.png";
    private String playerPath = "player.png";
    private int maxPlayers = 5;

    public String getUiPath() {
        return uiPath;
    }

    public String getPlayerSavePath() {
        return playerSavePath;
    }

    public String getGameStateSavePath() {
        return gameStateSavePath;
    }

    public int getMaxPlayers() {
        return maxPlayers;
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
}
