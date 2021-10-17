package com.company;

import java.util.ArrayList;
import java.util.Scanner;
import oracle.jdbc.driver.OracleConnection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;


public class testTaker {
    String currentUserID;
    utility util;
    Scanner scanner;

    public testTaker(String currentUserID){
        this.currentUserID = currentUserID;
        util = new utility();
        scanner = new Scanner(System.in);
    }

    public void application() throws SQLException {
        ArrayList<String[]> listOfExam = new ArrayList<>();
        ArrayList<String> listOfScoreExamID = new ArrayList<>();
        int optionCounter = 0;
        util.clearScreen();


        System.out.println("Please enter an option ID to partake in a test. Enter 0 to return to main menu. ");
        //System.out.println("\tExam ID:\t\tSubject ID:\t\tTest No:\t\tDate:\t\tTime:\t\tDuration:\t\t");
        System.out.printf("%7s %10s %11s %13s %10s %14s %10s %10s", "Option:", "Exam ID:", "Subject ID:", "Subject Name:", "Test No:", "Date:", "Time:", "Duration:");
        System.out.println();

        OracleConnection conn = utility.oracleStartCon();
        ResultSet rset5 = utility.sqlSelector(conn, "SELECT EXAM_ID FROM STUDENT_SCORE WHERE STUDENT_ID = '" + currentUserID+"'");
        while(rset5.next()) {
            listOfScoreExamID.add(rset5.getString("EXAM_ID"));
        }


        ResultSet rset = utility.sqlSelector(conn, "SELECT * FROM EXAM WHERE CLASS_ID = (SELECT CLASS_ID FROM STUDENT WHERE STUDENT_ID = '" + currentUserID +"') AND EXAM_DATE = TRUNC(SYSDATE)");
        while (rset.next()) {
            String[] examInfo = new String[7];
            examInfo[0] = Integer.toString(rset.getInt("EXAM_ID"));
            examInfo[1] = rset.getString("SUBJECT_ID");
            examInfo[3] = rset.getString("TEST_NO");
            Date tempDate = rset.getDate("EXAM_DATE");
            examInfo[4] = tempDate.toString();
            examInfo[5] = rset.getString("EXAM_TIME");
            examInfo[6] = rset.getString("EXAM_DURATION");

            ResultSet rset2 = utility.sqlSelector(conn, "SELECT SUBJECT_NAME FROM SUBJECT_NAME WHERE SUBJECT_ID = " + examInfo[1]);
            while(rset2.next())
                examInfo[2] = rset2.getString("SUBJECT_NAME");

            if (!(listOfScoreExamID.contains(examInfo[0]))) {
                listOfExam.add(examInfo);
                optionCounter++;
                System.out.format("%7s %10s %11s %13s %10s %14s %10s %10s", optionCounter + ".", examInfo[0], examInfo[1], examInfo[2], examInfo[3], examInfo[4], examInfo[5], examInfo[6]);
                System.out.println();
            }
        }



        //DEBUG ONLY

        /*
        String[] examInfo = new String[7];
        examInfo[0] = "2";
        examInfo[1] = "002";
        examInfo[2] = "English";
        examInfo[3] = "001";
        examInfo[4] = "2020-12-07";
        examInfo[5] = "0900";
        examInfo[6] = "12";
        listOfExam.add(examInfo);
        optionCounter++;
        System.out.println();
        //System.out.println(optionCounter+". \t"+examInfo[0]+"\t\t"+examInfo[1]+"\t\t"+examInfo[2]+"\t\t"+examInfo[3]+"\t\t"+examInfo[4]+"\t\t"+examInfo[5]+"\t\t");
        //System.out.printf("%10s %10s %10s %10s %12s %10s %10s", optionCounter, examInfo[0], examInfo[1], examInfo[2], examInfo[3], examInfo[4], examInfo[5]);
        System.out.format("%7s %10s %11s %13s %10s %14s %10s %10s",
                optionCounter+".", examInfo[0], examInfo[1], examInfo[2], examInfo[3], examInfo[4], examInfo[5],examInfo[6]);

         */


        System.out.println("\n\n");
        String input = "";
        boolean validInput = false;
        //while (!(Integer.parseInt(input) >= 0 && Integer.parseInt(input) <= optionCounter)) {
        while (validInput == false) {
            System.out.print("Your input: ");
            input = scanner.nextLine();
            if (isInteger(input) == false) {
                System.out.println("Invalid input! Please enter again!");
            }
            else if (isInteger(input) == true) {
                if (!(Integer.parseInt(input) >= 0 && Integer.parseInt(input) <= optionCounter)) {
                    System.out.println("Invalid input! Please enter again!");
                }
                else
                    validInput = true;
            }
        }

        if (input.equals("0")) {
            utility.oracleEndCon(conn);
            return;
        }
        else {
            String[] targetExamInfo = listOfExam.get(Integer.parseInt(input) - 1);
            String examIDInput = (listOfExam.get(Integer.parseInt(input) - 1))[0];
            partakeExam(examIDInput, targetExamInfo);
        }
        //TO BE COMPLETED: CHECK TIME
        utility.oracleEndCon(conn);
    }

