package edu.ucalgary.ensf409;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.sql.*;


public class Desk extends Main {

    /////// DATA MEMBERS:
     String category;
     String type;
     int quantity;

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
    static ArrayList<String> input = new ArrayList<>();
   
    // category: String
    // type: String
    // quantity: Int

    
    ////// METHODS:

    // Desk CTOR:
    Desk(String category, String type, int quantity){
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
            ResultSet rs = stmnt.executeQuery("SELECT * FROM DESK WHERE Type = '?'");
    
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
            int numofTops = 0;
            int numofDrawers = 0;
            int trigger = 0;
            boolean empty = false;

            for (int i = 0; i < input.size(); i++) { 

                if (numOfLegs == maxParts && numofTops == maxParts && numofDrawers == maxParts) {
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
                    ResultSet rs = stmnt.executeQuery("SELECT * FROM DESK WHERE ID = " +  "'" + idString + "'");
                    while(rs.next()) {
                        if (rs.getString("Legs").equals("N") && rs.getString("Top").equals("N") 
                        && rs.getString("Drawer").equals("N")) {
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
                        if (rs.getString("Top").equals("Y")) {
                            numofTops++;
                            if(numofTops > maxParts) {
                                numofTops--;
                                trigger += 1;
                            }
                        }
                        if (rs.getString("Drawer").equals("Y")) {
                            numofDrawers++;
                            if(numofDrawers > maxParts) {
                                numofDrawers--;
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
                        if (numOfLegs > 0 || numofTops > 0 || numofDrawers > 0) {
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

        if (numOfLegs != maxParts && numofTops != maxParts && numofDrawers != maxParts) {
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
            stmnt.executeUpdate("DELETE FROM DESK WHERE ID = " + idString);
            stmnt.close();
        }

        catch (SQLException e) {
            System.out.println("Deleting the part was unsucessful");
            e.printStackTrace();
        }
    }
}
