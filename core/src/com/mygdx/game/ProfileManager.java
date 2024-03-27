package com.mygdx.game;

import com.badlogic.gdx.utils.Json;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Responsible for managing student player profiles.
 * <br></br>
 * Includes methods to save and load student database, add a new student profile, and rename and remove student profiles,
 *
 * @author Joelene Hales
 */
public class ProfileManager implements Serializable {
    /** All student profiles. */
    private ArrayList<PlayerProfile> studentInformation = new ArrayList<PlayerProfile>();
    /** Top 5 students with the highest achieved individual game scores. */
    private ArrayList<PlayerProfile> highScoreList = new ArrayList<PlayerProfile>();
    /** Top 5 students with the highest overall lifetime scores. */
    private ArrayList<PlayerProfile> lifetimeHighScoreList = new ArrayList<PlayerProfile>();
    /** Filename of JSON file storing all student profiles. */
    private String studentInformationFilename;
    /** Filename of JSON file storing the students with the highest achieved individual game scores. */
    private String highScoreFilename;
    /** Filename of JSON file storing the students with the highest overall lifetime scores. */
    private String lifetimeScoreFilename;
    /** Object reads and writes profile data to file. */
    private final Json json = new Json();


    /**
     * Constructor loads and stores all student profiles.
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
        this.highScoreList = this.loadProfiles(this.highScoreFilename);                // Top 5 students with highest achieved individual game socres
        this.lifetimeHighScoreList = this.loadProfiles(this.lifetimeScoreFilename);    // Top 5 students with the highest overall lifetime scores

    }

    /**
     * Private no-arg constructor for serialization
     */
    private ProfileManager() {}


