@echo off
mkdir "test_temp"
mkdir "test_temp\server"
copy ".\server\target\server-1.0-SNAPSHOT.jar" "test_temp\server"
cd "test_temp\server"

:: Start Java server in a new window using `start`
start cmd /k java -jar "server-1.0-SNAPSHOT.jar"

:: Pause execution until user presses Enter
set /p "=Press [Enter] key to finish..." <nul