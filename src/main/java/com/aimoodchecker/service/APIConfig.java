package com.aimoodchecker.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Configuration class for API keys and settings
 * Loads configuration from properties file, .env file, or environment variables
 */
public class APIConfig {
    
    private static final String CONFIG_FILE = "config.properties";
    private static final String ENV_FILE = ".env";
    private static Properties properties;
    
    static {
        loadConfig();
        loadEnvFile();
    }
    
    /**
     * Loads configuration from properties file
     */
    private static void loadConfig() {
        properties = new Properties();
        
        try {
            // Try to load from properties file first
            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            properties.load(fis);
            fis.close();
            System.out.println("Configuration loaded from " + CONFIG_FILE);
        } catch (IOException e) {
            System.out.println("Config file not found, will use .env and environment variables");
        }
        
    }
    
    /**
     * Loads environment variables from .env file
     */
    private static void loadEnvFile() {
        try {
            if (Files.exists(Paths.get(ENV_FILE))) {
                Files.lines(Paths.get(ENV_FILE))
                    .filter(line -> !line.trim().isEmpty() && !line.trim().startsWith("#"))
                    .forEach(line -> {
                        if (line.contains("=")) {
                            String[] parts = line.split("=", 2);
                            if (parts.length == 2) {
                                String key = parts[0].trim();
                                String value = parts[1].trim();
                                // Set as system property so it's available to the app
                                System.setProperty(key, value);
                                System.out.println("Loaded from .env: " + key + "=" + (key.contains("KEY") ? "***" : value));
                            }
                        }
                    });
                System.out.println("Environment variables loaded from " + ENV_FILE);
            } else {
                System.out.println(".env file not found, using system environment variables");
            }
        } catch (IOException e) {
            System.err.println("Error reading .env file: " + e.getMessage());
        }
    }
    
    /**
     * Gets the OpenAI API key from config file, .env file, or environment variable
     * @return API key or null if not found
     */
    public static String getOpenAIKey() {
        // Try config file first, then .env/system properties, then environment variable
        String apiKey = properties.getProperty("openai.api.key");
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = System.getProperty("OPENAI_API_KEY");
        }
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = System.getenv("OPENAI_API_KEY");
        }
        return apiKey;
    }
    
    /**
     * Gets the ChatGPT model to use
     * @return Model name (default: gpt-3.5-turbo)
     */
    public static String getChatGPTModel() {
        return properties.getProperty("openai.model", "gpt-3.5-turbo");
    }
    
    /**
     * Gets the maximum tokens for ChatGPT responses
     * @return Max tokens (default: 150)
     */
    public static int getMaxTokens() {
        try {
            return Integer.parseInt(properties.getProperty("openai.max.tokens", "150"));
        } catch (NumberFormatException e) {
            return 150;
        }
    }
    
    /**
     * Gets the temperature setting for ChatGPT
     * @return Temperature (default: 0.7)
     */
    public static double getTemperature() {
        try {
            return Double.parseDouble(properties.getProperty("openai.temperature", "0.7"));
        } catch (NumberFormatException e) {
            return 0.7;
        }
    }
    
    /**
     * Checks if API is properly configured
     * @return true if API key is available
     */
    public static boolean isConfigured() {
        return getOpenAIKey() != null && !getOpenAIKey().isEmpty();
    }
}
