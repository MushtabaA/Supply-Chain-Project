package edu.ucalgary.ensf409;

//Imports used for this testing file 
import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChairTest {
    @Test
    //Passes this test
    public void constructorChairTest() {
        Chair testChair = new Chair("mesh", "chair", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        String[] expected = {"mesh", "chair", String.valueOf(1), "jdbc:mysql://localhost/inventory", "abhay", "ensf409"};
        String[] test = {testChair.getCategory(), testChair.getType(), String.valueOf(testChair.getQuantity()), testChair.getDBURL(), testChair.getUSERNAME(), testChair.getPASSWORD()};
        assertArrayEquals("The constructor was sucessful in initialzing the data memebers", expected, test);
    }

    @Test
    //Passes this test as well
    public void initializeConnectionTest() {
        Chair testChair1 = new Chair("mesh", "chair", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        testChair1.initializeConnection();
        ResultSet rs;
        String[] resultsTest = new String[2];
        Statement stmnt;
        try {
            stmnt = testChair1.createConnection.createStatement();
            rs = stmnt.executeQuery("SELECT ID FROM  CHAIR  WHERE Type = 'kneeling'");
            int i = 0;
            while (rs.next()) {
                resultsTest[i] = rs.getString("ID");
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] expected = {"C1320", "C3819"};

        assertArrayEquals("The connection was not working and the ID of the Chair was wrong", expected, resultsTest);
    }

    @Test
    //Passes this test as well
    public void getEverythingTest() {
        Chair testChair2 = new Chair("mesh", "chair", 2, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        testChair2.initializeConnection();
        ResultSet rs;
        ArrayList<String> inputTest = new ArrayList<>();
        Statement stmnt;
        try {
            stmnt = testChair2.createConnection.createStatement();
            rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = " + "'" + testChair2.getCategory() + "'");
            int i = 0;
            while (rs.next()) {
                inputTest.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
                i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<String> expected = new ArrayList<>();
        expected.add("175" + " " + "C0942" + " " + "005");
        expected.add("75" + " " + "C6748" + " " + "003");
        expected.add("75" + " " + "C8138" + " " + "005");
        expected.add("50" + " " + "C9890" + " " + "003");
        assertEquals("The input reterving based on the category was wrong", expected, inputTest);

    }
    @Test
    //Passes this test as well
    public void sortPriceTest(){
        Chair testChair3 = new Chair("mesh", "chair", 2, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        testChair3.initializeConnection();
        ResultSet rs;
        ArrayList<String> inputTest1 = new ArrayList<>();
        Statement stmnt;
        try {
            stmnt = testChair3.createConnection.createStatement();
            rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = " + "'" + testChair3.getCategory() + "'");
            int i = 0;
            while (rs.next()) {
                inputTest1.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
                i++;
            }
            testChair3.sortPrice(inputTest1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<String> expected = new ArrayList<>();
        expected.add("50" + " " + "C9890" + " " + "003");
        expected.add("75" + " " + "C6748" + " " + "003");
        expected.add("75" + " " + "C8138" + " " + "005");
        expected.add("175" + " " + "C0942" + " " + "005");
        assertEquals("The sorting price method failed", expected, inputTest1);
    }

    @Test
    //Passes this test as well
    public void numberOfPartsTest() {
        Chair testChair4 = new Chair("mesh", "chair", 3, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        int[] expected = {3};
        int[] test = {testChair4.numberOfParts(testChair4.getQuantity())};
        assertArrayEquals("The numberOfParts did not get the correct max parts", expected, test);
    }

    @Test
    //Passes this test as well
    public void checkPriceKneelingTest() {
        Chair testChair5 = new Chair("kneeling", "chair", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        testChair5.initializeConnection();
        ResultSet rs;
        ArrayList<String> inputTest2 = new ArrayList<>();
        Statement stmnt;
        try {
            stmnt = testChair5.createConnection.createStatement();
            rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = " + "'" + testChair5.getCategory() + "'");
            int i = 0;
            while (rs.next()) {
                inputTest2.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
                i++;
            }
            testChair5.sortPrice(inputTest2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int test = testChair5.checkPriceKneeling(inputTest2);
        int expected = 125;
        assertEquals("The lowest price was not determined", expected, test);
    }
    @Test
    //Passes this test as well
    public void checkPriceAllTest() {
        Chair testChair6 = new Chair("mesh", "chair", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        testChair6.initializeConnection();
        ResultSet rs;
        ArrayList<String> inputTest3 = new ArrayList<>();
        Statement stmnt;
        try {
            stmnt = testChair6.createConnection.createStatement();
            rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = " + "'" + testChair6.getCategory() + "'");
            int i = 0;
            while (rs.next()) {
                inputTest3.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
                i++;
            }
            testChair6.sortPrice(inputTest3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int test = testChair6.checkPriceAll(inputTest3);
        int expected = 200;
        assertEquals("The lowest price was not determined", expected, test);
    }

    @Test
    //Passes this test
    public void suggestedManufacturerTest() {
        Chair testChair7 = new Chair("mesh", "chair", 2, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        testChair7.initializeConnection();
        ResultSet rs;
        ArrayList<String> inputTest4 = new ArrayList<>();
        Statement stmnt;
        ArrayList<String> repeats = new ArrayList<>();

        try {
            stmnt = testChair7.createConnection.createStatement();
            rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = " + "'" + testChair7.getCategory() + "'");
            int i = 0;
            while (rs.next()) {
                inputTest4.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
                i++;
            }
            testChair7.sortPrice(inputTest4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < inputTest4.size(); i++) {
                final String REGEX3 = "([0-9]+$)";
                final Pattern PATTERN3 = Pattern.compile(REGEX3);
                final Matcher MAT3 = PATTERN3.matcher(inputTest4.get(i));
                boolean isRepeat = false;
                if (MAT3.find()) {
                    String manuID = MAT3.group();

                    stmnt = testChair7.createConnection.createStatement();
                    rs = stmnt.executeQuery("SELECT * FROM MANUFACTURER");

                    if (i > 0) {
                        for (int j = 0; j < repeats.size(); j++) {
                            if (manuID.equals(repeats.get(j))) {
                                isRepeat = true;
                                break;
                            }
                        }
                    }
                    if (isRepeat) {
                        continue;
                    }

                    while (rs.next()) {
                        if (rs.getString("ManuID").equals(manuID)) {
                            repeats.add(manuID);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            // Does nothing
        }
        ArrayList<String> expected = new ArrayList<>();
        expected.add("003");
        expected.add("005");
        assertEquals("The manufactueres are different", expected, repeats);
    }
}
