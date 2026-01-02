@echo off
REM ===================================================================
REM  RUN.bat - Chạy ứng dụng
REM ===================================================================

REM Cấu hình trực tiếp
set JAVA_HOME=C:\Users\PC\AppData\Local\Programs\Eclipse Adoptium\jdk-25.0.1.8-hotspot
set JAVAFX_PATH=C:\Users\PC\OneDrive\Desktop\openjfx-25.0.1_windows-x64_bin-sdk\javafx-sdk-25.0.1\lib
set MYSQL_CONNECTOR=C:\Users\PC\OneDrive\Desktop\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar

REM Kiểm tra thư mục output
if not exist out\application\Main.class (
    echo [LỖI] Không tìm thấy Main.class. Vui lòng chạy BUILD.bat trước.
    pause
    exit /b 1
)

echo.
echo ===== KHỞI ĐỘNG ỨNG DỤNG =====
echo.

REM Chạy ứng dụng
set "PATH_TO_JAVA=%JAVA_HOME%\bin"
echo [Đang khởi động...]
"%PATH_TO_JAVA%\java" --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp "out;bin;%MYSQL_CONNECTOR%" application.Main

if errorlevel 1 (
    echo.
    echo [LỖI] Ứng dụng gặp lỗi!
    echo Kiểm tra:
    echo   - MySQL server có chạy không
    echo   - Database đã import chưa
)

pause
