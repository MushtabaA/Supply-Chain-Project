package edu.ucalgary.ensf409;

public class UserInput {

    /////////// DATA MEMBERS (ALL PRIVATE):

    // DBURL: String (FINAL)
    // USER: String (FINAL)
    // USERPASS: String (FINAL)
    // Connection: Connect
    // ResultSet: result
    // FurnitureInput: String (Use regex later)
    // FurnitureCategory: String
    // FurnitureType: String
    // FurnitureQuantity: Int 

    ////////// METHODS:

    // userInput CTOR:
    // Params(DBURL, USER, USERPASS)

    // userInput CTOR:
    // Default Ctor, Params - none

    // Getters for all data members: 
    // Setters for all data members:

    // close:
    // Params - none

    // initializeConnection:
    // Params - none

    // splitOrder:
    // Params (FurnitureInput: String)
    // 3 regex strings for each group
    // Use Matcher/Pattern to set each group to the 3 data members
    // category, type, and quantity
    // Don't forget to convert Quantity from string to int (ParseInt)
    // "([a-zA-Z]+) ([a-zA-Z]+), ([0-9]+)", create local data members for these
    // Check if it is desk, lamp, chair, etc
    // Then call the related class' constructor, and then pass in all 3
    // local data members (the category, type, and quantity)
    // Ex: Chair(category, type, quantity)

    // userMenu:
    // Data Members: Username, Password, URL (All Strings)
    // Print Out: "Enter Username", and set Username
    // Print Out: "Enter Password", and set Password
    // Print Out: "Enter URL?????", and set URL
    // Call UserInput Constructor(Username, Password, URL)
    // Call initializeConnection Method (With try, catch statement)
    // Print Out: "Enter your order request like the following example:
    // "mesh chair, 2", and set furniture input (using the setter)
    // Call the splitOrder function

    // jdbc:mysql://127.0.0.1/COMPETITION
    // localhost/COMPETITION

}
