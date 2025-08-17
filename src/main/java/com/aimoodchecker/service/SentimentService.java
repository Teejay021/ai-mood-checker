package com.aimoodchecker.service;

/**
 * Service class for sentiment analysis
 * Currently a placeholder - can be extended with local sentiment analysis
 */
public class SentimentService {
    
    public SentimentService() {}
    
    /**
     * Analyzes sentiment of text (placeholder implementation)
     * @param text Text to analyze
     * @return Placeholder sentiment score
     */
    public double analyzeSentiment(String text) {
        return 0.5;
    }
    
    /**
     * Gets sentiment category based on score
     * @param score Sentiment score (0.0 to 1.0)
     * @return Sentiment category
     */
    public String getSentimentCategory(double score) {
        if (score >= 0.7) return "Positive";
        if (score <= 0.3) return "Negative";
        return "Neutral";
    }
}


