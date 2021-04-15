//Package statement 
package edu.ucalgary.ensf409;
//Import statements used in this 
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;
import java.util.Scanner;

public class Main {
	// Default Constructor
	public Main() {
		// Does nothing
	}
	//Instances of the furniture type which are used later in the main method
	//To access the class 
	static Chair chair;
	static Desk desk;
	static Lamp lamp;
	static Filing filing;

	/**
	 * Database url of the following format jdbc:subprotocol:subname
	 */
	public String dburl; // store the database url information
	/**
	 * Database user on whose behalf the connection will be made
	 */
	public String username; // store the user's account username
	/**
	 * User's password
	 */
	public String password; // store the user's account password

	public String furnitureInput;
	//Getters and setters for the furniture input 
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
	//Getters and setters for the connection 
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
		return this.dburl;
	}

	/**
	 * getUsername retervies the data
	 * 
	 * @return the username of the database user
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * getPassword
	 * 
	 * @return the password of the database user
	 */
	public String getPassword() {
		return this.password;
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
	/**
     * Creates the connection using the Drivers which get added to the classpath and 
     * checks if the connection is being made or not 
     */
	public void initializeConnection() {
		try {
			//Uses the drivers which will create the connection 
			createConnection = DriverManager.getConnection(getDburl(), getUsername(), getPassword());
		} catch (SQLException e) {
			System.out.println("Connection was failed try again.");
			e.printStackTrace();
		}
	}
	/**
	 * Close method which will help with closing the connection 
	 * In the main method when called 
	 */
	public void close() {
		try {
			//Closes the connection to the database
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
	/**
	 * This our only interface with the user the welcome screne in the 
	 * Terminal and the scanner which will take in the users request 
	 * @throws Exception Will do this if the user puts in something which
	 * Is not an furniturer type, category or a negative quantity
	 */
	public void userMenu() throws Exception {
		//Title printed 
		System.out.println("          WELCOME TO:    INVENTORY MANAGER     ");
		//Contributors name printed 
		System.out.println("CREATED BY: Mushtaba Al Yasseen, Abhay Khosla, and Parbir Lehal");
		//Scanner which is implemented to read from the terminal hence the .in
		sc = new Scanner(System.in);
		System.out.println("Enter Username for the Database access: ");
		//Stores the username which the user will enter in 
		this.username = sc.nextLine();
		this.username = username.trim();

		System.out.println("Enter Password for the Database access: ");
		//Stores the password the user will enter in 
		this.password = sc.nextLine();
		this.password = password.trim();
		//Prints the completed the URL out on the screen 
		System.out.println("The URL for the connection is this following: jdbc:mysql://localhost/inventory" + "/"
				+ username + "/" + password);
		this.dburl = "jdbc:mysql://localhost/inventory";
		//Asks for the users request 
		System.out.println("Enter the furniture category, examples include: mesh, ergonomic, kneeling, etc:");
		this.furnitureCategory = sc.nextLine();
		this.furnitureCategory = furnitureCategory.toLowerCase().trim();
		System.out.println("Enter the furniture type, examples include: chair, desk, filing, lamp:");
		this.furnitureType = sc.nextLine();
		this.furnitureType = furnitureType.toLowerCase().trim();
		System.out.println("Enter the furniture quantity, must be a positive integer greater than 0:");
		this.furnitureQuantity = Integer.parseInt(sc.nextLine().trim());
		
		
		//this.furnitureInput = sc.nextLine();
		//If any upper case accidentatly is inputed then will convert them 
		//furnitureInput.toLowerCase();
		//Calls the split order method which will use regex to sepearte 
		//splitOrder(furnitureInput);
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
	/**
	 * 
	 * @param furnitureInput Takes in the original request to make sure that it
	 * is properly formatted into the variables created 
	 * @throws Exception //Exception if there is wrong input by the user 
	 */
	// public void splitOrder(String furnitureInput) throws Exception {
	// 	//Regex for the type
	// 	final String REGEX = "(swing arm|[a-zA-Z]+)";
	// 	final Pattern PATTERN = Pattern.compile(REGEX);
	// 	//Regex for furniture category 
	// 	final String REGEX2 = "(l+a+m+p+|c+h+a+i+r+|f+i+l+i+n+g+|d+e+s+k+)";
	// 	final Pattern PATTERN2 = Pattern.compile(REGEX2);
	// 	//Regex for furntiure quantity 
	// 	final String REGEX3 = "([0-9]+)";
	// 	final Pattern PATTERN3 = Pattern.compile(REGEX3);
	// 	//Using the matcher 
	// 	final Matcher MAT = PATTERN.matcher(furnitureInput);
	// 	final Matcher MAT2 = PATTERN2.matcher(furnitureInput);
	// 	final Matcher MAT3 = PATTERN3.matcher(furnitureInput);
	// 	//Using the find method to group for these 
	// 	if (MAT.find()) {
	// 		furnitureCategory = MAT.group();
	// 	} else {
	// 		//If anything wrong prints this out 
	// 		throw new Exception("The given furniture category was invalid");
	// 	}
	// 	//Using the find method to group for these 
	// 	if (MAT2.find()) {
	// 		furnitureType = MAT2.group();
	// 	} else {
	// 		//If anything wrong prints this out 
	// 		throw new Exception("The given furniture type was invalid, valid furniture types include:"
	// 		+ "chair, desk, lamp, and filing");
	// 	}
	// 	//Using the find method to group for these 
	// 	if (MAT3.find()) {
	// 		//Parses the string to convert into integer 
	// 		furnitureQuantity = Integer.parseInt(MAT3.group());
	// 	} else {
	// 		//If anything wrong prints this out 
	// 		throw new Exception("The given quantity was invalid, must enter a positive number greater than 0.");
	// 	}
	// }
/**
 * 
 * @param args arguments which are taken in the main method 
 * @throws Exception Will be done if there is something which is not valid 
 */
	public static void main(String[] args) throws Exception {
		//New instance of this class to call the methods 
		Main main = new Main();
		//Prints the user menu when the method is ran 
		main.userMenu();
		//Creates the connection 
		main.initializeConnection();
		//Checks for the boundary cases using the getters 
		if (main.getFurnitureType().equals("chair") || main.getFurnitureType().equals("desk")
				|| main.getFurnitureType().equals("lamp") || main.getFurnitureType().equals("filing")) {
		} else {
			//Prints the error message if anything is invalid 
			throw new Exception("The given furniture type was invalid, valid furniture types include:"
					+ "chair, desk, lamp, and filing");
		}
		//Checks for the boundary cases using the getters 
		if (main.getFurnitureCategory().equals("kneeling") || main.getFurnitureCategory().equals("task")
				|| main.getFurnitureCategory().equals("mesh") || main.getFurnitureCategory().equals("executive")
				|| main.getFurnitureCategory().equals("ergonomic") || main.getFurnitureCategory().equals("standing")
				|| main.getFurnitureCategory().equals("adjustable") || main.getFurnitureCategory().equals("traditional")
				|| main.getFurnitureCategory().equals("desk") || main.getFurnitureCategory().equals("study")
				|| main.getFurnitureCategory().equals("swing arm") || main.getFurnitureCategory().equals("small")
				|| main.getFurnitureCategory().equals("medium") || main.getFurnitureCategory().equals("large")) {
		} else {
			//Prints the error message if anything is invalid 
			throw new Exception("The given furniture category was invalid");
		}

		if (main.getFurnitureQuantity() <= 0) {
			throw new Exception(
					//Prints the error message if anything is invalid 
					"The given furniture quantity was invalid, must enter a positive number greater than 0.");
		}
		//Checks if it is chair, desk, lamp or the filing 
		//Will call the constructors and pass in the information collected 
		if (main.getFurnitureType().equals("chair")) {
			chair = new Chair(main.getFurnitureCategory(), main.getFurnitureType(), main.getFurnitureQuantity(),
					main.dburl, main.username, main.password);
			//Calls the everything method which has in order of how the program should be run 
			chair.callEverything();
		//Checks if it is chair, desk, lamp or the filing 
		//Will call the constructors and pass in the information collected 
		} else if (main.getFurnitureType().equals("desk")) {
			desk = new Desk(main.getFurnitureCategory(), main.getFurnitureType(), main.getFurnitureQuantity(),
					main.dburl, main.username, main.password);
			//Calls the everything method which has in order of how the program should be run 
			desk.callEverything();
		//Checks if it is chair, desk, lamp or the filing 
		//Will call the constructors and pass in the information collected 
		} else if (main.getFurnitureType().equals("lamp")) {
			lamp = new Lamp(main.getFurnitureCategory(), main.getFurnitureType(), main.getFurnitureQuantity(),
					main.dburl, main.username, main.password);
			//Calls the everything method which has in order of how the program should be run 
			lamp.callEverything();
		//Checks if it is chair, desk, lamp or the filing 
		//Will call the constructors and pass in the information collected 
		} else if (main.getFurnitureType().equals("filing")) {
			filing = new Filing(main.getFurnitureCategory(), main.getFurnitureType(), main.getFurnitureQuantity(),
					main.dburl, main.username, main.password);
			//Calls the everything method which has in order of how the program should be run 
			filing.callEverything();

		}
		//Finally closes it at the very end 
		main.close();

	}

}
