/**
 * Stores a game state. Used by the GameBoard. Can be saved and loaded.
 * At the end of a game, the players profiles should be updated with anything relevant stored here.
 */
package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Items.Bike;
import com.mygdx.game.Items.FreezeItem;
import com.mygdx.game.Items.MultiDice;
import com.mygdx.game.Items.Shield;
import com.mygdx.game.Node.*;
import com.mygdx.game.Node.StarNode;


import java.io.Serializable;
import java.util.*;

public class GameState implements Serializable {
    private int id; // The id used to differentiate game saves. Should be unique for each different game.
    private List<Player> playerList;
    private int currPlayerTurn;
    private int turnNumber;

    //Stocks
    private Stock [] stocks;

    /**
     * TODO: check if roundNumber is 26, end game and change to score screen if so
     * maybe add warning at round 24 25??
     */
    private int roundNumber;
    private HashMap<String, Node> nodeMap;
    transient private AssetManager assetMan;
    private final int maxStar = 3;
    private final int minStar = 1;
    private int currentStar;
    private final int maxPen = 5;
    private final int minPen = 3;
    private int currentPen;
    private boolean gameOver = false; // Set to true when roundNumber exceeds the round maximum. Should be checked by GameBoard
    private boolean hardMode = false; // Hard mode changes the game mechanics to be less forgiving.
    transient private boolean debugMode = false; // Not saved; set to true whenever debugMode is enabled

    /**
     * Constructor
     * Will throw error if the profileList is null or empty
     * @param profileList the list of PlayerProfiles participating in the game
     * @param assets AssetManager to use
     * @param id UNIQUE ID of the gameState. Used to cleanup saves after termination of a game
     * @param hardMode controls if hard mode is enabled
     */
    public GameState(List<PlayerProfile> profileList, AssetManager assets, int id, boolean hardMode) {
        assetMan = assets;
        this.hardMode = hardMode;
        this.id = id;

        if (profileList == null || profileList.isEmpty()) {
            throw new IllegalArgumentException("Player list cannot be empty");
        }
        this.playerList = new ArrayList<Player>();
        currPlayerTurn = 0;
        turnNumber = 0;
        roundNumber = 1;

        for (PlayerProfile playerProfile : profileList) {
            Player player = new Player(playerProfile, assets);
            // Each player gets a different color
            player.getSprite().setColor(Utility.getRandom(0, 255) / 255f, Utility.getRandom(0, 255) / 255f, Utility.getRandom(0, 255) / 255f, 1);
            this.playerList.add(player);

            Config config = Config.getInstance();
            Skin skin = assets.get(config.getUiPath(), Skin.class);
            // TODO TESTING, 25% CHANCE PER ADDITIONAL RANDOM ITEM
            for (int i = 0; i == 0; i = Utility.getRandom(0, 3)) {
               player.addItem(new Bike(skin));
               player.addItem(new MultiDice(skin));
               player.addItem(new Shield(skin));
            }
            for (int i = 0; i == 0; i = Utility.getRandom(0, 3)) {
                player.addItem(new FreezeItem(skin));
            }
        }
        int map[][] = { {1,1,2,1,1,3,1,1,0,0},
                        {1,0,0,0,0,0,0,1,0,0},
                        {1,0,1,1,1,1,1,1,1,2},
                        {1,0,1,0,1,0,0,1,0,1},
                        {1,1,1,0,1,0,0,1,0,1},
                        {0,0,1,0,1,0,0,3,0,1},
                        {0,0,4,1,1,0,0,1,0,1},
                        {0,0,0,0,1,1,1,1,1,1}};
        /*
        0 = wall
        1 = normal
        2 = star
        3 = penalty
        4 = event
        */
        int direction[][]={ {3,4,4,4,4,4,4,4,0,0},
                            {3,0,0,0,0,0,0,1,0,0},
                            {3,0,2,2,3,4,4,14,4,4},
                            {3,0,1,0,3,0,0,1,0,1},
                            {2,2,13,0,3,0,0,1,0,1},
                            {0,0,3,0,3,0,0,1,0,1},
                            {0,0,2,2,3,0,0,1,0,1},
                            {0,0,0,0,2,2,2,12,2,1}};
        /*
        1 = N
        2 = E
        3 = S
        4 = W
        12 = N/E
        13 = N/S
        14 = N/W
        23 = E/S
        24 = E/W
        34 = S/W
        */
        //TODO direction matrix to 3D, innermost store array of 3 directions
        nodeMap = new HashMap<String, Node>();
        for(int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                boolean north = false;
                boolean east = false;
                boolean south = false;
                boolean west = false;
                String ID = String.valueOf(j) + "," + String.valueOf(map.length - i);
                switch (direction[i][j]) {
                    case 1:
                        north = true;
                        break;
                    case 2:
                        east = true;
                        break;
                    case 3:
                        south = true;
                        break;
                    case 4:
                        west = true;
                        break;
                    case 12:
                        north = true;
                        east = true;
                        break;
                    case 13:
                        north = true;
                        south = true;
                        break;
                    case 14:
                        north = true;
                        west = true;
                        break;
                    case 23:
                        east = true;
                        south = true;
                        break;
                    case 24:
                        east = true;
                        west = true;
                        break;
                    case 34:
                        west = true;
                        south = true;
                        break;
                    default:
                        break;
                }
                switch (map[i][j]) {
                    case 1:
                        nodeMap.put(ID, new NormalNode(j, map.length - i, north, east, south, west, nodeMap, assets));
                        break;
                    case 2:
                        nodeMap.put(ID, new StarNode(j, map.length - i, north, east, south, west, nodeMap, assets));
                        break;
                    case 3:
                        nodeMap.put(ID, new PenaltyNode(j, map.length - i, north, east, south, west, nodeMap, assets));
                        break;
                    case 4:
                        createEventNode(j, map.length - i, north, east, south, west);
                        break;
                    default:
                        break;
                }
            }
        }

