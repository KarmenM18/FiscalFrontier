package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.io.File;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Various utility functions used throughout the program
 *
 * @author Franck Limtung (flimtung)
 */
public final class Utility {

    /**
     * Private constructor to prevent subclassing
     */
    private Utility() {}

    /**
     * Returns a random value within the range
     *
     * @param min the minimum value of the range
     * @param max the maximum value of the range
     */
    public static int getRandom(int min, int max) throws IllegalArgumentException {

        if (min > max) {
            throw new IllegalArgumentException();
        }

        Random random = new Random();  // Create new randomizer

        return min + random.nextInt((max - min) + 1);
    }

    /**
     * Shuffle the elements in the List
     *
     * @param list List object
     */
    public static <T> void shuffle(List<T> list) {
        if (list.size() <= 1) return; // No need to do anything

        for (int i = list.size() - 1; i >= 0; i--) {
            int index = getRandom(0, i);
            T temp = list.get(index);
            list.set(index, list.get(i));
            list.set(i, temp);
        }
    }

    /**
     * Check if a file already exists and can be accessed
     * @param filename Path to file
     * @return True if the file exists, False if it doesn't
     */
    public static Boolean fileExists(String filename) {
        File f = new File(filename);
        return f.exists() && f.isFile();
    }

    /**
     * Check equality of two floats with an epsilon value. Allows "close enough" equality checking.
     * @param a float one
     * @param b float two
     * @param epsilon epsilon value
     * @return True or False based on abs(a - b) less than or equal to epsilon
     */
    public static Boolean epsilonEqual(float a, float b, float epsilon) {
        return abs(a - b) <= epsilon;
    }

    /**
     * Displays an error dialog box on the specified stage.
     *
     * @param text the error message to write
     * @param stage the Stage to show the box on
     * @param skin Skin used to generate the dialog box
     */
    public static void showErrorDialog(String text, Stage stage, Skin skin) {
        Dialog errorDialog = new Dialog("Error", skin);
        errorDialog.text(text);
        errorDialog.button("Continue", true);
        errorDialog.show(stage);

        SoundSystem.getInstance().playSound("error.wav");
    }
}
