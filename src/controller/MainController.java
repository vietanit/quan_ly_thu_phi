package controller;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import models.HoKhauModel;
import models.KhoanThuModel;
import models.NhanKhauModel;
import models.NopTienModel;
import models.QuanHeModel;
import services.HoKhauService;
import services.KhoanThuService;
import services.NhanKhauService;
import services.NopTienService;
import services.QuanHeService;

public class MainController implements Initializable{
	@FXML
	private Label lbSoHoKhau;

	@FXML
	private Label lbSoKhoanThu;

	@FXML
	private Label lbSoNhanKhau;

	@FXML
	private Label lbTongTienThu;

	@FXML
	private Label lbTiLeDoPhí;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			// Số hộ khẩu
			List<HoKhauModel> listHoKhau = new HoKhauService().getListHoKhau();
			long soHoKhau = listHoKhau.stream().count();
			lbSoHoKhau.setText(Long.toString(soHoKhau));
			
			// Số khoản thu
			List<KhoanThuModel> listKhoanThu = new KhoanThuService().getListKhoanThu();
			long soKhoanThu = listKhoanThu.stream().count();
			lbSoKhoanThu.setText(Long.toString(soKhoanThu));

			// Số nhân khẩu
			List<NhanKhauModel> listNhanKhau = new NhanKhauService().getListNhanKhau();
			long soNhanKhau = listNhanKhau.stream().count();
			lbSoNhanKhau.setText(Long.toString(soNhanKhau));

			// Tổng tiền thu (tính từ các khoản đã nộp)
			List<NopTienModel> listNopTien = new NopTienService().getListNopTien();
			double tongTienThu = 0;
			for (NopTienModel nop : listNopTien) {
				for (KhoanThuModel khoan : listKhoanThu) {
					if (nop.getMaKhoanThu() == khoan.getMaKhoanThu()) {
						tongTienThu += khoan.getSoTien();
						break;
					}
				}
			}
			NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
			lbTongTienThu.setText(nf.format((long)tongTienThu) + " đ");

			// Tỉ lệ đóng phí (số hộ đóng / tổng số hộ)
			QuanHeService quanHeService = new QuanHeService();
			List<QuanHeModel> listQuanHe = quanHeService.getListQuanHe();
			Set<Integer> distinctMaHoDaDong = listNopTien.stream()
				.map(nop -> {
					try {
						return quanHeService.getMaHoByIdThanhVien(nop.getIdNopTien());
					} catch (Exception e) {
						return -1;
					}
				}).filter(m -> m > 0).collect(Collectors.toSet());
			
			double tiLe = soHoKhau > 0 ? (distinctMaHoDaDong.size() * 100.0 / soHoKhau) : 0;
			lbTiLeDoPhí.setText(String.format("%.1f%%", tiLe));
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
	}
}
