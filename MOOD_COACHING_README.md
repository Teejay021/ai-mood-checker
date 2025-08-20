# 🧠 AI Mood Coaching System

## Overview

The AI Mood Coaching System is an intelligent feature that provides personalized mood improvement suggestions based on your mood history and current emotional state. It analyzes your past mood patterns, identifies what has made you happy, and offers tailored recommendations to help boost your mood.

## ✨ Key Features

### 🎯 **Intelligent Pattern Analysis**
- **Mood History Analysis**: Analyzes your complete mood logging history
- **Pattern Recognition**: Identifies trends, triggers, and positive experiences
- **Personalized Insights**: Understands what works specifically for you

### 🧠 **AI-Powered Coaching**
- **Context-Aware Suggestions**: Considers your current mood and description
- **Historical Learning**: References your past happy moments for inspiration
- **Practical Recommendations**: Provides actionable steps you can take immediately

### 📊 **Comprehensive Mood Profile**
- **Mood Statistics**: Counts of happy, neutral, and sad moments
- **Overall Pattern**: Identifies if you tend toward positive, negative, or balanced moods
- **Recent Experiences**: Analyzes your last 10 happy moments and 5 challenging moments

## 🔧 How It Works

### 1. **Mood Pattern Analysis**
The system analyzes your mood history to understand:
- Your overall mood patterns and trends
- What activities/experiences make you happy
- Common triggers for negative moods
- Your emotional resilience patterns

### 2. **AI Coaching Request**
When you request coaching, the system:
- Collects your current mood and description
- Analyzes your comprehensive mood profile
- Sends this data to ChatGPT with specific coaching instructions
- Requests personalized, actionable suggestions

### 3. **Personalized Response**
The AI considers:
- **Your Past Successes**: What has made you happy before
- **Current Context**: Your specific situation and feelings
- **Pattern Shifts**: How to move from current mood to positive state
- **Immediate Actions**: What you can do right now

## 🚀 Usage

### **When to Use Mood Coaching**
- **Feeling Down**: When you're sad or depressed
- **Neutral State**: When you want to improve a "meh" mood
- **Stressful Times**: When facing challenges or pressure
- **Routine Slumps**: When feeling stuck in negative patterns

### **How to Get Coaching**
1. **Log Your Mood**: Select your current mood (Happy/Neutral/Sad)
2. **Describe Your Feelings**: Write about what's happening
3. **Click "Get Mood Coaching"**: Request personalized suggestions
4. **Review Suggestions**: Read AI-generated recommendations
5. **Take Action**: Implement the suggested activities

## 📋 Example Coaching Scenarios

### **Scenario 1: Work Stress**
```
Current Mood: Sad
Description: "Work has been overwhelming this week. I'm behind on projects and my boss is putting pressure on me."

AI Coaching Response:
"Based on your mood history, I can see that exercise and social connections have helped you in the past. Here are some suggestions:

1. Take a 15-minute walk outside - you've mentioned feeling better after physical activity
2. Call a friend or family member - your happy moments often involve social connections
3. Break your work into smaller, manageable tasks to reduce overwhelm"
```

### **Scenario 2: General Low Mood**
```
Current Mood: Neutral
Description: "I'm feeling okay but not great. Just going through the motions today."

AI Coaching Response:
"Looking at your patterns, I notice you tend to feel better when you engage in creative activities and spend time in nature. Try:

1. Listen to your favorite music playlist - this has boosted your mood before
2. Take a short trip to a nearby park or green space
3. Start a small creative project you've been thinking about"
```

## 🏗️ Technical Implementation

### **Architecture Components**

#### **ChatGPTService.java**
- Handles API communication with OpenAI
- Processes mood coaching requests
- Parses AI responses using custom JSON parsing

#### **EntryRepository.java**
- Provides comprehensive mood pattern analysis
- Calculates mood statistics and trends
- Identifies recent happy and challenging moments

#### **ComposeController.java**
- Integrates coaching UI with mood logging
- Manages coaching button visibility based on mood
- Handles asynchronous coaching requests

### **Data Flow**
1. **User Input** → Mood selection + description
2. **Pattern Analysis** → Repository analyzes mood history
3. **AI Request** → ChatGPT receives comprehensive mood profile
4. **AI Response** → Personalized coaching suggestions
5. **User Display** → Suggestions shown in coaching container

### **Key Methods**

#### **`getMoodCoaching(String mood, String description)`**
- Main entry point for mood coaching
- Analyzes mood patterns and requests AI suggestions
- Returns personalized coaching text

#### **`getMoodPatterns()`**
- Analyzes complete mood history
- Calculates statistics and identifies patterns
- Returns comprehensive mood insights

