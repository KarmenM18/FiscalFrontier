package com.mygdx.game;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    @Test
    void getRandom() {
        // Test invalid argument
        assertThrows(IllegalArgumentException.class, () -> {
            Utility.getRandom(10, 5);
        });
        // Test range
        HashSet<Integer> reached = new HashSet<>();
        for (int i = 0; i < 200; i++) {
            int generated = Utility.getRandom(0, 10);
            reached.add(generated);
            assertTrue(generated >= 0 && generated <= 10);
        }
        for (int i = 0; i <= 10; i++) {
            assertTrue(reached.contains(i));
        }
    }

    @Test
    void shuffle() {
        assertDoesNotThrow(() -> {
            ArrayList<Integer> l1 = new ArrayList<Integer>(); // Empty list
            ArrayList<Integer> l2 = new ArrayList<Integer>(List.of(1)); // One element
            ArrayList<Integer> l3 = new ArrayList<Integer>(List.of(1, 2)); // Two elements
            ArrayList<Integer> l4 = new ArrayList<Integer>(List.of(1, 2, 3, 4, 5, 6, 7)); // Several elements
            Utility.shuffle(l1);
            Utility.shuffle(l2);
            Utility.shuffle(l3);
            Utility.shuffle(l4);
        });
    }

    @Test
    void epsilonEqual() {
        assertTrue(Utility.epsilonEqual(0.0f, 0.0f, 0.1f));
        assertTrue(Utility.epsilonEqual(5.0f, 6.0f, 1.1f));
        assertFalse(Utility.epsilonEqual(4.0f, 5.1f, 1.0f));
        assertTrue(Utility.epsilonEqual(-1.0f, 1.0f, 2.1f));
    }
}