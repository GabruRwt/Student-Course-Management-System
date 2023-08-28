package com.assignment.management;

import  java.sql.*;
public class DatabaseConnection {
                String url = "jdbc:oracle:thin:@localhost:1521:XE";
        public Statement startingDB() {
            Statement stmt = null;
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection con = DriverManager.getConnection(url, "system", "1234");
                stmt = con.createStatement();
            } catch (SQLException e) {
                e.getMessage();
                System.out.println("Connection Not Established");
            }
            catch (Exception e){
                e.getMessage();
            }
            return stmt;
        }
}
