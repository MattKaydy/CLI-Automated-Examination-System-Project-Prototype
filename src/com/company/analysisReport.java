package com.company;

import oracle.jdbc.driver.OracleConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class analysisReport {
    String currentUserID;
    Scanner scanner;

    public analysisReport(String currentUserID) {
        this.currentUserID = currentUserID;
        scanner = new Scanner(System.in);
    }

    public void application() throws SQLException {
        boolean terminate = false;

        OracleConnection conn;



        utility.clearScreen();
        System.out.println("Now running analysis report.");

        while(!terminate) {
            // Initialise tables
            Table listStuTable = new Table("Student ID", 15, "First name", 15, "Last name", 15, "Class", 10);
            Table listSubTable = new Table("SUBJECT_ID", 20, "Subject name", 20);
            Table listClassTable = new Table("Class ID", 15, "Class", 15);
            Table aStuTable = new Table("Exam ID", 15, "Subject Name", 15, "Score", 10, "Grade", 8);
            Table aSubTable = new Table("Subject ID", 15, "Subject name", 15, "Average mark",15);
            Table aClassTable = new Table("Class ID", 20, "Class name", 14, "Average mark", 15);

            // User input
            String userInputFucNo;
            utility.clearScreen();
            System.out.println("Please enter the following number to access your respective functions and press enter: ");
            System.out.println("1. List out all the student");
            System.out.println("2. List out all the subject");
            System.out.println("3. List out all the class");
            System.out.println("4. Generate a student result");
            System.out.println("5. Generate a subject result");
            System.out.println("6. Generate a class result");

            userInputFucNo = scanner.nextLine();

            // Start switch
            utility.clearScreen();
            String userInput;
            switch (userInputFucNo) {

                case "1":
                    String StudentID;
                    String FName;
                    String LName;
                    String ClassName;

                    utility.clearScreen();

                    // Getting data from database
                    conn = utility.oracleStartCon();
                    String sqlListStu = "WITH\n" +
                            "-- CLASS\n" +
                            "Query1 AS (\n" +
                            "\tSELECT * FROM CLASS WHERE TEACHER_ID = + '"+currentUserID+"'\n" +
                            "),\n" +
                            "-- STUDENT\n" +
                            "Query2 AS (\n" +
                            "\tSELECT STUDENT_ID, FNAME, LNAME, CLASS_ID FROM STUDENT\n" +
                            ")\n" +
                            " SELECT STUDENT_ID, FNAME, LNAME, Query1.CLASS_NAME FROM Query1 JOIN Query2 ON Query1.CLASS_ID = Query2.CLASS_ID ";
                    ResultSet ListStuSet = utility.sqlSelector(conn, sqlListStu);
                    while (ListStuSet.next()){
                        StudentID = ListStuSet.getString("STUDENT_ID");
                        FName = ListStuSet.getString("FNAME");
                        LName = ListStuSet.getString("LNAME");
                        ClassName = ListStuSet.getString("CLASS_NAME");

                        listStuTable.add(StudentID, FName, LName, ClassName);
                    }
                    utility.oracleEndCon(conn);
                    // Print out
                    listStuTable.printAll();;
                    break;

                case "2":
                    String SubID;
                    String SubName;

                    utility.clearScreen();

                    // Getting data from database
                    conn = utility.oracleStartCon();
                    String sqlListSub = "WITH\n" +
                            "-- CLASS\n" +
                            "Query1 AS (\n" +
                            "\tSELECT TEACHER_ID, CLASS_ID FROM CLASS WHERE TEACHER_ID = '"+currentUserID+"'\n" +
                            "),\n" +
                            "-- SUBJECT\n" +
                            "Query2 AS (\n" +
                            "\tSELECT TEACHER_ID, SUBJECT_ID FROM SUBJECT\n" +
                            "),\n" +
                            "-- Merge Query1 And Query2\n" +
                            "Query3 AS (\n" +
                            "\tSELECT * FROM Query1 JOIN Query2 ON Query1.TEACHER_ID = Query2.TEACHER_ID\n" +
                            ")\n" +
                            " SELECT Query3.SUBJECT_ID, SUBJECT_NAME.SUBJECT_NAME FROM Query3, SUBJECT_NAME WHERE Query3.SUBJECT_ID = SUBJECT_NAME.SUBJECT_ID GROUP BY Query3.SUBJECT_ID, SUBJECT_NAME.SUBJECT_NAME ORDER BY Query3.SUBJECT_ID ";
                    ResultSet ListSubSet = utility.sqlSelector(conn, sqlListSub);
                    while (ListSubSet.next()){
                        SubID = ListSubSet.getString("SUBJECT_ID");
                        SubName = ListSubSet.getString("SUBJECT_NAME");

                        listSubTable.add(SubID, SubName);
                    }
                    utility.oracleEndCon(conn);
                    // Print out
                    listSubTable.printAll();;
                    break;


                case "3":
                    String CLASS_ID;
                    String SubNaCLASS_NAMEme;

                    utility.clearScreen();

                    // Getting data from database
                    conn = utility.oracleStartCon();
                    String sqlListClass = " SELECT CLASS_ID, CLASS_NAME FROM CLASS WHERE TEACHER_ID = '"+currentUserID+"' ";
                    ResultSet listClassSet = utility.sqlSelector(conn, sqlListClass);
                    while (listClassSet.next()){
                        CLASS_ID = listClassSet.getString("CLASS_ID");
                        SubNaCLASS_NAMEme = listClassSet.getString("CLASS_NAME");

                        listClassTable.add(CLASS_ID, SubNaCLASS_NAMEme);
                    }
                    utility.oracleEndCon(conn);
                    // Print out
                    listClassTable.printAll();;
                    break;


                case "4":
                    utility.clearScreen();
                    String examID;
                    String subjectName;
                    String score;
                    String grade;

                    // Input student ID
                    System.out.println("Please input student ID:");
                    userInput = scanner.nextLine();

                    // Getting data from database
                    conn = utility.oracleStartCon();
                    String sqlStu = " WITH\n" +
                            "-- EXAM\n" +
                            "query1 AS (\n" +
                            "\tSELECT EXAM_ID, SUBJECT_ID FROM EXAM\n" +
                            "),\n" +
                            "-- SUBJECT\n" +
                            "query2 AS (\n" +
                            "\tSELECT STUDENT_ID, SUBJECT_ID FROM SUBJECT WHERE STUDENT_ID = '"+userInput+"'\n" +
                            "),\n" +
                            "-- Merge Query1 And Query2\n" +
                            "query3 AS (\n" +
                            "\tSELECT query1.EXAM_ID, query1.SUBJECT_ID, query2.STUDENT_ID FROM query1 JOIN query2 ON query1.SUBJECT_ID = query2.SUBJECT_ID\n" +
                            "),\n" +
                            "-- Merge Query3 And SUBJECT_NAME\n" +
                            "query4 AS (\n" +
                            "\tSELECT * FROM query3 JOIN SUBJECT_NAME ON query3.SUBJECT_ID = SUBJECT_NAME.SUBJECT_ID\n" +
                            "),\n" +
                            "-- Merge Query4 And STUDENT_SCORE\n" +
                            "query5 AS (\n" +
                            "\tSELECT query4.EXAM_ID, query4.SUBJECT_NAME, SUM(SCORE) SCORE FROM query4, STUDENT_SCORE WHERE query4.EXAM_ID = STUDENT_SCORE.EXAM_ID AND query4.STUDENT_ID = STUDENT_SCORE.STUDENT_ID GROUP BY query4.EXAM_ID, query4.SUBJECT_NAME\n" +
                            "),\n" +
                            "-- Merge query5 AND GRADES\n" +
                            "query6 AS (\n" +
                            "\tSELECT EXAM_ID, SUBJECT_NAME, SCORE, MIN(MAXVALUE) MAXVALUE FROM query5 JOIN GRADES ON SCORE < MAXVALUE GROUP BY EXAM_ID, SUBJECT_NAME, SCORE\n" +
                            ")\n" +
                            "SELECT EXAM_ID, SUBJECT_NAME, SCORE, GRADE FROM query6, GRADES WHERE query6.MAXVALUE = GRADES.MAXVALUE ";
                    ResultSet listAStuSet = utility.sqlSelector(conn, sqlStu);
                    while (listAStuSet.next()){
                        examID = String.valueOf(listAStuSet.getInt("EXAM_ID"));
                        subjectName = listAStuSet.getString("SUBJECT_NAME");
                        score = String.valueOf(listAStuSet.getInt("SCORE"));
                        grade = listAStuSet.getString("GRADE");

                        aStuTable.add(examID, subjectName, score, grade);
                    }
                    utility.oracleEndCon(conn);
                    // Print out
                    aStuTable.printAll();;
                    break;

                // Print one subject.
                case "5":
                    utility.clearScreen();
                    String subID;
                    String subName;
                    String avg;

                    // Input student ID
                    System.out.println("Please input subject ID:");
                    userInput = scanner.nextLine();

                    // Getting data from database
                    conn = utility.oracleStartCon();
                    String sqlSbj = " WITH\n" +
                            "-- EXAM\n" +
                            "query1 AS (\n" +
                            "\tSELECT EXAM_ID, SUBJECT_ID FROM EXAM WHERE SUBJECT_ID = '"+userInput+"'\n" +
                            "),\n" +
                            "-- STUDENT_SCORE FOR IN STUDENT\n" +
                            "query2 AS (\n" +
                            "\tSELECT EXAM_ID, STUDENT_ID, SUM(SCORE) SCORE FROM STUDENT_SCORE GROUP BY EXAM_ID, STUDENT_ID\n" +
                            "),\n" +
                            "-- MERGE 1 AND 2\n" +
                            "query3 AS (\n" +
                            "\tSELECT SUBJECT_ID, AVG(SCORE) AVERAGE FROM Query1 JOIN Query2 ON Query1.EXAM_ID = Query2.EXAM_ID GROUP BY SUBJECT_ID\n" +
                            ")\n" +
                            " SELECT query3.SUBJECT_ID, SUBJECT_NAME, AVERAGE FROM query3, SUBJECT_NAME WHERE query3.SUBJECT_ID = SUBJECT_NAME.SUBJECT_ID ";
                    ResultSet listASubSet = utility.sqlSelector(conn, sqlSbj);
                    while (listASubSet.next()){
                        subID = listASubSet.getString("SUBJECT_ID");
                        subName = listASubSet.getString("SUBJECT_NAME");
                        avg = String.valueOf(listASubSet.getInt("AVERAGE"));

                        aSubTable.add(subID, subName, avg);
                    }
                    utility.oracleEndCon(conn);
                    // Print out
                    aSubTable.printAll();;
                    break;

                // print a class
                case "6":
                    utility.clearScreen();
                    String classID;
                    String className;
                    String avgClass;

                    // Input student ID
                    System.out.println("Please input class ID:");
                    userInput = scanner.nextLine();

                    // Getting data from database
                    conn = utility.oracleStartCon();
                    String sqlClass = " WITH\n" +
                            "-- STUDENT_SCORE\n" +
                            "query1 AS (\n" +
                            "\tSELECT EXAM_ID, STUDENT_ID, SUM(SCORE) SCORE FROM STUDENT_SCORE GROUP BY EXAM_ID, STUDENT_ID\n" +
                            "),\n" +
                            "-- EXAM\n" +
                            "query2 AS (\n" +
                            "\tSELECT EXAM_ID, CLASS_ID FROM EXAM WHERE CLASS_ID = '"+userInput+"'\n" +
                            "),\n" +
                            "-- MERGE 1 AND 2\n" +
                            "query3 AS (\n" +
                            "\tSELECT CLASS_ID, AVG(SCORE) AVERAGE FROM Query1 JOIN Query2 ON Query1.EXAM_ID = Query2.EXAM_ID GROUP BY CLASS_ID\n" +
                            "),\n" +
                            "-- CLASS\n" +
                            "query4 AS (\n" +
                            "\tSELECT CLASS_ID, CLASS_NAME FROM CLASS\n" +
                            ")\n" +
                            " SELECT query3.CLASS_ID, query4.CLASS_NAME, AVERAGE FROM query3, query4 WHERE query3.CLASS_ID = query4.CLASS_ID ";
                    ResultSet listAClassSet = utility.sqlSelector(conn, sqlClass);
                    while (listAClassSet.next()){
                        classID = listAClassSet.getString("CLASS_ID");
                        className = listAClassSet.getString("CLASS_NAME");
                        avgClass = String.valueOf(listAClassSet.getInt("AVERAGE"));

                        aClassTable.add(classID, className, avgClass);
                    }
                    utility.oracleEndCon(conn);
                    // Print out
                    aClassTable.printAll();;
                    break;

            }


            // Terminate
            System.out.println("Exit to main menu? 1 for exit. 0 for go back to choose respective functions.");
            terminate = userInput("Invalid input: 1 for exit. 0 for go back to choose respective functions.");
        }


    }

    private boolean userInput(String errorMessage) {
        String tempInput;

        tempInput = scanner.nextLine();
        while (!tempInput.equals("1") && !tempInput.equals("0")){
            System.out.println(errorMessage);
            tempInput = scanner.nextLine();
        }
        return tempInput.equals("1");
    }
}

