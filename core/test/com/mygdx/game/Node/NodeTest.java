package com.mygdx.game.Node;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Note: not testing trivial getters/setters.
 */
class NodeTest {
    static GameContext gameContext;
    static AssetManager asset;

    @BeforeAll
    static void setUp() throws NoSuchFieldException {
        gameContext = new GameContext();
        asset = gameContext.getAssetManager();
    }

    @Test
    void getReachable() {
        Map<String, Node> nMap = new HashMap<String, Node>();
        // Use NormalNode to test Node's un-overridable functions
        NormalNode node = new NormalNode(0, 0, false, false, false, true, nMap, asset);
        nMap.put("0,0", node);
        NormalNode node2 = new NormalNode(-1, 0, false, true, false, false, nMap, asset);
        nMap.put("-1,0", node2);

        ArrayList<ArrayList<String>> paths = node.getReachable(1, null, nMap);
        assertEquals(1, paths.size());
        assertEquals(2, paths.get(0).size());
        assertEquals("0,0", paths.get(0).get(0)); // First element is always itself
        assertEquals("-1,0", paths.get(0).get(1));
        // Test no going back
        paths = node2.getReachable(1, node.getID(), nMap);
        assertEquals(0, paths.size());
        paths = node2.getReachable(1, null, nMap);
        assertEquals(1, paths.size());

        // Test multidirections
        NormalNode node3 = new NormalNode(0, 1, false, false, true, false, nMap, asset);
        nMap.put("0,1", node3);
        node.setNorth(true);
        paths = node.getReachable(1, null, nMap);
        assertEquals(2, paths.size());

        // Test range
        NormalNode node4 = new NormalNode(0, 2, false, false, true, false, nMap, asset);
        nMap.put("0,2", node4);
        node3.setNorth(true);
        paths = node4.getReachable(2, null, nMap);
        assertEquals(1, paths.size());
        NormalNode node5 = new NormalNode(1, 0, false, false, false, true, nMap, asset);
        nMap.put("1,0", node5);
        node.setEast(true);
        paths = node4.getReachable(3, null, nMap);
        assertEquals(2, paths.size());
    }
}