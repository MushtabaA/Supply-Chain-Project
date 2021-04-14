package edu.ucalgary.ensf409;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.io.IOException;
import java.sql.*;
import java.io.*;

public class Lamp {

    /////// DATA MEMBERS:
    /**
     * Creates the connection to the Database relating
     * towards the MySQL server
     */
    public Connection createConnection;

    /**
     * ResultSet which will hold all of the rows in which can
     * later be accessed by us
     */

    public ResultSet rs;

    /**
     * Database url of the following format jdbc:subprotocol:subname
     */
    public String DBURL;

    // store the database url information
    /**
     * Database user on whose behalf the connection will be made
     */
    public String USERNAME;

    // store the user's account username
    /**
     * User's password
     */
    public String PASSWORD;

    /**
     * Category for the Lamp which is the lamp
     */
    private String category;
    /**
     * The type which includes desk, study and swing arm
     */
    private String type;
    /**
     * The number of the furniture ordered by the user which is stored in here
     */
    private int quantity;
    /**
     * Stores the manufacturers for this specific part
     */
    private StringBuilder manufacturers = new StringBuilder();
    //This boolean will keep a track of the parts which have been bought
    private boolean boughtParts = true;
    //Checks if the file is being created or not for the unitTesting purposes
    private boolean fileStatus = false;
    /**
     * The array list which stores the database same categories 
     */
    static ArrayList<String> input = new ArrayList<>();
    /**
     * The ones which get removed from the database and later on 
     * for writing in the output file 
     */
    static ArrayList<String> partsOrdered = new ArrayList<>();
    /**
     * If there is any repeats of the manuIDs it will store them 
     * in here to make sure the same ID is not being 
     * written twice
     */
    static ArrayList<String> repeats = new ArrayList<>();
    /**
     * The totalPrice for the whole order is being stored in this int
     */
    int totalPrice;
    //Start of the getters and setters which get the public variables 
    public String getDBURL() {
        return this.DBURL;
    }

    public void setDBURL(String DBURL) {
        this.DBURL = DBURL;
    }

