@ECHO OFF
SETLOCAL
IF EXIST "%~dp0\gradle" (
  SET "GRADLE_HOME=%~dp0\gradle"
)
WHERE gradle >NUL 2>&1
IF %ERRORLEVEL% EQU 0 (
  gradle %*
) ELSE (
  ECHO Gradle is required to run this build. Please install Gradle 8.8 or higher.
  EXIT /B 1
)
