package com.aimoodchecker.controller;

import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import com.aimoodchecker.service.SentimentService;
import com.aimoodchecker.service.ChatGPTService;
import com.aimoodchecker.repository.EntryRepository;

public class AppController {
    
    @FXML private StackPane content;
    @FXML private Label statusLabel;

    private final EntryRepository repo = EntryRepository.getInstance();
    private final SentimentService sentiment = new SentimentService();
    private final ChatGPTService chatGPT = new ChatGPTService();

    @FXML
    private void initialize(){
        goHome();
    }

    private void setContent(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent page = loader.load();
            Object c = loader.getController();

            if (c instanceof NeedsDeps nd) nd.init(repo, sentiment, chatGPT);

            if (c instanceof RoutedController rc) rc.setApp(this);

            content.getChildren().setAll(page);

        } catch (IOException e) {

            e.printStackTrace();
            
            statusLabel.setText("Error loading " + fxml);
        }
    }

    public void setStatus(String msg) { statusLabel.setText(msg); }

    // Navigation API
    public void goHome()    { setContent("/HomeView.fxml"); }
    public void goCompose() { setContent("/ComposeView.fxml"); }
    public void goHistory() { setContent("/HistoryView.fxml"); }
    public void goGraph()   {setContent("/GraphView.fxml");}
    


}