    public String getUSERNAME() {
        return this.USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return this.PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
    // store the user's account password

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    ////// METHODS:
    // Default Lamp CTOR:
    Lamp() {
        // Does nothing
    }

    /**
   * Lamp constructor takes in 6 parameters for creating the Lamp and intializing it 
   */
    Lamp(String category, String type, int quantity, String dburl, String username, String password) {
        this.category = category;
        this.type = type;
        this.quantity = quantity;
        this.DBURL = dburl;
        this.USERNAME = username;
        this.PASSWORD = password;
    }
    /**
     * Creates the connection using the Drivers which get added to the classpath and 
     * checks if the connection is being made or not 
     */
    public void initializeConnection() {
        try {
            createConnection = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection was failed try again.");
            e.printStackTrace();
        }
    }
    /**
     * 
     * @throws IOException throws the IOException if the writeFile methods
     * are unsucessful 
     * No parameters for this method
     * Calls most of the methods in the order which they fulfill the requirement 
     */

    public void callEverything() throws IOException {
        initializeConnection();
        getEverything(category);
        sortPrice(input);
        totalPrice = lowestPrice();
        if (boughtParts) {
            removeParts();
            System.out.println("Look at the output.txt file for the full furniture order.");
            String originalRequest = getCategory() + " " + getType() + ", " + getQuantity();
            writeFileLampOrder(originalRequest, totalPrice); // Without manufacturers
        } else {
            writeFileSuggestedManu();
        }
    }
    /**
     * 
     * @param category Takes in the category which the user requested for 
     * Then creates a query which can realte with the database to store the Price
     * and the ID in an ArrayList in that order respectively 
     * Also throws an exception if it failed in getting this done 
     */
    public void getEverything(String category) {
        Statement stmnt;
        try {
            stmnt = createConnection.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM LAMP WHERE Type = " + "'" + category + "'");

            while (rs.next()) {
                input.add(rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID"));
            }

            stmnt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param input Takes in this array list which was taken from the user in that specific 
     * format which we had desired in the getEverything method and then using regex
     * to get the price from this arrayList which then sorts it from lowest to highest
     * Using sorting algorithm called BubbleSort 
     */

    public void sortPrice(ArrayList<String> input) {
        for (int i = input.size(); i > 0; i--) {
            for (int j = 0; j < input.size() - 1; j++) {
                String REGEX = "[0-9]+";
                Pattern PATTERN = Pattern.compile(REGEX);
                Matcher MAT = PATTERN.matcher(input.get(j));
                Matcher MAT2 = PATTERN.matcher(input.get(j + 1));
                if (MAT.find() && MAT2.find()) {
                    if (Integer.parseInt(MAT.group()) > Integer.parseInt(MAT2.group())) {
                        String tmp = input.get(j);
                        String tmp2 = input.get(j + 1);
                        input.set(j, tmp2);
                        input.set(j + 1, tmp);
                    }
                }
            }
        }
    }

    ////////// New Method for numberOfParts
    /**
     * 
     * @param quantity Takes in the user desired furniture pieces  
     * @return Adds to maximum value which can be taken 
     */
    public int numberOfParts(int quantity) {
        int value = 0;
        value += quantity;
        return value;
    }

    ///////// New Method for checkPrice:
    /**
     * 
     * @return Determines the lowestPrice takes in the factor of the kneelingprice
     * Which has a different method due to its requriements 
     * And has a returnPrice which is the lowest possible in
     * the combinations 
     */

    public int lowestPrice() {
        return checkPriceAll(input);
    }
    /**
     * 
     * @param input Takes in that input array made earlier in get everything 
     * @return Returns the lowest price possible in the combinations 
     * This category of chair will check for which is not including
     * The kneeling and is for the desk, study and swing arm
     * Uses regex to sepearte this arrayList 
     * Also has multiple different edge cases which
     * Is taken in account for when collecting
     * The combinations 
     */

    public int checkPriceAll(ArrayList<String> input) {
        int priceSum = 0;
        int maxParts = numberOfParts(quantity);
        int numOfBases = 0;
        int numOfBulbs = 0;
        int trigger = 0;
        int noCounter = 0;
        boolean empty = false;

        for (int i = 0; i < input.size(); i++) {

            if (numOfBases == maxParts && numOfBulbs == maxParts) {
                break;
            }

            final String REGEX = "([A-Z])\\w+";
            final String REGEX2 = "[0-9]+";
            final Pattern PATTERN = Pattern.compile(REGEX);
            final Pattern PATTERN2 = Pattern.compile(REGEX2);
            final Matcher MAT = PATTERN.matcher(input.get(i));
            final Matcher MAT2 = PATTERN2.matcher(input.get(i));

            if (MAT.find() && MAT2.find()) {
                String idString = MAT.group();
                int priceInt = Integer.parseInt(MAT2.group());
                try {
                    Statement stmnt2 = createConnection.createStatement();
                    ResultSet rs2 = stmnt2.executeQuery("SELECT * FROM LAMP WHERE ID = " + "'" + idString + "'");

                    while (rs2.next()) {
                    trigger = 0;
                    noCounter = 0;

                        if (rs2.getString("Base").equals("N") && rs2.getString("Bulb").equals("N")) {
                            empty = true;
                            break;
                        }
                        if (rs2.getString("Base").equals("Y")) {
                            numOfBases++;
                            if (numOfBases > maxParts) {
                                numOfBases--;
                                trigger += 1;
                            }
                        }
                        if (rs2.getString("Base").equals("N")) {
                            noCounter++;
                        }
                        if (rs2.getString("Bulb").equals("Y")) {
                            numOfBulbs++;
                            if (numOfBulbs > maxParts) {
                                numOfBulbs--;
                                trigger += 1;
                            }
                        }
                        if (rs2.getString("Bulb").equals("N")) {
                            noCounter++;
                        }
                    }
                    if (empty) {
                        empty = false;
                        continue;
                    }
                    if (trigger == 2 || trigger + noCounter == 2) {
                        noCounter = 0;
                        trigger = 0;
                        continue;
                    } else {
                        if (numOfBases > 0 || numOfBulbs > 0) {
                            priceSum += priceInt;
                            partsOrdered.add(idString);
                        }
                    }
                    stmnt2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (numOfBases != maxParts || numOfBulbs != maxParts) {
            boughtParts = false;
            suggestedManufacturer();
        }
        return priceSum;
    }

    public void suggestedManufacturer() {

        try {
            System.out.println("Order cannot be fulfilled based on current inventory.");
            System.out.println("Suggested Manufacturers can also be viewed in the output.txt file.");
            System.out.println("The suggested Manufacturers are: ");
            for (int i = 0; i < input.size(); i++) {
                final String REGEX3 = "([0-9]+$)";
                final Pattern PATTERN3 = Pattern.compile(REGEX3);
                final Matcher MAT3 = PATTERN3.matcher(input.get(i));
                boolean isRepeat = false;
                if (MAT3.find()) {
                    String manuID = MAT3.group();

                    Statement stmnt = createConnection.createStatement();
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
                            manufacturers.append("Name: " + rs.getString("Name") + "\n");
                            repeats.add(manuID);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            // Does nothing
        }
        System.out.println(manufacturers.toString());

    }

    public void removeParts() {
        Statement stmnt;
        try {
            for (int i = 0; i < partsOrdered.size(); i++) {
                stmnt = createConnection.createStatement();
                stmnt.executeUpdate("DELETE FROM LAMP WHERE ID = " + "'" + partsOrdered.get(i) + "'");
                stmnt.close();
            }
        }

        catch (SQLException e) {
            System.out.println("Deleting the part was unsucessful");
            e.printStackTrace();
        }
    }

    public boolean writeFileLampOrder(String originalRequest, int totalPrice) throws IOException {
        try {
            FileWriter fw = new FileWriter("output.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Furniture Order Form");
            bw.write('\n');
            bw.write("Faculty Name: ");
            bw.write('\n');
            bw.write("Contact: ");
            bw.write('\n');
            bw.write("Date: ");
            bw.write('\n');
            bw.write("Original Request: " + originalRequest);
            bw.write('\n');
            bw.write("Items Ordered" + "\n");
            for (int i = 0; i < partsOrdered.size(); i++) {
                bw.write("ID: " + partsOrdered.get(i) + "\n");
            }
            bw.write("Total Price: " + "$" + totalPrice);
            // Close the BufferedWriter Object and FileWriter object
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println("Failed to write to the output file");
        }
        return fileStatus = true;
    }

    public boolean writeFileSuggestedManu() throws IOException {
        try {
            FileWriter fw = new FileWriter("output.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Order cannot be fulfilled based on current inventory.");
            bw.write('\n');
            bw.write("Suggested Manufacturers:");
            bw.write('\n');
            bw.write(manufacturers.toString());
            // Close the BufferedWriter Object and FileWriter object
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println("Failed to write to the output file");
        }
        return fileStatus = true;
    }

}
