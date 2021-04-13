package edu.ucalgary.ensf409;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {
	//Default Constructor 
	public Main() {
		//Does nothing x
	}
	
	static Chair chair;
	static Desk desk;
	static Lamp lamp;
	static Filing filing;

	/**
	 * Database url of the following format jdbc:subprotocol:subname
	 */
	public String DBURL; // store the database url information
	/**
	 * Database user on whose behalf the connection will be made
	 */
	public String USERNAME; // store the user's account username
	/**
	 * User's password
	 */
	public String PASSWORD; // store the user's account password

	public String furnitureInput;

	public String getFurnitureInput() {
		return this.furnitureInput;
	}

	public void setFurnitureInput(String furnitureInput) {
		this.furnitureInput = furnitureInput;
	}

	public String furnitureCategory;

	public String furnitureType;

	public int furnitureQuantity;
	/**
	 * New instance of the connection type
	 */
	public Connection createConnection;

	public Connection getCreateConnection() {
		return this.createConnection;
	}

	public void setCreateConnection(Connection createConnection) {
		this.createConnection = createConnection;
	}

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
	 * 
	 * @return the database URL is given back
	 */
	public String getDburl() {
		return this.DBURL;
	}

	/**
	 * getUsername retervies the data
	 * 
	 * @return the username of the database user
	 */
	public String getUsername() {
		return this.USERNAME;
	}

	/**
	 * getPassword
	 * 
	 * @return the password of the database user
	 */
	public String getPassword() {
		return this.PASSWORD;
	}

	// Gets Furniture type
	public String getFurnitureType() {
		return this.furnitureType;
	}
	// Gets Furniture category

	public String getFurnitureCategory() {
		return this.furnitureCategory;
	}

	// Gets Furniture quantity
	public int getFurnitureQuantity() {
		return this.furnitureQuantity;
	}

	// Start of setters
	public String setFurnitureType(String furnitureType) {
		return this.furnitureType;
	}

	public String setFurnitureCategory(String furnitureCategory) {
		return this.furnitureCategory;
	}

	public int setFurnitureQuantity(String furnitureQuantity) {
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
	public void userMenu() throws Exception {

		System.out.println("          WELCOME TO:        ");


		System.out.println("CREATED BY: Mushtaba Al Yasseen, Abhay Khosla, and Parbir Lehal" + "\n");
		sc = new Scanner(System.in);
		System.out.println("Enter Username for the Database access: ");
		this.USERNAME = sc.nextLine();

		System.out.println("Enter Password for the Database access: ");
		this.PASSWORD = sc.nextLine();

		System.out.println(
				"The URL for the connection is this following: jdbc:mysql://localhost/inventory" + "/" + USERNAME + "/" + PASSWORD);
		this.DBURL = "jdbc:mysql://localhost/inventory";
		System.out.println("Enter your order request like the following example: mesh chair, 1");
		this.furnitureInput = sc.nextLine();
		furnitureInput.toLowerCase();
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
	public void splitOrder(String furnitureInput) throws Exception {
		final String REGEX = "(swing arm|[a-zA-Z]+)";
		final Pattern PATTERN = Pattern.compile(REGEX);

		final String REGEX2 = "(l+a+m+p+|c+h+a+i+r+|f+i+l+i+n+g+|d+e+s+k+)";
		final Pattern PATTERN2 = Pattern.compile(REGEX2);

		final String REGEX3 = "([0-9]+)";
		final Pattern PATTERN3 = Pattern.compile(REGEX3);

		final Matcher MAT = PATTERN.matcher(furnitureInput);
		final Matcher MAT2 = PATTERN2.matcher(furnitureInput);
		final Matcher MAT3 = PATTERN3.matcher(furnitureInput);

		if (MAT.find()) {
			furnitureCategory = MAT.group();
		} else {
			throw new Exception("The given furniture category was invalid");
		}

		if (MAT2.find()) {
			furnitureType = MAT2.group();
		} else {
			throw new Exception("The given furniture type was invalid, valid furniture types include:" 
			+ "chair, desk, lamp, and filing");
		}

		if (MAT3.find()) {
			furnitureQuantity = Integer.parseInt(MAT3.group());
		} else {
			throw new Exception("The given quantity was invalid, must enter a positive number greater than 0.");
		}
	}

	public static void main(String[] args) throws Exception {

		Main main = new Main();

		main.userMenu();
		main.initializeConnection();
		if (main.getFurnitureType().equals("chair") || main.getFurnitureType().equals("desk")
		|| main.getFurnitureType().equals("lamp") || main.getFurnitureType().equals("filing")) {
		} else {
			throw new Exception("The given furniture type was invalid, valid furniture types include:" 
			+ "chair, desk, lamp, and filing");
		}

		if (main.getFurnitureCategory().equals("kneeling") || main.getFurnitureCategory().equals("task") 
		|| main.getFurnitureCategory().equals("mesh") || main.getFurnitureCategory().equals("executive") 
		|| main.getFurnitureCategory().equals("ergonomic") || main.getFurnitureCategory().equals("standing") 
		|| main.getFurnitureCategory().equals("adjustable") || main.getFurnitureCategory().equals("traditional") 
		|| main.getFurnitureCategory().equals("desk") || main.getFurnitureCategory().equals("study") 
		|| main.getFurnitureCategory().equals("swing arm") || main.getFurnitureCategory().equals("small") 
		|| main.getFurnitureCategory().equals("medium") || main.getFurnitureCategory().equals("large")) {
		} else {
			throw new Exception("The given furniture category was invalid");
		}

		if (main.getFurnitureQuantity() <= 0) {
			throw new Exception("The given furniture quantity was invalid, must enter a positive number greater than 0.");
		}
		

		if (main.getFurnitureType().equals("chair")) {
		chair = new Chair(main.getFurnitureCategory(), main.getFurnitureType(),
		main.getFurnitureQuantity(), main.DBURL, main.USERNAME, main.PASSWORD);
		
		chair.callEverything();
		
		} else if (main.getFurnitureType().equals("desk")) {
		desk = new Desk(main.getFurnitureCategory(), main.getFurnitureType(),
		main.getFurnitureQuantity(), main.DBURL, main.USERNAME, main.PASSWORD);

		desk.callEverything();

		} else if (main.getFurnitureType().equals("lamp")) {
		lamp = new Lamp(main.getFurnitureCategory(), main.getFurnitureType(),
		main.getFurnitureQuantity(), main.DBURL, main.USERNAME, main.PASSWORD);

		lamp.callEverything();

		} else if (main.getFurnitureType().equals("filing")) {
		filing = new Filing(main.getFurnitureCategory(), main.getFurnitureType(),
		main.getFurnitureQuantity(), main.DBURL, main.USERNAME, main.PASSWORD);

		filing.callEverything();

		}

		main.close();

	}

}
