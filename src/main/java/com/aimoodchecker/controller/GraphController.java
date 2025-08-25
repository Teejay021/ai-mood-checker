package com.aimoodchecker.controller;

import com.aimoodchecker.repository.EntryRepository;
import com.aimoodchecker.repository.EntryRepository.TrendPoint;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class GraphController implements RoutedController {

    private AppController app;
    private final EntryRepository repo = EntryRepository.getInstance();

    @FXML
    private VBox chartContainer;

    @FXML 
    private ComboBox<String> rangeCombo;
    
    @FXML
    private Label statusLabel;

    @Override
    public void setApp(AppController app) { 
        this.app = app;
    }

    @FXML 
    private void initialize() {
        // Initialize range combo box
        rangeCombo.getItems().addAll("Last 7 days", "Last 30 days", "Last 90 days");
        rangeCombo.getSelectionModel().select(1); // Default to 30 days

        // Add listener for range changes
        rangeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(oldVal)) {
                System.out.println("Range changed from " + oldVal + " to " + newVal);
                rebuildChart();
            }
        });

        // Debug: Check if we can access the scene and stylesheets
        System.out.println("=== GraphController Initialization ===");
        System.out.println("Chart container: " + chartContainer);
        
        // Initial chart build
        rebuildChart();
    }

    @FXML
    private void onRefresh() {
        rebuildChart();
        if (app != null) {
            app.setStatus("Trends refreshed");
        }
    }

    @FXML
    private void onBack() {
        if (app != null) {
            app.goHistory();
        }
    }
    
    private int getSelectedDays() {
        String value = rangeCombo.getValue();
        if (value == null) return 30;
        if (value.contains("7")) return 7;
        if (value.contains("90")) return 90;
        return 30;
    }

    private void rebuildChart() {
        int days = getSelectedDays();
        System.out.println("=== Rebuilding Chart ===");
        System.out.println("Selected days: " + days);
        
        try {
            // Show loading status
            if (statusLabel != null) {
                statusLabel.setText("Loading trend data...");
                statusLabel.setTextFill(Color.BLUE);
            }

            List<TrendPoint> points = repo.findDailyAverages(days);
            System.out.println("Repository returned " + points.size() + " trend points");
            
            if (points.isEmpty()) {
                showNoDataMessage();
                return;
            }
            
            // Check if we have enough data points to create a meaningful chart
            if (points.size() < 2) {
                showNoDataMessage("Only " + points.size() + " data point(s) available. Need at least 2 points to show trends.");
                return;
            }
            
            // Check if we have data points on different dates to prevent vertical lines
            long firstEpoch = points.get(0).date().toEpochDay();
            long lastEpoch = points.get(points.size() - 1).date().toEpochDay();
            
            System.out.println("Data span: First date epoch=" + firstEpoch + ", Last date epoch=" + lastEpoch);
            
            if (firstEpoch == lastEpoch) {
                showNoDataMessage("All data points are on the same date (" + points.get(0).date() + 
                                "). Need data from different dates to show trends.");
                return;
            }

            // Create and configure the chart
            LineChart<Number, Number> chart = createChart(days);
            
            // Add data series
            XYChart.Series<Number, Number> moodSeries = createMoodSeries(points);
            XYChart.Series<Number, Number> aiSeries = createAISeries(points);
            
            chart.getData().add(moodSeries);
            if (!aiSeries.getData().isEmpty()) {
                chart.getData().add(aiSeries);
            }

            // Display the chart
            chartContainer.getChildren().setAll(chart);
            
            // Debug: Verify CSS styling after chart is displayed
            verifyChartStyling(chart);
            
            // Apply delayed styling to ensure tick labels are properly styled
            // This runs after the chart is fully rendered and has a scene
            javafx.application.Platform.runLater(() -> {
                // Wait a bit more to ensure the chart is fully rendered
                javafx.application.Platform.runLater(() -> {
                    applyTickLabelStyling(chart);
                    
                    // Try again after a longer delay to catch any late-rendered elements
                    javafx.application.Platform.runLater(() -> {
                        applyTickLabelStyling(chart);
                        
                        // Force a layout pass to ensure all elements are rendered
                        chart.requestLayout();
                        
                        // Try one more time after layout
                        javafx.application.Platform.runLater(() -> {
                            applyTickLabelStyling(chart);
                        });
                    });
                });
            });
            

            
            // Update status
            if (statusLabel != null) {
                statusLabel.setText("Chart loaded successfully - " + points.size() + " data points");
                statusLabel.setTextFill(Color.GREEN);
            }
            
            System.out.println("Chart rebuilt successfully with " + points.size() + " data points");
            System.out.println("=== End Rebuilding Chart ===");
            
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Failed to load trend data: " + e.getMessage());
            if (app != null) {
                app.setStatus("Failed to load trend data");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Unexpected error: " + e.getMessage());
            if (app != null) {
                app.setStatus("Unexpected error occurred");
            }
        }
    }
    
    private LineChart<Number, Number> createChart(int days) {
        // Create axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis(0, 1, 0.2); // Range 0-1 with 0.2 intervals
        
        // Configure axes
        xAxis.setLabel("Date");
        yAxis.setLabel("Score (0–1)");
        xAxis.setTickLabelRotation(45); // Rotate labels for better readability
        
        // Set X-axis range to prevent vertical lines
        // Calculate the date range for the selected period
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        long startEpoch = startDate.toEpochDay();
        long endEpoch = endDate.toEpochDay();
        
        System.out.println("Chart X-axis: Start=" + startDate + " (epoch " + startEpoch + 
                          "), End=" + endDate + " (epoch " + endEpoch + ")");
        
        xAxis.setLowerBound(startEpoch);
        xAxis.setUpperBound(endEpoch);
        
        // Set tick unit to show reasonable number of date labels
        long tickUnit = Math.max(1, (endEpoch - startEpoch) / 7); // Show max 7 labels
        xAxis.setTickUnit(tickUnit);
        
        // Ensure we have proper spacing to prevent vertical lines
        xAxis.setAutoRanging(false); // Force our custom bounds
        
        // Format X axis as dates
        xAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxis) {
            @Override 
            public String toString(Number object) {
                long epochDay = object.longValue();
                return LocalDate.ofEpochDay(epochDay).toString();
            }
        });

        // Create chart
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Mood vs. AI Sentiment Trends (" + days + " days)");
        chart.setLegendVisible(true);
        chart.setCreateSymbols(true);
        chart.setAnimated(true);
        
        // Apply CSS styling
        chart.getStyleClass().add("trend-chart");
        
        // Debug: Print applied style classes
        System.out.println("Chart style classes: " + chart.getStyleClass());
        
        // Also try to apply some direct styling to the axes
        xAxis.setStyle("-fx-tick-label-fill: white;");
        yAxis.setStyle("-fx-tick-label-fill: white;");
        
        // Force tick label styling as fallback to ensure CSS is applied
        // This will override any inline styles that might be interfering
        // Note: At this point, tick labels might not be rendered yet, so we'll do this later
        System.out.println("Chart created with trend-chart style class");
        
        return chart;
    }
    
    /**
     * Debug method to verify that CSS styling is properly applied to the chart
     */
    private void verifyChartStyling(LineChart<Number, Number> chart) {
        System.out.println("=== Chart Styling Verification ===");
        System.out.println("Chart style classes: " + chart.getStyleClass());
        
        // Check if trend-chart class is applied
        if (chart.getStyleClass().contains("trend-chart")) {
            System.out.println("trend-chart style class is applied");
        } else {
            System.out.println("trend-chart style class is NOT applied");
        }
        
        // Check the scene for stylesheets
        if (chart.getScene() != null) {
            System.out.println("Chart scene stylesheets: " + chart.getScene().getStylesheets());
        } else {
            System.out.println("Chart has no scene");
        }
        
        // Check tick labels
        var xAxis = (NumberAxis) chart.getXAxis();
        var yAxis = (NumberAxis) chart.getYAxis();
        
        int xTickLabels = xAxis.lookupAll(".tick-label").size();
        int yTickLabels = yAxis.lookupAll(".tick-label").size();
        
        System.out.println("X-axis tick labels found: " + xTickLabels);
        System.out.println("Y-axis tick labels found: " + yTickLabels);
        
        System.out.println("=== End Styling Verification ===");
    }
    
    /**
     * Apply tick label styling programmatically as a fallback
     * This ensures tick labels are white even if CSS fails
     */
    private void applyTickLabelStyling(LineChart<Number, Number> chart) {
        System.out.println("=== Applying Tick Label Styling ===");
        
        // Check if chart has a scene first
        if (chart.getScene() == null) {
            System.out.println("Chart still has no scene, retrying in 100ms...");
            javafx.application.Platform.runLater(() -> {
                applyTickLabelStyling(chart);
            });
            return;
        }
        
        var xAxis = (NumberAxis) chart.getXAxis();
        var yAxis = (NumberAxis) chart.getYAxis();
        
        // Style X-axis tick labels
        var xTickLabels = xAxis.lookupAll(".tick-label");
        System.out.println("Found " + xTickLabels.size() + " X-axis tick labels");
        
        xTickLabels.forEach(node -> {
            if (node instanceof javafx.scene.text.Text) {
                javafx.scene.text.Text text = (javafx.scene.text.Text) node;
                text.setFill(Color.WHITE);
                System.out.println("Applied white fill to X-axis tick label: " + text.getText());
            }
        });
        
        // Style Y-axis tick labels
        var yTickLabels = yAxis.lookupAll(".tick-label");
        System.out.println("Found " + yTickLabels.size() + " Y-axis tick labels");
        
        yTickLabels.forEach(node -> {
            if (node instanceof javafx.scene.text.Text) {
                javafx.scene.text.Text text = (javafx.scene.text.Text) node;
                text.setFill(Color.WHITE);
                System.out.println("Applied white fill to Y-axis tick label: " + text.getText());
            }
        });
        
        // Try alternative selectors if the main ones didn't work
        if (xTickLabels.isEmpty() || yTickLabels.isEmpty()) {
            System.out.println("Trying alternative selectors...");
            
            // Try different CSS selectors that might work
            var altXLabels = xAxis.lookupAll("Text");
            var altYLabels = yAxis.lookupAll("Text");
            
            System.out.println("Alternative X labels found: " + altXLabels.size());
            System.out.println("Alternative Y labels found: " + altYLabels.size());
            
            altXLabels.forEach(node -> {
                if (node instanceof javafx.scene.text.Text) {
                    javafx.scene.text.Text text = (javafx.scene.text.Text) node;
                    text.setFill(Color.WHITE);
                    System.out.println("Applied white fill to alternative X-axis text: " + text.getText());
                }
            });
            
            altYLabels.forEach(node -> {
                if (node instanceof javafx.scene.text.Text) {
                    javafx.scene.text.Text text = (javafx.scene.text.Text) node;
                    text.setFill(Color.WHITE);
                    System.out.println("Applied white fill to alternative Y-axis text: " + text.getText());
                }
            });
        }
        
        // Also try to style axis labels
        if (xAxis.getLabel() != null) {
            System.out.println("X-axis label: " + xAxis.getLabel());
        }
        if (yAxis.getLabel() != null) {
            System.out.println("Y-axis label: " + yAxis.getLabel());
        }
        
        // Try to find and style ALL text elements in the chart
        System.out.println("Searching for all text elements in chart...");
        var allTexts = chart.lookupAll("Text");
        System.out.println("Found " + allTexts.size() + " total text elements");
        
        allTexts.forEach(node -> {
            if (node instanceof javafx.scene.text.Text) {
                javafx.scene.text.Text text = (javafx.scene.text.Text) node;
                text.setFill(Color.WHITE);
                System.out.println("Applied white fill to text: " + text.getText() + " (class: " + node.getClass().getSimpleName() + ")");
            }
        });
        
        System.out.println("=== End Tick Label Styling ===");
    }
    
    private XYChart.Series<Number, Number> createMoodSeries(List<TrendPoint> points) {
        XYChart.Series<Number, Number> moodSeries = new XYChart.Series<>();
        moodSeries.setName("Your Mood (normalized to 0-1)");
        
        System.out.println("=== Creating Mood Series ===");
        System.out.println("Total trend points: " + points.size());
        
        for (TrendPoint point : points) {
            if (point.avgMood() != null && point.avgMood() > 0) {
                long x = point.date().toEpochDay();
                // Normalize mood from 1-5 scale to 0-1 scale
                double normalizedMood = (point.avgMood() - 1.0) / 4.0; // 1->0, 5->1
                XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(x, normalizedMood);
                moodSeries.getData().add(dataPoint);
                
                System.out.println("Mood Point: Date=" + point.date() + 
                                 " | Raw=" + point.avgMood() + 
                                 " | Normalized=" + normalizedMood + 
                                 " | X=" + x);
            }
        }
        
        System.out.println("Mood series data points: " + moodSeries.getData().size());
        if (moodSeries.getData().size() <= 1) {
            System.out.println("WARNING: Only " + moodSeries.getData().size() + " mood data points - this will cause vertical lines!");
        }
        
        // Check for duplicate X values (same date)
        long[] xValues = moodSeries.getData().stream()
            .mapToLong(data -> data.getXValue().longValue())
            .distinct()
            .toArray();
        System.out.println("Unique X values (dates) in mood series: " + xValues.length);
        if (xValues.length < moodSeries.getData().size()) {
            System.out.println("WARNING: Duplicate dates detected! This will cause vertical lines!");
        }
        
        System.out.println("=== End Mood Series ===");
        
        return moodSeries;
    }
    
    private XYChart.Series<Number, Number> createAISeries(List<TrendPoint> points) {
        XYChart.Series<Number, Number> aiSeries = new XYChart.Series<>();
        aiSeries.setName("AI Sentiment (0-1)");
        
        System.out.println("=== Creating AI Series ===");
        System.out.println("Total trend points: " + points.size());
        
        for (TrendPoint point : points) {
            if (point.avgAi() != null) {
                long x = point.date().toEpochDay();
                // AI sentiment is already 0-1, no normalization needed
                double sentimentScore = point.avgAi();
                XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(x, sentimentScore);
                aiSeries.getData().add(dataPoint);
                
                System.out.println("AI Point: Date=" + point.date() + 
                                 " | Score=" + sentimentScore + 
                                 " | X=" + x);
            }
        }
        
        System.out.println("AI series data points: " + aiSeries.getData().size());
        if (aiSeries.getData().size() <= 1) {
            System.out.println("WARNING: Only " + aiSeries.getData().size() + " AI data points - this will cause vertical lines!");
        }
        
        // Check for duplicate X values (same date)
        long[] xValues = aiSeries.getData().stream()
            .mapToLong(data -> data.getXValue().longValue())
            .distinct()
            .toArray();
        System.out.println("Unique X values (dates) in AI series: " + xValues.length);
        if (xValues.length < aiSeries.getData().size()) {
            System.out.println("WARNING: Duplicate dates detected! This will cause vertical lines!");
        }
        
        System.out.println("=== End AI Series ===");
        
        return aiSeries;
    }
    
    private void showNoDataMessage() {
        showNoDataMessage("No mood data available for the selected time period.\nTry logging some moods first!");
    }
    
    private void showNoDataMessage(String message) {
        Label noDataLabel = new Label(message);
        noDataLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray; -fx-alignment: center;");
        noDataLabel.setWrapText(true);
        noDataLabel.setMaxWidth(Double.MAX_VALUE);
        
        chartContainer.getChildren().setAll(noDataLabel);
        
        if (statusLabel != null) {
            statusLabel.setText("No data available");
            statusLabel.setTextFill(Color.ORANGE);
        }
    }
    
    private void showErrorMessage(String message) {
        Label errorLabel = new Label("Error: " + message + "\nPlease try refreshing or check your connection.");
        errorLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red; -fx-alignment: center;");
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(Double.MAX_VALUE);
        
        chartContainer.getChildren().setAll(errorLabel);
        
        if (statusLabel != null) {
            statusLabel.setText("Error occurred");
            statusLabel.setTextFill(Color.RED);
        }
    }
}