        // Set starting nodes - player cannot start on a special node, only a plain node
        // They also can't start on the same node as another player
        ArrayList<String> nodeIDs = new ArrayList<>();
        for (Node node : nodeMap.values()) {
            if (node.getClass().equals(NormalNode.class)) nodeIDs.add(node.getID());
        }
        Utility.shuffle(nodeIDs);

        assert(nodeIDs.size() >= playerList.size());
        for (Player player : playerList) {
            player.setCurrentTile(nodeIDs.get(nodeIDs.size() - 1), nodeMap);
            nodeIDs.remove(nodeIDs.size() - 1);
        }

        //Initializing Stocks
        iniStocks();
    }

    /**
     * No-arg constructor for deserialization
     */
    private GameState() {}

    /**
     * Reinitialize objects owned by the GameState which couldn't be fully serialized.
     *
     * @param assets an AssetManager loaded with all the assets required by the GameState
     */
    public void loadTextures(AssetManager assets) {
        assetMan = assets;
        for (Node node : nodeMap.values()) {
            // Restore Node Observers
            if (node instanceof EventNode) {
                ((EventNode)node).addEventListener(penaltyAmount -> globalEvent(penaltyAmount));
            }
            node.loadTextures(assets);
        }

        for (Player player : playerList) {
            player.loadTextures(assets);
        }
    }

    /**
     * moving to next round, not affecting anything other than player level
     * for now
     */
    public void nextRound(){
        if(turnNumber % playerList.size() == 0 && turnNumber != 0){
            roundNumber++;

            //Paying out dividend for safe and medium risk stocks for all players
            for (Player p : getPlayerList()) {
                payoutDividend(p, 0);
                payoutDividend(p, 1);
                payoutDividend(p, 3);
                payoutDividend(p, 4);
            }

            //Updating Safe and Medium Risk Stocks
            stocks[0].updatePrice();
            stocks[1].updatePrice();
            stocks[3].updatePrice();
            stocks[4].updatePrice();

            //Checking for payout

            // Check for game end
            if (roundNumber > Config.getInstance().getMaxRounds()) {
                for (Player p : getPlayerList()) {
                    p.calculateScore(); // Calculate the score for each player
                }
                gameOver = true;
            }
        }
        if(roundNumber > 0 && roundNumber % 2 == 0){ //Play level increases every other round
            for (Player p : getPlayerList()){
                p.levelUp();
            }
        }

        //Updating safe-medium stocks / dividends
    }
    /**
     * End the current Player's turn, and start the next Player's turn
     */
    public void nextTurn() {

        // Wipe Player's calculated turn values
        getCurrentPlayer().endTurn(nodeMap);
        removeStar(nodeMap);
        checkStar(nodeMap);
        checkPenalty(nodeMap);
        currPlayerTurn = (currPlayerTurn + 1) % playerList.size();
        if (getCurrentPlayer().isFrozen()) {
            // TODO Inform that player was frozen
            getCurrentPlayer().setFrozen(false);
            // Skip turn
            currPlayerTurn = (currPlayerTurn + 1) % playerList.size();
            turnNumber++;
        }
        getCurrentPlayer().startTurn(nodeMap);
        turnNumber++;

        //Paying out stocks that payout dividends every turn
        this.payoutDividend(getCurrentPlayer(), 2);
        this.payoutDividend(getCurrentPlayer(), 2);

        //Updating high risk stocks
        this.stocks[2].updatePrice();
        this.stocks[5].updatePrice();


        nextRound(); // Check current round;
    }
    public boolean getHardMode(){
        return this.hardMode;
    }
    /**
     * Get current Player. Based on the current turn.
     *
     * @return Player
     */
    public Player getCurrentPlayer() { return playerList.get(currPlayerTurn); }

    /**
     * @return List of Players
     */
    public List<Player> getPlayerList() { return playerList; }

    /**
     * @return Map of nodes
     */
    public Map<String, Node> getNodeMap() { return nodeMap; }

    /**
     * @return the current roundNumber
     */
    public int getRound() { return roundNumber; }

    /**
     * global event for event node, reduce all player's money
     * @param penaltyAmount
     */
    public void globalEvent(int penaltyAmount){
        //needs to be put here due to activation order
        for (Player p : getPlayerList()){
            if(p.getHasShield()){
                p.setHasShield(false);
            }else if(hardMode){
                if(p.getStars() > 0){
                    p.setStars(p.getStars() - 1);
                }else{
                    p.setMoney(p.getMoney() - penaltyAmount);
                }
            }else {
                if(p.getMoney() > 0){
                    p.setMoney(p.getMoney() - penaltyAmount);
                }else{
                    p.setMoney(0);
                }
            }
        }
    }

    /**
     * Runs when there is less star than per turn limit
     * per turn limit is rand based on minStar and maxStar
     * @param nodeMap
     */
    public void checkStar(HashMap<String, Node> nodeMap){
        //should only run when map has
        //not getting the right current node
        currentStar = 0;
        for (HashMap.Entry<String, Node> node : nodeMap.entrySet()) {
            if (node.getValue() instanceof StarNode) {
                currentStar++;
            }
        }
        //System.out.println(currentStar); // for testing
        int starLimit = Utility.getRandom(minStar, maxStar); //setting random number of max star every turn
        if(currentStar < starLimit){ //only add new star if currentStar is less than maxStar
            List<Node> normalNodeList = new ArrayList<Node>();
            for (HashMap.Entry<String, Node> node : nodeMap.entrySet()) {
                Node node1 = node.getValue();
                if (node1 instanceof NormalNode) {
                    normalNodeList.add(node1);
                    Utility.shuffle(normalNodeList);
                }
            }
            Node randNode = normalNodeList.get(Utility.getRandom(0, normalNodeList.size() - 1));
            String randKey = randNode.getID();
            int x = randNode.getMapX();
            int y = randNode.getMapY();
            boolean north = randNode.getNorth();
            boolean south = randNode.getSouth();
            boolean west = randNode.getWest();
            boolean east = randNode.getEast();
            StarNode newStar = new StarNode(x, y, north, east, south, west, nodeMap, assetMan);
            newStar.hasStar = true;
            newStar.checkStar();
            nodeMap.remove(randKey);
            nodeMap.put(randKey, newStar);
        }
    }

    /**
     * make taken star node back to normal node
     * @param nodeMap the map of nodes on the board
     */
    public void removeStar(HashMap<String, Node> nodeMap){
        Node currentNode = nodeMap.get(getCurrentPlayer().getCurrentTile());
        if(currentNode instanceof StarNode){
            if(!((StarNode) currentNode).hasStar){
                String currentKey = currentNode.getID();
                int x = currentNode.getMapX();
                int y = currentNode.getMapY();
                boolean north = currentNode.getNorth();
                boolean south = currentNode.getSouth();
                boolean west = currentNode.getWest();
                boolean east = currentNode.getEast();
                nodeMap.remove(currentKey);
                NormalNode newNormal = new NormalNode(x, y, north, east, south, west, nodeMap, assetMan);
                nodeMap.put(currentKey, newNormal);
            }
        }
    }

    public void createEventNode(int x, int y, boolean north, boolean east, boolean south, boolean west) {
        EventNode eventNode = new EventNode(x, y, north, east, south, west, nodeMap, assetMan);
        eventNode.addEventListener(penaltyValue -> globalEvent(penaltyValue));
        nodeMap.put(x + "," + y, eventNode);
    }

    /**
     * check number of penalty nodes on the board
     * @param nodeMap
     */
    public void checkPenalty(HashMap<String, Node> nodeMap){
        currentPen = 0;
        for (HashMap.Entry<String, Node> node : nodeMap.entrySet()) {
            if (node.getValue() instanceof PenaltyNode) {
                currentPen++;
            }
        }
        if(currentPen < Utility.getRandom(minPen, maxPen)){
            List<Node> normalNodeList = new ArrayList<Node>();
            for (HashMap.Entry<String, Node> node : nodeMap.entrySet()) {
                Node node1 = node.getValue();
                if (node1 instanceof NormalNode) {
                    normalNodeList.add(node1);
                    Utility.shuffle(normalNodeList);
                }
            }
            Node randNode = normalNodeList.get(Utility.getRandom(0, normalNodeList.size() - 1));
            String randKey = randNode.getID();
            int x = randNode.getMapX();
            int y = randNode.getMapY();
            boolean north = randNode.getNorth();
            boolean south = randNode.getSouth();
            boolean west = randNode.getWest();
            boolean east = randNode.getEast();
            PenaltyNode newPen = new PenaltyNode(x, y, north, east, south, west, nodeMap, assetMan);
            nodeMap.remove(randKey);
            nodeMap.put(randKey, newPen);
        }
    }
    /**
     * @return all stocks available to purchase
     */
    public Stock [] getAllStocks () {return this.stocks;}

    /**
     * Method to clean up code a bit. Initializes all 6 stock options
     * 1.) Safe Growth Stock
     * 2.) Medium Growth Stock
     * 3.) High Risk Growth Stock
     * 4.) Safe Dividend Stock
     * 5.) Medium Dividend Stock
     * 6.) High Risk Dividend Stock
     */
    private void iniStocks () {
        this.stocks = new Stock[6];

        //Temp variables
        String tickerName;
        String description;
        int price;
        double divPay;
        double minG;
        double minD;
        double maxG;
        double maxD;
        int risk;
        int divRisk;

        //SAFE GROWTH STOCK
        tickerName = "SGS";
        description = "Safe Growth Stock for the risk adverse with low risk low rewards and low dividend pay\n" +
                "                 \n" +
                "KEY INFROMATION: \n" +
                "Growth: 0.5% to 2% every ROUND \n" +
                "Decline: 0.1% to 1% every ROUND\n" +
                "Risk: 20% Chance to Decline\n" +
                "Dividend Pay: Every 5 Rounds\n" +
                "Dividend change: No Change. Constant 2%";
        price = 100;
        divPay = 2;
        minG = 0.5;
        maxG = 2;
        minD = 0.1;
        maxD = 1;
        risk = 2;
        divRisk = 0;
        stocks[0] = new Stock(tickerName, price, description, minG, minD, maxG, maxD, divPay, risk, divRisk);

        //MEDIUM RISK GROWTH STOCK
        tickerName ="MGS";
        description = "A Stock with good growth and slightly higher risk. Dividends are payed more frequently.\n" +
                "                 \n" +
                "KEY INFORMATION: \n" +
                "Growth: 2% to 10% every ROUND\n" +
                "Decline: 1% to 3% every ROUND\n" +
                "Risk: 40% Chance to decline\n" +
                "Dividend Pay: Every Round\n" +
                "Dividend change: No Change. Constant 2%";
        price = 75;
        minG = 2;
        maxG = 10;
        minD = 1;
        maxD = 3;
        risk = 4;
        stocks[1] = new Stock(tickerName, price, description, minG, minD, maxG, maxD, divPay, risk, divRisk);

        //HIGH RISK GROWTH STOCK "Penny Stock"
        tickerName ="HRGS";
        description = "High Risk Stock. Which Changes every turn instead of every Round Not advisable in most cases\n" +
                "                 \n" +
                "KEY INFORMATION: \n" +
                "Growth: 10% to 25% every TURN\n" +
                "Decline: 15% to 20% every TURN\n" +
                "Risk: 70% Chance to decline\n" +
                "Dividend Pay: Every 5 Rounds\n" +
                "Dividend Change: No Change. Constant 1%";
        price = 20;
        divPay = 1;
        minG = 10;
        maxG = 25;
        minD = 15;
        maxD = 20;
        risk = 7;
        stocks[2] = new Stock(tickerName, price, description, minG, minD, maxG, maxD, divPay, risk, divRisk);

        //SAFE RISK Dividend STOCK
        tickerName ="SDS";
        description = "Safe Consistent Dividend Stock. Low Stock Price change but more consistent income\n" +
                "                 \n" +
                "KEY INFORMATION: \n" +
                "This stock focuses on income rather than stock price growth\n" +
                "Growth: 1% to 2% every Round\n" +
                "Decline: 0.1% to 0.5% every ROUND\n" +
                "Risk: 20% Chance to decline in stock value\n" +
                "Risk: 10% Chance to decline in dividend Pay\n" +
                "Dividend payout: every ROUND\n" +
                "Dividend decrease: 0.5% decrease in pay or 1% pay increase";
        price = 120;
        divPay = 2;
        minG = 1;
        maxG = 2;
        minD = 0.1;
        maxD = 0.5;
        risk = 2;
        divRisk = 1;
        stocks[3] = new Stock(tickerName, price, description, minG, minD, maxG, maxD, divPay, risk, divRisk);

        //Medium RISK Dividend STOCK
        tickerName ="MRDS";
        description = "Increased payout with a higher risk for a decline in dividend pay " +
                "This stock focuses on income per turn rather than stock price increase\n" +
                "                 \n" +
                "KEY INFORMATION: \n" +
                "Growth: 1% to 2% every Round\n" +
                "Decline: 0.1% to 0.5% every ROUND\n" +
                "Risk: 20% Chance to decline in stock value\n" +
                "Risk: 10% Chance to decline in dividend Pay\n" +
                "Dividend payout: every ROUND\n" +
                "Dividend decrease: 2% decrease in pay or 10% pay increase";
        divRisk = 3;
        divPay = 20; //Dividend pay of 20% per round
        stocks[4] = new Stock(tickerName, price, description, minG, minD, maxG, maxD, divPay, risk, divRisk);

        //HIGH RISK Dividend STOCK
        tickerName ="HRDS";
        description = "Highest dividend payout with extreme payout inconsistency however, dividends are paid out every turn. " +
                "This stock focuses on income per turn rather than stock price increase\n" +
                "                 \n" +
                "KEY INFORMATION: \n" +
                "Growth: 1% to 2% every TURN\n" +
                "Decline: 0.1% to 0.5% every TURN\n" +
                "Risk: 20% Chance to decline in stock value\n" +
                "Risk: 10% Chance to decline in dividend Pay\n" +
                "Dividend payout: every TURN\n" +
                "Dividend decrease: 50% decrease in pay or 30% pay increase";
        divRisk = 5;
        divPay = 80; //80% div pay per turn
        stocks[5] = new Stock(tickerName, price, description, minG, minD, maxG, maxD, divPay, risk, divRisk);
    }

    /**
     * @return true if the game is over, otherwise false
     */
    public boolean isGameOver() {return gameOver;}

    /**
     * @return integer GameState's ID
     */
    public int getID() {return id;}

    private void payoutDividend (Player player, int stockID) {

        //Finding how much of each stock that the player owns
        int owned = player.getCurrentInvestments().get(stockID).size();

        int playMoney = player.getMoney();
        int divPay = this.stocks[stockID].dividendPay();

        //Paying out dividend:
        //Amount player has + how much the stock pays per share * how many shares the player owns.
        player.setMoney(playMoney + (divPay*owned));
    }


    /**
     * @return debugMode
     */
    public boolean isDebugMode() {
        return debugMode;
    }

    /**
     * @param debugMode boolean flag
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
}