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
}
