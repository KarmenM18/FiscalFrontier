package com.mygdx.game;

import com.badlogic.gdx.utils.Json;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Responsible for managing student player profiles.
 * <br></br>
 * Includes methods to save and load student database, add a new student profile, and rename and remove student profiles,
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
     *
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
     *
     * @return Array of student profiles
     */
    public ArrayList<PlayerProfile> getStudentProfiles() {
        return this.studentInformation;
    }


    /**
     * Saves the list of student profiles to the JSON file.
     */
    private void saveProfiles() {

        try {
            String profileString = json.prettyPrint(studentInformation);  // Serialize profile list
            Files.writeString(Paths.get(filename), profileString);  // Open file and write
        }
        catch (Exception e) {
            System.out.println("Error saving file!");  // TODO: Make this actually handle the errors
        }

    }


    /**
     * Finds the index of a student's profile in the list of profiles.
     *
     * @param name Student's name
     * @return Index of the student in the list of profiles, or -1 if the student does not exist
     */
    private int findProfile(String name) {

        // Search for student in the list of profiles by name
        for (int index = 0; index < this.studentInformation.size(); index++) {  // Iterate through list of profiles

            if (this.studentInformation.get(index).getName().equals(name)) {  // Profile found
                return index;
            }
        }

        return -1;  // Checked entire list and student not found
    }


    /**
     * Creates a new student profile and saves updates to file.
     *
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

        this.saveProfiles();   // Write changes to file
        
    }


    /**
     * Deletes the profile of the student with the given name and saves updates to file.
     *
     * @param name Student's name
     * @return Profile removed
     * @throws IllegalArgumentException If a student with the entered name does not exist
     */
    public PlayerProfile removeStudent(String name) throws IllegalArgumentException {

        PlayerProfile removedProfile;    // Stores the profile removed
        int index = findProfile(name);   // Find the profile of the student with the given name

        if (index == -1) {
            throw new IllegalArgumentException("Student with the entered name does not exist.");
        }

        removedProfile = this.studentInformation.get(index);  // Retrieve the profile to be removed
        this.studentInformation.remove(index);                // Remove profile from list

        this.saveProfiles();  // Write changes to file

        return removedProfile;

    }


    /**
     * Renames the student with the given name and saves updates to file.
     *
     * @param name Student's name
     * @param newName New name of student
     * @throws IllegalArgumentException If a student with the entered name does not exist
     */
    public void renameStudent(String name, String newName) throws IllegalArgumentException {

        int index = findProfile(name);  // Find the profile of the student with the given name

        if (index == -1) {
            throw new IllegalArgumentException("Student with the entered name does not exist.");
        }

        //this.studentInformation.get(index).setName(newName);   // Retrieve profile and rename
        this.saveProfiles();                                   // Write changes to file

    }

}
