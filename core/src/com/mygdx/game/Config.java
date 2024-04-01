package com.mygdx.game;

/**
 * Contains general configuration information for the game. Implements the singleton pattern.
 *
 * @author Franck Limtung (flimtung)
 * @author Kevin Chen (kchen546)
 */
public class Config {

    /* Student and game information */

    /** Maximum allowed players in a game. */
    private int maxPlayers = 5;
    /** Minimum required players in a game. */
    private int minPlayers = 2;
    /** The highest reachable round before the game ends. */
    private int maxRounds = 26;
    /** Maximum attainable student level. */
    private int maxLevel = 13;

    /* UI and sprite filepaths */

    /** Filepath to background image. */
    private String backgroundPath = "background.jpg";
    /** Filepath to UI elements. */
    private String uiPath = "ui/clean-crispy/skin/clean-crispy-ui.json"; // Path of UI skin file
    /** Filepath to gameboard general tile sprite. */
    private String tilePath = "kenny_block_pack/PNG/Double (128px)/tileGrass.png";
    /** Filepath to gameboard star tile sprite. */
    private String starTilePath = "star.png";
    /** Filepath to gameboard penalty tile sprite. */
    private String penaltyTilePath = "penalty-node1.png";
    /** Filepath to gameboard event tile sprite. */
    private String eventTilePath = "eventNode.png";
    /** Filepath to gameboard agility minigame tile sprite. */
    private String agilityTilePath = "math.png";
    /** Filepath to map arrow sprite. */
    private String mapArrowPath = "arrow.png";
    /** Filepath to player sprite. */
    private String playerPath = "player-alt.png";
    /** Filepath to player frozen sprite. */
    private String playerFreezePath = "kenny_block_pack/PNG/Double (128px)/detail_snow.png";
    /** Filepath to shield sprite. */
    private String playerShieldPath = "shield.png";

    /* Filepath to folders */

    /** Folder containing game save files. */
    private String saveFolder = "saves";
    /** Folder containing game music and sound files. */
    private String soundsFolder = "sounds/";

    /* Passwords */

    /** Instructor dashboard password. */
    private String instructorPassword = "CS2212";
    /** Debug mode password. */
    private String debugPassword = "noclip";

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

    /**
     * Private constructor to prevent instantiation
     */
    private Config() {}


    /**
     * Returns the maximum attainable student level.
     * @return Maximum attainable student level.
     */
    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * Returns the filepath to UI elements
     * @return Filepath to UI elements.
     */
    public String getUiPath() {
        return uiPath;
    }

    /**
     * Returns the filepath to background image.
     * @return Filepath to background image.
     */
    public String getBackgroundPath() {
        return backgroundPath;
    }

    /**
     * Returns the filepath to gameboard general tile sprite.
     * @return Filepath to gameboard general tile sprite.
     */
    public String getTilePath() {
        return tilePath;
    }

    /**
     * Returns the filepath to gameboard star tile sprite.
     * @return Filepath to gameboard star tile sprite.
     */
    public String getStarTilePath() {
        return starTilePath;
    }

    /**
     * Returns the filepath to gameboard penalty tile sprite.
     * @return Filepath to gameboard penalty tile sprite.
     */
    public String getPenaltyTilePath() {
        return penaltyTilePath;
    }

    /**
     * Returns the filepath to gameboard event tile sprite.
     * @return Filepath to gameboard event tile sprite.
     */
    public String getEventTilePath(){
        return eventTilePath;
    }

    /**
     * Returns filepath to player sprite.
     * @return Filepath to player sprite.
     */
    public String getPlayerPath() {
        return playerPath;
    }

    /**
     * Returns filepath to player frozen sprite.
     * @return Filepath to player frozen sprite.
     */
    public String getPlayerFreezePath() {
        return playerFreezePath;
    }

    /**
     * Returns filepath to shield sprite.
     * @return Filepath to shield sprite.
     */
    public String getPlayerShieldPath() {
        return playerShieldPath;
    }


    /**
     * Returns the filepath to map arrow sprite.
     * @return Filepath to map arrow sprite.
     */
    public String getMapArrowPath() {
        return mapArrowPath;
    }

    /**
     * Returns the filepath to gameboard agility minigame tile sprite.
     * @return Filepath to gameboard agility minigame tile sprite.
     */
    public String getAgilityTilePath() {
        return agilityTilePath;
    }

    /**
     * Returns the highest reachable round before the game ends.
     * @return Highest reachable round before the game ends.
     */
    public int getMaxRounds() {
        return maxRounds;
    }

    /**
     * Returns the maximum allowed players in a game.
     * @return Maximum allowed players in a game.
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Returns the minimum required players in a game.
     * @return Minimum required players in a game.
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Returns the folder containing game save files.
     * @return Folder containing game save files.
     */
    public String getSaveFolder() {
        return saveFolder;
    }

    /**
     * Returns the folder containing game music and sound files.
     * @return Folder containing game music and sound files.
     */
    public String getSoundsFolder() {
        return soundsFolder;
    }

    /**
     * Returns the debug mode password.
     * @return Debug mode password
     */
    public String getDebugPassword() {
        return debugPassword;
    }

    /**
     * Returns the instructor dashboard password.
     * @return Instructor dashboard password.
     */
    public String getInstructorPassword() {
        return this.instructorPassword;
    }


}
