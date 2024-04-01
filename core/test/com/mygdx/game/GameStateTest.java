package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Node.Node;
import com.mygdx.game.Node.NormalNode;
import com.mygdx.game.Node.StarNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    static GameContext gameContext;
    static AssetManager asset;

    @BeforeAll
    static void setUp() {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();
    }

    @Test
    void nextRound() {
        PlayerProfile profile = new PlayerProfile("test",0,0,0);
        PlayerProfile profile2 = new PlayerProfile("test2",0,0,0);
        PlayerProfile profile3 = new PlayerProfile("test3",0,0,0);
        PlayerProfile profile4 = new PlayerProfile("test4",0,0,0);
        GameState gs = new GameState(List.of(profile, profile2, profile3, profile4), asset, 0, false);
        // Test turn increase
        assertEquals(0, gs.getTurn());
        assertEquals(1, gs.getRound());
        gs.nextTurn();
        assertEquals(1, gs.getTurn());
        assertEquals(1, gs.getRound());
        // Test round increase
        gs.nextTurn();
        gs.nextTurn();
        gs.nextTurn();
        assertEquals(2, gs.getRound());
        // Test end of game
        for (int i = 0; i < 25; i++) {
            assertFalse(gs.isGameOver());
            gs.nextTurn();
            gs.nextTurn();
            gs.nextTurn();
            gs.nextTurn();
        }
        assertTrue(gs.isGameOver());
        assertEquals(13, gs.getPlayerList().get(0).getLevel()); // Check level-up
    }

    @Test
    void nextTurn() {
        PlayerProfile profile = new PlayerProfile("test",0,0,0);
        PlayerProfile profile2 = new PlayerProfile("test2",0,0,0);
        PlayerProfile profile3 = new PlayerProfile("test3",0,0,0);
        PlayerProfile profile4 = new PlayerProfile("test4",0,0,0);
        GameState gs = new GameState(List.of(profile, profile2, profile3, profile4), asset, 0, false);
        // Test turn increase
        assertEquals(0, gs.getTurn());
        gs.nextTurn();
        assertEquals(1, gs.getTurn());
        // Test frozen player
        gs.getPlayerList().get(2).setFrozen(true);
        gs.nextTurn();
        assertEquals(3, gs.getTurn());
        assertEquals(profile4, gs.getCurrentPlayer().getPlayerProfile());
    }

    @Test
    void getCurrentPlayer() {
        // Make sure it's always valid
        PlayerProfile profile = new PlayerProfile("test",0,0,0);
        PlayerProfile profile2 = new PlayerProfile("test2",0,0,0);
        PlayerProfile profile3 = new PlayerProfile("test3",0,0,0);
        PlayerProfile profile4 = new PlayerProfile("test4",0,0,0);
        GameState gs = new GameState(List.of(profile, profile2, profile3, profile4), asset, 0, false);
        for (int i = 0; i < 100; i++) {
            gs.nextTurn();
            assertNotNull(gs.getCurrentPlayer());
        }
        GameState gs2 = new GameState(List.of(profile), asset, 0, false);
        for (int i = 0; i < 100; i++) {
            gs2.nextTurn();
            assertNotNull(gs2.getCurrentPlayer());
        }
    }

    @Test
    void globalPenaltyEvent() {
        PlayerProfile profile = new PlayerProfile("test",0,0,0);
        PlayerProfile profile2 = new PlayerProfile("test2",0,0,0);
        PlayerProfile profile3 = new PlayerProfile("test3",0,0,0);
        PlayerProfile profile4 = new PlayerProfile("test4",0,0,0);
        GameState gs = new GameState(List.of(profile, profile2, profile3, profile4), asset, 0, false);
        List<Player> playerList = gs.getPlayerList();
        playerList.get(0).setHasShield(true); // Test the effect of the shield
        playerList.get(1).setMoney(-5); // Test the effect of negative money
        gs.globalPenaltyEvent(50);
        assertEquals(500, playerList.get(0).getMoney());
        assertEquals(-5, playerList.get(1).getMoney());
        assertEquals(450, playerList.get(2).getMoney());
        assertEquals(450, playerList.get(3).getMoney());

        // Test hardcore
        GameState gsHC = new GameState(List.of(profile, profile2, profile3, profile4), asset, 0, true);
        playerList = gsHC.getPlayerList();
        playerList.get(0).setMoney(-5); // negative, no star
        playerList.get(1).setStars(1); // negative, star
        playerList.get(1).setMoney(-5);
        playerList.get(2).setStars(1); // Star, not negative
        gsHC.globalPenaltyEvent(50);
        assertEquals(-55, playerList.get(0).getMoney());
        assertEquals(0, playerList.get(0).getStars());
        assertEquals(0, playerList.get(1).getStars());
        assertEquals(-5, playerList.get(1).getMoney());
        assertEquals(0, playerList.get(2).getStars());
        assertEquals(500, playerList.get(2).getMoney());
    }

    @Test
    void checkStar() {
        PlayerProfile profile = new PlayerProfile("test",0,0,0);
        GameState gs = new GameState(List.of(profile), asset, 0, false);
        // Remove all star nodes
        for (String id : gs.getNodeMap().keySet()) {
            Node node = gs.getNodeMap().get(id);
            if (node instanceof StarNode) {
                gs.getNodeMap().put(id, new NormalNode(node.getMapX(), node.getMapY(), node.getNorth(), node.getEast(), node.getSouth(), node.getWest(), gs.getNodeMap(), asset));
            }
        }


        // Run checkStar until there are 3 StarNodes, the max. It will loop forever if it doesn't work.
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            int starNum = 0;
            do {
                gs.checkStar(gs.getNodeMap());

                starNum = 0;
                for (Node node : gs.getNodeMap().values()) {
                    if (node instanceof StarNode) starNum++;
                }
            } while (starNum < 3);
        });
    }

    @Test
    void removeStar() {
        PlayerProfile profile = new PlayerProfile("test",0,0,0);
        GameState gs = new GameState(List.of(profile), asset, 0, false);

        for (Map.Entry<String, Node> entry : gs.getNodeMap().entrySet()) {
            if (entry.getValue() instanceof StarNode) {
                // Move player to node and try to remove
                gs.getCurrentPlayer().setCurrentTile(entry.getKey(), gs.getNodeMap());
                StarNode node = (StarNode)entry.getValue();
                node.hasStar = false;
                gs.removeStar(gs.getNodeMap());
                assertFalse(gs.getNodeMap().get(entry.getKey()) instanceof StarNode);
                break;
            }
        }
    }
}