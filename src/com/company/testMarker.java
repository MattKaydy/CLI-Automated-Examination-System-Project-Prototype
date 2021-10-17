package com.company;

import oracle.jdbc.driver.OracleConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class testMarker {
    String currentUserID;
    Scanner scanner;

    public testMarker(String currentUserID) {
        this.currentUserID = currentUserID;
        scanner = new Scanner(System.in);
    }

    public void application() throws SQLException {
        int examID;

        OracleConnection conn;
        OracleConnection connTwo;

        Table examIDTable;
        Table reviewTable;
        Table questionTable;
        Table eachStudentAnsInEachQueTable;

        boolean terminate = false;

        while(!terminate) {
            // Create tables
            examIDTable = new Table("EXAM_ID", 15, "SUBJECT_NAME", 20, "CLASS_NAME", 12, "TEST_NO", 9);
            reviewTable = new Table("Review", 30, "", 20);
            questionTable = new Table("Question no.", 15, "Full mark", 15, "Question", 68);


            utility.clearScreen();
            System.out.println("Now running test marker.");

            //print out all Exam ID that is related to user.
            System.out.println("Here is all of your exam: ");
            conn = utility.oracleStartCon();
            String sqlEXAM_ID = "WITH\n" +
                    "-- CLASS\n" +
                    "Query1 AS (\n" +
                    "\tSELECT CLASS_ID, CLASS_NAME FROM CLASS\n" +
                    "),\n" +
                    "-- EXAM\n" +
                    "Query2 AS (\n" +
                    "\tSELECT EXAM_ID, SUBJECT_ID, TEST_NO, CLASS_ID FROM EXAM WHERE TEACHER_ID = '"+currentUserID+"'\n" +
                    "),\n" +
                    "-- Merge Exam And Subject_Name\n" +
                    "Query3 AS (\n" +
                    "\tSELECT * FROM Query2 JOIN SUBJECT_NAME ON Query2.SUBJECT_ID = SUBJECT_NAME.SUBJECT_ID\n" +
                    "),\n" +
                    "-- Merge 1 And 3\n" +
                    "Query4 AS (\n" +
                    "\tSELECT * FROM Query1 JOIN Query3 ON Query1.CLASS_ID = Query3.CLASS_ID\n" +
                    ")\n" +
                    "SELECT EXAM_ID, SUBJECT_NAME, CLASS_NAME, TEST_NO FROM Query4";
            ResultSet EXAM_IDSet = utility.sqlSelector(conn, sqlEXAM_ID);
            while (EXAM_IDSet.next()){
                examIDTable.add(String.valueOf(EXAM_IDSet.getInt("EXAM_ID")), EXAM_IDSet.getString("SUBJECT_NAME"), EXAM_IDSet.getString("CLASS_NAME"), EXAM_IDSet.getString("TEST_NO"));
            }
            utility.oracleEndCon(conn);
            examIDTable.printAll();

            // Input Exam ID
            System.out.println("Please input Exam ID for marking:");
            do {
                examID = Integer.parseInt(scanner.nextLine());
            } while (!validExamID(examID));

            // Put examID into reviewTable
            reviewTable.add("Exam ID:", String.valueOf(examID));

            // Getting Number of Text Question from database into reviewTable
            int numberOfTextQ = 0;
            conn = utility.oracleStartCon();
            String sqlOne = "SELECT COUNT(QUESTION_TYPE) FROM EXAM_QUESTIONS WHERE EXAM_ID = '"+examID+"' AND QUESTION_TYPE = 2 GROUP BY QUESTION_TYPE";
            ResultSet numberOfTextQSet = utility.sqlSelector(conn, sqlOne);
            while (numberOfTextQSet.next()){
                numberOfTextQ = numberOfTextQSet.getInt("COUNT(QUESTION_TYPE)");
                reviewTable.add("Number of Text Question:", String.valueOf(numberOfTextQSet.getInt("COUNT(QUESTION_TYPE)")));
            }
            utility.oracleEndCon(conn);

            // Getting Number of student from database into reviewTable
            int numberOfStu = 0;
            conn = utility.oracleStartCon();
            String sqlTwo = "SELECT COUNT(STUDENT_ID) FROM EXAM, STUDENT WHERE EXAM_ID = '"+examID+"' AND EXAM.CLASS_ID = STUDENT.CLASS_ID GROUP BY EXAM_ID";
            ResultSet numberOfStuSet = utility.sqlSelector(conn, sqlTwo);
            while (numberOfStuSet.next()){
                numberOfStu = numberOfStuSet.getInt("COUNT(STUDENT_ID)");
                reviewTable.add("Number of student:", String.valueOf(numberOfStuSet.getInt("COUNT(STUDENT_ID)")));
            }
            utility.oracleEndCon(conn);

            // Print table review
            utility.clearScreen();
            reviewTable.printAll();
            System.out.print("\n");     // Indentation

            // Confirm to proceed
            System.out.println("Confirm to proceed? 1 for proceed, 0 for exit");
            terminate = userInput("Invalid input: 1 for proceed, 0 for exit");
            if(!terminate)
                break;


            // Add Question number, Question and Question full mark into eachQuestionTable
            conn = utility.oracleStartCon();
            String sqlThree = "SELECT QUESTION_NO, FULL_MARK, QUESTION_DESC FROM EXAM_QUESTIONS WHERE EXAM_ID = '"+examID+"' AND QUESTION_TYPE = 2";
            ResultSet eachQuestionSet = utility.sqlSelector(conn, sqlThree);
            while (eachQuestionSet.next()){
                String quesNo = String.valueOf(eachQuestionSet.getInt("QUESTION_NO"));
                String fullMark = String.valueOf(eachQuestionSet.getInt("FULL_MARK"));
                String question = eachQuestionSet.getString("QUESTION_DESC");
                questionTable.add(quesNo, fullMark, question);
            }
            utility.oracleEndCon(conn);


            // marking for each question
            int numberOfQuestion = questionTable.getNumberOfRows();
            for(int i = 0; i < numberOfQuestion; i++) {
                utility.clearScreen();
                eachStudentAnsInEachQueTable = new Table("Student ID", 14, "Student first name", 50, "Student last name", 50, "Answer", 150);
                questionTable.printTitle();
                System.out.println(questionTable.getARow(i));
                System.out.print("\n");     // Indentation

                // Add Student ID, Student name and Student Ans into eachStudentAnsInEachQueTable
                int questionNo = Integer.parseInt((String) questionTable.getAColumn(0).get(i));
                conn = utility.oracleStartCon();
                String sqlFour = "WITH\n" +
                        "QUERY1 AS (\n" +
                        "\tSELECT STUDENT_ID, FNAME, LNAME FROM STUDENT\n" +
                        "),\n" +
                        "QUERY2 AS (\n" +
                        "\tSELECT STUDENT_ID, STUDENT_ANSWER_DESC FROM STUDENT_ANSWER WHERE EXAM_ID = '"+examID+"' AND QUESTION_NO = '"+questionNo+"'\n" +
                        ")\n" +
                        "SELECT QUERY1.STUDENT_ID, QUERY1.FNAME, QUERY1.LNAME, QUERY2.STUDENT_ANSWER_DESC\n" +
                        "FROM QUERY1, QUERY2\n" +
                        "WHERE QUERY1.STUDENT_ID = QUERY2.STUDENT_ID";
                ResultSet eachQuestionAnsSet = utility.sqlSelector(conn, sqlFour);
                while (eachQuestionAnsSet.next()){
                    String STUDENT_ID = eachQuestionAnsSet.getString("STUDENT_ID");
                    String FName = eachQuestionAnsSet.getString("FNAME");
                    String LName = eachQuestionAnsSet.getString("LNAME");
                    String Ans = eachQuestionAnsSet.getString("STUDENT_ANSWER_DESC");
                    eachStudentAnsInEachQueTable.add(STUDENT_ID, FName, LName, Ans);
                }
                utility.oracleEndCon(conn);

                // Print and mark the answer for each student
                int numberOfStudent = eachStudentAnsInEachQueTable.getNumberOfRows();
                for(int m = 0; m < numberOfStudent; m++) {
                    eachStudentAnsInEachQueTable.printTitle();
                    System.out.println(eachStudentAnsInEachQueTable.getARow(m));

                    // Get STUDENT_ID
                    String STUDENT_ID = (String) eachStudentAnsInEachQueTable.getAColumn(0).get(m);

                    // Input mark
                    int mark = 0;
                    System.out.print("Mark: ");
                    do {
                        mark = Integer.parseInt(scanner.nextLine());
                    } while (!validMark(examID, questionNo, mark));

                    // put mark into STUDENT_SCORE

                    try {
                        conn = utility.oracleStartCon();
                        String sqlFive ="INSERT INTO STUDENT_SCORE VALUES ('"+examID+"', '"+questionNo+"', '"+STUDENT_ID+"', '"+mark+"')";
                        utility.sqlUpdater(conn, sqlFive);
                        utility.oracleEndCon(conn);
                    } catch (SQLIntegrityConstraintViolationException e) {
                        System.out.println("Already marked. Replace the previous one? 1 for replace, 0 for discard.");
                        if(userInput("Invalid input. 1 for replace, 0 for discard.")) {
                            connTwo = utility.oracleStartCon();
                            String sqlSix ="UPDATE STUDENT_SCORE SET SCORE = '"+mark+"' WHERE EXAM_ID = '"+examID+"' AND QUESTION_NO = '"+questionNo+"' AND STUDENT_ID = '"+STUDENT_ID+"'";
                            utility.sqlUpdater(connTwo, sqlSix);
                            utility.oracleEndCon(connTwo);
                        }
                    } finally {
                        utility.oracleEndCon(conn);
                    }


                }
                System.out.print("\n\n");     // Indentation
            }


            // Terminate
            System.out.println("No more question. Exit? 1 for exit. 0 for mark another exam.");
            terminate = userInput("Invalid input: 1 for exit. 0 for mark another exam.");
        }

    }

    /**
     * Check the validation of Exam ID
     *
     * @param examID - Exam ID
     * @return - True if it is valid, vice versa
     * @throws SQLException - SQLException
     */
    private boolean validExamID(int examID) throws SQLException {
        // Check inputted Exam ID exist or not
        OracleConnection conn = utility.oracleStartCon();
        String sql = "SELECT COUNT(EXAM_ID) FROM EXAM WHERE EXAM_ID = '"+examID+"' GROUP BY EXAM_ID";
        ResultSet numberOfExamID = utility.sqlSelector(conn, sql);
        while (numberOfExamID.next()){
            if(numberOfExamID.getInt("COUNT(EXAM_ID)") == 1) {
                utility.oracleEndCon(conn);
                return true;
            }
        }
        utility.oracleEndCon(conn);
        System.out.println("No EXAM ID can be found. Please try again: ");
        return false;
    }

    /**
     * Check the validation of mark
     *
     * @param examID - Exam ID
     * @param questionNo - question number
     * @param mark - mark
     * @return - True if it is valid, vice versa
     * @throws SQLException - SQLException
     */
    private boolean validMark(int examID, int questionNo, int mark) throws SQLException {
        OracleConnection conn = utility.oracleStartCon();
        String sql = "SELECT FULL_MARK FROM EXAM_QUESTIONS WHERE EXAM_ID = '"+examID+"' AND QUESTION_NO = '"+questionNo+"' ";
        ResultSet numberOfExamID = utility.sqlSelector(conn, sql);
        while (numberOfExamID.next()){
            if(numberOfExamID.getInt("FULL_MARK") >= mark) {
                utility.oracleEndCon(conn);
                return true;
            }
        }
        utility.oracleEndCon(conn);
        System.out.println("Invalid mark. Please try again: ");
        return false;
    }

    /**
     * Internal use for method application. Get user input
     *
     * @param errorMessage - error message if neither 1 or 0 is inputted.
     * @return - if user input "1", return true; if input 0, return false.
     */
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