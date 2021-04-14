package edu.ucalgary.ensf409;

//Imports used for this testing file
import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilingTest {
    @Test
    // Passes this test
    public void constructorFilingTest() {
        Filing testFiling = new Filing("small", "filing", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        String[] expected = { "small", "filing", String.valueOf(1), "jdbc:mysql://localhost/inventory", "abhay",
                "ensf409" };
        String[] test = { testFiling.getCategory(), testFiling.getType(), String.valueOf(testFiling.getQuantity()),
                testFiling.getDBURL(), testFiling.getUSERNAME(), testFiling.getPASSWORD() };
        assertArrayEquals("The constructor was sucessful in initialzing the data memebers", expected, test);
    }

    @Test
    // Passes this test as well
    public void initializeConnectionTest() {
        Filing testFiling1 = new Filing("small", "filing", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        testFiling1.initializeConnection();
        ResultSet rs;
        String[] resultsTest = new String[5];
        Statement stmnt;
        try {
            stmnt = testFiling1.createConnection.createStatement();
            rs = stmnt.executeQuery("SELECT ID FROM  FILING  WHERE Type = 'small'");
            int i = 0;
            while (rs.next()) {
                resultsTest[i] = rs.getString("ID");
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] expected = { "F001", "F004", "F005", "F006", "F013" };

        assertArrayEquals("The connection was not working and the ID of the Filing was wrong", expected, resultsTest);
    }

    @Test
    // Passes this test as well
    public void getEverythingTest() {
        Filing testFiling2 = new Filing("small", "filing", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        testFiling2.initializeConnection();
        ResultSet rs;
        ArrayList<String> inputTest = new ArrayList<>();
        Statement stmnt;
        try {
            stmnt = testFiling2.createConnection.createStatement();
            rs = stmnt.executeQuery("SELECT * FROM FILING WHERE Type = " + "'" + testFiling2.getCategory() + "'");
            int i = 0;
            while (rs.next()) {
                inputTest.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
                i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<String> expected = new ArrayList<>();
        expected.add("50" + " " + "F001" + " " + "005");
        expected.add("75" + " " + "F004" + " " + "004");
        expected.add("75" + " " + "F005" + " " + "005");
        expected.add("50" + " " + "F006" + " " + "005");
        expected.add("50" + " " + "F013" + " " + "002");
        assertEquals("The input reterving based on the category was wrong", expected, inputTest);

    }

    @Test
    // Passes this test as well
    public void sortPriceTest() {
        Filing testFiling3 = new Filing("medium", "filing", 2, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        testFiling3.initializeConnection();
        ResultSet rs;
        ArrayList<String> inputTest1 = new ArrayList<>();
        Statement stmnt;
        try {
            stmnt = testFiling3.createConnection.createStatement();
            rs = stmnt.executeQuery("SELECT * FROM FILING WHERE Type = " + "'" + testFiling3.getCategory() + "'");
            int i = 0;
            while (rs.next()) {
                inputTest1.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
                i++;
            }
            testFiling3.sortPrice(inputTest1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<String> expected = new ArrayList<>();
        expected.add("50" + " " + "F008" + " " + "005");
        expected.add("100" + " " + "F002" + " " + "004");
        expected.add("100" + " " + "F009" + " " + "004");
        expected.add("150" + " " + "F007" + " " + "002");
        expected.add("200" + " " + "F014" + " " + "002");
        assertEquals("The sorting price method failed", expected, inputTest1);
    }

    @Test
    // Passes this test as well
    public void numberOfPartsTest() {
        Filing testFiling4 = new Filing("large", "filing", 3, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        int[] expected = { 3 };
        int[] test = { testFiling4.numberOfParts(testFiling4.getQuantity()) };
        assertArrayEquals("The numberOfParts did not get the correct max parts", expected, test);
    }

    @Test
    // Passes this test as well
    public void checkPriceAllTest() {
        Filing testFiling6 = new Filing("small", "filing", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        testFiling6.initializeConnection();
        ResultSet rs;
        ArrayList<String> inputTest3 = new ArrayList<>();
        Statement stmnt;
        try {
            stmnt = testFiling6.createConnection.createStatement();
            rs = stmnt.executeQuery("SELECT * FROM FILING WHERE Type = " + "'" + testFiling6.getCategory() + "'");
            int i = 0;
            while (rs.next()) {
                inputTest3.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
                i++;
            }
            testFiling6.sortPrice(inputTest3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int test = testFiling6.checkPriceAll(inputTest3);
        int expected = 100;
        assertEquals("The lowest price was not determined", expected, test);
    }

    @Test
    // Passes this test
    public void suggestedManufacturerTest() {
        Filing testFiling7 = new Filing("large", "filing", 3, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        testFiling7.initializeConnection();
        ResultSet rs;
        ArrayList<String> inputTest4 = new ArrayList<>();
        Statement stmnt;
        ArrayList<String> repeats = new ArrayList<>();

        try {
            stmnt = testFiling7.createConnection.createStatement();
            rs = stmnt.executeQuery("SELECT * FROM FILING WHERE Type = " + "'" + testFiling7.getCategory() + "'");
            int i = 0;
            while (rs.next()) {
                inputTest4.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
                i++;
            }
            testFiling7.sortPrice(inputTest4);
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

                    stmnt = testFiling7.createConnection.createStatement();
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
        expected.add("005");
        expected.add("004");
        expected.add("002");
        assertEquals("The manufactueres are different", expected, repeats);
    }

    @Test
    // Passes this test as well
    public void removePartsTest() {
        Filing testFiling8 = new Filing("small", "filing", 2, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        testFiling8.initializeConnection();
        ResultSet rs;
        ArrayList<String> inputTest5 = new ArrayList<>();
        Statement stmnt;
        ArrayList<String> partsOrdered = new ArrayList<>();

        try {
            stmnt = testFiling8.createConnection.createStatement();
            rs = stmnt.executeQuery("SELECT * FROM FILING WHERE Type = " + "'" + testFiling8.getCategory() + "'");
            int i = 0;
            while (rs.next()) {
                inputTest5.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
                i++;
            }
            testFiling8.sortPrice(inputTest5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < inputTest5.size(); i++) {
                stmnt = testFiling8.createConnection.createStatement();
                stmnt.executeUpdate("DELETE FROM FILING WHERE ID = " + "'" + inputTest5.get(i) + "'");
                stmnt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<String> expected = new ArrayList<>();
        assertEquals("The deletion was not sucessful", expected, partsOrdered);
    }

    @Test
    // Passes this test as well
    public void writeFileFilingOrderTest() throws IOException {
        String orginialOrderRequest = "small filing, 1";
        int priceTotal = 150;
        Filing testFiling9 = new Filing("small", "filing", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        boolean result = testFiling9.writeFileFilingOrder(orginialOrderRequest, priceTotal);
        boolean expected = true;
        assertEquals("File to create file: ", expected, result);
    }

    @Test
    // Passes this test as well
    public void writeFileSuggestedManuTest() throws IOException {
        Filing testFiling10 = new Filing("small", "filing", 7, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
        boolean result = testFiling10.writeFileSuggestedManu();
        boolean expected = true;
        assertEquals("File to create file: ", expected, result);
    }
}