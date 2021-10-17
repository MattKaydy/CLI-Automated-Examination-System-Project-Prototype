package com.company;

import java.util.Scanner;

import java.io.*;
import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

public class inputIDAndPass {
    Scanner scanner;
    utility util;

    public inputIDAndPass() {
        this.scanner = new Scanner(System.in);
        this.util = new utility();
    }

    public void application() {
        boolean exitFlag = false;

        while (exitFlag == false) {
            util.clearScreen();

            //Welcome message
            System.out.println("Welcome to the Automatic Examination System!\nPlease enter your user ID and password:\n ");





            //Ask user to enter or exit the system.
            String exitInput = "";

            while (!((exitInput.equals("0")) || (exitInput.equals("1")))) {
                System.out.print("Press 1 to login to the system. Press 0 to exit the system: ");
                exitInput = scanner.nextLine();

                if (!((exitInput.equals("0")) || (exitInput.equals("1"))))
                    System.out.println("Invalid Input! Please enter again. \n");
            }




            //Exit if exitFlag is flagged. Or else get login credentials
            if (exitInput.equals("0"))
                exitFlag = true;

            else if ((exitInput.equals("1"))) {
                util.clearScreen();
                String username = "";
                String password = "";
                System.out.print("Username: ");
                username = scanner.nextLine();

                System.out.print("Password: ");
                password = scanner.nextLine();

                //FOR DEBUG ONLY IN INTELLIJ TERMINAL
                //studentMainMenu sMenu = new studentMainMenu(username);
                //sMenu.application();

                //FOR DEBUG ONLY IN INTELLIJ TERMINAL
                //teacherMainMenu tMenu = new teacherMainMenu(username);
                //tMenu.application();

                //Check credientials, run respective programs according to the type of user account.
                try {
                    checkCredentials(username, password);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        }


        //Exit the program
        util.clearScreen();
        System.out.println("Goodbye!");

    }

    public static boolean checkCredentials (String username, String password) throws SQLException {
        String serverUsername, serverPassword;
        serverUsername = utility.usernameForServer;
        serverPassword = utility.passwordForServer;

        // Connection
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection(
                "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms",
                serverUsername, serverPassword);
        Statement stmt = conn.createStatement();
        String SQLTemp = "SELECT count(*) FROM STUDENT WHERE STUDENT_ID='" + username + "' AND PASSWORD='" + password + "'";

        ResultSet rset = stmt.executeQuery(SQLTemp);
        rset.next();
        if (rset.getInt("count(*)") == 1) {
            studentMainMenu sMenu = new studentMainMenu(username);
            sMenu.application();
            conn.close();
            return true;
        } else {
            SQLTemp = "SELECT count(*) FROM TEACHER WHERE TEACHER_ID='" + username + "' AND PASSWORD='" + password + "'";
            rset = stmt.executeQuery(SQLTemp);
            rset.next();
            if (rset.getInt("count(*)") == 1) {
                teacherMainMenu tMenu = new teacherMainMenu(username);
                tMenu.application();
                conn.close();
                return true;
            }
        }

        System.out.println("Incorrect username or password please try again !");
        conn.close();

        return false;
    }



}
