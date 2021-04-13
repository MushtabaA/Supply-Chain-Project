package edu.ucalgary.ensf409;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.io.IOException;
import java.sql.*;
import java.io.*;

public class Chair {

    /////// DATA MEMBERS:
    public Connection createConnection;

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

    private String category;
    private String type;
    private int quantity;
    public StringBuilder manufacturers = new StringBuilder();
    private boolean boughtParts = true;
    private boolean fileStatus = false;
    static ArrayList<String> input = new ArrayList<>();
    static ArrayList<String> partsOrdered = new ArrayList<>();
    static ArrayList<String> repeats = new ArrayList<>();
    int totalPrice;

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
    //Default Chair CTOR:
    Chair() {
        //Does nothing
    }

    // Chair CTOR:
    Chair(String category, String type, int quantity, String dburl, String username, String password) {
        this.category = category;
        this.type = type;
        this.quantity = quantity;
        this.DBURL = dburl;
        this.USERNAME = username;
        this.PASSWORD = password;
    }

    public void initializeConnection() {
        try {
            createConnection = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection was failed try again.");
            e.printStackTrace();
        }
    }

    public void callEverything() throws IOException {
        initializeConnection();
        getEverything(category);
        sortPrice(input);
        totalPrice = lowestPrice();
        if (boughtParts) {
            removeParts();
            System.out.println("Look at the output.txt file for the full furniture order.");
            String originalRequest = getCategory() + " " + getType() + ", " + getQuantity();
            writeFileChairOrder(originalRequest, totalPrice); //Without manufacturers
        } else {
            writeFileSuggestedManu();
        }
    }

    public void getEverything(String category) {
        Statement stmnt;
        try {
            stmnt = createConnection.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = " + "'" + category + "'");

            while (rs.next()) {
                input.add(rs.getString("Price") + " " + rs.getString("ID") + " " + rs.getString("ManuID"));
            }

            stmnt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public int numberOfParts(int quantity) {
        int value = 0;
        value += quantity;
        return value;
    }

    ///////// New Method for checkPrice:

    public int lowestPrice() {
        int returnPrice;
        
        if (category.equals("kneeling")) {
            returnPrice = checkPriceKneeling(input);
        } else {
            returnPrice = checkPriceAll(input);
        }

        return returnPrice;
    }

    public int checkPriceKneeling(ArrayList<String> input) {
        int priceSum = 0;
        int maxParts = numberOfParts(quantity);
        int numOfLegs = 0;
        int numOfSeats = 0;
        int trigger = 0;
        int noCounter = 0;
        boolean empty = false;

        for (int i = 0; i < input.size(); i++) {

            if (numOfLegs == maxParts && numOfSeats == maxParts) {
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
                    ResultSet rs2 = stmnt2.executeQuery("SELECT * FROM CHAIR WHERE ID = " + "'" + idString + "'");

                        while(rs2.next()) {
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

        if (numOfLegs != maxParts || numOfSeats != maxParts) {
            boughtParts = false;
            suggestedManufacturer();
        }
        return priceSum;
    }


    public int checkPriceAll(ArrayList<String> input) {
        int priceSum = 0;
        int maxParts = numberOfParts(quantity);
        int numOfLegs = 0;
        int numOfArms = 0;
        int numOfSeats = 0;
        int numOfCushions = 0;
        int trigger = 0;
        int noCounter = 0;
        boolean empty = false;

        for (int i = 0; i < input.size(); i++) {

            if (numOfLegs == maxParts && numOfArms == maxParts && numOfSeats == maxParts && numOfCushions == maxParts) {
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
                    ResultSet rs2 = stmnt2.executeQuery("SELECT * FROM CHAIR WHERE ID = " + "'" + idString + "'");

                        while(rs2.next()) {
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
                        if (rs2.getString("Arms").equals("Y")) {
                            numOfArms++;
                            if (numOfArms > maxParts) {
                                numOfArms--;
                                trigger += 1;
                            }
                        }
                        if (rs2.getString("Arms").equals("N")) {
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
                        if (rs2.getString("Cushion").equals("Y")) {
                            numOfCushions++;
                            if (numOfCushions > maxParts) {
                                numOfCushions--;
                                trigger += 1;
                            }
                        }
                        if (rs2.getString("Cushion").equals("N")) {
                            noCounter++;
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

        if (numOfLegs != maxParts || numOfArms != maxParts || numOfSeats != maxParts || numOfCushions != maxParts) {
            boughtParts = false;
            suggestedManufacturer();
        }
        return priceSum;
    }

    public void suggestedManufacturer() {

        try {
            System.out.println("Order cannot be fulfilled based on current inventory.");
            System.out.print("Suggested Manufacturers can also be viewed in the output.txt file.");
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
                stmnt.executeUpdate("DELETE FROM CHAIR WHERE ID = "+ "'" + partsOrdered.get(i) + "'");
                stmnt.close();
            }
        }

        catch (SQLException e) {
            System.out.println("Deleting the part was unsuccessful");
            e.printStackTrace();
        }
    }

    public boolean writeFileChairOrder(String originalRequest, int totalPrice) throws IOException {
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
