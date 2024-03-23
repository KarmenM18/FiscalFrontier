package com.mygdx.game;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Responsible for managing student player profiles.
 * <br></br>
 * Includes methods to save and load existing student, add a new student profile.
 * TODO: Also add methods to edit/remove
 *
 * @author Joelene Hales
 */
public class ProfileManager implements Serializable {
    /** Stores all student profiles while the application runs. */
    private ArrayList<PlayerProfile> studentInformation = new ArrayList<PlayerProfile>();
    /** Filename of JSON file storing all student profiles. */
    private String filename;
    /** Object reads and writes profile data to file. */
    private Json json = new Json();


    /**
     * Constructor loads and stores all student profiles.
     * @param filename Filename of JSON file storing student profiles.
     */
    public ProfileManager(String filename) {

        this.filename = filename;
        this.loadData();  // Load student profiles

    }

    /**
     * Private no-arg constructor for serialization
     */
    private ProfileManager() {}


    /**
     * Loads student profiles from file.
     */
    public void loadData() {

        try {

            // Open file
            String inputString = Files.readString(Path.of(this.filename));

            if (inputString.isEmpty()) {  // Prevent error if file is empty
                inputString = "[]";
            }

            // Load student profiles
            this.studentInformation = json.fromJson(ArrayList.class, PlayerProfile.class, inputString);

        }
        catch (Exception e) {
            System.out.println("Error loading player information!");  // TODO: Make this actually handle the errors
        }

    }


    /**
     * Returns an array of student profiles
     * @return Array of student profiles
     */
    public ArrayList<PlayerProfile> getStudentProfiles() {
        return this.studentInformation;
    }

    /**
     * Saves the list of student profiles to the JSON file.
     */
    private void saveProfile() {

        try {

            String profileString = json.prettyPrint(studentInformation);  // Serialize profile list
            Files.writeString(Paths.get(filename), profileString);  // Open file and write

        }
        catch (Exception e) {
            System.out.println("Error saving file!");  // TODO: Make this actually handle the errors
        }
    }

    /**
     * Creates a new student profile.
     * @param name Student's name
     * @throws IllegalArgumentException If a student with the entered name already exists
     */
    public void addStudent(String name) throws IllegalArgumentException {

        // Check that a student with the entered name does not already exist
        for (PlayerProfile student : this.studentInformation) {
            if (student.getName().equals(name)) {
                throw new IllegalArgumentException("Student with the entered name already exists.");
            }
        }

        // Create new profile for the student with the given name
        PlayerProfile studentProfile = new PlayerProfile(name);
        this.studentInformation.add(studentProfile);

        // Write changes to file
        this.saveProfile();
        
    }


}
