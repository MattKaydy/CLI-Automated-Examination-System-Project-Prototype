package com.company;

import oracle.jdbc.driver.OracleConnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class testDesigner {
    String currentUserID;
    Scanner scanner;

    public testDesigner(String currentUserID) {
        this.currentUserID = currentUserID;
        scanner = new Scanner(System.in);
    }

    public int findMaxExamID() throws SQLException {
        OracleConnection conn = utility.oracleStartCon();
        ResultSet rset = utility.sqlSelector(conn,"SELECT MAX(EXAM_ID),count(*) FROM EXAM");
        rset.next();
        if(rset.getInt("count(*)")==0){
            conn.close();
            return 0;
        }else{
            int temp = rset.getInt("MAX(EXAM_ID)");
            utility.oracleEndCon(conn);
            return(temp);
        }
    }

public void printSubjectByTeacherID(String TEACHER_ID) throws SQLException {
    OracleConnection conn = utility.oracleStartCon();
    ResultSet rset = utility.sqlSelector(conn,"SELECT * FROM SUBJECT_NAME WHERE SUBJECT_ID IN (SELECT SUBJECT_ID FROM SUBJECT WHERE TEACHER_ID = '"+TEACHER_ID+"') ORDER BY SUBJECT_ID");

    System.out.println("Below are the subject(s) that you are teaching:");
    System.out.printf("%-20s %-15s %n", "Subject Name", "Subject ID");
    System.out.println("================================");
    while (rset.next()){
        System.out.printf("%-20s %-15s %n", rset.getString("SUBJECT_NAME"), rset.getString("SUBJECT_ID"));
    }
    utility.oracleEndCon(conn);
    System.out.println("================================");

}


    public void printClassByTeacherID(String TEACHER_ID) throws SQLException {
        OracleConnection conn = utility.oracleStartCon();
        ResultSet rset = utility.sqlSelector(conn,"SELECT CLASS_NAME,CLASS_ID FROM CLASS WHERE TEACHER_ID = '"+TEACHER_ID+"'");
        System.out.println("Below are the class(es) that you are teaching:");
        System.out.printf("%-20s %-15s %n", "Class Name", "Class ID");
        System.out.println("================================");
        while (rset.next()){
            System.out.printf("%-20s %-15s %n", rset.getString("CLASS_NAME"), rset.getString("CLASS_ID"));
        }
        utility.oracleEndCon(conn);
        System.out.println("================================");

    }

    public void application() throws SQLException {
        utility.clearScreen();
        String sqlStatement ="";
        String temp = "";
        String SUBJECT_ID = "";
        String CLASS_ID = "";
        String TEST_NO = "";
        String EXAM_DATE = "";
        String EXAM_TIME = "";
        String EXAM_DURATION = "";
        int EXAM_ID = findMaxExamID()+1;
        boolean ExamCreatedFlag = false;

        System.out.println("Here is the EXAM_ID: "+EXAM_ID);
        System.out.println("Which subject is this exam for? (Please enter a Subject ID)");
        System.out.println();
        printSubjectByTeacherID(this.currentUserID);
        SUBJECT_ID = scanner.nextLine();
        System.out.println();
        System.out.println("Which class is this exam for? (Please enter a Class ID)");
        System.out.println();
        printClassByTeacherID(this.currentUserID);
        CLASS_ID = scanner.nextLine();
        System.out.println();
        System.out.println("Please enter the exam number: ");
        TEST_NO = scanner.nextLine();
        System.out.println();
        System.out.println("What is the date of the exam? (DD-MM-YYYY)");
        EXAM_DATE =scanner.nextLine();
        System.out.println();
        System.out.println("What is the time of the exam? (HH:MM 24-Hr format)");
        EXAM_TIME =scanner.nextLine();
        System.out.println();
        System.out.println("What is the duration of the exam? (mins)");
        EXAM_DURATION =scanner.nextLine();
        System.out.println();

        System.out.println("Confirm to add this Exam? (y/n)");
        temp = scanner.nextLine();
        while(!(temp.equals("y") || temp.equals("n"))){
            System.out.println("Invalid Input - please input y/n");
            System.out.println("Confirm to add this Exam? (y/n)");
            temp = scanner.nextLine();
        }
        if(temp.equals("y")){
            OracleConnection conn = utility.oracleStartCon();
            utility.sqlUpdater(conn,"INSERT INTO EXAM VALUES('"+EXAM_ID+"', '"+CLASS_ID+"', '"+SUBJECT_ID+"', '"+TEST_NO+"', '"+currentUserID+"', to_date('"+EXAM_DATE+"','dd-mm-yyyy'), '"+EXAM_TIME+"', '"+EXAM_DURATION+"')");
            utility.oracleEndCon(conn);
            try{
                utility.sqlRunner(sqlStatement);
            }catch (SQLException throwables){
                System.out.print("Invalid Input!");
                utility.oracleEndCon(conn);
            }
            ExamCreatedFlag = true;
        }else{
        System.out.println("The question had been deleted.");
        System.out.println("Press enter to continue ...");
        temp = scanner.nextLine();
            utility.clearScreen();
        }



        utility.clearScreen();



        boolean completeFlag = false;
        int questionNo = 1;
        String QUESTION_NO = "";
        String IS_COMPULSORY = "";
        String FULL_MARK = "";
        String QUESTION_TYPE = "";
        String QUESTION_DESC = "";
        String ANSWER = "";


        while(!completeFlag && ExamCreatedFlag){
            System.out.println("Question "+questionNo+":");
            System.out.println("Is the question compulsory? (0 for not compulsory, 1 for compulsory)");
            IS_COMPULSORY = scanner.nextLine();
            while (!(IS_COMPULSORY.equals("0")||IS_COMPULSORY.equals("1"))){
                System.out.println("Invalid Input - Please enter the again");
                IS_COMPULSORY = scanner.nextLine();
            }
            System.out.println();
            System.out.println("What is the full mark for this question?");
            FULL_MARK = scanner.nextLine();
            System.out.println();
            System.out.println("What is the question type?");
            System.out.println("(Please input 0:multiple-choice  1:fill in the blank  2:standard full-length test questions)");
            QUESTION_TYPE = scanner.nextLine();
            while (!(QUESTION_TYPE.equals("0")||QUESTION_TYPE.equals("1")||QUESTION_TYPE.equals("2"))){
                System.out.println("Invalid Input - Please enter the question type again");
                QUESTION_TYPE = scanner.nextLine();
            }
            System.out.println();
            System.out.println("Please enter the question");
            QUESTION_DESC = scanner.nextLine();
            System.out.println();
            System.out.println("Please enter the answer for the question");
            ANSWER = scanner.nextLine();
            if(QUESTION_TYPE.equals("0")){
                while (!(ANSWER.equals("A")||ANSWER.equals("B")||ANSWER.equals("C")||ANSWER.equals("D"))){
                    System.out.println("Invalid Input - Please enter the answer again");
                    ANSWER = scanner.nextLine();
                }
            }
            System.out.println();

            System.out.println("Confirm to add this question? (y/n)");
            temp = scanner.nextLine();
            while(!(temp.equals("y") || temp.equals("n"))){
                System.out.println("Invalid Input - please input y/n");
                System.out.println("Confirm to add this question? (y/n)");
                temp = scanner.nextLine();
            }
            if(temp.equals("y")){
                try{
                    OracleConnection conn = utility.oracleStartCon();
                    utility.sqlUpdater(conn,"INSERT INTO EXAM_QUESTIONS VALUES('"+EXAM_ID+"', '"+questionNo+"', '"+IS_COMPULSORY+"', '"+FULL_MARK+"', '"+QUESTION_TYPE+"', '"+QUESTION_DESC+"', '"+ANSWER+"')");
                    utility.oracleEndCon(conn);
                    questionNo++;
                }catch (SQLException throwables){
                    throwables.printStackTrace();
                    System.out.print("Invalid Input!");
                }
            }else{
                System.out.println("The question had been deleted.");
                System.out.println("Press enter to continue ...");
                temp = scanner.nextLine();
                utility.clearScreen();
            }


            System.out.println("Any more question? (y/n)");
            temp = scanner.nextLine();
            while(!(temp.equals("y") || temp.equals("n"))){
                System.out.println("Invalid Input - please input y/n");
                System.out.println("Any more question? (y/n)");
                temp = scanner.nextLine();
            }
            if(temp.equals("y") || temp.equals("n"))
                if(temp.equals("n")) completeFlag = true;
            utility.clearScreen();
        }



    }
}