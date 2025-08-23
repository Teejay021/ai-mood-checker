package com.aimoodchecker;

import com.aimoodchecker.service.ChatGPTService;
import com.aimoodchecker.repository.EntryRepository;

/**
 * Test class to demonstrate the mood coaching functionality
 * This can be run independently to test the AI coaching features
 */
public class TestMoodCoaching {
    
    public static void main(String[] args) {
        System.out.println("🧠 AI Mood Coaching System Test");
        System.out.println("================================\n");
        
        try {
            // Initialize services
            ChatGPTService chatGPT = new ChatGPTService();
            
            // Test mood coaching for different scenarios
            testMoodCoaching(chatGPT, "Sad", "I'm feeling really down today. Work has been stressful and I'm not sure how to cope.");
            testMoodCoaching(chatGPT, "Neutral", "I'm feeling okay but not great. Just going through the motions today.");
            
        } catch (Exception e) {
            System.err.println("Error testing mood coaching: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testMoodCoaching(ChatGPTService chatGPT, String mood, String description) {
        System.out.println("🎯 Testing Mood Coaching for: " + mood);
        System.out.println("Description: " + description);
        System.out.println("---");
        
        try {
            EntryRepository repository = EntryRepository.getInstance();
            String coaching = chatGPT.getMoodCoaching(mood, description, repository);
            System.out.println("AI Coaching Response:");
            System.out.println(coaching);
            System.out.println("\n" + "=".repeat(50) + "\n");
            
        } catch (Exception e) {
            System.err.println("Error getting coaching: " + e.getMessage());
            System.out.println("\n" + "=".repeat(50) + "\n");
        }
    }
}
