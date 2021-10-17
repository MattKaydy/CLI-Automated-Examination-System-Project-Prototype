package com.company;

import oracle.jdbc.driver.OracleConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class recordChecker {
    String currentUserID;
    utility util;
    Scanner scanner;

    public recordChecker(String currentUserID) {
        this.currentUserID = currentUserID;
        util = new utility();
        scanner = new Scanner(System.in);
    }

    public void application() {
        util.clearScreen();

        ArrayList<String[]> ExamInfo = new ArrayList<>();

        ResultSet rset = null;
        try {
            OracleConnection conn = utility.oracleStartCon();
            rset = utility.sqlSelector(conn,"SELECT EXAM_ID FROM STUDENT_SCORE WHERE STUDENT_ID ='" + this.currentUserID + "' GROUP BY EXAM_ID");
            while (rset.next()) {
                ExamInfo.add(new String[]{rset.getString("EXAM_ID"), "", ""});
            }
            utility.oracleEndCon(conn);
        } catch (SQLException throwables) {
            System.out.println("No result to be displayed!");
        }


        for(int i=0; i<ExamInfo.size(); i++){
            try {
                OracleConnection conn = utility.oracleStartCon();
                rset = utility.sqlSelector(conn,"SELECT SUM(SCORE) FROM STUDENT_SCORE WHERE STUDENT_ID ='" + this.currentUserID + "' AND EXAM_ID ='"+ExamInfo.get(i)[0]+"'");
                rset.next();
                int studentScore = rset.getInt("SUM(SCORE)");
                utility.oracleEndCon(conn);

                conn = utility.oracleStartCon();
                rset = utility.sqlSelector(conn,"SELECT SUM(FULL_MARK) FROM EXAM_QUESTIONS WHERE EXAM_ID ='"+ExamInfo.get(i)[0]+"' AND IS_COMPULSORY = '1' ");
                rset.next();
                int FullMark = rset.getInt("SUM(FULL_MARK)");
                utility.oracleEndCon(conn);
                ExamInfo.set(i, new String[]{ExamInfo.get(i)[0], Integer.toString(studentScore), Integer.toString(FullMark)});

            } catch (SQLException throwables) {
                System.out.println("No result to be displayed!");
            }
        }
        double totalSocre = 0;
        for (String[] strings : ExamInfo){

            totalSocre += ((double)Integer.parseInt(strings[1])/(double)Integer.parseInt(strings[2]))*100;
        }
        totalSocre = totalSocre/(double)ExamInfo.size();

        System.out.printf("%-15s %-12s %-10s %n", "Subject Name", "Test No.", "Grade");
        System.out.println("==========================================");
        String subjectName,testNo,grade;
        double studentScore;
        try{
            for (String[] strings : ExamInfo) {
                OracleConnection conn = utility.oracleStartCon();
                rset = utility.sqlSelector(conn, "SELECT SUBJECT_NAME FROM SUBJECT_NAME WHERE SUBJECT_ID = (SELECT SUBJECT_ID FROM EXAM WHERE EXAM_ID = '" + strings[0] + "')");
                rset.next();
                subjectName = rset.getString("SUBJECT_NAME");
                utility.oracleEndCon(conn);

                conn = utility.oracleStartCon();
                rset = utility.sqlSelector(conn, "SELECT TEST_NO FROM EXAM WHERE EXAM_ID = '" + strings[0] + "'");
                rset.next();
                testNo = rset.getString("TEST_NO");
                utility.oracleEndCon(conn);


                studentScore = (double)Integer.parseInt(strings[1])/(double)Integer.parseInt(strings[2]);
                studentScore = studentScore * 100;
                conn = utility.oracleStartCon();
                rset = utility.sqlSelector(conn, "SELECT GRADE FROM GRADES WHERE MAXVALUE < " + studentScore);
                rset.next();
                grade = rset.getString("GRADE");
                utility.oracleEndCon(conn);


                System.out.printf("%-15s %-12s %-10s %n", subjectName, testNo, grade);
            }
            System.out.println("==========================================");
            String finalGrade;
            OracleConnection conn = utility.oracleStartCon();
            rset = utility.sqlSelector(conn, "SELECT GRADE FROM GRADES WHERE MAXVALUE < " + totalSocre);
            rset.next();
            finalGrade = rset.getString("GRADE");
            utility.oracleEndCon(conn);
            System.out.println("Average Grade: "+finalGrade);
            System.out.println("==========================================");
            System.out.println();
            System.out.println("Press Enter to continue...");
            String temp = scanner.nextLine();

        }catch (SQLException throwables) {
            System.out.println("No result to be displayed!");
            throwables.printStackTrace();
            System.out.println("Press Enter to continue...");
            String temp = scanner.nextLine();
        }
        utility.clearScreen();
    }

}
