@echo off
echo Starting AI Mood Checker...
java --module-path "C:\Program Files (x86)\openjfx-24.0.2_windows-x64_bin-sdk\javafx-sdk-24.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp target/classes com.aimoodchecker.Main
pause
