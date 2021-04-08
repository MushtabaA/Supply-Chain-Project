package edu.ucalgary.ensf409;
import java.io.*;
import java.util.Date;
public class UserOutput extends Main{

    public UserOutput() {
        //Does nothing
    }

    public void writeOrder() throws IOException {
        try {
            FileWriter fw = new FileWriter("output.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Furniture Order Form");
            bw.write('\n');
            bw.write("Faculty Name: ");
            bw.write('\n');
            bw.write("Contact: ");
            bw.write('\n');
            bw.write("Date: ");
            bw.write('\n');
            bw.write("Original Request: "+ getFurnitureType() + " " + getFurnitureCategory() + ", " + getFurnitureQuantity());
            bw.write('\n');
            bw.write("Items Ordered");
            
            //Close the BufferedWriter Object
            bw.close();
        } catch (Exception e) {
            System.out.println("Failed to write to the output file");
        }
    }
}
