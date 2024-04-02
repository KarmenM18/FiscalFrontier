package com.mygdx.game;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerProfileTest {
    PlayerProfile profile;

    @BeforeEach
    void setUpEach() {
        // Create a new player profile
        profile = new PlayerProfile("test", 0, 0, 0);
    }

    @Test
    void updateKnowledgeLevel() {
        assertEquals(0, profile.getKnowledgeLevel());
        assertEquals(0, profile.getTipCount());
        assertEquals(0, profile.getLearned().size());
        // Try to increase knowledge level and see what happens
        profile.updateKnowledgeLevel();
        assertEquals(1, profile.getKnowledgeLevel());
        assertEquals(5, profile.getTipCount());
        assertEquals(5, profile.getLearned().size());

        // Iterate over levels
        for (int i = 1; i < 13; i++) {
            profile.updateKnowledgeLevel();
            assertEquals(5 + 5 * i, profile.getTipCount());
            assertEquals(profile.getTipCount(), profile.getLearned().size());
        }

        // Extend further; the tipCount shouldn't increase further
        for (int i = 0; i < 10; i++) {
            profile.updateKnowledgeLevel();
            assertEquals(65, profile.getTipCount());
            assertEquals(65, profile.getLearned().size());
        }
    }

    @Test
    void getName() {
        String name = profile.getName();
        assertEquals("test", name);
    }

    @Test
    void setName() {
        String newName = "testName";
        profile.setName(newName);
        assertEquals(newName, profile.getName());
    }

    @Test
    void getLifetimeScore() {
        profile = new PlayerProfile("test", 100, 0, 0);
        int lifeTimeScore = 1337;
        lifeTimeScore = profile.getLifetimeScore();
        assertEquals(100, lifeTimeScore);
    }

    @Test
    void setLifetimeScore() {
        assertEquals(0, profile.getLifetimeScore());
        profile.setLifetimeScore(1337);
        assertEquals(1337, profile.getLifetimeScore());
    }

    @Test
    void getHighScore() {
        assertEquals(0, profile.getHighScore());
        profile.setHighScore(100);
        assertEquals(100, profile.getHighScore());
    }

    @Test
    void setHighScore() {
        assertEquals(0, profile.getHighScore());
        profile.setHighScore(500);
        assertEquals(500, profile.getHighScore());
    }

    @Test
    void getKnowledgeLevel() {
        assertEquals(0, profile.getKnowledgeLevel());
        profile.updateKnowledgeLevel();
        assertEquals(1, profile.getKnowledgeLevel());

        // Iterate over levels
        for (int i = 1; i < 13; i++) {
            assertEquals(i, profile.getKnowledgeLevel());
            profile.updateKnowledgeLevel();
        }

        // Can still keep leveling, but it does nothing
        // Iterate over levels
        for (int i = 1; i < 10; i++) {
            profile.updateKnowledgeLevel();
            assertEquals(13 + i, profile.getKnowledgeLevel());
        }
    }

    @Test
    void setKnowledgeLevel() {
        assertEquals(0, profile.getKnowledgeLevel());
        // Try setting to the same
        profile.setKnowledgeLevel(0);
        assertEquals(0, profile.getKnowledgeLevel());
        assertEquals(0, profile.getLearned().size());
        // Try normal value
        profile.setKnowledgeLevel(5);
        assertEquals(5, profile.getKnowledgeLevel());
        assertEquals(5 * 5, profile.getLearned().size());
        // Try too large number
        profile.setKnowledgeLevel(50);
        assertEquals(50, profile.getKnowledgeLevel());
        assertEquals(5 * 13, profile.getLearned().size());
        // Try negative
        profile.setKnowledgeLevel(-5);
        assertEquals(-5, profile.getKnowledgeLevel());
        assertEquals(0, profile.getLearned().size());
        profile.updateKnowledgeLevel();
        assertEquals(-4, profile.getKnowledgeLevel());
        assertEquals(0, profile.getLearned().size());
    }

    @Test
    void getSpritePath() {
        assertNotNull(profile.getSpritePath());
    }

    @Test
    void getLearned() {
        assertNotNull(profile.getLearned());
        profile.updateKnowledgeLevel();
        assertNotNull(profile.getLearned());
        assertEquals(5, profile.getLearned().size());
    }

    @Test
    void getTipCount() {
        assertEquals(0, profile.getTipCount());
        // Try setting to the same
        profile.setKnowledgeLevel(0);
        assertEquals(0, profile.getTipCount());
        // Try normal value
        profile.setKnowledgeLevel(5);
        assertEquals(5 * 5, profile.getTipCount());
        // Try too large number
        profile.setKnowledgeLevel(50);
        assertEquals(5 * 13, profile.getTipCount());
        // Try negative
        profile.setKnowledgeLevel(-5);
        assertEquals(0, profile.getTipCount());
        profile.updateKnowledgeLevel();
        assertEquals(0, profile.getTipCount());
    }
}