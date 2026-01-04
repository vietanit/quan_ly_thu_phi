package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPaymentService {
	
	/**
	 * Lấy danh sách khoản phí đã đóng của user (dựa vào CMND)
	 * @param cmnd - số CMND của user
	 * @param loaiKhoan - 0: bắt buộc, 1: tự nguyện, -1: tất cả
	 * @return List<Map> với thông tin khoản phí đã đóng
	 */
	public List<Map<String, Object>> getPaidFees(String cmnd, int loaiKhoan) throws ClassNotFoundException, SQLException {
		List<Map<String, Object>> list = new ArrayList<>();
		Connection connection = MysqlConnection.getMysqlConnection();
		
		String query = "SELECT k.MaKhoanThu, k.TenKhoanThu, k.SoTien, n.NgayThu, k.LoaiKhoanThu " +
					   "FROM khoan_thu k " +
					   "INNER JOIN nop_tien n ON k.MaKhoanThu = n.MaKhoanThu " +
					   "INNER JOIN nhan_khau nh ON n.IDNopTien = nh.ID " +
					   "WHERE nh.CMND = ? ";
		
		if (loaiKhoan != -1) {
			query += " AND k.LoaiKhoanThu = ? ";
		}
		
		query += "ORDER BY n.NgayThu DESC";
		
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, cmnd);
		if (loaiKhoan != -1) {
			preparedStatement.setInt(2, loaiKhoan);
		}
		
		ResultSet rs = preparedStatement.executeQuery();
		
		while (rs.next()) {
			Map<String, Object> map = new HashMap<>();
			map.put("MaKhoanThu", rs.getInt("MaKhoanThu"));
			map.put("TenKhoanThu", rs.getString("TenKhoanThu"));
			map.put("SoTien", rs.getDouble("SoTien"));
			map.put("NgayThu", rs.getDate("NgayThu"));
			map.put("LoaiKhoanThu", rs.getInt("LoaiKhoanThu"));
			list.add(map);
		}
		
		preparedStatement.close();
		connection.close();
		return list;
	}
	
	/**
	 * Lấy danh sách khoản phí chưa đóng của user (dựa vào CMND)
	 * @param cmnd - số CMND của user
	 * @param loaiKhoan - 0: bắt buộc, 1: tự nguyện, -1: tất cả
	 * @return List<Map> với thông tin khoản phí chưa đóng
	 */
	public List<Map<String, Object>> getUnpaidFees(String cmnd, int loaiKhoan) throws ClassNotFoundException, SQLException {
		List<Map<String, Object>> list = new ArrayList<>();
		Connection connection = MysqlConnection.getMysqlConnection();
		
		String query = "SELECT k.MaKhoanThu, k.TenKhoanThu, k.SoTien, k.LoaiKhoanThu " +
					   "FROM khoan_thu k " +
					   "WHERE k.MaKhoanThu NOT IN (" +
					   "  SELECT DISTINCT n.MaKhoanThu " +
					   "  FROM nop_tien n " +
					   "  INNER JOIN nhan_khau nh ON n.IDNopTien = nh.ID " +
					   "  WHERE nh.CMND = ?" +
					   ") ";
		
		if (loaiKhoan != -1) {
			query += " AND k.LoaiKhoanThu = ? ";
		}
		
		query += "ORDER BY k.MaKhoanThu";
		
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, cmnd);
		if (loaiKhoan != -1) {
			preparedStatement.setInt(2, loaiKhoan);
		}
		
		ResultSet rs = preparedStatement.executeQuery();
		
		while (rs.next()) {
			Map<String, Object> map = new HashMap<>();
			map.put("MaKhoanThu", rs.getInt("MaKhoanThu"));
			map.put("TenKhoanThu", rs.getString("TenKhoanThu"));
			map.put("SoTien", rs.getDouble("SoTien"));
			map.put("LoaiKhoanThu", rs.getInt("LoaiKhoanThu"));
			list.add(map);
		}
		
		preparedStatement.close();
		connection.close();
		return list;
	}
	
}
