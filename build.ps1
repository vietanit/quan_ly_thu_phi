# PowerShell script để biên dịch project

$JAVA_HOME = "C:\Users\PC\AppData\Local\Programs\Eclipse Adoptium\jdk-25.0.1.8-hotspot"
$JAVAFX_PATH = "C:\Users\PC\OneDrive\Desktop\openjfx-25.0.1_windows-x64_bin-sdk\javafx-sdk-25.0.1\lib"
$MYSQL_CONNECTOR = "C:\Users\PC\OneDrive\Desktop\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar"

$JAVAC = "$JAVA_HOME\bin\javac.exe"

# Chuyển sang thư mục script (project root)
Set-Location $PSScriptRoot

Write-Host ""
Write-Host "===== BẮT ĐẦU BIÊN DỊCH ====="
Write-Host "Sẽ mất vài phút tùy kích thước project..."
Write-Host ""

# Kiểm tra javac
if (-not (Test-Path $JAVAC)) {
    Write-Host "[LỖI] Không tìm thấy javac tại: $JAVAC"
    exit 1
}

# Xoá thư mục cũ
if (Test-Path "out") {
    Write-Host "Xoá thư mục 'out' cũ..."
    Remove-Item -Path "out" -Recurse -Force
}

# Tạo thư mục output
New-Item -ItemType Directory -Path "out" -Force | Out-Null
Write-Host "[OK] Tạo thư mục 'out'"

# Tìm tất cả file .java và tạo danh sách
$javaFiles = @(Get-ChildItem -Path "src" -Filter "*.java" -Recurse | ForEach-Object { $_.FullName })

if ($javaFiles.Count -eq 0) {
    Write-Host "[LỖI] Không tìm thấy file .java"
    exit 1
}

Write-Host "Tạo danh sách $($javaFiles.Count) file Java..."

# Lưu danh sách vào file (UTF-8 encoding, không BOM)
$Utf8NoBomEncoding = New-Object System.Text.UTF8Encoding($false)
[System.IO.File]::WriteAllLines("sources.txt", $javaFiles, $Utf8NoBomEncoding)

Write-Host "[OK] Danh sách tạo xong"
Write-Host ""
Write-Host "[Đang biên dịch... Chờ chút]"
Write-Host ""

# Biên dịch dùng file list
& $JAVAC --module-path $JAVAFX_PATH --add-modules javafx.controls,javafx.fxml -cp $MYSQL_CONNECTOR -d out -encoding UTF-8 "@sources.txt"

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "[LỖI] Biên dịch thất bại!"
    exit 1
}

Write-Host "[OK] Biên dịch thành công!"

# Copy resources
Write-Host ""
Write-Host "[Đang copy resources...]"

if (Test-Path "src\views") {
    Copy-Item -Path "src\views" -Destination "out\views" -Recurse -Force | Out-Null
    Write-Host "[OK] Copy views"
}

if (Test-Path "src\source") {
    Copy-Item -Path "src\source" -Destination "out\source" -Recurse -Force | Out-Null
    Write-Host "[OK] Copy source (CSS)"
}

if (Test-Path "src\application\*.css") {
    Copy-Item -Path "src\application\*.css" -Destination "out\application\" -Force | Out-Null
    Write-Host "[OK] Copy application CSS"
}

Write-Host ""
Write-Host "===== HOÀN THÀNH ====="
Write-Host "Thư mục output: out\"
Write-Host ""
Write-Host "Chạy RUN.bat để khởi động ứng dụng"
Write-Host ""