#### **`createEnhancedCoachingRequest()`**
- Builds detailed prompt for ChatGPT
- Includes mood history, patterns, and coaching instructions
- Ensures AI has full context for personalized suggestions

## 🎨 UI Integration

### **Coaching Button**
- **Visibility Logic**: Shows for Neutral/Sad moods, hidden for Happy
- **Smart Positioning**: Appears prominently when coaching is relevant
- **Loading States**: Shows progress while getting AI suggestions

### **Coaching Container**
- **Professional Styling**: Clean, modern design with subtle shadows
- **Responsive Layout**: Adapts to content length
- **Status Feedback**: Shows loading, success, and error states

### **CSS Styling**
- **Gradient Backgrounds**: Professional appearance
- **Hover Effects**: Interactive button animations
- **Color Coding**: Consistent with mood themes

## 🔒 Privacy & Security

### **Data Handling**
- **Local Storage**: All mood data stored locally in SQLite
- **API Security**: OpenAI API key stored securely in `.env` file
- **No External Sharing**: Mood data never leaves your device

### **API Usage**
- **Minimal Data**: Only sends current mood and description to AI
- **Pattern Summary**: Sends statistical summaries, not raw entries
- **Secure Communication**: Uses HTTPS for all API calls

## 🧪 Testing

### **TestMoodCoaching.java**
- Standalone test class for coaching functionality
- Tests different mood scenarios
- Demonstrates AI response quality

### **Test Commands**
```bash
# Compile test class
javac -cp "target/classes;src/main/java" src/main/java/com/aimoodchecker/TestMoodCoaching.java

# Run test
java -cp "target/classes;src/main/java" com.aimoodchecker.TestMoodCoaching

# Or use batch file
test-coaching.bat
```

## 🚀 Future Enhancements

### **Advanced Pattern Recognition**
- **Time-based Analysis**: Identify mood patterns by time of day/week
- **Trigger Detection**: Automatically identify mood triggers
- **Seasonal Patterns**: Recognize seasonal mood variations

### **Enhanced Coaching**
- **Follow-up Questions**: AI asks clarifying questions for better suggestions
- **Progress Tracking**: Monitor how coaching suggestions improve mood
- **Customization**: Allow users to set coaching preferences

### **Integration Features**
- **Calendar Integration**: Suggest activities based on your schedule
- **Weather Awareness**: Adjust suggestions based on weather conditions
- **Social Context**: Consider social events and relationships

## 💡 Best Practices

### **For Users**
- **Be Specific**: Detailed descriptions lead to better coaching
- **Regular Logging**: More data means more accurate patterns
- **Try Suggestions**: Implement coaching recommendations to see results
- **Track Progress**: Notice how coaching affects your mood over time

### **For Developers**
- **Error Handling**: Graceful fallbacks when AI is unavailable
- **Performance**: Asynchronous processing for smooth UI
- **Caching**: Store recent coaching responses to reduce API calls
- **Monitoring**: Track coaching effectiveness and user satisfaction

## 🔍 Troubleshooting

### **Common Issues**

#### **"Unable to get coaching suggestions"**
- Check your internet connection
- Verify OpenAI API key is valid
- Ensure you have sufficient API credits

#### **"No mood history available"**
- Log a few mood entries first
- Check database connection
- Verify mood data is being saved

#### **Generic Suggestions**
- Provide more detailed mood descriptions
- Log more mood entries for better pattern recognition
- Check if API is returning detailed responses

## 📚 API Reference

### **ChatGPTService Methods**

```java
// Get personalized mood coaching
String coaching = chatGPT.getMoodCoaching(mood, description);

// Analyze sentiment of text
String sentiment = chatGPT.analyzeSentiment(text);

// Get sentiment score (0.0 to 1.0)
double score = chatGPT.getSentimentScore(text);
```

### **EntryRepository Methods**

```java
// Get comprehensive mood patterns
MoodPatterns patterns = repository.getMoodPatterns();

// Get mood statistics
MoodStatistics stats = repository.getMoodStatistics(days);

// Get trend data for charts
List<TrendPoint> trends = repository.findDailyAverages(days);
```

## 🎯 Conclusion

The AI Mood Coaching System transforms your mood logging from simple tracking into an intelligent, personalized wellness companion. By analyzing your patterns and providing actionable suggestions, it helps you understand your emotions better and take positive steps toward improved mental well-being.

The system learns from your experiences, remembers what makes you happy, and offers guidance that's uniquely tailored to your emotional patterns and current situation. It's like having a personal mood coach who knows you well and wants to help you feel better.

---

*This feature represents a significant advancement in personal mental health technology, combining the power of AI with the intimacy of personal mood data to create truly personalized wellness support.*
