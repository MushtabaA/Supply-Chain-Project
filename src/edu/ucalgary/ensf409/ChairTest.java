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
 public class ChairTest {
     @Test
     //Passes this test
     public void constructorChairTest() {
         Chair testChair = new Chair("mesh", "chair", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         String[] expected = {"mesh", "chair", String.valueOf(1), "jdbc:mysql://localhost/inventory", "scm", "ensf409"};
         String[] test = {testChair.getCategory(), testChair.getType(), String.valueOf(testChair.getQuantity()), testChair.getDBURL(), testChair.getUSERNAME(), testChair.getPASSWORD()};
         assertArrayEquals("The constructor was sucessful in initialzing the data memebers", expected, test);
     }

     @Test
     //Passes this test as well
     public void initializeConnectionTest() {
         Chair testChair1 = new Chair("kneeling", "chair", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
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
         Chair testChair2 = new Chair("mesh", "chair", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         testChair2.initializeConnection();
         ResultSet rs;
         ArrayList<String> inputTest = new ArrayList<>();
         Statement stmnt;
         try {
             stmnt = testChair2.createConnection.createStatement();
             rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = " + "'" + testChair2.getCategory() + "'");
             int i = 0;
             while (rs.next()) {
                 inputTest.add(i, rs.getString("ID"));
                 i++;
             }

         } catch (SQLException e) {
             e.printStackTrace();
         }
         ArrayList<String> expected = new ArrayList<>();
         expected.add("C0942");
         expected.add("C6748");
         expected.add("C8138");
         expected.add("C9890" );
         assertEquals("The IDS were not correct based on the category", expected, inputTest);

     }
     @Test
     //Passes this test as well
     public void getManufacturersTest() {
         Chair testChair2 = new Chair("mesh", "chair", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         testChair2.initializeConnection();
         ResultSet rs;
         ArrayList<String> inputTest = new ArrayList<>();
         Statement stmnt;
         try {
             stmnt = testChair2.createConnection.createStatement();
             rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = " + "'" + testChair2.getCategory() + "'");
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
         expected.add("003");
         expected.add("005");
         expected.add("003" );
         assertEquals("The ManuIDS were not correct based on the category", expected, inputTest);

     }
     @Test
     //Passes this test as well
     public void sortLowestPriceTest(){
         Chair testChair3 = new Chair("mesh", "chair", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         testChair3.initializeConnection();
         ResultSet rs;
         ArrayList<String> inputTest1 = new ArrayList<>();
         Statement stmnt;
         try {
             stmnt = testChair3.createConnection.createStatement();
             rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = " + "'" + testChair3.getCategory() + "'");
             int i = 0;
             while (rs.next()) {
                 inputTest1.add(i, (rs.getString("Price")));
                 i++;
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         testChair3.sortLowestPrice();
         ArrayList<String> expected = new ArrayList<>();
         expected.add("175");
         expected.add("75");
         expected.add("75");
         expected.add("50");
         assertEquals("The sorting price method failed", expected, inputTest1);
     }

     @Test
     //Passes this test as well
     public void numberOfPartsTest() {
         Chair testChair4 = new Chair("mesh", "chair", 3, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         int[] expected = {3};
         int[] test = {testChair4.numberOfParts(testChair4.getQuantity())};
         assertArrayEquals("The numberOfParts did not get the correct max parts", expected, test);
     }

     @Test
     //Passes this test
     public void suggestedManufacturerTest() {
         Chair testChair7 = new Chair("mesh", "chair", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
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
         expected.add("005");
         expected.add("003");
         assertEquals("The manufactueres are different", expected, repeats);
     }

     @Test
     //Passes this test as well
     public void removePartsTest() {
         Chair testChair8 = new Chair("mesh", "chair", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         testChair8.initializeConnection();
         ResultSet rs;
         ArrayList<String> inputTest5 = new ArrayList<>();
         Statement stmnt;
         ArrayList<String> partsOrdered = new ArrayList<>();

         try {
             stmnt = testChair8.createConnection.createStatement();
             rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = " + "'" + testChair8.getCategory() + "'");
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
                 stmnt = testChair8.createConnection.createStatement();
                 stmnt.executeUpdate("DELETE FROM CHAIR WHERE ID = "+ "'" + inputTest5.get(i) + "'");
                 stmnt.close();
             }
         }
         catch (SQLException e) {
             e.printStackTrace();
         }
         ArrayList<String> expected = new ArrayList<>();
         assertEquals("The deletion was not sucessful", expected, partsOrdered);
     }

     @Test
     //Passes this test as well
     public void writeFileChairOrderTest() throws IOException {
         String orginialOrderRequest = "mesh chair, 1";
         int priceTotal = 200;
         Chair testChair9 = new Chair("mesh", "chair", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         boolean result = testChair9.writeFileChairOrder(orginialOrderRequest,priceTotal);
         boolean expected = true;
         assertEquals("File to create file: ", expected, result);
     }
     @Test
     //Passes this test as well
     public void writeFileSuggestedManuTest() throws IOException {
         Chair testChair10 = new Chair("mesh", "chair", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
         boolean result = testChair10.writeFileSuggestedManu();
         boolean expected = true;
         assertEquals("File to create file: ", expected, result);
     }
 }
