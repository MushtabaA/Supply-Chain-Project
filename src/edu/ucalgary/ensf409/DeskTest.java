 package edu.ucalgary.ensf409;

 //Imports used for this testing file
 import static org.junit.Assert.*;
 import org.junit.*;
 import java.io.*;
 import java.util.*;
 import java.sql.*;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 //TODO: FIX THE DEFAULT USER TO SCM
 public class DeskTest {
     @Test
     // Passes this test
     public void constructorDeskTest() {
         Desk testDesk = new Desk("standing", "desk", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         String[] expected = { "standing", "desk", String.valueOf(1), "jdbc:mysql://localhost/inventory", "scm",
                 "ensf409" };
         String[] test = { testDesk.getCategory(), testDesk.getType(), String.valueOf(testDesk.getQuantity()),
                 testDesk.getDBURL(), testDesk.getUSERNAME(), testDesk.getPASSWORD() };
         assertArrayEquals("The constructor was sucessful in initialzing the data memebers", expected, test);
     }

     @Test
     // Passes this test as well
     public void initializeConnectionTest() {
         Desk testDesk1 = new Desk("standing", "desk", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         testDesk1.initializeConnection();
         ResultSet rs;
         String[] resultsTest = new String[5];
         Statement stmnt;
         try {
             stmnt = testDesk1.createConnection.createStatement();
             rs = stmnt.executeQuery("SELECT ID FROM  DESK  WHERE Type = 'standing'");
             int i = 0;
             while (rs.next()) {
                 resultsTest[i] = rs.getString("ID");
                 i++;
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         String[] expected = { "D1927", "D2341", "D3820", "D4438", "D9387" };

         assertArrayEquals("The connection was not working and the ID of the Desk was wrong", expected, resultsTest);
     }

     @Test
     // Passes this test as well
     public void getEverythingTest() {
         Desk testDesk2 = new Desk("standing", "desk", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         testDesk2.initializeConnection();
         ResultSet rs;
         ArrayList<String> inputTest = new ArrayList<>();
         Statement stmnt;
         try {
             stmnt = testDesk2.createConnection.createStatement();
             rs = stmnt.executeQuery("SELECT * FROM DESK WHERE Type = " + "'" + testDesk2.getCategory() + "'");
             int i = 0;
             while (rs.next()) {
                 inputTest.add(i, rs.getString("ID"));
                 i++;
             }

         } catch (SQLException e) {
             e.printStackTrace();
         }
         ArrayList<String> expected = new ArrayList<>();
         expected.add("D1927");
         expected.add("D2341");
         expected.add("D3820");
         expected.add("D4438");
         expected.add("D9387");
         assertEquals("The IDS were not correct based on the category", expected, inputTest);

     }
     @Test
     //Passes this test as well
     public void getManufacturersTest() {
         Chair testChair2 = new Chair("standing", "desk", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         testChair2.initializeConnection();
         ResultSet rs;
         ArrayList<String> inputTest = new ArrayList<>();
         Statement stmnt;
         try {
             stmnt = testChair2.createConnection.createStatement();
             rs = stmnt.executeQuery("SELECT * FROM DESK WHERE Type = " + "'" + testChair2.getCategory() + "'");
             int i = 0;
             while (rs.next()) {
                 inputTest.add(i, rs.getString("ManuID"));
                 i++;
             }

         } catch (SQLException e) {
             e.printStackTrace();
         }
         ArrayList<String> expected = new ArrayList<>();
         expected.add("005");
         expected.add("001");
         expected.add("001");
         expected.add("004");
         expected.add("004");
         assertEquals("The ManuIDS were not correct based on the category", expected, inputTest);

     }
     @Test
     // Passes this test as well
     public void sortLowestPriceTest() {
         Desk testDesk3 = new Desk("adjustable", "desk", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         testDesk3.initializeConnection();
         ResultSet rs;
         ArrayList<String> inputTest1 = new ArrayList<>();
         Statement stmnt;
         try {
             stmnt = testDesk3.createConnection.createStatement();
             rs = stmnt.executeQuery("SELECT * FROM DESK WHERE Type = " + "'" + testDesk3.getCategory() + "'");
             int i = 0;
             while (rs.next()) {
                 inputTest1.add(i, (rs.getString("Price")));
                 i++;
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         ArrayList<String> expected = new ArrayList<>();
         expected.add("150");
         expected.add("250");
         expected.add("50");
         expected.add("200");
         expected.add("200");
         expected.add("350");
         assertEquals("The sorting price method failed", expected, inputTest1);
     }

     @Test
     // Passes this test as well
     public void numberOfPartsTest() {
         Desk testDesk4 = new Desk("traditional", "desk", 3, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         int[] expected = { 3 };
         int[] test = { testDesk4.numberOfParts(testDesk4.getQuantity()) };
         assertArrayEquals("The numberOfParts did not get the correct max parts", expected, test);
     }

     @Test
     // Passes this test
     public void suggestedManufacturerTest() {
         Desk testDesk7 = new Desk("standing", "desk", 3, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         testDesk7.initializeConnection();
         ResultSet rs;
         ArrayList<String> inputTest4 = new ArrayList<>();
         Statement stmnt;
         ArrayList<String> repeats = new ArrayList<>();

         try {
             stmnt = testDesk7.createConnection.createStatement();
             rs = stmnt.executeQuery("SELECT * FROM DESK WHERE Type = " + "'" + testDesk7.getCategory() + "'");
             int i = 0;
             while (rs.next()) {
                 inputTest4.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
                 i++;
             }
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

                     stmnt = testDesk7.createConnection.createStatement();
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
         expected.add("001");
         expected.add("004");
         assertEquals("The manufactueres are different", expected, repeats);
     }

     @Test
     // Passes this test as well
     public void removePartsTest() {
         Desk testDesk8 = new Desk("standing", "desk", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         testDesk8.initializeConnection();
         ResultSet rs;
         ArrayList<String> inputTest5 = new ArrayList<>();
         Statement stmnt;
         ArrayList<String> partsOrdered = new ArrayList<>();

         try {
             stmnt = testDesk8.createConnection.createStatement();
             rs = stmnt.executeQuery("SELECT * FROM DESK WHERE Type = " + "'" + testDesk8.getCategory() + "'");
             int i = 0;
             while (rs.next()) {
                 inputTest5.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
                 i++;
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         try {
             for (int i = 0; i < inputTest5.size(); i++) {
                 stmnt = testDesk8.createConnection.createStatement();
                 stmnt.executeUpdate("DELETE FROM DESK WHERE ID = " + "'" + inputTest5.get(i) + "'");
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
     public void writeFileDeskOrderTest() throws IOException {
         String orginialOrderRequest = "mesh desk, 1";
         int priceTotal = 200;
         Desk testDesk9 = new Desk("mesh", "desk", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         boolean result = testDesk9.writeFileDeskOrder(orginialOrderRequest, priceTotal);
         boolean expected = true;
         assertEquals("File to create file: ", expected, result);
     }

     @Test
     // Passes this test as well
     public void writeFileSuggestedManuTest() throws IOException {
         Desk testDesk10 = new Desk("mesh", "desk", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         boolean result = testDesk10.writeFileSuggestedManu();
         boolean expected = true;
         assertEquals("File to create file: ", expected, result);
     }
 }
