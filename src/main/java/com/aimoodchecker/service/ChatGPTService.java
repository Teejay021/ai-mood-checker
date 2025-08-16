package com.aimoodchecker.service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;

/**
 * Service class for making API calls to ChatGPT/OpenAI
 * Handles sentiment analysis and mood insights
 */
public class ChatGPTService {
    
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    
    private final HttpClient httpClient;
    
    public ChatGPTService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }
    
    /**
     * Analyzes the sentiment of a mood description using ChatGPT
     * @param moodDescription The user's mood description
     * @return Sentiment analysis result
     */
    public String analyzeSentiment(String moodDescription) {
        String apiKey = APIConfig.getOpenAIKey();
        
        try {
            // Create the request body for ChatGPT
            String requestBody = createChatGPTRequest(moodDescription);
            
            // Build HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OPENAI_API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .timeout(Duration.ofSeconds(30))
                    .build();
            
            // Send request and get response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Process the response
            if (response.statusCode() == 200) {
                return parseChatGPTResponse(response.body());
            } else {
                System.err.println("API call failed with status: " + response.statusCode());
                return "API call failed: " + response.statusCode();
            }
            
        } catch (Exception e) {
            System.err.println("Error calling ChatGPT API: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
    
    /**
     * Creates the JSON request body for ChatGPT API
     */
    private String createChatGPTRequest(String moodDescription) {
        return """
            {
                "model": "gpt-3.5-turbo",
                "messages": [
                    {
                        "role": "system",
                        "content": "You are a mental health AI assistant. Analyze the sentiment of the user's mood description and provide a brief, supportive response. Focus on understanding and empathy."
                    },
                    {
                        "role": "user",
                        "content": "Please analyze this mood description: %s"
                    }
                ],
                "max_tokens": 150,
                "temperature": 0.7
            }
            """.formatted(moodDescription);
    }
    
    /**
     * Parses the ChatGPT API response
     */
    private String parseChatGPTResponse(String responseBody) {
        try {
            // Parse the actual ChatGPT response format
            if (responseBody.contains("\"content\":")) {
                // Find the content field in the message
                int contentIndex = responseBody.indexOf("\"content\":");
                if (contentIndex != -1) {
                    // Find the start of the content value
                    int startIndex = responseBody.indexOf("\"", contentIndex + 11) + 1;
                    // Find the end of the content value
                    int endIndex = responseBody.indexOf("\"", startIndex);
                    if (endIndex > startIndex) {
                        return responseBody.substring(startIndex, endIndex).trim();
                    }
                }
            }
            return "Response parsing failed";
        } catch (Exception e) {
            System.err.println("Error parsing ChatGPT response: " + e.getMessage());
            return "Response parsing error";
        }
    }
    
    /**
     * Gets a sentiment score (0.0 to 1.0) from the analysis
     * @param moodDescription User's mood description
     * @return Sentiment score between 0.0 (very negative) and 1.0 (very positive)
     */
    public double getSentimentScore(String moodDescription) {
        String analysis = analyzeSentiment(moodDescription);
        
        // Simple sentiment scoring based on keywords
        String lowerAnalysis = analysis.toLowerCase();
        
        if (lowerAnalysis.contains("positive") || lowerAnalysis.contains("happy") || 
            lowerAnalysis.contains("good") || lowerAnalysis.contains("great")) {
            return 0.8;
        } else if (lowerAnalysis.contains("neutral") || lowerAnalysis.contains("okay") || 
                   lowerAnalysis.contains("fine")) {
            return 0.5;
        } else if (lowerAnalysis.contains("negative") || lowerAnalysis.contains("sad") || 
                   lowerAnalysis.contains("bad") || lowerAnalysis.contains("worried")) {
            return 0.2;
        }
        
        return 0.5; // Default neutral score
    }
}
