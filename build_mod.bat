@echo off
cd /d "c:\Users\Пользователь\Pictures\ReVisual"
echo Running Gradle build...
call gradlew.bat build --no-daemon > build_log.txt 2>&1
echo Build completed with exit code: %ERRORLEVEL%
