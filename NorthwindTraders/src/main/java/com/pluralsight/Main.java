package com.pluralsight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3307/northwind";
        String user = args[0]; //
        String password = args[1]; //

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products")) {
            System.out.println("Option 1: Stacked Information");
            while (rs.next()) {
                System.out.println("Product Id: " + rs.getInt("ProductID"));
                System.out.println("Name: " + rs.getString("ProductName"));
                System.out.println("Price: " + rs.getDouble("UnitPrice"));
                System.out.println("Stock: " + rs.getInt("UnitsInStock"));
                System.out.println("------------------");
            }

            // Reset cursor for second display format
            //rs.beforeFirst(); //forward only not working now
            rs.close();
            ResultSet rs2 = stmt.executeQuery("SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products");

            System.out.println("\nOption 2: Rows of Information");
            System.out.printf("%-15s %-35s %-12s %-15s%n", "Id", "Name", "Price", "Stock");
            System.out.println("----      ---------------------                 -------------  --------");
            while (rs2.next()) {
                System.out.printf("%-15d %-35s %-12.2f %-15d%n",
                        rs2.getInt("ProductID"),
                        rs2.getString("ProductName"),
                        rs2.getDouble("UnitPrice"),
                        rs2.getInt("UnitsInStock"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        /* try -catch closes resources automatically after code block*/
    }
}
