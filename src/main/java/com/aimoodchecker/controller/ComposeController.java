package com.aimoodchecker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import com.aimoodchecker.service.SentimentService;
import com.aimoodchecker.service.ChatGPTService;
import com.aimoodchecker.repository.EntryRepository;
import com.aimoodchecker.dao.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ComposeController implements RoutedController, NeedsDeps {
    
    private AppController app;
    private EntryRepository repo;
    private SentimentService sentiment;
    private ChatGPTService chatGPT;

    @FXML private TextArea moodText;
    @FXML private Label selectedMoodLabel;

    @FXML
    private void initialize() {
        // Set initial state
        selectedMoodLabel.setText("No mood selected yet");
        selectedMoodLabel.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
    }
    
    @Override public void setApp(AppController app) { this.app = app; }
    @Override public void init(EntryRepository r, SentimentService s, ChatGPTService c) { this.repo = r; this.sentiment = s; this.chatGPT = c; }


    private String selectedMood = "Neutral"; // Default mood
    private boolean hasExplicitlySelectedMood = false; // Track if user clicked a mood button

    @FXML private void onHappy() {
        selectedMood = "Happy";
        hasExplicitlySelectedMood = true;
        selectedMoodLabel.setText("Selected Mood: 😊 Happy");
        selectedMoodLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        app.setStatus("Mood set to Happy 😊");
    }

    @FXML private void onNeutral() {
        selectedMood = "Neutral";
        hasExplicitlySelectedMood = true;
        selectedMoodLabel.setText("Selected Mood: 😐 Neutral");
        selectedMoodLabel.setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold;");
        app.setStatus("Mood set to Neutral 😐");
    }

    @FXML private void onSad() {
        selectedMood = "Sad";
        hasExplicitlySelectedMood = true;
        selectedMoodLabel.setText("Selected Mood: 😔 Sad");
        selectedMoodLabel.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
        app.setStatus("Mood set to Sad 😔");
    }

    @FXML private void onSave() {
        try {
            
            String description = moodText.getText().trim();
            
            // Check if user has selected a mood
            if (selectedMood.equals("Neutral") && !hasExplicitlySelectedMood) {
                showValidationError("Please select a mood (Happy, Neutral, or Sad) before saving!");
                app.setStatus("Please select a mood (Happy, Neutral, or Sad) before saving!");

                return;
            }
            
            if (description.isEmpty()) {
                app.setStatus("Please enter a description of your mood!");
                showValidationError("Please enter a description of your mood!");
                return;
            }
            
            // Save to database
            saveMoodToDatabase(description);
            
            app.setStatus("Mood saved successfully!"); 
            app.goHome();
            
        } catch (Exception e) {
            System.err.println("Error saving mood: " + e.getMessage());
            app.setStatus("Error saving mood. Please try again.");
        }
    }

    private void showValidationError(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Saves the mood entry to the database
     */
    private void saveMoodToDatabase(String description) throws SQLException {
       
        String currentDate = java.time.LocalDate.now().toString();
        
        // SQL command to insert a new mood entry
        String sql = "INSERT INTO mood_entries (date, mood_type, description, sentiment_score) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            
            pstmt.setString(1, currentDate);           
            pstmt.setString(2, selectedMood);          
            pstmt.setString(3, description);        
            pstmt.setDouble(4, chatGPT.getSentimentScore(description));
            
            // Execute the command
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Mood saved to database successfully!");
            } else {
                System.out.println("No rows were affected when saving mood.");
            }
        }
    }

    @FXML private void onCancel() { app.goHome(); }
}
