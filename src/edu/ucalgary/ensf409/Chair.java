package edu.ucalgary.ensf409;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.sql.*;


public class Chair extends Main {

    /////// DATA MEMBERS:
    final String category;
    final String type;
    final int quantity;
    static ArrayList<String> input = new ArrayList<>();
   
    // category: String
    // type: String
    // quantity: Int

    
    ////// METHODS:

    // Chair CTOR:
    Chair(String category, String type, int quantity){
       this.category = category;
       this.type = type;
       this.quantity = quantity;
    }
    // Params(category, type, quantity)

    // Mesh Chair:
    // Needs: Legs, Arms, Seat, Cushion 

    // Kneeling Chair:
    // Needs: Legs, Seat

    // Task Chair:
    // Needs: Legs, Seat, Cushion

    // chairResults:
    // Params(type: String)
    // executeQuery("SELECT * FROM CHAIR WHERE Type = ?")
    // setString (1, type) 
    // new arraylist <String>
    // If the type == result.getString(type)
    // Add to arraylist every row of result
    // Pass this arraylist to calculatePrice constructor

    
    public void getEverything() {
        Statement stmnt;
        try {
            stmnt = createConnection.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = 'Mesh'");
    
            while (rs.next()) {
                input.add(rs.getString("Price") + "_" + rs.getString("ID") + "_" + rs.getString("ManuID")); 
            }
    
            stmnt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

            ////////// Method for getting inventory:
            // static ArrayList<String> input = new ArrayList <>();
            // public void getEverything() {
            //     try {
            //         Statement stmnt;
            //         stmnt = createConnection.createStatement();
            //         ResultSet rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE Type = 'Mesh'");
            //         while (rs.next()) {
        
            //             inner.add(rs.getString("Price") + "_" + rs.getString("ID") + "_" + rs.getString("ManuID"));
        
            //         }
            //     }
            //     catch (SQLException e) {
            //         //Do Nothing
            //     }
            // }

            public void sortPrice() {
                for (int i = input.size(); i > 0 ; i--) {
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
            ////////// Sorting Method Confirmed:
            /**
             *      for (int i = input.size(); i > 0 ; i--) {
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
             */

            ////////// New Method for numberOfParts 
             
            public int numberOfParts(int quantity) {
                int value = 0;
                value += quantity;
                return value;
              }
              
            ////////// REGEX Strings:
             // String regex = "([0-9]+)";
             // ([A-Z])\w+
            // ([0-9]+$) -- MAN ID  

             ///////// New Method for checkPrice: 

            public int checkPrice() {
            int priceSum = 0;
            int maxParts = numberOfParts(quantity);
            int numOfLegs = 0;
            int numOfArms = 0;
            int numOfSeats = 0;
            int numOfCushions = 0;
            int trigger = 0;
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
                    Statement stmnt = createConnection.createStatement();
                    ResultSet rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE ID = " +  "'" + idString + "'");
                    while(rs.next()) {
                        if (rs.getString("Legs").equals("N") && rs.getString("Arms").equals("N") 
                        && rs.getString("Seat").equals("N") && rs.getString("Cushion").equals("N")) {
                            empty = true;
                            break;
                        }
                        if (rs.getString("Legs").equals("Y")) {
                            numOfLegs++;
                            if(numOfLegs > maxParts) {
                                numOfLegs--;
                                trigger += 1;
                            }
                        }
                        if (rs.getString("Arms").equals("Y")) {
                            numOfArms++;
                            if(numOfArms > maxParts) {
                                numOfArms--;
                                trigger += 1;
                            }
                        }
                        if (rs.getString("Seat").equals("Y")) {
                            numOfSeats++;
                            if(numOfSeats > maxParts) {
                                numOfSeats--;
                                trigger += 1;
                            }
                        }
                        if (rs.getString("Cushion").equals("Y")) {
                            numOfCushions++;
                            if(numOfCushions > maxParts) {
                                numOfCushions--;
                                trigger += 1;
                            }
                        }
                    }
                    if (empty) {
                        empty = false;
                        continue;
                    }
                    if (trigger == 4) {
                        trigger = 0;
                        continue;
                    } else {
                        if (numOfLegs > 0 || numOfArms > 0 || numOfSeats > 0 || numOfCushions > 0) {
                            priceSum += priceInt;
                            removeParts(idString);
                        }
                    }
                    stmnt.close();
                }
                 catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (numOfLegs != maxParts && numOfArms != maxParts && numOfSeats != maxParts && numOfCushions != maxParts) {
            suggestedManufacturer();
        }
        return priceSum;
    }

    public String suggestedManufacturer() {

        StringBuilder manufacturers = new StringBuilder();

        try {
            for (int i = 0; i < input.size(); i++) {
                final String REGEX3 = "([0-9]+$)";
                final Pattern PATTERN3 = Pattern.compile(REGEX3);
                final Matcher MAT3 = PATTERN3.matcher(input.get(i));
                ArrayList<String> repeats = new ArrayList <>();

                if (MAT3.find()) {
                    String manuID = MAT3.group();

                    Statement stmnt = createConnection.createStatement();
                    rs = stmnt.executeQuery("SELECT * FROM MANUFACTURER");
                    
                    while (rs.next()) {
                        if (i > 0) {
                            if (repeats.get(i - 1).equals(manuID)) {
                                break; 
                            }
                        }
                        if (rs.getString("manuID").equals(manuID)) {
                            manufacturers.append(rs.getString("Name"));
                            repeats.add(manuID); 
                        }
                    }
                }
            }
        } catch (SQLException e) {
            //Does nothing
        }
        return  manufacturers.toString();
    }

    public void removeParts(String idString) {
        Statement stmnt;
        try {
            stmnt = createConnection.createStatement();
            stmnt.executeUpdate("DELETE FROM CHAIR WHERE ID = " + idString);
            stmnt.close();
        }

        catch (SQLException e) {
            System.out.println("Deleting the part was unsucessful");
            e.printStackTrace();
        }
    }
}
