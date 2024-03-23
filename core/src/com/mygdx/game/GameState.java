/**
 * Stores a game state. Used by the GameBoard. Can be saved and loaded.
 * At the end of a game, the players profiles should be updated with anything relevant stored here.
 */
package com.mygdx.game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Items.Bike;
import com.mygdx.game.Items.FreezeItem;
import com.mygdx.game.Items.MultiDice;
import com.mygdx.game.Items.Shield;
import com.mygdx.game.Node.*;
import com.mygdx.game.Node.StarNode;
import com.mygdx.game.Stocks.Stock;


import java.io.Serializable;
import java.util.*;

public class GameState implements Serializable {
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

    /**
     * Constructor
     * Will throw error if the profileList is null or empty
     *
     * @param profileList The profile list of players in the game
     */
    public GameState(List<PlayerProfile> profileList, AssetManager assets) {
        assetMan = assets;
        if (profileList == null || profileList.isEmpty()) {
            throw new IllegalArgumentException("Player list cannot be empty");
        }
        this.playerList = new ArrayList<Player>();
        currPlayerTurn = 0;
        turnNumber = 0;
        roundNumber = 0;

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
        /**
         * base concept for better generation of map
         * curr problem is node-node direction setup
        int map[][] = { {1,1,2,1,1,3,1,1,0,0},
                        {1,0,0,0,0,0,0,1,0,0},
                        {1,0,1,1,1,1,1,1,1,2},
                        {1,0,1,0,1,0,0,1,0,1},
                        {1,1,1,0,1,0,0,1,0,1},
                        {0,0,1,0,1,0,0,1,0,1},
                        {0,0,4,1,1,0,0,1,0,3},
                        {0,0,0,0,1,1,1,1,1,1}};
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                String ID = String.valueOf(i) + "," + String.valueOf(j);
                if(map[i][j] == 1){
                    nodeMap.put(ID, new NormalNode(i, j, false, false, false, false, nodeMap, assets));
                }
            }
        }
        */
        // Setup nodes
        // define junctions use the x y as conditional check for auto generation of
        //TODO add logic for direction check to avoid missed null
        //TODO automate ID and x, y process based on initial node and for loop limit
        //TODO look into logic for automate direction
        int x = 0;//for const col
        int y = 0;//for const row
        nodeMap = new HashMap<String, Node>();
        nodeMap.put("0,0", new NormalNode(x, y, true, true, false, false, nodeMap, assets));
        //0,0 top straight, shorter to J1 but has penalty
        for(y = 1; y < 5; y++){
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            if(y == 3){
                nodeMap.put(ID, new PenaltyNode(x, y, true, false, false, false, nodeMap, assets));
            }else{
                nodeMap.put(ID, new NormalNode(x, y, true, false, false, false, nodeMap, assets));
            }
        }
        y = 0; //back to 0,0
        //0,0 right
        for(x = 1; x < 2; x++){
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            nodeMap.put(ID, new NormalNode(x, y, false, true, false, false, nodeMap, assets));
        }
        //2,0 top straight normal nodes, longer but no penalty route;
        for(y = 0; y < 5; y++){
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            nodeMap.put(ID, new NormalNode(x, y, true, false, false, false, nodeMap, assets));
        }
        //2,5 back to 0,5 (J1)
        for(x = 2; x > 0 ; x--){
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            nodeMap.put(ID, new NormalNode(x, y, false, false, false, true, nodeMap, assets));
        }
        //junction 1 (0,5)
        nodeMap.put("0,5", new NormalNode(x, y, true, false, false, true, nodeMap, assets));
        //left direction J1 (0,5) to J2 (-4,5)
        for(x = 0; x > -4; x--){
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            nodeMap.put(ID, new NormalNode(x, y, false, false, false, true, nodeMap, assets));
        }
        //junction 2 (-4,5)
        nodeMap.put("-4,5", new StarNode(x, y, false, false, true, false, nodeMap, assets));
        x = 0;//back to J1
        //to top from J1
        for(y = 6; y < 9; y++){
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            nodeMap.put(ID, new NormalNode(x, y, true, false, false, false, nodeMap, assets));
        }
        //left straight at the top
        for(x = 0; x > -9; x--){
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            if(x == -4){
                nodeMap.put(ID, new PenaltyNode(x, y, false, false, false, true, nodeMap, assets));
            }else{
                nodeMap.put(ID, new NormalNode(x, y, false, false, false, true, nodeMap, assets));
            }
        }
        //down straight 5
        for(y = 9; y > 3; y--){
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            if(y == 9){
                nodeMap.put(ID, new StarNode(x, y, false, false, true, false, nodeMap, assets)); //left down stretch
            }else{
                nodeMap.put(ID, new NormalNode(x, y, false, false, true, false, nodeMap, assets)); //left down stretch
            }
        }
        //right straight 2
        for(x = -9; x < -6; x++) {
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            nodeMap.put(ID, new NormalNode(x, y, false, true, false, false, nodeMap, assets)); //left down stretch
        }
        //junction 3
        nodeMap.put("-6,3", new StarNode(x, y, true, false, true, false, nodeMap, assets));
        //top path
        for(y = 4; y < 5; y++){
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            nodeMap.put(ID, new NormalNode(x, y, true, false, false, false, nodeMap, assets)); //left down stretch
        }
        for(x = -6; x < -4; x++){
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            nodeMap.put(ID, new NormalNode(x, y, false, true, false, false, nodeMap, assets)); //left down stretch
        }
        //set x back to J3
        x = -6;
        //down path from J3
        for(y = 2; y > 1; y--){
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            nodeMap.put(ID, new NormalNode(x, y, false, false, true, false, nodeMap, assets)); //left down stretch
        }
        //making event node
        nodeMap.put(String.valueOf(x) + "," + String.valueOf(y), new NormalNode(x, y, false, true, false, false, nodeMap, assets));
        createEventNode(x, y, false, true, false, false);
        //right to J2 down path
        for(x = -5; x < -4; x++){
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            nodeMap.put(ID, new NormalNode(x, y, false, true, false, false, nodeMap, assets)); //left down stretch
        }
        //junction 4
        nodeMap.put("-4,1", new StarNode(x, y, false, false, true, false, nodeMap, assets));
        //back to J2, down path from J2
        for(y = 4; y > 0; y--) {
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            if(y == 2){
                nodeMap.put(ID, new PenaltyNode(x, y, false, false, true, false, nodeMap, assets)); //left down stretch
            }else{
                nodeMap.put(ID, new NormalNode(x, y, false, false, true, false, nodeMap, assets)); //left down stretch
            }
        }
        //right back to 0,0
        for(x = -4; x < 0; x++){
            String ID = String.valueOf(x) + "," + String.valueOf(y);
            nodeMap.put(ID, new NormalNode(x, y, false, true, false, false, nodeMap, assets)); //left down stretch
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
     * once 26 rounds is reached, end the game????
     */
    public void nextRound(){
        if(turnNumber % playerList.size() == 0 && turnNumber != 0){
            roundNumber++;
        }
        if(roundNumber > 0 && roundNumber % 3 == 0){
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
     * global event for event node, reduce all player's money
     * @param penaltyAmount
     */
    public void globalEvent(int penaltyAmount){
        //TODO adjust Money penalty logic for hardmode
        for (Player p : getPlayerList()){
            if(p.getHasShield()){
                //do nothing
            }else if(p.getStars() > 0){
                p.setStars(p.getStars() - 1);
            }else if(p.getMoney() > 0){
                p.setMoney(p.getMoney() - penaltyAmount);
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
     * @return all stocks in the shop
     */
    public Stock[] getAllStocks () {return this.stocks;}

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
        double price;
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
                "Growth: 0.5% to 2% every ROUND\n" +
                "Decline: 0.1% to 1% every ROUND\n" +
                "Risk: 20% Chance to Decline\n" +
                "Dividend Pay: Every 2 Rounds\n" +
                "Dividend change: No Change. Constant 2%";
        price = 100.00;
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
                "Growth: 2% to 10% every ROUND\n" +
                "Decline: 1% to 3% every ROUND\n" +
                "Risk: 40% Chance to decline\n" +
                "Dividend Pay: Every 2 Rounds\n" +
                "Dividend change: No Change. Constant 2%";
        price = 75.00;
        minG = 2;
        maxG = 10;
        minD = 1;
        maxD = 3;
        risk = 4;
        stocks[1] = new Stock(tickerName, price, description, minG, minD, maxG, maxD, divPay, risk, divRisk);

        //HIGH RISK GROWTH STOCK "Penny Stock"
        tickerName ="HRGS";
        description = "High Risk Stock. Which Changes every turn instead of every Round Not advisable in most cases\n" +
                "Growth: 10% to 25% every TURN\n" +
                "Decline: 15% to 20% every TURN\n" +
                "Risk: 70% Chance to decline\n" +
                "Dividend Pay: Every 5 Rounds\n" +
                "Dividend Change: No Change. Constant 1%";
        price = 1;
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
        description = "Increased payout with a higher risk for a decline in dividend pay\n" +
                "This stock focuses on income per turn rather than stock price increase\n" +
                "Growth: 1% to 2% every Round\n" +
                "Decline: 0.1% to 0.5% every ROUND\n" +
                "Risk: 20% Chance to decline in stock value\n" +
                "Risk: 10% Chance to decline in dividend Pay\n" +
                "Dividend payout: every ROUND\n" +
                "Dividend decrease: 2% decrease in pay or 10% pay increase";
        divRisk = 3;
        stocks[4] = new Stock(tickerName, price, description, minG, minD, maxG, maxD, divPay, risk, divRisk);

        //HIGH RISK Dividend STOCK
        tickerName ="MRDS";
        description = "Highest dividend payout with extreme payout inconsistency however, dividends are paid out every turn\n" +
                "This stock focuses on income per turn rather than stock price increase\n" +
                "Growth: 1% to 2% every TURN\n" +
                "Decline: 0.1% to 0.5% every TURN\n" +
                "Risk: 20% Chance to decline in stock value\n" +
                "Risk: 10% Chance to decline in dividend Pay\n" +
                "Dividend payout: every TURN\n" +
                "Dividend decrease: 50% decrease in pay or 30% pay increase";
        divRisk = 5;
        stocks[5] = new Stock(tickerName, price, description, minG, minD, maxG, maxD, divPay, risk, divRisk);
    }
}
