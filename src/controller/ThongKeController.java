package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.KhoanThuModel;
import models.NopTienModel;
import services.HoKhauService;
import services.KhoanThuService;
import services.NopTienService;
import services.QuanHeService;

public class ThongKeController implements Initializable {
	@FXML
	TableColumn<KhoanThuModel, String> colTenPhi;
	@FXML
	TableColumn<KhoanThuModel, String> colSoHoDaDong;
	@FXML
	TableColumn<KhoanThuModel, String> colSoHoChuaDong;
	@FXML
	Button btnShowDetails;
	@FXML
	TableView<KhoanThuModel> tvThongKe;
	@FXML
	ComboBox<String> cbChooseSearch;

	private ObservableList<KhoanThuModel> listValueTableView;
	private List<KhoanThuModel> listKhoanThu;

	// map maKhoanThu -> set of MaHo đã đóng
	private Map<Integer, Set<Integer>> paidHouseholdsByKhoan = new HashMap<>();

	public void showThongKe() throws ClassNotFoundException, SQLException {
		listKhoanThu = new KhoanThuService().getListKhoanThu();
		listValueTableView = FXCollections.observableArrayList(listKhoanThu);

		List<NopTienModel> listNopTien = new NopTienService().getListNopTien();

		// build mapping from MaKhoanThu -> set of MaHo (unique) that have paid
		paidHouseholdsByKhoan.clear();
		QuanHeService quanHeService = new QuanHeService();
		for (NopTienModel n : listNopTien) {
			int maKhoan = n.getMaKhoanThu();
			int idThanhVien = n.getIdNopTien();
			int maHo = quanHeService.getMaHoByIdThanhVien(idThanhVien);
			if (maHo <= 0) continue;
			paidHouseholdsByKhoan.computeIfAbsent(maKhoan, k -> new HashSet<>()).add(maHo);
		}

		// tổng số hộ (tính một lần)
		long tongHo = new HoKhauService().getListHoKhau().size();

		// thiết lập các cột
		colTenPhi.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("tenKhoanThu"));
		colSoHoDaDong.setCellValueFactory((CellDataFeatures<KhoanThuModel, String> p) -> {
			int ma = p.getValue().getMaKhoanThu();
			int val = paidHouseholdsByKhoan.getOrDefault(ma, new HashSet<>()).size();
			return new ReadOnlyStringWrapper(Integer.toString(val));
		});
		colSoHoChuaDong.setCellValueFactory((CellDataFeatures<KhoanThuModel, String> p) -> {
			int ma = p.getValue().getMaKhoanThu();
			int daDong = paidHouseholdsByKhoan.getOrDefault(ma, new HashSet<>()).size();
			long chua = tongHo - daDong;
			return new ReadOnlyStringWrapper(Long.toString(chua < 0 ? 0 : chua));
		});

		tvThongKe.setItems(listValueTableView);

		ObservableList<String> listComboBox = FXCollections.observableArrayList("Bắt buộc", "Tự nguyện");
		cbChooseSearch.setValue("Tất cả");
		cbChooseSearch.setItems(listComboBox);
	}

	public void showDetails() {
		KhoanThuModel selected = tvThongKe.getSelectionModel().getSelectedItem();
		if (selected == null) return;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ThongKeDetails.fxml"));
			Parent root = loader.load();
			ThongKeDetailsController ctrl = loader.getController();
			ctrl.loadData(selected.getMaKhoanThu());
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Chi tiết khoản thu");
			stage.setScene(new Scene(root));
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loc() {
		ObservableList<KhoanThuModel> listValueTableView_tmp = null;

		List<KhoanThuModel> listKhoanThuBatBuoc = new ArrayList<>();
		List<KhoanThuModel> listKhoanThuTuNguyen = new ArrayList<>();
		for (KhoanThuModel khoanThuModel : listKhoanThu) {
			if (khoanThuModel.getLoaiKhoanThu() == 0) {
				listKhoanThuTuNguyen.add(khoanThuModel);
			} else {
				listKhoanThuBatBuoc.add(khoanThuModel);
			}
		}

		SingleSelectionModel<String> typeSearch = cbChooseSearch.getSelectionModel();
		String typeSearchString = typeSearch.getSelectedItem();

		switch (typeSearchString) {
		case "Tất cả":
			tvThongKe.setItems(listValueTableView);
			break;
		case "Bắt buộc":
			listValueTableView_tmp = FXCollections.observableArrayList(listKhoanThuBatBuoc);
			tvThongKe.setItems(listValueTableView_tmp);
			break;
		case "Tự nguyện":
			listValueTableView_tmp = FXCollections.observableArrayList(listKhoanThuTuNguyen);
			tvThongKe.setItems(listValueTableView_tmp);
			break;
		default:
			break;
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			showThongKe();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}
}
