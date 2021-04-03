package edu.ucalgary.ensf409;

public class Chair {

    /////// DATA MEMBERS:
    // category: String
    // type: String
    // quantity: Int

    
    ////// METHODS:

    // Chair CTOR:
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


    //Algorithm For the Cheapest Price:
    

    /**
     *  static ArrayList<String> inner = new ArrayList<String>();
    public void insertNewComposition() {
        Statement stmnt;
        try {
            stmnt = createConnection.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM COMPETITOR WHERE Instrument = 'Oboe'");
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    inner.add(rs.getString(i));
                  //  System.out.print(rs.getString(i) + ", ");
                }
                //System.out.println();
            }
            stmnt.close();
        }
        catch (SQLException e) {
            System.out.println("There has been a failure in inserting the composition.");
            e.printStackTrace();
        }
    }
     */

     /**
      *  myJDBC.initializeConnection();
        myJDBC.insertNewComposition();
        StringBuilder hello = new StringBuilder();
        int counter = 0;
        for(int a = 0; a < inner.size(); a++) {
            if(counter < 6) {
                hello.append(inner.get(a) + ", ");
                counter++;
            }
            else {
                hello.append('\n');
                hello.append(inner.get(a) + ", ");
                counter = 0;
            }
        }
        String test = hello.toString();
        System.out.print(test);
      */

      /**
       *    stmnt = createConnection.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM COMPETITOR WHERE Type = 'Mesh'");
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            boolean hasLegs, hasArms, hasSeat, hasCushion = false;
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    inner.add(rs.getString(i));
                    
                }
                if (rs.getString("Legs").equals("Y")) {
                    hasLegs = true;
                }
            }
       */

       /**
        *  int counter = 0;
        for(int a = 0; a < inner.size(); a++) {
            if(counter == 4) {
                hello.append(inner.get(a) + ", ");
                counter = -1;
            }
            else {
                counter++;
            }
        */

        // String = "75" + "_C3098"
        // BEFORE:
        // 100_C4598, 75_C3098, 80_C4098, 75_C4998
        // AFTER:
        // 75_C3098, 75_C4998, 80_C4098, 100_C4598
        
        // String regex = "([0-9]+)";
        // ([A-Z])\w+  
        // Matcher 
        
        //Sorting the price method 
        /**
         * 	for (int i = input.size(); i > 0 ; i--) {
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
		
		for (int i = 0; i < input.size(); i++) { 
			System.out.println(input.get(i));
		}
         */


         // ArrayList<String> removeParts = new ArrayList<>();
         /**
          * boolean hasLegs, hasArms, hasSeat, hasCushion = false;
            boolean isRepeated = false;
            int priceSum = 0;

            for (int i = 0; i < input.size(); i++) {
                
                if (hasLegs == true && hasArms == true && hasSeat == true && hasCushion == true) {
                    break;
                }

                String REGEX = "([A-Z])\w+";
                String REGEX2 = "[0-9]+";
		        Pattern PATTERN = Pattern.compile(REGEX);
                Pattern PATTERN2 = Pattern.compile(REGEX2);
                Matcher MAT = PATTERN.matcher(input.get(i));
                Matcher MAT2 = PATTERN.matcher(input.get(i));
                
                if (MAT.find() && MAT2.find()) {
                    String idString = MAT.group();
                    int priceInt = Integer.parseInt(MAT2.group());
                    stmnt = createConnection.createStatement();
                    ResultSet rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE ID = '?'");

                    while(rs.next()) {
                        if (rs.getString("Legs").equals("Y") && hasLegs == true) {
                            isRepeated = true;
                            break;
                        }
                        if (rs.getString("Arms").equals("Y") && hasArms == true) {
                            isRepeated = true;
                            break;
                        }
                        if (rs.getString("Seat").equals("Y") && hasSeat == true) {
                            isRepeated = true;
                            break;
                        }
                        if (rs.getString("Cushion").equals("Y") && hasCushion == true) {
                            isRepeated = true;
                            break;
                        }
                        if (rs.getString("Legs").equals("Y")) {
                            isRepeated = false;
                            hasLegs = true;
                        }
                        if (rs.getString("Arms").equals("Y")) {
                            isRepeated = false;
                            hasArms = true;
                        }
                        if (rs.getString("Seat").equals("Y")) {
                            isRepeated = false;
                            hasSeat = true;
                        }
                        if (rs.getString("Cushion").equals("Y")) {
                            isRepeated = false;
                            hasCushion = true;
                        }
                    }
//chair check       
                    if (isRepeated) {
                        continue;
                    }

                    if (hasLegs == true || hasArms == true || hasSeat == true || hasCushion == true) {
                        priceSum += priceInt;
                        removeParts.add(idString);
                    }
                }
            }

            return priceSum;

          */
        
          /**
           * input.add("50_C9890");
           * input.add("100_C0942");
           * input.add("75_C6748");
           * input.add("75_C8138");
           */

           /**
            * Statement statement = connect.createStatement();
			    result = statement.executeQuery("SELECT * FROM CHAIR WHERE ID = " + "'" + idString + "'");
            */

            /**
             * 	String REGEX = "[0-9]+";
		        Pattern PATTERN = Pattern.compile(REGEX);
             */



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

             ///////// New Method for checkPrice: 
             /**
              *   public int checkPrice() {
            boolean hasLegs = false;
            boolean hasArms = false;
            boolean hasSeat = false;
            boolean hasCushion = false;
            boolean isRepeated = false;
            int priceSum = 0;

            for (int i = 0; i < input.size(); i++) {

                if (hasLegs == true && hasArms == true && hasSeat == true && hasCushion == true) {
                    break;
                }

            String REGEX = "([A-Z])\\w+";
            String REGEX2 = "[0-9]+";
            Pattern PATTERN = Pattern.compile(REGEX);
            Pattern PATTERN2 = Pattern.compile(REGEX2);
            Matcher MAT = PATTERN.matcher(input.get(i));
            Matcher MAT2 = PATTERN2.matcher(input.get(i));

            if (MAT.find() && MAT2.find()) {
                String idString = MAT.group();
                int priceInt = Integer.parseInt(MAT2.group());
                try {
                    Statement stmnt = createConnection.createStatement();
                    rs = stmnt.executeQuery("SELECT * FROM CHAIR WHERE ID = " +  "'" + idString + "'");
                    while(rs.next()) {
                        if (rs.getString("Legs").equals("Y") && hasLegs == true) {
                            isRepeated = true;
                            break;
                        }
                        if (rs.getString("Arms").equals("Y") && hasArms == true) {
                            isRepeated = true;
                            break;
                        }
                        if (rs.getString("Seat").equals("Y") && hasSeat == true) {
                            isRepeated = true;
                            break;
                        }
                        if (rs.getString("Cushion").equals("Y") && hasCushion == true) {
                            isRepeated = true;
                            break;
                        }
                        if (rs.getString("Legs").equals("Y")) {
                            isRepeated = false;
                            hasLegs = true;
                        }
                        if (rs.getString("Arms").equals("Y")) {
                            isRepeated = false;
                            hasArms = true;
                        }
                        if (rs.getString("Seat").equals("Y")) {
                            isRepeated = false;
                            hasSeat = true;
                        }
                        if (rs.getString("Cushion").equals("Y")) {
                            isRepeated = false;
                            hasCushion = true;
                        }
                    }
//chair check
                    if (isRepeated) {
                        continue;
                    }
                    if (hasLegs == true || hasArms == true || hasSeat == true || hasCushion == true) {
                        priceSum += priceInt;
                        removeParts.add(idString);
                    }
                    stmnt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return priceSum;
    }
              */
             
}
