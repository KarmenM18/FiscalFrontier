package com.mygdx.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProfileManagerTest {

    static ProfileManager profileManager;

    static String studentFile;

    static String highScoreFile;

    static String lifetimeScoreFile;

    static String studentDatabase;

    static String highScoreDatabase;

    static String lifetimeScoreDatabase;


    @BeforeAll
    static void setUpClass() {

        String test = System.getProperty("user.dir");

        try {

            // Define filepaths to each database
            studentFile = test + "/TestingDatabases/studentDatabase1.json";
            highScoreFile = test + "/TestingDatabases/highScoreDatabase1.json";
            lifetimeScoreFile = test + "/TestingDatabases/lifetimeScoreDatabase1.json";

            // Store database contents to be restored after each test
            studentDatabase = Files.readString(Path.of(studentFile));
            highScoreDatabase = Files.readString(Path.of(highScoreFile));
            lifetimeScoreDatabase = Files.readString(Path.of(lifetimeScoreFile));

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    @BeforeEach
    void setUp() {

        // Create profile manager and load testing student databases
        profileManager = new ProfileManager(studentFile, highScoreFile, lifetimeScoreFile);

    }

    @AfterEach
    void tearDown() {

        try {

            // Restore databases to their original state before test from backup
            Files.deleteIfExists(Path.of(studentFile));
            Files.deleteIfExists(Path.of(highScoreFile));
            Files.deleteIfExists(Path.of(lifetimeScoreFile));

            Files.writeString(Path.of(studentFile), studentDatabase);
            Files.writeString(Path.of(highScoreFile), highScoreDatabase);
            Files.writeString(Path.of(lifetimeScoreFile), lifetimeScoreDatabase);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Test
    void numberStudentProfiles() {

        ArrayList<PlayerProfile> studentProfiles = profileManager.getStudentProfiles();

        // Verify correct number of students were returned
        assertEquals(studentProfiles.size(), 8);

    }

    @Test
    void studentInitialization() {

        ArrayList<PlayerProfile> studentProfiles = profileManager.getStudentProfiles();

        // Verify that one student with optional attributes (high score, lifetime score) was initialized correctly
        PlayerProfile student5 = studentProfiles.get(4);
        assertEquals("Student5", student5.getName());
        assertEquals(12, student5.getKnowledgeLevel());
        assertEquals(13, student5.getHighScore());
        assertEquals(46, student5.getLifetimeScore());

        // Verify that a student without optional attributes was initialized correctly
        PlayerProfile student6 = studentProfiles.get(5);
        assertEquals("Student6", student6.getName());
        assertEquals(1, student6.getKnowledgeLevel());
        assertEquals(0, student6.getHighScore());
        assertEquals(0, student6.getLifetimeScore());

    }


    @Test
    void addDuplicateStudent() {
        // Attempt to add a student with a name that is already taken in the database
        assertThrows(IllegalArgumentException.class, () ->{profileManager.addStudent("Student1");});
    }

    @Test
    void addStudent() {

        profileManager.addStudent("NewStudent");  // Add a new student to the database

        // Confirm student was added to the list of profiles
        PlayerProfile lastStudent = profileManager.getStudentProfiles().get(-1);
        assertEquals("NewStudent", lastStudent.getName());

        // Load the database with another instance to confirm changes were written to file
        ProfileManager secondManager = new ProfileManager(studentFile, highScoreFile, lifetimeScoreFile);
        PlayerProfile fileLastStudent = secondManager.getStudentProfiles().get(-1);
        assertEquals("NewStudent", fileLastStudent.getName());

    }

    @Test
    void removeStudent() {
    }

    @Test
    void renameStudent() {
    }

    @Test
    void changeKnowledgeLevel() {
    }

    @Test
    void updateHighScore() {
    }

    @Test
    void updateLifetimeScore() {
    }

    @Test
    void updateHighScores() {
    }

    @Test
    void getHighScoreList() {
    }

    @Test
    void getLifetimeHighScoreList() {
    }
}