    public boolean isInteger(String input) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

    public void partakeExam(String examID, String[] examInfo) throws SQLException{
        util.clearScreen();

        ArrayList<String[]> listOfQuestions = new ArrayList<>();

        //System.out.println("Current exam ID: " + examID);

        OracleConnection conn = utility.oracleStartCon();
        ResultSet rset3 = utility.sqlSelector(conn, "SELECT * FROM EXAM_QUESTIONS WHERE EXAM_ID = " + examID + " ORDER BY QUESTION_NO");

        //Read all question info and store them into local storage.
        int questionCount = 0;
        while (rset3.next()) {
            //System.out.println("Reading.");
            /*
            Index:
            0 - QUESTION_NO
            1 - QUESTION_TYPE
            2 - QUESTION_DESC
            3 - FULL_MARK
            4 - IS_COMPULSORY
            5 - STUDENT_ANSWER - THIS IS NOT TO BE ASSIGNED AT THIS STAGE YET
             */

            String[] questionInfo = new String[6];
            questionInfo[0] = Integer.toString(rset3.getInt("QUESTION_NO"));
            questionInfo[1] = Integer.toString(rset3.getInt("QUESTION_TYPE"));
            questionInfo[2] = rset3.getString("QUESTION_DESC");
            questionInfo[3] = Integer.toString(rset3.getInt("FULL_MARK"));
            questionInfo[4] = Integer.toString(rset3.getInt("IS_COMPULSORY"));
            questionCount++;
            listOfQuestions.add(questionInfo);
        }

        //System.out.println("Question Count: "+questionCount);

        //Print exam info and ask for confirmation
        System.out.println("Current user ID: " + currentUserID);
        System.out.println("Exam ID: " + examInfo[0]);
        System.out.println("Subject: " + examInfo[2]);
        System.out.println("Test No: " + examInfo[3]);
        System.out.println("Duration: "+ examInfo[4]);
        System.out.println("\nEnter 1 to start the exam. Enter 0 to return to main menu.");
        String input = "";
        while (!(input.equals("0") || input.equals("1"))) {
            input = scanner.nextLine();
            System.out.print("Your option: ");
            if (!(input.equals("0") || input.equals("1"))) {
                System.out.println("Invalid input! Enter again!");
            }
        }

        if (input.equals("0")) {
            utility.oracleEndCon(conn);
            util.clearScreen();
            return;
        }

        //Loop the questions.
        for (int i = 0; i < questionCount; i++) {
            util.clearScreen();
            System.out.println("Current user ID: " + currentUserID);
            System.out.println("Exam ID: " + examInfo[0]);
            System.out.println("Subject: " + examInfo[2]);
            System.out.println("Test No: " + examInfo[3]);
            System.out.println("Duration: "+ examInfo[4]);
            System.out.println("\n\n");
            //System.out.println("Current ")
            System.out.println("Q." + (listOfQuestions.get(i))[0] + " " + (listOfQuestions.get(i))[2] + " (" + (listOfQuestions.get(i))[3] + " mark(s))");
            input = "";

            //MC cases
            if ((listOfQuestions.get(i))[1].equals("0")) {
                boolean skippable = !((listOfQuestions.get(i))[4].equals("1"));
                System.out.println("Enter the following number for the respective answer: ");
                if (skippable)
                    System.out.println("This question can be skipped. Enter 0 to skip: ");
                System.out.println("\n1. A\n2. B\n3. C\n4. D");


                //Ask for input
                if(skippable == false) {
                    while (!(input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4"))) {
                        System.out.print("Your answer: ");
                        input = scanner.nextLine();
                        if (!(input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4"))) {
                            System.out.println("Invalid input! Enter again!");
                        }
                    }
                }

                else {
                    while (!(input.equals("0") ||input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4"))) {
                        System.out.print("Your answer: ");
                        input = scanner.nextLine();
                        if (!(input.equals("0") || input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4"))) {
                            System.out.println("Invalid input! Enter again!");
                        }
                    }
                }

                String ans = "";

                if (input.equals("1")) {
                    ans = "A";
                }
                else if (input.equals("2")) {
                    ans = "B";
                }
                else if (input.equals("3")) {
                    ans = "C";
                }
                else if (input.equals("4")) {
                    ans = "D";
                }
                else if (input.equals("0")) {
                    ans = "NULL";
                }

                System.out.println("Ans: " + ans);

                (listOfQuestions.get(i))[5] = ans;
                //INSERT INTO STUDENT_ANSWER VALUES ('002', '1', 's000000001', 'A')
                //"INSERT INTO STUDENT_ANSWER VALUES ('"+examID+"', '"+i+"', '"+currentUserID+"', '"+ans+"')"
                String sqlStatement = "INSERT INTO STUDENT_ANSWER VALUES ("+examID+", "+(i+1)+", '"+currentUserID+"', '"+ans+"')";
                System.out.println(sqlStatement);
                try {
                    utility.sqlRunner(sqlStatement);
                } catch (Exception e) {
                    System.out.println("Exception detected with updated SQL statement...");
                }
            }

            else {
                boolean skippable = !((listOfQuestions.get(i))[4].equals("1"));

                if (skippable)
                    System.out.println("This question can be skipped. Enter 0 to skip: ");

                //Ask for input
                if(skippable == false) {
                    while ((input.equals("") || input.equals("0"))) {
                        System.out.print("Your answer: ");
                        input = scanner.nextLine();
                        if (input.equals("")) {
                            System.out.println("Invalid input! You must input something!");
                        }
                        else if (input.equals("0")) {
                            System.out.println("Invalid input! This question cannot be skipped!");
                        }
                    }
                }

                else {
                    while ((input.equals(""))) {
                        System.out.print("Your answer: ");
                        input = scanner.nextLine();
                        if ((input.equals(""))) {
                            System.out.println("Invalid input! You must input something!");
                        }
                    }
                }

                String ans = input;
                if ((input.equals("0"))) {
                    ans = "NULL";
                }

                (listOfQuestions.get(i))[5] = input;
                String sqlStatement = "INSERT INTO STUDENT_ANSWER VALUES ("+examID+", "+(i+1)+", '"+currentUserID+"', '"+ans+"')";
                System.out.println(sqlStatement);
                try {
                    utility.sqlRunner(sqlStatement);
                } catch (Exception e) {
                    System.out.println("Exception detected with updated SQL statement...");
                }
            }

        }
        util.clearScreen();

        //Check MC answers
        ArrayList<String> listOfAnswers = new ArrayList<>();
        String sqlStatement = "SELECT ANSWER FROM EXAM_QUESTIONS WHERE EXAM_ID = " + examID;
        ResultSet rset4 = utility.sqlSelector(conn, sqlStatement);
        while(rset4.next()){
            listOfAnswers.add(rset4.getString("ANSWER"));
        }

        for (int i = 0; i < questionCount; i++) {
            if ((listOfQuestions.get(i))[1].equals("0") || (listOfQuestions.get(i))[1].equals("1")) {
                if ((listOfQuestions.get(i))[5].equals(listOfAnswers.get(i))) {
                    String sqlStatement2 = "INSERT INTO STUDENT_SCORE VALUES (" + examID + ", " + (i + 1) + ", '" + currentUserID + "', '" + (listOfQuestions.get(i))[3] + "')";
                    //System.out.println(sqlStatement2);
                    try {
                        utility.sqlRunner(sqlStatement2);
                    } catch (Exception e) {
                        System.out.println("Exception detected with updated SQL statement...");
                        break;
                    }
                } else {
                    String sqlStatement2 = "INSERT INTO STUDENT_SCORE VALUES (" + examID + ", " + (i + 1) + ", '" + currentUserID + "', '" + 0 + "')";
                    //System.out.println(sqlStatement2);
                    try {
                        utility.sqlRunner(sqlStatement2);
                    } catch (Exception e) {
                        System.out.println("Exception detected with updated SQL statement...");
                        break;
                    }

                }
            }
        }



        System.out.println("Test Completed! Press enter/return key to return to main menu!");
        scanner.nextLine();


        utility.oracleEndCon(conn);
        util.clearScreen();


    }
}
