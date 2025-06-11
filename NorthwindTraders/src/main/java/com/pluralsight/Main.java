package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = args[2];
        String user = args[0]; //
        String password = args[1]; //


        Scanner scanner = new Scanner(System.in);
        // Initialize DataSource with CLI credentials
        DataSourceFactory.initializeDataSource(url, user, password);
        int choice;

        do {
            System.out.println("\nWhat do you want to do?");
            System.out.println("1) Display all products");
            System.out.println("2) Display all customers");
            System.out.println("3) Display all categories");
            System.out.println("0) Exit");
            System.out.print("Select an option: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayProducts();
                    break;
                case 2:
                    displayCustomers();
                    break;
                case 3:
                    displayCategories(scanner);
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
    private static void displayProducts() {
        try (Connection conn = DataSourceFactory.getDataSource().getConnection();
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
    private static void displayCustomers() {
        try (Connection conn = DataSourceFactory.getDataSource().getConnection();
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
    private static void displayCategories( Scanner scanner) {
        String query = "SELECT CategoryID, CategoryName FROM Categories ORDER BY CategoryID";

        try (Connection conn = DataSourceFactory.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nCategories List:");
            System.out.printf("%-15s %-35s%n", "Category ID", "Category Name");
            System.out.println("-------------- ---------------------");

            while (rs.next()) {
                System.out.printf("%-15d %-35s%n",
                        rs.getInt("CategoryID"),
                        rs.getString("CategoryName"));
            }

            System.out.print("\nEnter a Category ID to view products: ");
            int categoryId = scanner.nextInt();
            displayProductsByCategory(categoryId);

        } catch (SQLException e) {
            System.err.println("Error retrieving categories: " + e.getMessage());
        }
    }

    private static void displayProductsByCategory( int categoryId) {
        String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products WHERE CategoryID = ?";

        try (Connection conn = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nProducts in Selected Category:");
                System.out.printf("%-15s %-35s %-12s %-15s%n", "Id", "Name", "Price", "Stock");
                System.out.println("----      ---------------------                 -------------  --------");

                while (rs.next()) {
                    System.out.printf("%-10d %-40s %-12.2f %-15d%n",
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getDouble("UnitPrice"),
                            rs.getInt("UnitsInStock"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving products for category " + categoryId + ": " + e.getMessage());
        }
    }
}
