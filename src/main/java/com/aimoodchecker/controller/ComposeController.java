package com.aimoodchecker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import com.aimoodchecker.service.SentimentService;
import com.aimoodchecker.repository.EntryRepository;

public class ComposeController implements RoutedController, NeedsDeps {
    
    private AppController app;
    private EntryRepository repo;
    private SentimentService sentiment;

    @FXML private TextArea moodText;

    @Override public void setApp(AppController app) { this.app = app; }
    @Override public void init(EntryRepository r, SentimentService s) { this.repo = r; this.sentiment = s; }


    @FXML private void onHappy() {
        // TODO: Set mood to happy
        app.setStatus("Mood set to Happy");
    }

    @FXML private void onNeutral() {
        // TODO: Set mood to neutral
        app.setStatus("Mood set to Neutral");
    }

    @FXML private void onSad() {
        // TODO: Set mood to sad
        app.setStatus("Mood set to Sad");
    }

    @FXML private void onSave() {
        // validate, persist, maybe compute sentiment
        String mood = moodText.getText();
        System.out.println("User typed: " + mood);
        app.setStatus("Saved entry"); 
        app.goHome();
    }

    @FXML private void onCancel() { app.goHome(); }
}
