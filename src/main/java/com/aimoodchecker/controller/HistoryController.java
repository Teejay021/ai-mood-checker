package com.aimoodchecker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import com.aimoodchecker.dao.DBConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryController implements RoutedController {
    
    private AppController app;
    
    @FXML private TableView<?> historyTable;
    @FXML private TableColumn<?, ?> dateColumn;
    @FXML private TableColumn<?, ?> moodColumn;
    @FXML private TableColumn<?, ?> descriptionColumn;
    @FXML private Label noDataLabel;

    @Override
    public void setApp(AppController app) {
        this.app = app;
        // Load mood history when this view is opened
        loadMoodHistory();
    }

    @FXML
    private void onRefresh() {
        // Load and display mood history data
        loadMoodHistory();
        app.setStatus("History refreshed");
    }
    
    /**
     * Loads mood history from the database and displays it
     */
    private void loadMoodHistory() {
        try {
            // SQL command to get all mood entries, newest first
            String sql = "SELECT * FROM mood_entries ORDER BY date DESC, created_at DESC";
            
            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                System.out.println("=== Loading Mood History ===");
                
                // Loop through each row (each mood entry)
                while (rs.next()) {
                    // Get data from each column
                    int id = rs.getInt("id");
                    String date = rs.getString("date");
                    String moodType = rs.getString("mood_type");
                    String description = rs.getString("description");
                    String createdAt = rs.getString("created_at");
                    
                    // Print each mood entry (for debugging)
                    System.out.println("ID: " + id + " | Date: " + date + " | Mood: " + moodType + " | Description: " + description);
                    
                    // TODO: Add this data to the TableView
                    // We'll implement this next!
                }
                
                System.out.println("=== End of Mood History ===");
                
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading mood history: " + e.getMessage());
            e.printStackTrace();
            app.setStatus("Error loading mood history");
        }
    }

    @FXML
    private void onBackHome() {
        app.goHome();
    }
}
