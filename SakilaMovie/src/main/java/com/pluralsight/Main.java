package com.pluralsight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: java -jar SakilaMovies.jar <DB_URL> <USERNAME> <PASSWORD>");
            System.exit(1);
        }

        String dbUrl = args[2];
        String dbUser = args[0];
        String dbPassword = args[1];

        // Initialize DataSource with CLI credentials
        DataSourceFactory.initializeDataSource(dbUrl, dbUser, dbPassword);

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter an actor's last name: ");
        String lastName = scanner.nextLine();
        searchActorsByLastName(lastName);

        System.out.print("\nEnter an actor's first and last name to see their movies: ");
        String firstName = scanner.next();
        String fullLastName = scanner.next();
        searchMoviesByActor(firstName, fullLastName);

        scanner.close();
    }

    private static void searchActorsByLastName(String lastName) {
        String query = "SELECT actor_id, first_name, last_name FROM actor WHERE last_name = ?";

        try (Connection conn = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, lastName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nYour matches:");
                    do {
                        System.out.printf("ID: %d | Name: %s %s%n",
                                rs.getInt("actor_id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"));
                    } while (rs.next());
                } else {
                    System.out.println("No matches found!");
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving actors: " + e.getMessage());
        }
    }

    private static void searchMoviesByActor(String firstName, String lastName) {
        String query = "SELECT f.title FROM film f " +
                "JOIN film_actor fa ON f.film_id = fa.film_id " +
                "JOIN actor a ON fa.actor_id = a.actor_id " +
                "WHERE a.first_name = ? AND a.last_name = ?";

        try (Connection conn = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nMovies featuring " + firstName + " " + lastName + ":");
                    do {
                        System.out.println("- " + rs.getString("title"));
                    } while (rs.next());
                } else {
                    System.out.println("No movies found for this actor!");
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving movies: " + e.getMessage());
        }
    }
}
