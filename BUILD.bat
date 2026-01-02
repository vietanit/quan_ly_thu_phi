@echo off
REM ===================================================================
REM  BUILD.bat - Biên dịch project
REM ===================================================================

echo Gọi PowerShell script để biên dịch...
powershell -NoProfile -ExecutionPolicy Bypass -File "build.ps1"

if errorlevel 1 (
    echo.
    echo [LỖI] Biên dịch thất bại!
    pause
    exit /b 1
)

pause
