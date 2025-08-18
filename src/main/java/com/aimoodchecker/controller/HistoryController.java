package com.aimoodchecker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.aimoodchecker.repository.EntryRepository;
import com.aimoodchecker.model.MoodEntry;
import com.aimoodchecker.service.SentimentService;
import com.aimoodchecker.service.ChatGPTService;
import java.sql.SQLException;
import java.util.List;

public class HistoryController implements RoutedController, NeedsDeps {
    
    private AppController app;
    private EntryRepository entryRepository;
    private SentimentService sentiment;
    private ChatGPTService chatGPT;
    
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

    @Override
    public void init(EntryRepository repo, SentimentService sentiment, ChatGPTService chatGPT) {
        this.entryRepository = repo;
        this.sentiment = sentiment;
        this.chatGPT = chatGPT;
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
            
            if (moodEntries.isEmpty()) {
                noDataLabel.setVisible(true);
                historyTable.setVisible(false);
            } else {
                noDataLabel.setVisible(false);
                historyTable.setVisible(true);
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

    @FXML
    private void onViewGraph() {
        app.goGraph();
    }
}
