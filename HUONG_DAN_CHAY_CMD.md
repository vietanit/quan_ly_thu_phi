# Hướng dẫn Chạy Project từ CMD (Windows)

## Tóm tắt nhanh
1. **SETUP.bat** — Cấu hình đường dẫn (chạy 1 lần duy nhất)
2. **BUILD.bat** — Biên dịch project (mỗi lần sửa code)
3. **RUN.bat** — Chạy ứng dụng

---

## Yêu cầu cài đặt trước

### 1. JDK 17 (hoặc 11+)
- Tải từ: https://adoptium.net/
- Cài đặt vào thư mục (ví dụ: `C:\Program Files\Java\jdk-17.0.2`)
- Kiểm tra lại:
  ```cmd
  java -version
  javac -version
  ```

### 2. JavaFX SDK 17
- Tải từ: https://gluonhq.com/products/javafx/
- Giải nén vào thư mục (ví dụ: `C:\javafx-sdk-17.0.2`)
- Thư mục `lib` bên trong sẽ là: `C:\javafx-sdk-17.0.2\lib`

### 3. MySQL Server
- Cài từ: https://dev.mysql.com/downloads/mysql/
- Hoặc dùng XAMPP/WAMP (bao gồm MySQL)
- Khởi động MySQL service
- Kiểm tra:
  ```cmd
  mysql -u root -p
  (Enter password nếu có, hoặc bỏ trống)
  ```

### 4. MySQL Connector/J (JDBC Driver)
- Tải từ: https://dev.mysql.com/downloads/connector/j/
- Tải phiên bản `mysql-connector-java-8.0.xx.jar`
- Lưu vào thư mục (ví dụ: `C:\libs\mysql-connector-java-8.0.33.jar`)

### 5. Import Database
Mở CMD/PowerShell, chạy:
```cmd
mysql -u root -p < "d:\bài tập lớn nmcnpm\quan-ly-thu-tien\database\quan_ly_khoan_thu.sql"
```
(Nếu password trống, chỉ Enter. Nếu có password, nhập khi được hỏi)

Hoặc dùng phpMyAdmin (nếu dùng XAMPP):
- Truy cập: http://localhost/phpmyadmin
- Tạo database `quan_ly_khoan_thu`
- Import file `database/quan_ly_khoan_thu.sql`

---

## Các bước để chạy

### Bước 1: Cấu hình (Lần đầu tiên)

Mở CMD, navigate đến thư mục project:
```cmd
cd d:\bài tập lớn nmcnpm\quan-ly-thu-tien
```

Chạy script cấu hình:
```cmd
SETUP.bat
```

**Khi chạy SETUP.bat, bạn sẽ:**
1. Thấy yêu cầu chỉnh sửa các đường dẫn trong script nếu chúng khác
2. Nhấn **OK** nếu tất cả đường dẫn chính xác

**Nếu cần chỉnh sửa đường dẫn:**
- Mở `SETUP.bat` bằng Notepad (hoặc editor bất kỳ)
- Tìm các dòng sau và thay thế bằng đường dẫn thực tế trên máy:
  ```batch
  set JAVA_HOME=C:\Program Files\Java\jdk-17.0.2
  set JAVAFX_PATH=C:\javafx-sdk-17.0.2\lib
  set MYSQL_CONNECTOR=C:\libs\mysql-connector-java-8.0.33.jar
  ```
- Lưu file

### Bước 2: Biên dịch Project

Chạy:
```cmd
BUILD.bat
```

**Khi chạy BUILD.bat:**
- Tìm tất cả file `.java` trong `src/`
- Biên dịch vào thư mục `out/`
- Copy các tệp resources (FXML, CSS) vào `out/`

**Kết quả:**
- Thư mục `out/` sẽ chứa tất cả bytecode và resources

**Nếu gặp lỗi biên dịch:**
- Kiểm tra thông báo lỗi (syntax error trong code)
- Sửa lỗi trong file `.java` tương ứng
- Chạy lại `BUILD.bat`

### Bước 3: Chạy Ứng dụng

Chạy:
```cmd
RUN.bat
```

**Khi chạy RUN.bat:**
1. Kiểm tra xem `out/` đã có `Main.class` chưa
2. Nếu có, khởi động JVM với:
   - Module path: trỏ đến JavaFX
   - Add modules: `javafx.controls,javafx.fxml`
   - Classpath: `out/` + MySQL driver JAR
   - Main class: `application.Main`
3. Ứng dụng sẽ hiển thị cửa sổ Login

**Khi ứng dụng khởi động:**
- Giao diện Login sẽ xuất hiện
- Nếu có lỗi database, kiểm tra MySQL service đã chạy không

---

## Các lỗi thường gặp

### Lỗi 1: "Cannot find JAVA_HOME" (Trong SETUP.bat)
**Nguyên nhân:** JDK chưa cài hoặc đường dẫn sai

