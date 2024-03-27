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

    static int numStudents;


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

            // Define expected values in all tests
            numStudents = 8;

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
        assertEquals(studentProfiles.size(), numStudents);

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
        assertEquals(numStudents + 1, profileManager.getStudentProfiles().size());
        PlayerProfile lastStudent = profileManager.getStudentProfiles().get(numStudents);
        assertEquals("NewStudent", lastStudent.getName());

        // Load the database with another profile manager instance to confirm changes were written to file
        ProfileManager secondManager = new ProfileManager(studentFile, highScoreFile, lifetimeScoreFile);
        PlayerProfile fileLastStudent = secondManager.getStudentProfiles().get(numStudents);
        assertEquals("NewStudent", fileLastStudent.getName());

    }

    @Test
    void removeFakeStudent() {
        // Attempt to remove a student that does not exist
        assertThrows(IllegalArgumentException.class, () ->{profileManager.removeStudent("FakeStudent");});
    }

    @Test
    void removeStudentListUpdates() {

        // Remove an existing student from the database
        String studentName = "Student3";  // Name of student to remove
        PlayerProfile removed = profileManager.removeStudent(studentName);

        // Confirm student was removed from the list of profiles
        assertEquals(removed.getName(), studentName);                                        // Confirm correct student was found and returned
        assertEquals(numStudents - 1, profileManager.getStudentProfiles().size());  // Confirm that a student was removed from the list
        assertNotEquals(studentName, profileManager.getStudentProfiles().get(2).getName());  // Confirm a different student is now in the removed student's place

        // Confirm high score lists were updated
        assertNotEquals(studentName, profileManager.getHighScoreList().get(4).getName());  // Confirm a different student is now in the removed student's place
        assertEquals(5, profileManager.getHighScoreList().size());                // Confirm that another student was added to fill the list

        assertNotEquals(studentName, profileManager.getHighScoreList().get(3).getName());  // Confirm a different student is now in the removed student's place
        assertEquals(5, profileManager.getHighScoreList().size());                // Confirm that another student was added to fill the list

    }

    @Test
    void removeStudentFileUpdates() {

        // Remove an existing student from the database
        String studentName = "Student3";  // Name of student to remove
        profileManager.removeStudent(studentName);

        // Load the database with another profile manager instance to confirm changes were written to file
        ProfileManager secondManager = new ProfileManager(studentFile, highScoreFile, lifetimeScoreFile);

        // Confirm student database file was written to
        assertNotEquals(studentName, secondManager.getStudentProfiles().get(2).getName());  // Confirm a different student is now in the removed student's place

        // Confirm high score databases were updated
        assertNotEquals(studentName, secondManager.getHighScoreList().get(4).getName());  // Confirm a different student is now in the removed student's place
        assertEquals(5, secondManager.getHighScoreList().size());                // Confirm that another student was added to fill the list

        assertNotEquals(studentName, secondManager.getHighScoreList().get(3).getName());  // Confirm a different student is now in the removed student's place
        assertEquals(5, secondManager.getHighScoreList().size());                // Confirm that another student was added to fill the list

    }

    @Test
    void renameFakeStudent() {
        // Attempt to rename a student that does not exist
        assertThrows(IllegalArgumentException.class, () ->{profileManager.renameStudent("FakeStudent", "NewName");});
    }

    @Test
    void renameStudentListUpdates() {

        // Rename an existing student from the database
        String newName = "NewName";  // Name of student to remove
        profileManager.renameStudent("Student4", newName);

        // Confirm student was renamed in each list
        assertEquals(profileManager.getStudentProfiles().get(3).getName(), newName);        // Renamed in list of all student profiles
        assertEquals(profileManager.getHighScoreList().get(0).getName(), newName);          // Renamed in high score list
        assertEquals(profileManager.getLifetimeHighScoreList().get(2).getName(), newName);  // Renamed in lifetime high score list

    }


    @Test
    void renameStudentFileUpdates() {

        // Rename an existing student from the database
        String newName = "NewName";  // Name of student to remove
        profileManager.renameStudent("Student4", newName);

        // Load the database with another instance to confirm changes were written to file
        ProfileManager secondManager = new ProfileManager(studentFile, highScoreFile, lifetimeScoreFile);

        // Confirm each database file was written
        assertEquals(secondManager.getStudentProfiles().get(3).getName(), newName);        // Renamed in student profiles database
        assertEquals(secondManager.getHighScoreList().get(0).getName(), newName);          // Renamed in high score database
        assertEquals(secondManager.getLifetimeHighScoreList().get(2).getName(), newName);  // Renamed in lifetime high score database

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