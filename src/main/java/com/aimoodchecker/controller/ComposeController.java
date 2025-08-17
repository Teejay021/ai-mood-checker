package com.aimoodchecker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import com.aimoodchecker.service.SentimentService;
import com.aimoodchecker.service.ChatGPTService;
import com.aimoodchecker.repository.EntryRepository;
import com.aimoodchecker.dao.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class ComposeController implements RoutedController, NeedsDeps {
    
    private AppController app;
    private EntryRepository repo;
    private SentimentService sentiment;
    private ChatGPTService chatGPT;

    @FXML private TextArea moodText;

    @Override public void setApp(AppController app) { this.app = app; }
    @Override public void init(EntryRepository r, SentimentService s, ChatGPTService c) { this.repo = r; this.sentiment = s; this.chatGPT = c; }


    private String selectedMood = "Neutral"; // Default mood

    @FXML private void onHappy() {
        selectedMood = "Happy";
        app.setStatus("Mood set to Happy 😊");
    }

    @FXML private void onNeutral() {
        selectedMood = "Neutral";
        app.setStatus("Mood set to Neutral 😐");
    }

    @FXML private void onSad() {
        selectedMood = "Sad";
        app.setStatus("Mood set to Sad 😔");
    }

    @FXML private void onSave() {
        try {
            
            String description = moodText.getText().trim();
            
            
            if (description.isEmpty()) {
                app.setStatus("Please enter a description of your mood!");
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
