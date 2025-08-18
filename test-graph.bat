@echo off
echo Testing Graph Controller...
echo.

echo Compiling GraphController...
javac -cp "target/classes;src/main/java" src/main/java/com/aimoodchecker/controller/GraphController.java

echo.
echo Compilation complete. Check for errors above.
pause