    /**
     * Loads student profiles from file.
     *
     * @param filename Filename of JSON file containing student profiles.
     * @return Array of student profiles loaded.
     */
    public ArrayList<PlayerProfile> loadProfiles(String filename) {
        //FIXME sus but works
        ArrayList<PlayerProfile> profiles;  // Stores player profiles read from file
        ClassLoader CL = getClass().getClassLoader();
        URI temp = null;
        try {
            temp = CL.getResource(filename).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String main = Paths.get(temp).toString();
        try {

            // Open file and read data
            String inputString = Files.readString(Path.of(main));

            if (inputString.isEmpty()) {  // Prevent error if file is empty
                inputString = "[]";
            }

            // Create student profiles from read data
            profiles = json.fromJson(ArrayList.class, PlayerProfile.class, inputString);

        }
        catch (IOException e) {  // Error opening file
            profiles = new ArrayList<PlayerProfile>();  // Initialize an empty list
        }

        return profiles;

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
     * Saves a list of profiles to a JSON file.
     *
     * @param profiles List of student profiles to save
     * @param filename Filename of JSON file
     */
    private void saveProfiles(ArrayList<PlayerProfile> profiles, String filename) {

        try {
            String profileString = json.prettyPrint(profiles);  // Serialize profile list
            Files.writeString(Paths.get(filename), profileString);  // Open file and write
        }
        catch (IOException e) {
            System.out.println("Error saving file.");  // TODO: Make this actually handle the errors
        }

    }


    /**
     * Finds the index of a student's profile in the list of profiles.
     *
     * @param name Student's name
     * @return Index of the student in the list of profiles, or -1 if the student does not exist
     */
    private int findProfileIndex(String name) {

        // Search for student in the list of profiles by name
        for (int index = 0; index < this.studentInformation.size(); index++) {  // Iterate through list of profiles

            if (this.studentInformation.get(index).getName().equals(name)) {  // Profile found
                return index;
            }
        }

        return -1;  // Checked entire list and student not found

    }


    /**
     * Checks if a profile with the given name exists.
     *
     * @param name Student's name
     * @return True if there is a profile with the given name, false if otherwise
     */
    private boolean exists(String name) {

        // Search the list of profiles for the student
        for (PlayerProfile student : this.studentInformation) {
            if (student.getName().equals(name)) {   // Profile with the given name found
                return true;
            }
        }

        return false;  // Checked entire list and student not found

    }


    /**
     * Retrieves the profile of the student with the given name.
     *
     * @param name Student's name
     * @return Student's profile
     * @throws IllegalArgumentException If a student with the entered name does not exist
     */
    private PlayerProfile getProfile(String name) throws IllegalArgumentException {

        int index = findProfileIndex(name);  // Find the profile's index in the list of profiles

        if (index == -1) {
            throw new IllegalArgumentException("Student with the entered name does not exist.");
        }

        return this.studentInformation.get(index);   // Return profile found

    }


    /**
     * Creates a new student profile and saves updates to file.
     *
     * @param name Student's name
     * @throws IllegalArgumentException If a student with the entered name already exists
     */
    public void addStudent(String name) throws IllegalArgumentException {

        // Check that a student with the entered name does not already exist
        if (this.exists(name)) {
            throw new IllegalArgumentException("Student with the entered name already exists.");
        }

        // Create new profile for the student with the given name
        PlayerProfile studentProfile = new PlayerProfile(name);
        this.studentInformation.add(studentProfile);

        this.saveProfiles(this.studentInformation, this.studentInformationFilename);  // Write changes to file
        this.updateHighScores();  // Update high score tables
        
    }


    /**
     * Deletes the profile of the student with the given name and saves updates to file.
     *
     * @param name Student's name
     * @return Profile removed
     * @throws IllegalArgumentException If a student with the entered name does not exist
     */
    public PlayerProfile removeStudent(String name) throws IllegalArgumentException {

        PlayerProfile removedProfile = getProfile(name);  // Retrieve the profile to be removed
        this.studentInformation.remove(removedProfile);   // Remove profile from list

        this.saveProfiles(this.studentInformation, this.studentInformationFilename);  // Write changes to file
        this.updateHighScores();  // Update high score tables

        return removedProfile;

    }


    /**
     * Renames the student with the given name and saves updates to file.
     *
     * @param name Student's name
     * @param newName New name of student
     * @throws IllegalArgumentException If the student to be renamed does not exist
     */
    public void renameStudent(String name, String newName) throws IllegalArgumentException {

        PlayerProfile profile = getProfile(name);   // Retrieve the profile of the student with the given name
        profile.setName(newName);   // Rename student

        this.saveProfiles(this.studentInformation, this.studentInformationFilename);  // Write changes to file
        this.updateHighScores();  // Update high score tables

    }


    /**
     * Changes the knowledge level of a student and writes updates to file.
     *
     * @param name Student's name
     * @param newKnowledgeLevel New knowledge level of student
     * @throws IllegalArgumentException If a student with the entered name does not exist
     */
    public void changeKnowledgeLevel(String name, int newKnowledgeLevel) throws IllegalArgumentException {

        PlayerProfile profile = getProfile(name);   // Retrieve the profile of the student with the given name
        profile.setKnowledgeLevel(newKnowledgeLevel);   // Change student's knowledge level

        this.saveProfiles(this.studentInformation, this.studentInformationFilename);  // Write changes to file
        this.updateHighScores();  // Update high score tables

    }


    /**
     * Updates the high score of a student and writes updates to file.
     *
     * @param name Student's name
     * @param newHighScore Student's new high score
     * @throws IllegalArgumentException If a student with the entered name does not exist
     */
    public void updateHighScore(String name, int newHighScore) throws IllegalArgumentException {

        PlayerProfile profile = getProfile(name);   // Retrieve the profile of the student with the given name

        // Only update the score if it's higher
        if (profile.getHighScore() > newHighScore) return;
        else {
            profile.setHighScore(newHighScore);     // Update student's high score
        }

        this.saveProfiles(this.studentInformation, this.studentInformationFilename);  // Write changes to file
        this.updateHighScores();  // Update high score tables

    }

    /**
     * Updates the lifetime score of a student and writes updates to file.
     *
     * @param name Student's name
     * @param newLifetimeScore Student's new lifetime score
     * @throws IllegalArgumentException If a student with the entered name does not exist
     */
    public void updateLifetimeScore(String name, int newLifetimeScore) throws IllegalArgumentException {

        PlayerProfile profile = getProfile(name);    // Retrieve the profile of the student with the given name
        profile.setLifetimeScore(newLifetimeScore);  // Update student's lifetime score

        this.saveProfiles(this.studentInformation, this.studentInformationFilename);  // Write changes to file
        this.updateHighScores();  // Update high score tables

    }

    /**
     * Adds to the student's lifetime score and writes updates to file.
     *
     * @param name Student's name
     * @param newScore Student's new score
     * @throws IllegalArgumentException If a student with the entered name does not exist
     */
    public void addLifetimeScore(String name, int newScore) throws IllegalArgumentException {

        PlayerProfile profile = getProfile(name);    // Retrieve the profile of the student with the given name
        profile.setLifetimeScore(profile.getLifetimeScore() + newScore);  // Update student's lifetime score

        this.saveProfiles(this.studentInformation, this.studentInformationFilename);  // Write changes to file
        this.updateHighScores();  // Update high score tables

    }


    /**
     * Creates a copy of the list of student profiles.
     * The list itself is a deep copy. Each student profile within the list is a shallow copy.
     * @return Copy of the student profile list
     */
    private ArrayList<PlayerProfile> copyProfileList() {

        ArrayList<PlayerProfile> listCopy = new ArrayList<PlayerProfile>(this.studentInformation.size());  // Create a new empty list with the same size
        listCopy.addAll(this.studentInformation);   // Add each student profile to the list copy

        return listCopy;

    }

    /**
     * Create a comparator used to sort student profiles on highest achieved individual game score, in descending order.
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
     * Create a comparator used to sort student profiles on overall lifetime score, in descending order.
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

        ArrayList<PlayerProfile> sortedCopy = copyProfileList();  // Copy list of student profiles
        sortedCopy.sort(highScoreComparator);  // Sort on highest achieved individual game score, in descending order

        return sortedCopy;

    }


    /**
     * Sorts the student profiles on overall lifetime score, in descending order.
     *
     * @return Copy of the student profile list sorted on overall lifetime score, in descending order.
     */
    private ArrayList<PlayerProfile> sortLifetimeScore() {

        ArrayList<PlayerProfile> sortedCopy = copyProfileList();  // Copy list of student profiles
        sortedCopy.sort(lifetimeScoreComparator);  // Sort on overall lifetime score, in descending order

        return sortedCopy;

    }


    /**
     * Updates the individual game and lifetime high score tables.
     */
    public void updateHighScores() {

        // Find top 5 students with the highest achieved game scores and lifetime scores
        this.highScoreList = this.sortHighScore();
        this.lifetimeHighScoreList = this.sortLifetimeScore();

        if (this.studentInformation.size() > 5) {  // More than 5 students in the database
            this.highScoreList =  (ArrayList<PlayerProfile>) this.highScoreList.subList(0, 5);  // Take only the top 5 students
            this.lifetimeHighScoreList =  (ArrayList<PlayerProfile>) this.lifetimeHighScoreList.subList(0, 5);
        }

        // Save high score tables to file
        this.saveProfiles(highScoreList, this.highScoreFilename);
        this.saveProfiles(lifetimeHighScoreList, this.lifetimeScoreFilename);

    }


    /**
     * Returns the top 5 students with the highest achieved individual game scores.
     * Sorted from highest score to lowest score.
     *
     * @return Students with the highest achieved individual game scores.
     */
    public ArrayList<PlayerProfile> getHighScoreList() {
        return this.highScoreList;
    }


    /**
     * Returns the top 5 students with the highest overall lifetime scores, in order from highest score to lowest score.
     *
     * @return Students with the highest overall lifetime scores.
     */
    public ArrayList<PlayerProfile> getLifetimeHighScoreList() {
        return this.lifetimeHighScoreList;
    }


}
