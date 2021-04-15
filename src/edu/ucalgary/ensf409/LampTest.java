// package edu.ucalgary.ensf409;

// //Imports used for this testing file
// import static org.junit.Assert.*;
// import org.junit.*;
// import java.io.*;
// import java.util.*;
// import java.sql.*;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;
// //TODO: FIX THE DEFAULT USER TO SCM
// public class LampTest {
//     @Test
//     // Passes this test
//     public void constructorLampTest() {
//         Lamp testLamp = new Lamp("study", "lamp", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
//         String[] expected = { "study", "lamp", String.valueOf(1), "jdbc:mysql://localhost/inventory", "abhay",
//                 "ensf409" };
//         String[] test = { testLamp.getCategory(), testLamp.getType(), String.valueOf(testLamp.getQuantity()),
//                 testLamp.getDBURL(), testLamp.getUSERNAME(), testLamp.getPASSWORD() };
//         assertArrayEquals("The constructor was sucessful in initialzing the data memebers", expected, test);
//     }

//     @Test
//     // Passes this test as well
//     public void initializeConnectionTest() {
//         Lamp testLamp1 = new Lamp("study", "lamp", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
//         testLamp1.initializeConnection();
//         ResultSet rs;
//         String[] resultsTest = new String[4];
//         Statement stmnt;
//         try {
//             stmnt = testLamp1.createConnection.createStatement();
//             rs = stmnt.executeQuery("SELECT ID FROM  LAMP  WHERE Type = 'study'");
//             int i = 0;
//             while (rs.next()) {
//                 resultsTest[i] = rs.getString("ID");
//                 i++;
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         String[] expected = { "L223", "L928", "L980", "L982" };

//         assertArrayEquals("The connection was not working and the ID of the Filing was wrong", expected, resultsTest);
//     }

//     @Test
//     // Passes this test as well
//     public void getEverythingTest() {
//         Lamp testLamp2 = new Lamp("study", "lamp", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
//         testLamp2.initializeConnection();
//         ResultSet rs;
//         ArrayList<String> inputTest = new ArrayList<>();
//         Statement stmnt;
//         try {
//             stmnt = testLamp2.createConnection.createStatement();
//             rs = stmnt.executeQuery("SELECT * FROM LAMP WHERE Type = " + "'" + testLamp2.getCategory() + "'");
//             int i = 0;
//             while (rs.next()) {
//                 inputTest.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
//                 i++;
//             }

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         ArrayList<String> expected = new ArrayList<>();
//         expected.add("2" + " " + "L223" + " " + "005");
//         expected.add("10" + " " + "L928" + " " + "002");
//         expected.add("2" + " " + "L980" + " " + "004");
//         expected.add("8" + " " + "L982" + " " + "002");
//         assertEquals("The input reterving based on the category was wrong", expected, inputTest);

//     }

//     @Test
//     // Passes this test as well
//     public void sortPriceTest() {
//         Lamp testLamp3 = new Lamp("study", "lamp", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
//         testLamp3.initializeConnection();
//         ResultSet rs;
//         ArrayList<String> inputTest1 = new ArrayList<>();
//         Statement stmnt;
//         try {
//             stmnt = testLamp3.createConnection.createStatement();
//             rs = stmnt.executeQuery("SELECT * FROM LAMP WHERE Type = " + "'" + testLamp3.getCategory() + "'");
//             int i = 0;
//             while (rs.next()) {
//                 inputTest1.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
//                 i++;
//             }
//             testLamp3.sortPrice(inputTest1);
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         ArrayList<String> expected = new ArrayList<>();
//         expected.add("2" + " " + "L223" + " " + "005");
//         expected.add("2" + " " + "L980" + " " + "004");
//         expected.add("8" + " " + "L982" + " " + "002");
//         expected.add("10" + " " + "L928" + " " + "002");
//         assertEquals("The sorting price method failed", expected, inputTest1);
//     }

//     @Test
//     // Passes this test as well
//     public void numberOfPartsTest() {
//         Lamp testLamp4 = new Lamp("swing arm", "lamp", 3, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
//         int[] expected = { 3 };
//         int[] test = { testLamp4.numberOfParts(testLamp4.getQuantity()) };
//         assertArrayEquals("The numberOfParts did not get the correct max parts", expected, test);
//     }

