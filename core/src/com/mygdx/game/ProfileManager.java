package com.mygdx.game;

import com.badlogic.gdx.utils.Json;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Responsible for managing the database of student profiles and high score tables.
 * <br><br>
 * Includes methods to add, edit, or remove student profiles. Any changes made to the student profiles will
 * automatically update the high score tables and write the changes to file.
 *
 * @author Joelene Hales
 */
public class ProfileManager implements Serializable {

    /** Object used to read/write student profiles to/from files. */
    private static final Json json = new Json();
    /** Filename of JSON file storing all student profiles. */
    private String studentInformationFilename;
    /** Filename of JSON file storing the students with the highest achieved individual game scores. */
    private String highScoreFilename;
    /** Filename of JSON file storing the students with the highest overall lifetime scores. */
    private String lifetimeScoreFilename;


    /** All student profiles. */
    private ArrayList<PlayerProfile> studentInformation = new ArrayList<PlayerProfile>();
    /** Top 5 students with the highest achieved individual game scores. */
    private ArrayList<PlayerProfile> highScoreList = new ArrayList<PlayerProfile>();
    /** Top 5 students with the highest overall lifetime scores. */
    private ArrayList<PlayerProfile> lifetimeHighScoreList = new ArrayList<PlayerProfile>();


    /**
     * Constructor loads and stores all student profiles and high scores.
     *
     * @param studentInformationFilename Filename of JSON file storing student profiles.
     * @param highScoreFilename Filename of JSON file storing the students with the highest achieved individual game scores.
     * @param lifetimeScoreFilename Filename of JSON file storing the students with the highest overall lifetime scores.
     */
    public ProfileManager(String studentInformationFilename, String highScoreFilename, String lifetimeScoreFilename) {

        // Store filenames
        this.studentInformationFilename = studentInformationFilename;
        this.highScoreFilename = highScoreFilename;
        this.lifetimeScoreFilename = lifetimeScoreFilename;

        // Load student profiles
        this.studentInformation = this.loadProfiles(this.studentInformationFilename);  // All student profiles
        this.highScoreList = this.loadProfiles(this.highScoreFilename);                // Top 5 students with highest achieved individual game scores
        this.lifetimeHighScoreList = this.loadProfiles(this.lifetimeScoreFilename);    // Top 5 students with the highest overall lifetime scores

    }


    /**
     * Private no-arg constructor for serialization.
     */
    private ProfileManager() {}


    /**
     * Loads a list of student profiles from file.
     *
     * @param filename Filename of JSON file containing student profiles.
     * @return List of student profiles loaded.
     */
    private ArrayList<PlayerProfile> loadProfiles(String filename) {

        ArrayList<PlayerProfile> profiles;  // Stores player profiles read from file

        try {
            // Open file and read data
            String inputString = Files.readString(Path.of(filename));

            if (inputString.isEmpty()) {  // Empty file
                profiles = new ArrayList<PlayerProfile>();  // Initialize an empty list of profiles
            }
            else {
                // Create student profiles from read data
                profiles = json.fromJson(ArrayList.class, PlayerProfile.class, inputString);
            }

        }
        catch (IOException e) {  // Error opening file
            profiles = new ArrayList<PlayerProfile>();  // Initialize an empty list of profiles
        }

        return profiles;

    }


    /**
     * Returns a list of all student profiles.
     *
     * @return List of all student profiles.
     */
    public ArrayList<PlayerProfile> getStudentProfiles() {
        return this.studentInformation;
    }


    /**
     * Returns the top 5 students with the highest achieved individual game scores. Students are sorted from highest
     * to lowest score.
     *
     * @return Students with the highest achieved individual game scores.
     */
    public ArrayList<PlayerProfile> getHighScoreList() {
        return this.highScoreList;
    }


    /**
     * Returns the top 5 students with the highest overall lifetime scores. Students are sorted from highest to
     * lowest score.
     *
     * @return Students with the highest overall lifetime scores.
     */
    public ArrayList<PlayerProfile> getLifetimeHighScoreList() {
        return this.lifetimeHighScoreList;
    }


