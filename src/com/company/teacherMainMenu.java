package com.company;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Let teacher choose three functions: TestMarker, TestDesigner, AnalysisReport
 * Call the respective method after choosing
 */
public class teacherMainMenu {
    utility ut;
    String currentUserID;
    Scanner scanner;

    public teacherMainMenu(String currentUserID) {
        ut = new utility();
        this.currentUserID = currentUserID;
        scanner = new Scanner(System.in);
    }

    public void application() throws SQLException {
        //Clear screen before output
        ut.clearScreen();

        boolean exitFlag = false;

        while (exitFlag == false) {



            //Print welcome message
            System.out.println("Welcome teacher! (Current User ID: "+currentUserID+")\n");
            System.out.println("Please enter the following number to access their respective functions and press enter: ");
            System.out.println("1. Design a new test");
            System.out.println("2. Mark a test");
            System.out.println("3. Generate an analysis report");
            System.out.println("0. Exit the main menu and logout");




            //Get user input, re-request input if input is invalid
            String userInput = "";

            while (!(userInput.equals("0") || userInput.equals("1") || userInput.equals("2") || userInput.equals("3"))) {
                System.out.print("\nYour option: ");
                userInput = scanner.nextLine();
                if (!(userInput.equals("0") || userInput.equals("1") || userInput.equals("2") || userInput.equals("3")))
                    System.out.println("Invalid input! Please enter your option again.");
            }




            //Run the input's respective function, or flag the exit flag.
            if (userInput.equals("1")) {
                testDesigner t = new testDesigner(this.currentUserID);
                try {
                    t.application();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else if (userInput.equals("2")) {
                testMarker t = new testMarker(this.currentUserID);
                    t.application();
            } else if (userInput.equals("3")) {
                analysisReport t = new analysisReport(this.currentUserID);
                t.application();
            } else if (userInput.equals("0")) {
                exitFlag = true;
            }

        }
    }

}
