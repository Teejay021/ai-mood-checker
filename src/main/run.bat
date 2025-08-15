@echo off
echo Running AI Mood Checker...
java --module-path "C:\openjfx-24.0.2_windows-x64_bin-sdk\javafx-sdk-24.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp "java;resources" com.aimoodchecker.Main
pause
