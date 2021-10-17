package com.company;

import java.sql.SQLException;
import java.util.Scanner;
/**
 * Let student choose two functions: Test Taker or Record Checker, and then call their methods to run
 */
public class studentMainMenu {
    utility ut;
    String currentUserID;
    Scanner scanner;

    public studentMainMenu(String currentUserID) {
        ut = new utility();
        this.currentUserID = currentUserID;
        scanner = new Scanner(System.in);

    }

    public void application() {
        //Clear screen before output
        ut.clearScreen();

        boolean exitFlag = false;

        while (exitFlag == false) {



            //Print welcome message
            System.out.println("Welcome student! (Current User ID: "+currentUserID+")\n");
            System.out.println("Please enter the following number to access their respective functions and press enter: ");
            System.out.println("1. Participate in a test");
            System.out.println("2. Check your previous records");
            System.out.println("0. Exit the main menu and logout");




            //Get user input, re-request input if input is invalid
            String userInput = "";

            while (!(userInput.equals("0") || userInput.equals("1") || userInput.equals("2"))) {
                System.out.print("\nYour option: ");
                userInput = scanner.nextLine();
                if (!(userInput.equals("0") || userInput.equals("1") || userInput.equals("2")))
                    System.out.println("Invalid input! Please enter your option again.");
            }




            //Run the input's respective function, or flag the exit flag.
            if (userInput.equals("1")) {
                testTaker t = new testTaker(this.currentUserID);
                try {
                    t.application();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else if (userInput.equals("2")) {
                recordChecker t = new recordChecker(this.currentUserID);
                t.application();
            } else if (userInput.equals("0")) {
                exitFlag = true;
            }

        }
    }

}
