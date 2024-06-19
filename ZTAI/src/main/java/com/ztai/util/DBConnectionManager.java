package com.ztai.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {

    private static final String URL = "jdbc:mysql://localhost:3306/coding";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "admin";
    private static Connection con ;

    // Private constructor to prevent instantiation
    private DBConnectionManager() {}

    // Method to get a database connection
    public static synchronized Connection getConnection() throws SQLException {
        try {
            if (con == null || con.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            // Log or handle the exception appropriately
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        return con;
    }

    // Method to close a database connection
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
