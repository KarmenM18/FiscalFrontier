package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

public final class Utility {
    private static final Random random = new Random();

    // Private constructor to prevent subclassing
    private Utility() {}

    /**
     * Returns a random value within the range
     *
     * @param min the minimum value of the range
     * @param max the maximum value of the range
     */
    public static int getRandom(int min, int max) {
        return min + random.nextInt((max - min) + 1);
    }

    /**
     * Shuffle the elements in the List
     *
     * @param list List object
     */
    public static <T> void shuffle(List<T> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            int index = getRandom(0, i);
            T temp = list.get(index);
            list.set(index, list.get(i));
            list.set(i, temp);
        }
    }

    /**
     * Save an object to the specified path
     *
     * @param object The object we are saving
     * @param filename The path we are using to save the object
     */
    public static void saveObject(Object object, String filename) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(filename)));
            oos.writeObject(object);

            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load an object from the specified path
     *
     * @param filename The path we are using to load the object
     * @return The object we read or null
     */
    public static Object loadObject(String filename) {
        try {
            ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(filename)));
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
    }
}
