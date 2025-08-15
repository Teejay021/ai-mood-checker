package com.aimoodchecker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeController implements RoutedController {
    private AppController app;

    @FXML private Button logMoodBtn;
    @FXML private Button viewHistoryBtn;

    @Override
    public void setApp(AppController app) { this.app = app; }
    
    @FXML private void handleLogMood()    { app.goCompose(); }
    @FXML private void handleViewHistory(){ app.goHistory(); }
}
    