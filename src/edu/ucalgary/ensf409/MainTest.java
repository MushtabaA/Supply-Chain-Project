// package edu.ucalgary.ensf409;

// //Imports used for this testing file
// import static org.junit.Assert.*;
// import org.junit.*;
// import java.util.Scanner;
// import java.io.*;
// import java.util.*;
// import java.sql.*;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;
// //TODO: FIX THE DEFAULT USER TO SCM
// public class MainTest {
//     @Test

//     public void splitOrderTest() throws Exception {
//         Main testMain = new Main();
//         testMain.furnitureInput = "mesh chair, 1";

//         final String REGEX = "(swing arm|[a-zA-Z]+)";
//         final Pattern PATTERN = Pattern.compile(REGEX);

//         final String REGEX2 = "(l+a+m+p+|c+h+a+i+r+|f+i+l+i+n+g+|d+e+s+k+)";
//         final Pattern PATTERN2 = Pattern.compile(REGEX2);

//         final String REGEX3 = "([0-9]+)";
//         final Pattern PATTERN3 = Pattern.compile(REGEX3);

//         final Matcher MAT = PATTERN.matcher(testMain.furnitureInput);
//         final Matcher MAT2 = PATTERN2.matcher(testMain.furnitureInput);
//         final Matcher MAT3 = PATTERN3.matcher(testMain.furnitureInput);
//         ArrayList<String> expected = new ArrayList<>();
//         expected.add("mesh");
//         expected.add("chair");
//         expected.add("1");

//         ArrayList<String> test = new ArrayList<>();

//         if (MAT.find() && MAT2.find() && MAT3.find()) {
//             test.add(MAT.group());
//             test.add(MAT2.group());
//             test.add(MAT3.group());
//         }
//         assertEquals("The furniture input was not split correctly", expected, test);
//     }
// }