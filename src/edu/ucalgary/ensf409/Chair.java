
//Package statement used for this class
package edu.ucalgary.ensf409;
//Import statements used
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.io.IOException;
import java.sql.*;
import java.io.*;

/**
 * Class for the furniture category of Chair this accounts for the cheapestPrice
 * Also the stores which hold this furniture piece it accounts for
 * Writes into the file as well and creates the output.txt for the order
 */
public class Chair {

    /////// The DATA MEMBERS:
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
    /**
     * Database user on whose behalf the connection will be made
     */
    public String USERNAME;
    /**
     * User's password for the ability to access the database
     */
    public String PASSWORD;
    /**
     * Category for the Chair which is the chair
     */
    private String category;
    /**
     * The type which includes mesh,executive,ergonomic,task, and kneeling
     */
    private String type;
    /**
     * The number of the furniture ordered by the user which is stored in here
     */
    private int quantity;
    /**
     * Stores the manufacturers for this specific part
     */
    public StringBuilder manufacturers = new StringBuilder();
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
    //Default Chair constructor:
    Chair() {
        //Does nothing
    }

  /**
   * Chair constructor takes in 6 parameters for creating the Chair and intializing it 
   */
    Chair(String category, String type, int quantity, String dburl, String username, String password) {
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
            //Uses the drivers which will create the connection 
            createConnection = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            //If not able to prints this message out 
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
        //Sorts the prices from lowest to highest when gathered 
        sortPrice(input);
        totalPrice = lowestPrice();
        //Checks if the parts are being taken in the order if not
        //The else case would write into the file the manufacturers 
        if (boughtParts) {
            removeParts();
            System.out.println("Look at the output.txt file for the full furniture order.");
            String originalRequest = getCategory() + " " + getType() + ", " + getQuantity();
            writeFileChairOrder(originalRequest, totalPrice); //Without manufacturers
        } else {
            //Calls this method which writes into the output file 
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
            ResultSet rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = " + "'" + category + "'");
            //This loop will end up getting all of the instances of the category and
            //Store them in this format
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
                //The regex to get the price from the input array 
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
    /**
     * 
     * @return Determines the lowestPrice takes in the factor of the kneelingprice
     * Which has a different method due to its requriements 
     * And has a returnPrice which is the lowest possible in
     * the combinations 
     */
    public int lowestPrice() {
        int returnPrice;
        //If statement to check for kneeling
        if (category.equals("kneeling")) {
            returnPrice = checkPriceKneeling(input);
        } else {
            returnPrice = checkPriceAll(input);
        }

        return returnPrice;
    }
    /**
     * 
     * @param input Takes in that input array made earlier in get everything 
     * @return Returns the lowest price possible in the combinations 
     * This category of chair only has legs and seats 
     * Uses regex to sepearte this arrayList 
     * Also has multiple different edge cases which
     * Is taken in account for when collecting
     * The combinations 
     */
    public int checkPriceKneeling(ArrayList<String> input) {
        //Data fields used in this method 
        int priceSum = 0;
        int maxParts = numberOfParts(quantity);
        int numOfLegs = 0;
        int numOfSeats = 0;
        int trigger = 0;
        //Keeps a track of N's in the database row 
        int noCounter = 0;
        boolean empty = false;
        //Big for loop to iterate through our inputs generated from the users
        //Orginal request 
        for (int i = 0; i < input.size(); i++) {
            //If it meets the requirement breaks out of the loop
            if (numOfLegs == maxParts && numOfSeats == maxParts) {
                break;
            }
            //Regex to grab the category 
            final String REGEX = "([A-Z])\\w+";
            //Regex for price 
            final String REGEX2 = "[0-9]+";
            final Pattern PATTERN = Pattern.compile(REGEX);
            final Pattern PATTERN2 = Pattern.compile(REGEX2);
            final Matcher MAT = PATTERN.matcher(input.get(i));
            final Matcher MAT2 = PATTERN2.matcher(input.get(i));

            //If there is a pattern found then it will go inside this statement 
            if (MAT.find() && MAT2.find()) {
                //Puts the idString and the price in their respective variables 
                String idString = MAT.group();
                int priceInt = Integer.parseInt(MAT2.group());
                try {
                    Statement stmnt2 = createConnection.createStatement();
                    ResultSet rs2 = stmnt2.executeQuery("SELECT * FROM CHAIR WHERE ID = " + "'" + idString + "'");
                    //Different cases which could occur during the database combinations 
                    //Collecting them 
                        while(rs2.next()) {
                        trigger = 0;
                        noCounter = 0;

                        if (rs2.getString("Legs").equals("N") && rs2.getString("Arms").equals("N")
                                && rs2.getString("Seat").equals("N") && rs2.getString("Cushion").equals("N")) {
                            empty = true;
                            break;
                        }
                        if (rs2.getString("Legs").equals("Y")) {
                            numOfLegs++;
                            if (numOfLegs > maxParts) {
                                numOfLegs--;
                                trigger += 1;
                            }
                        }
                        if (rs2.getString("Legs").equals("N")) {
                            noCounter++;
                        }
                        if (rs2.getString("Seat").equals("Y")) {
                            numOfSeats++;
                            if (numOfSeats > maxParts) {
                                numOfSeats--;
                                trigger += 1;
                            }
                        }
                        if (rs2.getString("Seat").equals("N")) {
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
                        if (numOfLegs > 0 || numOfSeats > 0) {
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
        //If no possible combination then sets the boolean 
        //To false and calls another method to wirte
        //The manufacturers 
        if (numOfLegs != maxParts || numOfSeats != maxParts) {
            boughtParts = false;
            suggestedManufacturer();
        }
        return priceSum;
    }
    /**
     * 
     * @param input Takes in that input array made earlier in get everything 
     * @return Returns the lowest price possible in the combinations 
     * This category of chair will check for which is not including
     * The kneeling and is for the task,mesh,executive, and
     * the ergonmoic 
     * Uses regex to sepearte this arrayList 
     * Also has multiple different edge cases which
     * Is taken in account for when collecting
     * The combinations 
     */

    public int checkPriceAll(ArrayList<String> input) {
        //Data fields used in this method 
        int priceSum = 0;
        //Uses the method to receive the the limit of parts
        int maxParts = numberOfParts(quantity);
        //These will keep a track of the amount 
        //of peices 
        int numOfLegs = 0;
        int numOfArms = 0;
        int numOfSeats = 0;
        int numOfCushions = 0;
        //Trigger for checking the cases which
        //Could occur in which the order is 
        //Not able to be completed 
        int trigger = 0;
        boolean isRepeat = false;
        boolean oneChair = false;
        boolean trigger1 = false;
        boolean trigger1Repeat = false;
        boolean trigger2 = false;
        boolean trigger2Repeat = false;
        boolean trigger3 = false;
        boolean trigger3Repeat = false;
        boolean trigger4 = false;
        boolean trigger4Repeat = false;
        //If there is already enough parts then this will go again 
        if (maxParts == 1) {
            oneChair = true;
        }
        //Keeps a track of N's in the database row 
        int noCounter = 0;
        boolean empty = false;
        //Big for loop to iterate through our inputs generated from the users
        //Orginal request 
        for (int i = 0; i < input.size(); i++) {

            if (numOfLegs == maxParts && numOfArms == maxParts && numOfSeats == maxParts && numOfCushions == maxParts) {
                break;
            }
            //Regex to grab the category 
            final String REGEX = "([A-Z])\\w+";
            //Regex for price 
            final String REGEX2 = "[0-9]+";
            final Pattern PATTERN = Pattern.compile(REGEX);
            final Pattern PATTERN2 = Pattern.compile(REGEX2);
            final Matcher MAT = PATTERN.matcher(input.get(i));
            final Matcher MAT2 = PATTERN2.matcher(input.get(i));

            //If there is a pattern found then it will go inside this statement 
            if (MAT.find() && MAT2.find()) {
                //Puts the idString and the price in their respective variables 
                String idString = MAT.group();
                int priceInt = Integer.parseInt(MAT2.group());
                try {
                    Statement stmnt2 = createConnection.createStatement();
                    ResultSet rs2 = stmnt2.executeQuery("SELECT * FROM CHAIR WHERE ID = " + "'" + idString + "'");
                     //Different cases which could occur during the database combinations 
                    //Collecting them 
                        while(rs2.next()) {
                        trigger = 0;
                        noCounter = 0;
                        isRepeat = false;
                        trigger1 = false;
                        trigger1Repeat = false;
                        trigger2 = false;
                        trigger2Repeat = false;
                        trigger3 = false;
                        trigger3Repeat = false;
                        trigger4 = false;
                        trigger4Repeat = false;

                        if (rs2.getString("Legs").equals("N") && rs2.getString("Arms").equals("N")
                                && rs2.getString("Seat").equals("N") && rs2.getString("Cushion").equals("N")) {
                            empty = true;
                            break;
                        }
                        //Checks for the chairs legs and what their status 
                        //Is in the database 
                        if (rs2.getString("Legs").equals("Y")) {
                            numOfLegs++;
                            trigger1 = true;
                            if (numOfLegs > maxParts) {
                                numOfLegs--;
                                trigger += 1;
                                isRepeat = true;
                                trigger1Repeat = true;
                            }
                        }
                        if (rs2.getString("Legs").equals("N")) {
                            noCounter++;
                        }
                          //Checks for the chairs arms and what their status 
                        //Is in the database 
                        if (rs2.getString("Arms").equals("Y")) {
                            numOfArms++;
                            trigger2 = true;
                            if (numOfArms > maxParts) {
                                numOfArms--;
                                trigger += 1;
                                isRepeat = true;
                                trigger2Repeat = true;
                            }
                        }
                        if (rs2.getString("Arms").equals("N")) {
                            noCounter++;
                        }
                            //Checks for the chairs seat and what their status 
                        //Is in the database 
                        if (rs2.getString("Seat").equals("Y")) {
                            numOfSeats++;
                            trigger3 = true;
                            if (numOfSeats > maxParts) {
                                numOfSeats--;
                                trigger += 1;
                                isRepeat = true;
                                trigger3Repeat = true;
                            }
                        }
                        if (rs2.getString("Seat").equals("N")) {
                            noCounter++;
                        }
                            //Checks for the chairs cushion and what their status 
                        //Is in the database 
                        if (rs2.getString("Cushion").equals("Y")) {
                            numOfCushions++;
                            trigger4 = true;
                            if (numOfCushions > maxParts) {
                                numOfCushions--;
                                trigger += 1;
                                isRepeat = true;
                                trigger4Repeat = true;
                            }
                        }
                        if (rs2.getString("Cushion").equals("N")) {
                            noCounter++;
                        }
                    }
                    
                    if (numOfLegs == 1 && numOfArms == 1 && numOfSeats == 1 && numOfCushions == 1) {
                    } else {
                        if (isRepeat && oneChair) {
                            if (trigger1) {
                                if (trigger1 && trigger1Repeat) {
                                } else {
                                    numOfLegs--;
                                }
                            }
                            if (trigger2) {
                                if (trigger2 && trigger2Repeat) {
                                } else {
                                    numOfArms--;
                                }
                            }
                            if (trigger3) {
                                if (trigger3 && trigger3Repeat) {
                                } else {
                                    numOfSeats--;
                                }
                            }
                            if (trigger4) {
                                if (trigger4 && trigger4Repeat) {
                                } else {
                                    numOfCushions--;
                                }
                            }
                            isRepeat = false;
                            trigger1 = false;
                            trigger2 = false;
                            trigger3 = false;
                            trigger4 = false;
                            trigger1Repeat = false;
                            trigger2Repeat = false;
                            trigger3Repeat = false;
                            trigger4Repeat = false;
                            continue;
                        }
                    }

                    if (empty) {
                        empty = false;
                        continue;
                    }
                    if (trigger == 4 || trigger + noCounter == 4) {
                        noCounter = 0;
                        trigger = 0;
                        continue;
                    } else {
                        if (numOfLegs > 0 || numOfArms > 0 || numOfSeats > 0 || numOfCushions > 0) {
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
        //If no possible combination then sets the boolean 
        //To false and calls another method to wirte
        //The manufacturers 
        if (numOfLegs != maxParts || numOfArms != maxParts || numOfSeats != maxParts || numOfCushions != maxParts) {
            boughtParts = false;
            suggestedManufacturer();
        }
        return priceSum;
    }
    /**
     * Will check for the manufactueres using their ID
     * Collected in the earlier input ArrayList 
     * And then checks if they are repeated in the order so 
     * They are not written twice in the output file 
     */
    public void suggestedManufacturer() {
        //Try and catch block for the the terminal printing 
        try {
            //This will get printed to the console before the manufacturers string 
            System.out.println("Order cannot be fulfilled based on current inventory.");
            System.out.print("Suggested Manufacturers can also be viewed in the output.txt file.");
            System.out.println("The suggested Manufacturers are: ");
            //For loop which will iterate through the input array originally 
            for (int i = 0; i < input.size(); i++) {
                //Regex for finding the manuID in the array
                final String REGEX3 = "([0-9]+$)";
                final Pattern PATTERN3 = Pattern.compile(REGEX3);
                final Matcher MAT3 = PATTERN3.matcher(input.get(i));
                //Boolean created to make sure it is not repeated 
                boolean isRepeat = false;
                //Uses the regex to get the manuID only 
                if (MAT3.find()) {
                    String manuID = MAT3.group();

                    Statement stmnt = createConnection.createStatement();
                    //Quert which will select everything from it 
                    rs = stmnt.executeQuery("SELECT * FROM MANUFACTURER");
                    //This going through the repeats array and if there 
                    //One seen then it will break out of this 
                    if (i > 0) {
                        for (int j = 0; j < repeats.size(); j++) {
                            if (manuID.equals(repeats.get(j))) {
                                isRepeat = true;
                                break;
                            }
                        }
                    }
                    //If there is a repeat then it will go again
                    if (isRepeat) {
                        continue;
                    }
                    //This will store the manuID in the String Builder
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
        //Prints the string builder out by converting it to a string
        System.out.println(manufacturers.toString());
    }
    /**
     * Deletion of the parts which have been ordered 
     * Uses the array generated in the checkPriceAll method 
     * Which then later on will update the database 
     * So we don't buy the same parts again and again
     */
    public void removeParts() {
        Statement stmnt;
        try {
            //For loop which will iterate through the arraylist of parts which
            //The user will buy in order to complete the order 
            for (int i = 0; i < partsOrdered.size(); i++) {
                stmnt = createConnection.createStatement();
                //Query similar to Assignment 9
                stmnt.executeUpdate("DELETE FROM CHAIR WHERE ID = "+ "'" + partsOrdered.get(i) + "'");
                stmnt.close();
            }
        }
        //If there is something gone wrong then this message will be printed to 
        //To prompt the user 
        catch (SQLException e) {
            System.out.println("Deleting the part was unsuccessful");
            e.printStackTrace();
        }
    }
    /**
     * This writeFile method is for order sucessful completetion and is only 
     * Called if the order requirements are met 
     * @param originalRequest The user request which inputed in the terminal is used here 
     * @param totalPrice Price which we ended up calculating based on our algorithm 
     * @return The boolean which checks if the file is created 
     * @throws IOException If the file is not created then throws this exception
     */
    public boolean writeFileChairOrder(String originalRequest, int totalPrice) throws IOException {
        try {
            //File name which will be created with 
            FileWriter fw = new FileWriter("orderform.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            //This writes into the file using the BufferedWriter
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
            //This will iterate through the parts which are being ordered 
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
    /**
     * Method if the order is not able to completed to write to the file 
     * And then this will use the String Builder and convert 
     * It to a proper string for the user to read 
     * @return The boolean which checks if the file is created
     * @throws IOException If the file is not created then throws this exception
     */
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
