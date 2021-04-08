package edu.ucalgary.ensf409;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.sql.*;

public class Desk extends Main {
/////// DATA MEMBERS:
final String category;
final String type;
final int quantity;
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

public void deskResults(String type) {
    try {
      Statement myStmt;
      myStmt = createConnection.createStatement();
      ResultSet result = myStmt.executeQuery("SELECT * FROM DESK WHERE Type = ?");
     
     if (type == result.getString(type)){
         
     }
    }
    catch(SQLException ex){
        ex.printStackTrace();
    }
 }
 
}
