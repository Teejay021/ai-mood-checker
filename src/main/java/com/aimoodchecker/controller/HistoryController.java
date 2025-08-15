package com.aimoodchecker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;

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
    }

    @FXML
    private void onRefresh() {
        // TODO: Refresh mood history data
        app.setStatus("History refreshed");
    }

    @FXML
    private void onBackHome() {
        app.goHome();
    }
}
