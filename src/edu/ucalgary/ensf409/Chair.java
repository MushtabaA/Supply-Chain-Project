/**
 * Name: Mushtaba, Abhay, Parbir
 * mushtaba.alyasseen@ucalgary.ca
 * UCID: 30094000
 * abhay.khosla1@ucalgary.ca
 * UCID: 30085789
 * parbir.lehal@ucalgary.ca
 * UCID: 30096001
 */
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
 * Writes into the file as well and creates the orderform.txt for the order
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

    //Checks if the file is being created or not for the unitTesting purposes
    private boolean fileStatus = false;

    /**
     * The array list which stores the database same categories 
     */
    static ArrayList<String> input = new ArrayList<>();
    
    /**
     * If there is any repeats of the manuIDs it will store them 
     * in here to make sure the same ID is not being 
     * written twice
     */
    static ArrayList<String> repeats = new ArrayList<>();
    
    /**
     * The totalPrice for the whole order is being stored in this int
     */
    private int totalPrice;

    /**
     * Possible combinations which are found recursively and stored in this arraylist
     */
     static ArrayList<ArrayList<String>> possibleCombinations = new ArrayList<ArrayList<String>>();

    /**
     * Which are checked to have worked with our algorithm
     */
     static ArrayList<ArrayList<String>> confirmedCombinations = new ArrayList<ArrayList<String>>();

    /**
     * Prices arraylist which will store of the combinations 
     */
    static ArrayList<Integer> prices = new ArrayList<>();

    /**
     * To see if there has been found 
     */
    private boolean combinationFound = false;
    /**
     * ManufacturerID for storing the ids, which will be later 
     * Used for writing into the file 
     */
    ArrayList<String> manufacturerIDs = new ArrayList<>();


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
    public void initializeConnection() { //Abhay
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
    public void callEverything() throws IOException { //Abhay
        initializeConnection();
        getEverything(category);
        getManufacturers(category);
        getCombinations(input, input.size());
        if (category.equals("kneeling")) {
            findingCombinationsKneeling();
        } else {
            findingCombinations();
        }
        //Checks if the parts are being taken in the order if not
        //The else case would write into the file the manufacturers 
        if (combinationFound) {
            //Sorts the prices from lowest to highest when gathered 
            sortLowestPrice();
            totalPrice = getLowestPrice();
            removeParts();
            System.out.println("Look at the orderform.txt file for the full furniture order.");
            String originalRequest = getCategory() + " " + getType() + ", " + getQuantity();
            writeFileChairOrder(originalRequest, totalPrice); //Without manufacturers
        } else {
            //Calls this method which writes into the orderform file 
            suggestedManufacturer();
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
    public void getEverything(String category) { //Abhay
        Statement stmnt;
        try {
            stmnt = createConnection.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = " + "'" + category + "'");
            //This loop will end up getting all of the instances of the category and
            //Store them in this format
            while (rs.next()) {
                input.add(rs.getString("ID"));
            }

            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Is adding to the arraylist of the manufactueresID which will keep
     * a track of ManuIDs which are being used to be displayed 
     * If the order is unsucessul due to the inventory 
     * @param category Takes in the category which the user passed into
     * their original request helps with accessing the database 
     */
    public void getManufacturers(String category) { //Abhay
        Statement stmnt;
        try {
            //Makes a connection via a statement 
            stmnt = createConnection.createStatement();
            //Uses the result set to gather data about the chair which is for the specfic
            //Category as requested by the user 
            ResultSet rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = " + "'" + category + "'");
            //This loop will end up getting all of the instances of the category and
            //Store them in this format
            while (rs.next()) {
                //Adds to that arrayList of the manuIDs 
                manufacturerIDs.add(rs.getString("ManuID"));
            }

            stmnt.close();
            //If something goes wrong then will catch it while accessing the database
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     *.The input array copies the size of the of the parts we have in order to make 
     * for example a mesh chair. Then it will just add 0s in the array list. 
     * The posssible combinations will be the list of list which will have the ones 
     * which are find from the combinations method which looks for by using 
     * a mathematical approach of a famous leetcode problem subsets of sets 
     * to derive a recursion solution which implements dividing the problem 
     * into smaller parts. 
     * @param currentCombinations This arraylist is being passed to keep a track of the 
     * combinations which can be possibly made from the inventory we receive 
     * @param size this is the amount of inventory which is getting into from
     * the database the amount of parts we were able to select from the idString 
     */
    static void getCombinations(ArrayList<String> currentCombinations, int size) {
        //The input array which will have the same size as the 
        //Currentcombinations
        ArrayList<String> input = new ArrayList<>();
        int i = 0;
        //Fills them in with 0s as strings 
        while(i < size) {
            input.add("0");
            i++;
        }
        //Counter to keep a track of the recursion base case and will
        //Stop if it fails this requirement 
        int counter = 0;
        while(counter < currentCombinations.size()) {
            combinations(currentCombinations, input, 0, size - 1, 0, counter);
            counter++;
        }
        possibleCombinations.add(currentCombinations);
    }

    /**
     * This method for combinations is using the another temp array list to find the combination and set 
     * it to the input array which is passed into this method 
     * @param currentCombinations This arraylist is being passed to keep a track of the 
     * combinations which can be possibly made from the inventory we receive 
     * @param input This is the input which is getting passed from the getCombination method which was the copied array
     * @param start The begining of the arraylist which is at 0
     * @param end This is the near last of the arraylist which gets passed as size - 1 in the getCombination to avoid 
     * IndexOutOfBounds 
     * @param index The current position of the array list is keep a track same as the start for right now 
     * @param counter The counter which will be compared 
     */
    static void combinations(ArrayList<String> currentCombinations, ArrayList<String> input, int start, int end, int index, int counter) {
        if (index == counter)
        {
            int j = 0;
            ArrayList<String> combinationsInner = new ArrayList<>();
            while(j < counter) {
                combinationsInner.add(input.get(j));
                j++;
            }
            possibleCombinations.add(combinationsInner);
        }

        for (int i = start; i <= end; i++)
        {
            String tmp = currentCombinations.get(i);
            input.set(index, tmp);
            combinations(currentCombinations, input, i + 1, end, index + 1, counter);
        }
    }
    
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
     * Takes in no parameters and used another confirmedCombinations arraylist which is used after checking
     * through the possible combinations and adding them to the partsIDs as well. 
     * Also it has the priceSum which will keep a track of price of the each confirmed 
     * combination which was found from the select chair table. 
     */
    public void findingCombinations() {
        //The number of maxParts which will be getting used from the method 
        // of numberOfParts which the user put the quanity in. 
        int maxParts = numberOfParts(quantity);
        //The amount of each furniture piece to make one chair 
        int numOfLegs = 0;
        int numOfArms = 0;
        int numOfSeats = 0;
        int numOfCushions = 0;
        //The sum of the combination 
        int priceSum = 0;
        //The combinations which were received from the method earlier 
        //This now will do more testing to ensure we have the best 
        //Combination desired for the lowest price 
        for (int a = 0; a < possibleCombinations.size(); a++) {
            //Will keep a track of the parts in the combination 
            ArrayList<String> partIDs = new ArrayList<>();
            //Amount of the legs, arms, seats and cushions which
            //Are later on incremented 
            numOfLegs = 0;
            numOfArms = 0;
            numOfSeats = 0;
            numOfCushions = 0;
            priceSum = 0;
              //Takes the specific combination and adds all of the parts ID numbers 
              //To that array last to be accessed by the database later 
            for (int j = 0; j < possibleCombinations.get(a).size(); j++) {
                partIDs.add(possibleCombinations.get(a).get(j));
            }
            //Now this will traverse through the partsID to get them 
            for (int i = 0; i < partIDs.size(); i++) {
                String idString = partIDs.get(i);

                try {
                    //Statement to make a new connection to the database 
                    Statement stmnt2 = createConnection.createStatement();
                    //The result set which will look at the rows to gather data 
                    ResultSet rs2 = stmnt2.executeQuery("SELECT * FROM CHAIR WHERE ID = " + "'" + idString + "'");
                    //Will simply now just check for Yes in that one part row and then increment them. 
                    while(rs2.next()) {
                         //Checks for the chairs legs and what their status 
                        //Is in the database 
                        if (rs2.getString("Legs").equals("Y")) {
                            numOfLegs++;
                        }
                         //Checks for the chairs arms and what their status 
                        //Is in the database 
                        if (rs2.getString("Arms").equals("Y")) {
                            numOfArms++;
                        }
                         //Checks for the chairs seat and what their status 
                        //Is in the database 
                        if (rs2.getString("Seat").equals("Y")) {
                            numOfSeats++;
                        }
                         //Checks for the chairs cushion and what their status 
                        //Is in the database 
                        if (rs2.getString("Cushion").equals("Y")) {
                            numOfCushions++;
                        }
                        //Increments the sum by looking at the price for the part 
                        priceSum += rs2.getInt("Price");
                    }
                    //WIll catch the exception if something goes wrong 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            //Checks if the legs equal the maxParts or not 
            //If it is sucessfull it will add to the confirmedCombinations arraylist to be 
            //Used by the writeFile method 
            //Also adds the prices and sets the boolean to true as well. 
            if (numOfLegs >= maxParts && numOfArms >= maxParts && numOfSeats >= maxParts && numOfCushions >= maxParts) {
                confirmedCombinations.add(partIDs);
                prices.add(priceSum);
                combinationFound = true;
            }
        }
        
    }

    /**
     * Takes in no parameters and used another confirmedCombinations arraylist which is used after checking
     * through the possible combinations and adding them to the partsIDs as well. 
     * Also it has the priceSum which will keep a track of price of the each confirmed 
     * combination which was found from the select chair table. 
     */
    public void findingCombinationsKneeling() {
        //The number of maxParts which will be getting used from the method 
        // of numberOfParts which the user put the quanity in. 
        int maxParts = numberOfParts(quantity);
        //The amount of each furniture piece to make one chair 
        int numOfLegs = 0;
        int numOfSeats = 0;
        //The sum of the combination 
        int priceSum = 0;
        //The combinations which were received from the method earlier 
        //This now will do more testing to ensure we have the best 
        //Combination desired for the lowest price 
        for (int a = 0; a < possibleCombinations.size(); a++) {
            //Will keep a track of the parts in the combination 
            ArrayList<String> partIDs = new ArrayList<>();
            //Amount of the legs, arms, seats and cushions which
            //Are later on incremented 
            numOfLegs = 0;
            numOfSeats = 0;
            priceSum = 0;
              //Takes the specific combination and adds all of the parts ID numbers 
              //To that array last to be accessed by the database later 
            for (int j = 0; j < possibleCombinations.get(a).size(); j++) {
                partIDs.add(possibleCombinations.get(a).get(j));
            }
            //Now this will traverse through the partsID to get them 
            for (int i = 0; i < partIDs.size(); i++) {
                String idString = partIDs.get(i);

                try {
                    //Statement to make a new connection to the database 
                    Statement stmnt2 = createConnection.createStatement();
                    //The result set which will look at the rows to gather data 
                    ResultSet rs2 = stmnt2.executeQuery("SELECT * FROM CHAIR WHERE ID = " + "'" + idString + "'");
                    //Will simply now just check for Yes in that one part row and then increment them. 
                    while(rs2.next()) {
                         //Checks for the chairs legs and what their status 
                        //Is in the database 
                        if (rs2.getString("Legs").equals("Y")) {
                            numOfLegs++;
                        }
                         //Checks for the chairs seat and what their status 
                        //Is in the database 
                        if (rs2.getString("Seat").equals("Y")) {
                            numOfSeats++;
                        }
                        //Increments the sum by looking at the price for the part 
                        priceSum += rs2.getInt("Price");
                    }
                    //WIll catch the exception if something goes wrong 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            //Checks if the legs equal the maxParts or not 
            //If it is sucessfull it will add to the confirmedCombinations arraylist to be 
            //Used by the writeFile method 
            //Also adds the prices and sets the boolean to true as well. 
            if (numOfLegs >= maxParts && numOfSeats >= maxParts) {
                confirmedCombinations.add(partIDs);
                prices.add(priceSum);
                combinationFound = true;
            }
        }
        
    }

    /**
     * Sorts the price ArrayList from lowest to highest using a famous
     * known sorting algorithm bubble sort. 
     * Two temp arraylists to swap the elements in the array and 
     * ensures there is no overlap 
     */
    public void sortLowestPrice() { //Abhay
        for (int i = prices.size(); i > 0; i--) {
            for (int j = 0; j < prices.size() - 1; j++) {
                    if (prices.get(j) > prices.get(j + 1)) {
                        int tmp = prices.get(j);
                        int tmp2 = prices.get(j + 1);
                        prices.set(j, tmp2);
                        prices.set(j + 1, tmp);
                        //For swapping purposes we have these two array lists which will 
                        //Make sure the elements are being rightly ordered 
                        ArrayList<String> tmp3 = confirmedCombinations.get(j);
                        ArrayList<String> tmp4 = confirmedCombinations.get(j + 1);
                        confirmedCombinations.set(j, tmp4);
                        confirmedCombinations.set(j + 1, tmp3);   
                }
            }
        }
    }

    /**
     * Just a getter to be used when needed the lowest price 
     * @return The price from the arrayList which is at the first
     * Index also the lowest 
     */
    public int getLowestPrice() { //Abhay
        return prices.get(0);
    }

   
    /**
     * Will check for the manufactueres using their ID
     * Collected in the earlier input ArrayList 
     * And then checks if they are repeated in the order so 
     * They are not written twice in the orderform file 
     */
    public void suggestedManufacturer() { //Abhay
        //Try and catch block for the the terminal printing 
        try {
            //This will get printed to the console before the manufacturers string 
            System.out.println("Order cannot be fulfilled based on current inventory.");
            System.out.print("Suggested Manufacturers can also be viewed in the orderform.txt file.");
            System.out.println("The suggested Manufacturers are: ");
            //For loop which will iterate through the input array originally 
            for (int i = 0; i < manufacturerIDs.size(); i++) {
                
                //Boolean created to make sure it is not repeated 
                boolean isRepeat = false;
                
                    Statement stmnt = createConnection.createStatement();
                    //Quert which will select everything from it 
                    rs = stmnt.executeQuery("SELECT * FROM MANUFACTURER");
                    //This going through the repeats array and if there 
                    //One seen then it will break out of this 
                    if (i > 0) {
                        for (int j = 0; j < repeats.size(); j++) {
                            if (manufacturerIDs.get(i).equals(repeats.get(j))) {
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
                        if (rs.getString("ManuID").equals(manufacturerIDs.get(i))) {
                            manufacturers.append("Name: " + rs.getString("Name") + "\n");
                            repeats.add(manufacturerIDs.get(i));
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
    public void removeParts() { //Abhay
        ArrayList<String> removedParts = confirmedCombinations.get(0);
        Statement stmnt;
        try {
            //For loop which will iterate through the arraylist of parts which
            //The user will buy in order to complete the order 
            for (int i = 0; i < removedParts.size(); i++) {
                stmnt = createConnection.createStatement();
                //Query similar to Assignment 9
                stmnt.executeUpdate("DELETE FROM CHAIR WHERE ID = "+ "'" + removedParts.get(i) + "'");
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
    public boolean writeFileChairOrder(String originalRequest, int totalPrice) throws IOException { //Abhay
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
            ArrayList<String> partsOrdered = confirmedCombinations.get(0); 
            for (int i = 0; i < partsOrdered.size(); i++) {
                bw.write("ID: " + partsOrdered.get(i) + "\n");
            }
            bw.write("Total Price: " + "$" + totalPrice);
            // Close the BufferedWriter Object and FileWriter object
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println("Failed to write to the orderform file");
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
    public boolean writeFileSuggestedManu() throws IOException { //Abhay
        try {
            FileWriter fw = new FileWriter("orderform.txt");
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
            System.out.println("Failed to write to the orderform file");
        }
        return fileStatus = true;
    }
}