**Cách sửa:**
- Kiểm tra JDK đã cài tại `C:\Program Files\Java\jdk-17.x.x`
- Mở Notepad → chỉnh sửa `SETUP.bat`:
  ```batch
  set JAVA_HOME=C:\Program Files\Java\jdk-17.0.2
  ```
  Thay thế bằng đường dẫn thực tế (nếu cài ở chỗ khác)

### Lỗi 2: "javac: error: module not found: javafx.controls" (Khi BUILD.bat)
**Nguyên nhân:** JavaFX SDK chưa cài hoặc đường dẫn sai

**Cách sửa:**
- Kiểm tra thư mục `C:\javafx-sdk-17.0.2\lib` có tồn tại không
- Mở Notepad → chỉnh sửa `SETUP.bat`:
  ```batch
  set JAVAFX_PATH=C:\javafx-sdk-17.0.2\lib
  ```
  Thay thế bằng đường dẫn thực tế

### Lỗi 3: "ClassNotFoundException: com.mysql.cj.jdbc.Driver" (Khi RUN.bat)
**Nguyên nhân:** MySQL Connector JAR chưa tải hoặc đường dẫn sai

**Cách sửa:**
- Tải MySQL Connector JAR từ: https://dev.mysql.com/downloads/connector/j/
- Lưu vào `C:\libs\mysql-connector-java-8.0.33.jar`
- Mở Notepad → chỉnh sửa `SETUP.bat`:
  ```batch
  set MYSQL_CONNECTOR=C:\libs\mysql-connector-java-8.0.33.jar
  ```

### Lỗi 4: "java.sql.SQLException: Cannot get a connection" (Khi chạy app)
**Nguyên nhân:** MySQL server chưa chạy hoặc database chưa import

**Cách sửa:**
1. Kiểm tra MySQL server chạy:
   ```cmd
   mysql -u root -p
   (Nếu kết nối được, máy chủ đang chạy)
   ```
2. Nếu chưa chạy, khởi động MySQL:
   - Windows: Mở Services (services.msc) → tìm MySQL → Start
   - Hoặc nếu dùng XAMPP, mở XAMPP Control Panel → Start MySQL
3. Import database (nếu chưa):
   ```cmd
   mysql -u root -p < "d:\bài tập lớn nmcnpm\quan-ly-thu-tien\database\quan_ly_khoan_thu.sql"
   ```

### Lỗi 5: Thông báo "Exception in thread 'main' javafx.fxml.LoadException" (Khi chạy app)
**Nguyên nhân:** FXML files không được copy vào `out/`

**Cách sửa:**
- Chạy lại `BUILD.bat` (nó sẽ copy FXML và CSS vào `out/`)
- Hoặc kiểm tra thư mục `out/views/` có chứa `Login.fxml` không

### Lỗi 6: Database connection có username/password khác
**Nguyên nhân:** Cấu hình trong MysqlConnection.java không khớp

**Cách sửa:**
- Mở `src/services/MysqlConnection.java`
- Tìm các dòng:
  ```java
  String userName = "root";
  String password = "";
  ```
- Thay thế bằng thông tin MySQL thực tế trên máy bạn
- Lưu file
- Chạy lại `BUILD.bat` và `RUN.bat`

---

## Quy trình thường ngày

Sau khi setup lần đầu, mỗi lần bạn muốn chạy:

```cmd
cd d:\bài tập lớn nmcnpm\quan-ly-thu-tien
BUILD.bat
RUN.bat
```

Nếu chỉ sửa code và không cài thêm library, chỉ cần:
```cmd
BUILD.bat
RUN.bat
```

Nếu muốn chỉ chạy lại (không biên dịch lại):
```cmd
RUN.bat
```

---

## Lệnh CMD thủ công (nếu không muốn dùng .bat)

Nếu bạn muốn chạy thủ công từng lệnh:

### Biên dịch:
```cmd
set PATH_TO_FX=C:\javafx-sdk-17.0.2\lib
set MYSQL_JAR=C:\libs\mysql-connector-java-8.0.33.jar
mkdir out
javac --module-path "%PATH_TO_FX%" --add-modules javafx.controls,javafx.fxml -cp "%MYSQL_JAR%" -d out -encoding UTF-8 src\**\*.java
```

### Copy resources:
```cmd
xcopy /I /E /Y src\views out\views
xcopy /I /E /Y src\source out\source
xcopy /I /Y src\application\*.css out\application
```

### Chạy:
```cmd
set PATH_TO_FX=C:\javafx-sdk-17.0.2\lib
set MYSQL_JAR=C:\libs\mysql-connector-java-8.0.33.jar
java --module-path "%PATH_TO_FX%" --add-modules javafx.controls,javafx.fxml -cp "out;%MYSQL_JAR%" application.Main
```

---

## Liên hệ hỗ trợ

Nếu gặp vấn đề không có trong danh sách trên, kiểm tra:
1. Phiên bản JDK, JavaFX, MySQL Connector có phù hợp không
2. Tất cả dịch vụ (MySQL) đã khởi động chưa
3. Không có khoảng trắng hoặc ký tự đặc biệt trong đường dẫn file
