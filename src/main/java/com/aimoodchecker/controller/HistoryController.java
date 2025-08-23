package com.aimoodchecker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.aimoodchecker.repository.EntryRepository;
import com.aimoodchecker.model.MoodEntry;
import com.aimoodchecker.service.SentimentService;
import com.aimoodchecker.service.ChatGPTService;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.control.TableCell;

public class HistoryController implements RoutedController, NeedsDeps {
    
    private AppController app;
    private EntryRepository entryRepository;
    
    @FXML private TableView<MoodEntry> historyTable;
    @FXML private TableColumn<MoodEntry, String> dateColumn;
    @FXML private TableColumn<MoodEntry, String> moodColumn;
    @FXML private TableColumn<MoodEntry, String> descriptionColumn;
    @FXML private TableColumn<MoodEntry, String> sentimentColumn;
    @FXML private TableColumn<MoodEntry, Void> actionsColumn;
    @FXML private Label noDataLabel;

    @FXML
    private void initialize() {
        // Set up the TableView columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        moodColumn.setCellValueFactory(new PropertyValueFactory<>("moodType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        sentimentColumn.setCellValueFactory(new PropertyValueFactory<>("sentimentCategory"));
        
        // Set up the actions column with delete buttons
        actionsColumn.setCellFactory(param -> new TableCell<MoodEntry, Void>() {
            private final Button deleteButton = new Button("🗑️");
            
            {
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> {
                    MoodEntry entry = getTableView().getItems().get(getIndex());
                    if (entry != null) {
                        deleteMoodEntry(entry);
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }
    
    @Override
    public void setApp(AppController app) {
        this.app = app;
        // Load mood history when this view is opened
        loadMoodHistory();
    }

    @Override
    public void init(EntryRepository repo, SentimentService sentiment, ChatGPTService chatGPT) {
        this.entryRepository = repo;
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
            // Use repository to get all mood entries
            List<MoodEntry> moodEntries = entryRepository.getAllMoodEntries();
            
            System.out.println("=== Loading Mood History ===");
            
            for (MoodEntry entry : moodEntries) {
                System.out.println("ID: " + entry.getId() + 
                                 " | Date: " + entry.getDate() + 
                                 " | Mood: " + entry.getMoodType() + 
                                 " | Description: " + entry.getDescription() + 
                                 " | Sentiment Score: " + entry.getSentimentScore());
            }
            
            System.out.println("=== End of Mood History ===");
            
            // Convert to ObservableList for TableView
            ObservableList<MoodEntry> observableEntries = FXCollections.observableArrayList(moodEntries);
            historyTable.setItems(observableEntries);
            
            // Update UI based on data availability
            if (moodEntries.isEmpty()) {
                noDataLabel.setVisible(true);
                historyTable.setVisible(false);
                if (app != null) {
                    app.setStatus("No mood entries found");
                }
            } else {
                noDataLabel.setVisible(false);
                historyTable.setVisible(true);
                if (app != null) {
                    app.setStatus("Loaded " + moodEntries.size() + " mood entries");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading mood history: " + e.getMessage());
            e.printStackTrace();
            
            // Show error in UI
            noDataLabel.setText("Error loading mood history: " + e.getMessage());
            noDataLabel.setVisible(true);
            historyTable.setVisible(false);
            
            if (app != null) {
                app.setStatus("Error loading mood history");
            }
        }
    }

    /**
     * Deletes a mood entry after user confirmation
     */
    private void deleteMoodEntry(MoodEntry entry) {
        // Show confirmation dialog
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Mood Entry");
        alert.setHeaderText("Are you sure you want to delete this mood entry?");
        alert.setContentText("Date: " + entry.getFormattedDate() + "\n" +
                           "Mood: " + entry.getMoodType() + "\n" +
                           "Description: " + entry.getDescription() + "\n\n" +
                           "This action cannot be undone.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    // Delete from database using repository
                    entryRepository.deleteMoodEntry(entry.getId());
                    
                    // Show success message
                    Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Mood Entry Deleted");
                    successAlert.setContentText("The mood entry has been successfully deleted.");
                    successAlert.showAndWait();
                    
                    // Refresh the table
                    loadMoodHistory();
                    
                    // Update app status
                    if (app != null) {
                        app.setStatus("Mood entry deleted successfully");
                    }
                    
                } catch (SQLException e) {
                    // Show error message
                    Alert errorAlert = new Alert(AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Failed to Delete Entry");
                    errorAlert.setContentText("An error occurred while deleting the mood entry:\n" + e.getMessage());
                    errorAlert.showAndWait();
                    
                    System.err.println("Error deleting mood entry: " + e.getMessage());
                    e.printStackTrace();
                    
                    if (app != null) {
                        app.setStatus("Error deleting mood entry");
                    }
                }
            }
        });
    }

    @FXML
    private void onBackHome() {
        app.goHome();
    }

    @FXML
    private void onViewGraph() {
        app.goGraph();
    }
}
