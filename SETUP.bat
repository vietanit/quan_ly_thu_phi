@echo off
REM ===================================================================
REM  SETUP.bat - Cấu hình đường dẫn cho JavaFX, JDK, MySQL Connector
REM ===================================================================
REM Bạn PHẢI chỉnh sửa các đường dẫn dưới đây cho phù hợp với máy mình

REM ===== CHỈNH SỬA CÁC ĐƯỜNG DẪN SAU =====

REM 1. Đường dẫn JDK (thư mục cài Java, có thư mục "bin" bên trong)
REM    Ví dụ: C:\Program Files\Java\jdk-17.0.2
REM    hoặc: C:\javarepo\jdk-17
set JAVA_HOME=C:\Users\PC\AppData\Local\Programs\Eclipse Adoptium\jdk-25.0.1.8-hotspot

REM 2. Đường dẫn thư mục lib của JavaFX SDK
REM    Ví dụ: C:\javafx-sdk-17.0.2\lib
set JAVAFX_PATH=C:\Users\PC\OneDrive\Desktop\openjfx-25.0.1_windows-x64_bin-sdk\javafx-sdk-25.0.1\lib

REM 3. Đường dẫn file mysql-connector-java JAR
REM    Ví dụ: C:\libs\mysql-connector-java-8.0.33.jar
set MYSQL_CONNECTOR=C:\Users\PC\OneDrive\Desktop\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar

REM ===== THÔNG TIN MYSQL (tuỳ chọn, chỉnh nếu khác mặc định) =====
REM Mặc định trong project:
REM - Host: localhost
REM - Database: quan_ly_khoan_thu
REM - Username: root
REM - Password: (trống)
REM Nếu khác, hãy chỉnh trong src\services\MysqlConnection.java

REM ===== KIỂM TRA CÁC ĐƯỜNG DẪN =====
echo.
echo ===== KIỂM TRA CẤU HÌNH =====
if not exist "%JAVA_HOME%\bin\javac.exe" (
    echo [LỖI] Không tìm thấy JDK tại: %JAVA_HOME%
    echo        Vui lòng chỉnh sửa JAVA_HOME trong file này
    pause
    exit /b 1
)
echo [OK] JDK tìm thấy tại: %JAVA_HOME%

if not exist "%JAVAFX_PATH%" (
    echo [LỖI] Không tìm thấy JavaFX lib tại: %JAVAFX_PATH%
    echo        Vui lòng chỉnh sửa JAVAFX_PATH trong file này
    pause
    exit /b 1
)
echo [OK] JavaFX tìm thấy tại: %JAVAFX_PATH%

if not exist "%MYSQL_CONNECTOR%" (
    echo [CẢNH BÁO] Không tìm thấy MySQL Connector JAR tại: %MYSQL_CONNECTOR%
    echo            Nếu không dùng database, bạn có thể bỏ qua. Nếu dùng, hãy chỉnh đường dẫn.
)
echo [OK] MySQL Connector: %MYSQL_CONNECTOR%

echo.
echo ===== CẤU HÌNH ĐÃ KIỂM TRA =====
echo JAVA_HOME = %JAVA_HOME%
echo JAVAFX_PATH = %JAVAFX_PATH%
echo MYSQL_CONNECTOR = %MYSQL_CONNECTOR%
echo.
echo Nếu tất cả [OK], bạn có thể chạy:
echo   BUILD.bat   (để biên dịch project)
echo   RUN.bat     (để chạy ứng dụng đã biên dịch)
echo.
pause
