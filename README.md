# AI Mood Checker 📊

A JavaFX-based desktop application for tracking and analyzing your daily mood patterns. Built with modern Java technologies and a clean, intuitive user interface.

## Features

- **🎭 Mood Logging**: Quick mood selection (Happy, Neutral, Sad) with detailed text descriptions
- **📅 History Tracking**: View your mood entries over time in an organized table
- **🔄 Dynamic Navigation**: Seamless switching between different views
- **💾 Data Persistence**: SQLite database for storing mood entries
- **🎨 Modern UI**: Clean, responsive JavaFX interface with custom styling

## 🏗️ Architecture

- **Frontend**: JavaFX with FXML for UI layout
- **Backend**: Java 17+ with modular architecture
- **Database**: SQLite for local data storage
- **Pattern**: MVC (Model-View-Controller) with dependency injection

## 🚀 Getting Started

### Prerequisites

- **Java 24** (JDK 24 recommended)
- **JavaFX SDK 24.0.2** (included in project setup)
- **Maven** (for dependency management)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/ai-mood-checker.git
   cd ai-mood-checker
   ```

2. **Download JavaFX SDK**
   - Download from: https://openjfx.io/
   - Extract to: `C:\Program Files (x86)\openjfx-24.0.2_windows-x64_bin-sdk\`

3. **Compile and Run**
   ```bash
   # Compile
   javac -cp "C:\Program Files (x86)\openjfx-24.0.2_windows-x64_bin-sdk\javafx-sdk-24.0.2\lib\*" -d target/classes src/main/java/com/aimoodchecker/*.java src/main/java/com/aimoodchecker/controller/*.java src/main/java/com/aimoodchecker/dao/*.java src/main/java/com/aimoodchecker/service/*.java src/main/java/com/aimoodchecker/repository/*.java

   # Run
   java --module-path "C:\Program Files (x86)\openjfx-24.0.2_windows-x64_bin-sdk\javafx-sdk-24.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp target/classes com.aimoodchecker.Main
   ```

### Alternative: Use the provided batch file
```bash
run-app.bat
```

## Project Structure

```
AIMoodChecker/
├── src/
│   ├── main/
│   │   ├── java/com/aimoodchecker/
│   │   │   ├── controller/          # UI controllers
│   │   │   ├── dao/                 # Database access
│   │   │   ├── repository/          # Data repository layer
│   │   │   ├── service/             # Business logic services
│   │   │   └── Main.java           # Application entry point
│   │   └── resources/
│   │       ├── App.fxml            # Main application layout
│   │       ├── HomeView.fxml       # Home screen
│   │       ├── ComposeView.fxml    # Mood logging form
│   │       ├── HistoryView.fxml    # Mood history display
│   │       ├── styles.css          # Custom styling
│   │       └── images/             # Application assets
├── target/                          # Compiled classes
├── pom.xml                          # Maven configuration
├── run-app.bat                      # Windows run script
└── README.md                        # This file
```

## Usage

1. **Launch the application** - You'll see the home screen with your logo
2. **Click "Log mood"** - Navigate to the mood logging interface
3. **Select your mood** - Select your mood
4. **Add details** - Write a description of how you're feeling
5. **Save entry** - Your mood is stored in the database
6. **View history** - See all your previous mood entries

## 🔧 Development

### Key Components

- **`AppController`**: Main application controller managing navigation
- **`HomeController`**: Handles home screen interactions
- **`ComposeController`**: Manages mood entry form
- **`HistoryController`**: Displays mood history
- **`EntryRepository`**: Data access layer for mood entries
- **`SentimentService`**: Future AI sentiment analysis integration

### Adding New Features

1. Create new FXML file in `src/main/resources/`
2. Create corresponding controller implementing `RoutedController`
3. Add navigation method in `AppController`
4. Update UI to include navigation to new feature

## Future Enhancements

- [ ] **AI Sentiment Analysis**: Analyze mood descriptions using NLP
- [ ] **Mood Trends**: Charts and graphs showing mood patterns
- [ ] **Reminders**: Daily mood check-in notifications
- [ ] **Export Data**: CSV/PDF reports of mood history
- [ ] **Cloud Sync**: Backup mood data to cloud storage

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request



## Acknowledgments

- Built with JavaFX and modern Java technologies
- Inspired by the need for better mental health tracking
- Special thanks to the JavaFX community
