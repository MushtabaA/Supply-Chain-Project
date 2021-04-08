package edu.ucalgary.ensf409;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;
import java.util.Scanner;

public class Main {

	static Chair oneChair;
	static Desk oneDesk;
	static Lamp oneLamp;
	static Filing oneFiling;

	/**
     * Database url of the following format jdbc:subprotocol:subname
     */
    public String DBURL; //store the database url information
    /**
     * Database user on whose behalf the connection will be made
     */
    public String USERNAME; //store the user's account username
    /**
     * User's password
     */
    public String PASSWORD; //store the user's account password

    public String furnitureInput;
    
    public String furnitureCategory;
    
    public String furnitureType;

    public int furnitureQuantity;
	    /**
     * New instance of the connection type
     */
    public Connection createConnection;
    /**
     * New instance of the result set object
     */
    public ResultSet rs;

	/**
	 * 
	 * New instance of the Scanner object 
	 */
	private Scanner sc;
	    // Getters for all data members: 
     /**
     * getDburl retervie the data
     * @return the database URL is given back
     */
    public String getDburl() {
        return this.DBURL;
    }

    /**
     * getUsername retervies the data
     * @return the username of the database user
     */
    public String getUsername() {
        return this.USERNAME;
    }

    /**
     * getPassword
     * @return the password of the database user
     */
    public String getPassword() {
        return this.PASSWORD;
    }
	//Gets Furniture type
	public String getFurnitureType() {
        return this.furnitureType;
    }
	//Gets Furniture category

    public String getFurnitureCategory(){
        return this.furnitureCategory;
    }
	//Gets Furniture quantity
    public int getFurnitureQuantity(){
        return this.furnitureQuantity;
    }
	//Start of setters 
	public String setFurnitureType(String furnitureType) {
        return this.furnitureType;
    }

    public String setFurnitureCategory(String furnitureCategory){
        return this.furnitureCategory;
    }
    public int setFurnitureQuantity(String furnitureQuantity){
        return this.furnitureQuantity;
    }

	public void initializeConnection() {
        try {
            createConnection = DriverManager.getConnection(getDburl(), getUsername(), getPassword());
        } catch (SQLException e) {
            System.out.println("Connection was failed try again.");
            e.printStackTrace();
        }
    }
	
	public void close() {
		try {
			createConnection.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

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
	public void userMenu() {
		sc = new Scanner(System.in); 
		System.out.println("Enter Username for the Database access: ");
		this.USERNAME = sc.nextLine(); 

		System.out.println("Enter Password for the Database access: ");
		this.PASSWORD = sc.nextLine(); 

		System.out.println("The URL for the connection is this following: jdbc:mysql://localhost/inventory" + USERNAME + PASSWORD);
		
		System.out.println("Enter your order request like the following (mesh chair, 2):  ");
		this.furnitureInput = sc.nextLine(); 
		splitOrder(furnitureInput);
	
		// Set each variable to password, username, and url strings above 
	}
	
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
	public void splitOrder(String furnitureInput) {
		final String REGEX = "([a-zA-Z]+)";
		final Pattern PATTERN = Pattern.compile(REGEX);

		final String REGEX2 = "([a-zA-Z]+)";
		final Pattern PATTERN2 = Pattern.compile(REGEX2);

		final String REGEX3 = "([0-9]+)";
		final Pattern PATTERN3 = Pattern.compile(REGEX3);

		final Matcher MAT = PATTERN.matcher(furnitureInput);
		final Matcher MAT2 = PATTERN2.matcher(furnitureInput);
		final Matcher MAT3 = PATTERN3.matcher(furnitureInput);

		if (MAT.find() && MAT2.find() && MAT3.find()) {
			furnitureCategory = MAT.group();
			furnitureType = MAT2.group();
			furnitureQuantity = Integer.parseInt(MAT3.group());
		}
	}


	public static void main(String[] args) {

		Main main = new Main();

		main.userMenu();
		
		if (main.getFurnitureType().equals("Chair")) {
			oneChair = new Chair(main.getFurnitureCategory(), main.getFurnitureType(), main.getFurnitureQuantity());
		} else if (main.getFurnitureType().equals("Desk")) {
		 	oneDesk = new Desk(main.getFurnitureCategory(), main.getFurnitureType(), main.getFurnitureQuantity());

		} else if (main.getFurnitureType().equals("Lamp")) {
		 	oneLamp = new Lamp(main.getFurnitureCategory(), main.getFurnitureType(), main.getFurnitureQuantity());

		} else if (main.getFurnitureType().equals("Filing")) {
		 	oneFiling = new Filing(main.getFurnitureCategory(), main.getFurnitureType(), main.getFurnitureQuantity());
		}

        main.initializeConnection();

		main.close();
	 
	}

}