    /**
     * Saves a list of student profiles to a JSON file.
     *
     * @param profiles List of student profiles to save.
     * @param filename Filename of JSON file.
     */
    private void saveProfiles(ArrayList<PlayerProfile> profiles, String filename) {

        try {
            String profileString = json.prettyPrint(profiles);  // Serialize profile list
            Files.writeString(Paths.get(filename), profileString);  // Open file and write
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Finds the index of a student's profile in a list of profiles.
     *
     * @param name Student's name.
     * @param profileList List of student profiles to search.
     * @return Index of the student in the list of profiles, or -1 if the student could not be found.
     */
    private int findProfileIndex(String name, ArrayList<PlayerProfile> profileList) {

        // Search for student in the list of profiles by name
        for (int index = 0; index < profileList.size(); index++) {  // Iterate through list of profiles

            if (profileList.get(index).getName().equals(name)) {  // Profile found
                return index;
            }
        }

        return -1;  // Checked entire list and student not found

    }


    /**
     * Retrieves the profile of the student with the given name.
     *
     * @param name Student's name
     * @return Student's profile
     * @throws IllegalArgumentException If a student with the entered name does not exist
     */
    private PlayerProfile getProfile(String name) throws IllegalArgumentException {

        int index = findProfileIndex(name, this.studentInformation);  // Find the profile's index in the list of profiles

        if (index == -1) {
            throw new IllegalArgumentException("Student with the entered name does not exist.");
        }

        return this.studentInformation.get(index);   // Return profile found

    }


    /**
     * Creates a new student profile and writes updates to the database files.
     *
     * @param name Student's name.
     * @throws IllegalArgumentException If a student with the entered name already exists.
     */
    public void addStudent(String name) throws IllegalArgumentException {

        // Check that a student with the entered name does not already exist
        if (findProfileIndex(name, this.studentInformation) >= 0) {  // Index returned, indicates a student with the same name was found in the list of all students
            throw new IllegalArgumentException("Student with the entered name already exists.");
        }

        // Create new profile for the student with the given name
        PlayerProfile studentProfile = new PlayerProfile(name);
        this.studentInformation.add(studentProfile);

        this.saveProfiles(this.studentInformation, this.studentInformationFilename);  // Write changes to file
        this.updateHighScoreFiles();  // Update high score tables
        
    }


    /**
     * Deletes an existing student's profile and writes updates to the database files.
     *
     * @param name Student's name.
     * @return Profile removed.
     * @throws IllegalArgumentException If a student with the entered name does not exist.
     */
    public PlayerProfile removeStudent(String name) throws IllegalArgumentException {

        PlayerProfile removedProfile = getProfile(name);  // Retrieve the profile to be removed
        this.studentInformation.remove(removedProfile);   // Remove profile from list

        this.saveProfiles(this.studentInformation, this.studentInformationFilename);  // Write changes to file
        this.updateHighScoreFiles();  // Update high score tables

        return removedProfile;

    }


    /**
     * Renames the student with the given name and writes updates to the database files.
     *
     * @param name Student's name.
     * @param newName New name of student.
     * @throws IllegalArgumentException If the student to be renamed does not exist.
     */
    public void renameStudent(String name, String newName) throws IllegalArgumentException {

        PlayerProfile profile = getProfile(name);   // Retrieve the profile of the student with the given name
        profile.setName(newName);   // Rename student

        this.saveProfiles(this.studentInformation, this.studentInformationFilename);  // Write changes to file
        this.updateHighScoreFiles();  // Update high score tables

    }


    /**
     * Changes the knowledge level of a student and writes updates to the database files.
     *
     * @param name Student's name.
     * @param newKnowledgeLevel New knowledge level of student.
     * @throws IllegalArgumentException If a student with the entered name does not exist.
     */
    public void changeKnowledgeLevel(String name, int newKnowledgeLevel) throws IllegalArgumentException {

        PlayerProfile profile = getProfile(name);      // Retrieve the profile of the student with the given name
        profile.setKnowledgeLevel(newKnowledgeLevel);  // Change student's knowledge level

        this.saveProfiles(this.studentInformation, this.studentInformationFilename);  // Write changes to file
        this.updateHighScoreFiles();  // Update high score tables

    }


    /**
     * Updates the high score of a student and writes updates to the database files.
     *
     * @param name Student's name.
     * @param newHighScore Student's new high score.
     * @throws IllegalArgumentException If a student with the entered name does not exist.
     */
    public void updateHighScore(String name, int newHighScore) throws IllegalArgumentException {

        PlayerProfile profile = getProfile(name);  // Retrieve the profile of the student with the given name

        // Only update the score if it's higher
        if (profile.getHighScore() > newHighScore) return;
        else {
            profile.setHighScore(newHighScore);  // Update student's high score
        }

        this.saveProfiles(this.studentInformation, this.studentInformationFilename);  // Write changes to file
        this.updateHighScoreFiles();  // Update high score tables

    }


    /**
     * Adds to the student's lifetime score and writes updates to the database files.
     *
     * @param name Student's name.
     * @param newScore Individual game score to add to lifetime score.
     * @throws IllegalArgumentException If a student with the entered name does not exist.
     */
    public void addLifetimeScore(String name, int newScore) throws IllegalArgumentException {

        PlayerProfile profile = getProfile(name);  // Retrieve the profile of the student with the given name
        profile.setLifetimeScore(profile.getLifetimeScore() + newScore);  // Update student's lifetime score

        this.saveProfiles(this.studentInformation, this.studentInformationFilename);  // Write changes to file
        this.updateHighScoreFiles();  // Update high score tables

    }


    /**
     * Creates a copy of a list of student profiles. The list itself is a deep copy. Each student profile within the
     * list is a shallow copy.
     *
     * @param profileList List of profiles to copy
     * @return Copy of the student profile list
     */
    private ArrayList<PlayerProfile> copyProfileList(ArrayList<PlayerProfile> profileList) {

        ArrayList<PlayerProfile> listCopy = new ArrayList<PlayerProfile>(profileList.size());  // Create a new empty list with the same size
        listCopy.addAll(profileList);   // Add each student profile to the list copy

        return listCopy;

    }


    /**
     * Comparator used to sort student profiles on highest achieved individual game score, in descending order.
     */
    private static final Comparator<PlayerProfile> highScoreComparator = new Comparator<PlayerProfile>() {

        @Override
        public int compare(PlayerProfile p1, PlayerProfile p2) {

            Integer score1 = p1.getHighScore();
            Integer score2 = p2.getHighScore();

            return score2.compareTo(score1);  // Return in descending order

        }
    };


    /**
     *Comparator used to sort student profiles on overall lifetime score, in descending order.
     */
    private static final Comparator<PlayerProfile> lifetimeScoreComparator = new Comparator<PlayerProfile>() {

        @Override
        public int compare(PlayerProfile p1, PlayerProfile p2) {

            Integer score1 = p1.getLifetimeScore();
            Integer score2 = p2.getLifetimeScore();

            return score2.compareTo(score1);  // Return in descending order

        }
    };


    /**
     * Sorts the student profiles on highest achieved individual game score, in descending order.
     *
     * @return Copy of the student profile list sorted on highest achieved individual game score, in descending order.
     */
    private ArrayList<PlayerProfile> sortHighScore() {

        ArrayList<PlayerProfile> sortedCopy = copyProfileList(this.studentInformation);  // Copy list of student profiles
        sortedCopy.sort(highScoreComparator);  // Sort on highest achieved individual game score, in descending order

        return sortedCopy;

    }


    /**
     * Sorts the student profiles on overall lifetime score, in descending order.
     *
     * @return Copy of the student profile list sorted on overall lifetime score, in descending order.
     */
    private ArrayList<PlayerProfile> sortLifetimeScore() {

        ArrayList<PlayerProfile> sortedCopy = copyProfileList(this.studentInformation);  // Copy list of student profiles
        sortedCopy.sort(lifetimeScoreComparator);  // Sort on overall lifetime score, in descending order

        return sortedCopy;

    }


    /**
     * Updates the individual game and lifetime high score tables and writes updates to file.
     */
    private void updateHighScoreFiles() {

        // Find top 5 students with the highest achieved game scores and lifetime scores
        this.highScoreList = this.sortHighScore();
        this.lifetimeHighScoreList = this.sortLifetimeScore();

        if (this.studentInformation.size() >= 5) {  // More than 5 students in the database
            this.highScoreList =  new ArrayList<PlayerProfile>(this.highScoreList.subList(0, 5));  // Take only the top 5 students
            this.lifetimeHighScoreList =  new ArrayList<PlayerProfile>(this.lifetimeHighScoreList.subList(0, 5));
        }

        // Save high score tables to file
        this.saveProfiles(highScoreList, this.highScoreFilename);
        this.saveProfiles(lifetimeHighScoreList, this.lifetimeScoreFilename);

    }

}
