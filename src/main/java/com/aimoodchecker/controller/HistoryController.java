package com.aimoodchecker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.aimoodchecker.dao.DBConnection;
import com.aimoodchecker.model.MoodEntry;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class HistoryController implements RoutedController {
    
    private AppController app;
    
    @FXML private TableView<MoodEntry> historyTable;
    @FXML private TableColumn<MoodEntry, String> dateColumn;
    @FXML private TableColumn<MoodEntry, String> moodColumn;
    @FXML private TableColumn<MoodEntry, String> descriptionColumn;
    @FXML private TableColumn<MoodEntry, String> sentimentColumn;
    @FXML private Label noDataLabel;

    @FXML
    private void initialize() {
        // Set up the TableView columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        moodColumn.setCellValueFactory(new PropertyValueFactory<>("moodType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        sentimentColumn.setCellValueFactory(new PropertyValueFactory<>("sentimentCategory"));
    }
    
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
     * Loads mood history from the database and displays it in the TableView
     */
    private void loadMoodHistory() {
        try {
            // SQL command to get all mood entries, newest first
            String sql = "SELECT * FROM mood_entries ORDER BY date DESC, created_at DESC";
            
            ObservableList<MoodEntry> moodEntries = FXCollections.observableArrayList();
            
            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                System.out.println("=== Loading Mood History ===");
                
                // Loop through each row (each mood entry)
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String dateStr = rs.getString("date");
                    String moodType = rs.getString("mood_type");
                    String description = rs.getString("description");
                    Double sentimentScore = rs.getDouble("sentiment_score");
                    String createdAt = rs.getString("created_at");

                    LocalDate date = null;
                    try {
                        date = LocalDate.parse(dateStr);
                    } catch (Exception e) {
                        System.err.println("Error parsing date: " + dateStr);
                    }
                    
                    // Create MoodEntry object and add to list
                    MoodEntry entry = new MoodEntry(id, date, moodType, description, sentimentScore, createdAt);
                    moodEntries.add(entry);
                    
                    // Print each mood entry (for debugging)
                    System.out.println("ID: " + id + " | Date: " + date + " | Mood: " + moodType + " | Description: " + description + " | Sentiment Score: " + sentimentScore);
                }
                
                System.out.println("=== End of Mood History ===");
                
                // Update the TableView with the data
                historyTable.setItems(moodEntries);
                
                // Show/hide no data label
                if (moodEntries.isEmpty()) {
                    noDataLabel.setVisible(true);
                    historyTable.setVisible(false);
                } else {
                    noDataLabel.setVisible(false);
                    historyTable.setVisible(true);
                    
                }
                
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
