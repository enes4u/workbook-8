package com.pluralsight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3307/northwind";
        String user = "root"; //
        String password = "yearup"; //

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ProductName FROM Products")) {

            while (rs.next()) {
                System.out.println("Product: " + rs.getString("ProductName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
