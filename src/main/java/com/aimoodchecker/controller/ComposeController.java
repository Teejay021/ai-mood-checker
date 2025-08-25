package com.aimoodchecker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;

import com.aimoodchecker.service.SentimentService;
import com.aimoodchecker.service.ChatGPTService;
import com.aimoodchecker.repository.EntryRepository;


public class ComposeController implements RoutedController, NeedsDeps {
    
    private AppController app;
    private EntryRepository entryRepository;
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
    @Override
    public void init(EntryRepository repo, SentimentService sentiment, ChatGPTService chatGPT) {
        this.entryRepository = repo;
        this.chatGPT = chatGPT;
    }

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
            
            // Save to database using repository
            entryRepository.saveMoodEntry(selectedMood, description);
            
            app.setStatus("Mood saved successfully! Getting AI suggestions...");
            
            // Automatically get AI coaching suggestions after saving
            showCoachingSuggestions(selectedMood, description);
            
        } catch (Exception e) {
            System.err.println("Error saving mood: " + e.getMessage());
            app.setStatus("Error saving mood. Please try again.");
        }
    }

    /**
     * Shows AI coaching suggestions automatically after saving
     */
    private void showCoachingSuggestions(String mood, String description) {
        // Get coaching suggestions in background
        new Thread(() -> {
            try {
                String coaching = chatGPT.getMoodCoaching(mood, description, entryRepository);
                
                // Update UI on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    showCoachingDialog(coaching);
                    app.setStatus("Mood saved with personalized AI suggestions! 💡");
                });
                
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    showCoachingDialog("Mood saved successfully! 💚\n\nI couldn't get AI suggestions right now, but your mood has been recorded.");
                    app.setStatus("Mood saved successfully!");
                });
            }
        }).start();
    }
    
    /**
     * Shows AI coaching in a popup dialog with scrollable content
     */
    private void showCoachingDialog(String coaching) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("🤖 AI Mood Coach");
        alert.setHeaderText("Your Personalized Mood Suggestions");
        
        // Create a scrollable text area for the coaching content
        TextArea contentArea = new TextArea(coaching);
        contentArea.setEditable(false);
        contentArea.setWrapText(true);
        contentArea.setPrefRowCount(15); // Show more rows
        contentArea.setPrefColumnCount(60); // Wider text area
        contentArea.setStyle("-fx-font-size: 14px; -fx-font-family: 'Segoe UI';");
        
        // Set the content area as the dialog content
        alert.getDialogPane().setContent(contentArea);
        
        // Make dialog resizable
        alert.getDialogPane().setPrefSize(600, 500);
        
        // Add a close button
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(closeButton);
        
        // Show the dialog and then return to home
        alert.showAndWait();
        
        // After dialog closes, return to home page
        app.goHome();
    }

    private void showValidationError(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Remove the old saveMoodToDatabase method since we're now using the repository

    @FXML private void onCancel() { app.goHome(); }
}
