package com.aimoodchecker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Main controller for the AI Mood Checker application
 */
public class MainController {

    @FXML
    private Label statusLabel;
    
    @FXML
    private Button checkMoodButton;
    
    @FXML
    private Button viewHistoryButton;
    
    /**
     * Initialize method called after FXML loading
     */
    @FXML
    public void initialize() {
        // Set up button event handlers
        checkMoodButton.setOnAction(event -> handleCheckMood());
        viewHistoryButton.setOnAction(event -> handleViewHistory());
        
        // Initialize status
        statusLabel.setText("App Ready");
    }
    
    /**
     * Handle Check Mood button click
     */
    private void handleCheckMood() {
        statusLabel.setText("Checking mood...");
        // TODO: Implement mood checking logic
        System.out.println("Check Mood button clicked");
    }
    
    /**
     * Handle View History button click
     */
    private void handleViewHistory() {
        statusLabel.setText("Loading history...");
        // TODO: Implement history viewing logic
        System.out.println("View History button clicked");
    }
}
