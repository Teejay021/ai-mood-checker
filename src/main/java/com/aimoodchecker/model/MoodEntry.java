package com.aimoodchecker.model;

import java.time.LocalDate;

/**
 * Model class representing a mood entry
 */
public class MoodEntry {
    private int id;
    private LocalDate date;
    private String moodType;
    private String description;
    private Double sentimentScore;
    private String createdAt;
    
    public MoodEntry(int id, LocalDate date, String moodType, String description, Double sentimentScore, String createdAt) {
        this.id = id;
        this.date = date;
        this.moodType = moodType;
        this.description = description;
        this.sentimentScore = sentimentScore;
        this.createdAt = createdAt;
    }
    
    // Getters
    public int getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getMoodType() { return moodType; }
    public String getDescription() { return description; }
    public Double getSentimentScore() { return sentimentScore; }
    public String getCreatedAt() { return createdAt; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setMoodType(String moodType) { this.moodType = moodType; }
    public void setDescription(String description) { this.description = description; }
    public void setSentimentScore(Double sentimentScore) { this.sentimentScore = sentimentScore; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    /**
     * Gets formatted date string for display
     */
    public String getFormattedDate() {
        return date != null ? date.toString() : "Unknown";
    }
    
    /**
     * Gets sentiment category based on score
     */
    public String getSentimentCategory() {
        if (sentimentScore == null) return "Unknown";
        if (sentimentScore >= 0.7) return "Positive";
        if (sentimentScore <= 0.3) return "Negative";
        return "Neutral";
    }
}
