package edu.ucalgary.ensf409;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.sql.*;

public class Lamp extends Main {
/////// DATA MEMBERS:
final String category;
final String type;
final int quantity;
static ArrayList<String> input = new ArrayList<>();

// category: String
// type: String
// quantity: Int


////// METHODS:

// Lamp CTOR:
Lamp(String category, String type, int quantity){
   this.category = category;
   this.type = type;
   this.quantity = quantity;
}

}
