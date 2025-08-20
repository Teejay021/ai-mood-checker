@echo off
echo Testing AI Mood Coaching System...
echo.

REM Compile the test class
javac -cp "target/classes;src/main/java" src/main/java/com/aimoodchecker/TestMoodCoaching.java

REM Run the test
java -cp "target/classes;src/main/java" com.aimoodchecker.TestMoodCoaching

pause