//     @Test
//     // Passes this test as well
//     public void checkPriceAllTest() {
//         Lamp testLamp6 = new Lamp("study", "lamp", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
//         testLamp6.initializeConnection();
//         ResultSet rs;
//         ArrayList<String> inputTest3 = new ArrayList<>();
//         Statement stmnt;
//         try {
//             stmnt = testLamp6.createConnection.createStatement();
//             rs = stmnt.executeQuery("SELECT * FROM LAMP WHERE Type = " + "'" + testLamp6.getCategory() + "'");
//             int i = 0;
//             while (rs.next()) {
//                 inputTest3.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
//                 i++;
//             }
//             testLamp6.sortPrice(inputTest3);
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         int test = testLamp6.checkPriceAll(inputTest3);
//         int expected = 12;
//         assertEquals("The lowest price was not determined", expected, test);
//     }

//     @Test
//     // Passes this test
//     public void suggestedManufacturerTest() {
//         Lamp testLamp7 = new Lamp("desk", "lamp", 3, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
//         testLamp7.initializeConnection();
//         ResultSet rs;
//         ArrayList<String> inputTest4 = new ArrayList<>();
//         Statement stmnt;
//         ArrayList<String> repeats = new ArrayList<>();

//         try {
//             stmnt = testLamp7.createConnection.createStatement();
//             rs = stmnt.executeQuery("SELECT * FROM LAMP WHERE Type = " + "'" + testLamp7.getCategory() + "'");
//             int i = 0;
//             while (rs.next()) {
//                 inputTest4.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
//                 i++;
//             }
//             testLamp7.sortPrice(inputTest4);
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         try {
//             for (int i = 0; i < inputTest4.size(); i++) {
//                 final String REGEX3 = "([0-9]+$)";
//                 final Pattern PATTERN3 = Pattern.compile(REGEX3);
//                 final Matcher MAT3 = PATTERN3.matcher(inputTest4.get(i));
//                 boolean isRepeat = false;
//                 if (MAT3.find()) {
//                     String manuID = MAT3.group();

//                     stmnt = testLamp7.createConnection.createStatement();
//                     rs = stmnt.executeQuery("SELECT * FROM MANUFACTURER");

//                     if (i > 0) {
//                         for (int j = 0; j < repeats.size(); j++) {
//                             if (manuID.equals(repeats.get(j))) {
//                                 isRepeat = true;
//                                 break;
//                             }
//                         }
//                     }
//                     if (isRepeat) {
//                         continue;
//                     }

//                     while (rs.next()) {
//                         if (rs.getString("ManuID").equals(manuID)) {
//                             repeats.add(manuID);
//                         }
//                     }
//                 }
//             }
//         } catch (SQLException e) {
//             // Does nothing
//         }
//         ArrayList<String> expected = new ArrayList<>();
//         expected.add("005");
//         expected.add("002");
//         expected.add("004");
//         assertEquals("The manufactueres are different", expected, repeats);
//     }

//     @Test
//     // Passes this test as well
//     public void removePartsTest() {
//         Lamp testLamp8 = new Lamp("study", "lamp", 2, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
//         testLamp8.initializeConnection();
//         ResultSet rs;
//         ArrayList<String> inputTest5 = new ArrayList<>();
//         Statement stmnt;
//         ArrayList<String> partsOrdered = new ArrayList<>();

//         try {
//             stmnt = testLamp8.createConnection.createStatement();
//             rs = stmnt.executeQuery("SELECT * FROM LAMP WHERE Type = " + "'" + testLamp8.getCategory() + "'");
//             int i = 0;
//             while (rs.next()) {
//                 inputTest5.add(i, (rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID")));
//                 i++;
//             }
//             testLamp8.sortPrice(inputTest5);
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         try {
//             for (int i = 0; i < inputTest5.size(); i++) {
//                 stmnt = testLamp8.createConnection.createStatement();
//                 stmnt.executeUpdate("DELETE FROM LAMP WHERE ID = " + "'" + inputTest5.get(i) + "'");
//                 stmnt.close();
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         ArrayList<String> expected = new ArrayList<>();
//         assertEquals("The deletion was not sucessful", expected, partsOrdered);
//     }

//     @Test
//     // Passes this test as well
//     public void writeFileLampOrderTest() throws IOException {
//         String orginialOrderRequest = "study lamp, 1";
//         int priceTotal = 150;
//         Lamp testLamp9 = new Lamp("study", "lamp", 1, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
//         boolean result = testLamp9.writeFileLampOrder(orginialOrderRequest, priceTotal);
//         boolean expected = true;
//         assertEquals("File to create file: ", expected, result);
//     }

//     @Test
//     // Passes this test as well
//     public void writeFileSuggestedManuTest() throws IOException {
//         Lamp testLamp10 = new Lamp("study", "lamp", 7, "jdbc:mysql://localhost/inventory", "abhay", "ensf409");
//         boolean result = testLamp10.writeFileSuggestedManu();
//         boolean expected = true;
//         assertEquals("File to create file: ", expected, result);
//     }
// }