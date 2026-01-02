package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @Thanh - Updated Connection
 */
public class MysqlConnection {
    
    public static Connection getMysqlConnection() throws SQLException, ClassNotFoundException {
        String hostName = "localhost";
        String dbName = "quan_ly_khoan_thu";
        String userName = "root";
        
        // --- LƯU Ý QUAN TRỌNG VỀ MẬT KHẨU ---
        // Nếu bạn dùng XAMPP/WAMP mặc định, password thường là để trống: ""
        // Nếu bạn tự cài MySQL và đặt pass là "1" thì giữ nguyên dòng dưới:
        String password = ""; 
        
        return getMysqlConnection(hostName, dbName, userName, password);
    }
    
    public static Connection getMysqlConnection(String hostName, String dbName, String userName, String password) 
            throws SQLException, ClassNotFoundException {
        
        // 1. Khai báo Driver (Dùng com.mysql.cj.jdbc.Driver cho MySQL bản mới)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Fallback cho trường hợp dùng thư viện cũ
            Class.forName("com.mysql.jdbc.Driver");
        }

        // 2. Chuỗi kết nối đầy đủ (Thêm cấu hình Tiếng Việt, TimeZone và tắt SSL để không lỗi)
        String connectionUrl = "jdbc:mysql://" + hostName + ":3306/" + dbName 
                + "?useUnicode=true&characterEncoding=UTF-8"
                + "&autoReconnect=true"
                + "&useSSL=false"
                + "&serverTimezone=Asia/Ho_Chi_Minh"; // Cài múi giờ để tránh lỗi TimeZone

        Connection conn = DriverManager.getConnection(connectionUrl, userName, password);
        return conn;
    }
}