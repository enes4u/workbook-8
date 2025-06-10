package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3307/northwind";
        String user = args[0]; //
        String password = args[1]; //


        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nWhat do you want to do?");
            System.out.println("1) Display all products");
            System.out.println("2) Display all customers");
            System.out.println("0) Exit");
            System.out.print("Select an option: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayProducts(url, user, password);
                    break;
                case 2:
                    displayCustomers(url, user, password);
                    break;
                case 0:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }
    private static void displayProducts(String url, String user, String password) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products")) {
//            System.out.println("Option 1: Stacked Information");
//            while (rs.next()) {
//                System.out.println("Product Id: " + rs.getInt("ProductID"));
//                System.out.println("Name: " + rs.getString("ProductName"));
//                System.out.println("Price: " + rs.getDouble("UnitPrice"));
//                System.out.println("Stock: " + rs.getInt("UnitsInStock"));
//                System.out.println("------------------");
//            }
//
//            // Reset cursor for second display format
//            //rs.beforeFirst(); //forward only not working now
//            rs.close();
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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        /* try -with resources  closes resources automatically after code block*/
    }
    private static void displayCustomers(String url, String user, String password) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ContactName, CompanyName, City, Country, Phone FROM Customers ORDER BY Country")) {

            System.out.println("\nCustomer List:");
            System.out.println("------------------------------------------------------");
            while (rs.next()) {
                System.out.println("Contact Name: " + rs.getString("ContactName"));
                System.out.println("Company Name: " + rs.getString("CompanyName"));
                System.out.println("City: " + rs.getString("City"));
                System.out.println("Country: " + rs.getString("Country"));
                System.out.println("Phone: " + rs.getString("Phone"));
                System.out.println("------------------------------------------------------");
            }
        } catch (Exception e) {
            System.err.println("Error retrieving customers: " + e.getMessage());
        }
    }
}
