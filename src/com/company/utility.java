package com.company;

import oracle.jdbc.driver.OracleConnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class utility {
    public static final String usernameForServer ="\""";   // Your Oracle Account ID
    public static final String passwordForServer ="";        // Password of Oracle Account

    public static void clearScreen(){

        //Clears Screen in java

        System.out.print("\033[H\033[2J");
        System.out.flush();

    }

    public static void sqlRunner (String sqlStatement) throws SQLException {
        String serverUsername, serverPassword;
        serverUsername = usernameForServer;
        serverPassword = passwordForServer;

        // Connection
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection(
                "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms",
                serverUsername, serverPassword);

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sqlStatement);

        conn.close();
    }

    /**
     * Start connection of Oracle server
     *
     * @return - OracleConnection
     * @throws SQLException - SQL Exception
     */
    public static OracleConnection oracleStartCon () throws SQLException {
        String serverUsername, serverPassword;
        serverUsername = usernameForServer;
        serverPassword = passwordForServer;

        // Connection
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection(
                "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms",
                serverUsername, serverPassword);
        return conn;
    }

    /**
     * SELECT from Oracle server
     *
     * @param conn - OracleConnection
     * @param sqlStatement - sql Statement
     * @return - ResultSet
     * @throws SQLException - SQL Exception
     */
    public static ResultSet sqlSelector (OracleConnection conn, String sqlStatement) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet output = stmt.executeQuery(sqlStatement);
        return output;
    }

    /**
     *  INSERT OR UPDATE Oracle server.
     *
     * @param conn - OracleConnection
     * @param sqlStatement - sql Statement
     * @throws SQLException - SQL Exception
     */
    public static void sqlUpdater (OracleConnection conn, String sqlStatement) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sqlStatement);
    }

    /**
     * End connection of Oracle server
     *
     * @param conn - OracleConnection
     * @throws SQLException - SQL Exception
     */
    public static void oracleEndCon (OracleConnection conn) throws SQLException {
        conn.close();
    }


}
