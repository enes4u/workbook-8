package com.pluralsight;

import com.pluralsight.model.Actor;
import com.pluralsight.model.Film;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private DataSource dataSource;

    public DataManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Search for actors by last name
    public List<Actor> searchActorsByLastName(String lastName) {
        List<Actor> actors = new ArrayList<>();
        String query = "SELECT actor_id, first_name, last_name FROM actor WHERE last_name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, lastName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    actors.add(new Actor(rs.getInt("actor_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name")));
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving actors: " + e.getMessage());
        }

        return actors;
    }

    // Get a list of films by actor ID
    public List<Film> getFilmsByActorId(int actorId) {
        List<Film> films = new ArrayList<>();
        String query = "SELECT f.film_id, f.title, f.description, f.release_year, f.length " +
                "FROM film f " +
                "JOIN film_actor fa ON f.film_id = fa.film_id " +
                "WHERE fa.actor_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, actorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    films.add(new Film(rs.getInt("film_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getInt("release_year"),
                            rs.getInt("length")));
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving films: " + e.getMessage());
        }

        return films;
    }
}
