package edu.ucalgary.ensf409;

//Imports used for this testing file 
import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;
import java.sql.*;

public class DeskTest {
    @Test
    public void constructorDeskTest() {
        Desk testDesk = new Desk("standing", "desk", 1);
        String[] expected = { "standing", "desk", String.valueOf(1) };
        String[] test = { testDesk.getType(), testDesk.getCategory(), String.valueOf(testDesk.getQuantity()) };
        assertArrayEquals("The constructor was sucessful in initialzing the data memebers", expected, test);
    }
}
