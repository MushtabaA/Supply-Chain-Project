package edu.ucalgary.ensf409;

//Imports used for this testing file 
import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;
import java.sql.*;

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
    public void checkPriceTest() {

    }
}
