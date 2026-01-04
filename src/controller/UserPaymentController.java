package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.UserSession;
import services.UserPaymentService;

public class UserPaymentController {
	
	@FXML
	private TableView<Map<String, Object>> tablePaidFees;
	@FXML
	private TableColumn<Map<String, Object>, Integer> colPaidMaKhoan;
	@FXML
	private TableColumn<Map<String, Object>, String> colPaidTenKhoan;
	@FXML
	private TableColumn<Map<String, Object>, Double> colPaidSoTien;
	@FXML
	private TableColumn<Map<String, Object>, String> colPaidLoai;
	@FXML
	private TableColumn<Map<String, Object>, String> colPaidNgayDong;
	
	@FXML
	private TableView<Map<String, Object>> tableUnpaidFees;
	@FXML
	private TableColumn<Map<String, Object>, Integer> colUnpaidMaKhoan;
	@FXML
	private TableColumn<Map<String, Object>, String> colUnpaidTenKhoan;
	@FXML
	private TableColumn<Map<String, Object>, Double> colUnpaidSoTien;
	@FXML
	private TableColumn<Map<String, Object>, String> colUnpaidLoai;
	
	@FXML
	private TextField tfSearchPaid;
	@FXML
	private TextField tfSearchUnpaid;
	@FXML
	private CheckBox cbBatBuoc;
	@FXML
	private CheckBox cbTuNguyen;
	
	private UserPaymentService paymentService;
	private List<Map<String, Object>> allPaidFees;
	private List<Map<String, Object>> allUnpaidFees;
	
	@FXML
	public void initialize() {
		paymentService = new UserPaymentService();
		allPaidFees = new ArrayList<>();
		allUnpaidFees = new ArrayList<>();
		
		// Setup table columns
		setupPaidTableColumns();
		setupUnpaidTableColumns();
		
		// Load data
		loadPaymentData();
	}
	
	private void setupPaidTableColumns() {
		colPaidMaKhoan.setCellValueFactory(param -> {
			Integer value = (Integer) param.getValue().get("MaKhoanThu");
			return new javafx.beans.property.SimpleObjectProperty<>(value);
		});
		
		colPaidTenKhoan.setCellValueFactory(param -> {
			String value = (String) param.getValue().get("TenKhoanThu");
			return new javafx.beans.property.SimpleObjectProperty<>(value);
		});
		
		colPaidSoTien.setCellValueFactory(param -> {
			Double value = (Double) param.getValue().get("SoTien");
			return new javafx.beans.property.SimpleObjectProperty<>(value);
		});
		
		colPaidLoai.setCellValueFactory(param -> {
			Integer loai = (Integer) param.getValue().get("LoaiKhoanThu");
			String text = loai == 0 ? "Bắt buộc" : "Tự nguyện";
			return new javafx.beans.property.SimpleObjectProperty<>(text);
		});
		
		colPaidNgayDong.setCellValueFactory(param -> {
			java.sql.Date date = (java.sql.Date) param.getValue().get("NgayThu");
			String value = date != null ? date.toString() : "";
			return new javafx.beans.property.SimpleObjectProperty<>(value);
		});
	}
	
	private void setupUnpaidTableColumns() {
		colUnpaidMaKhoan.setCellValueFactory(param -> {
			Integer value = (Integer) param.getValue().get("MaKhoanThu");
			return new javafx.beans.property.SimpleObjectProperty<>(value);
		});
		
		colUnpaidTenKhoan.setCellValueFactory(param -> {
			String value = (String) param.getValue().get("TenKhoanThu");
			return new javafx.beans.property.SimpleObjectProperty<>(value);
		});
		
		colUnpaidSoTien.setCellValueFactory(param -> {
			Double value = (Double) param.getValue().get("SoTien");
			return new javafx.beans.property.SimpleObjectProperty<>(value);
		});
		
		colUnpaidLoai.setCellValueFactory(param -> {
			Integer loai = (Integer) param.getValue().get("LoaiKhoanThu");
			String text = loai == 0 ? "Bắt buộc" : "Tự nguyện";
			return new javafx.beans.property.SimpleObjectProperty<>(text);
		});
	}
	
	private void loadPaymentData() {
		String cmnd = UserSession.getInstance().getCmnd();
		if (cmnd == null) {
			showAlert(AlertType.ERROR, "Lỗi", "Không tìm thấy thông tin người dùng!");
			return;
		}
		
		try {
			allPaidFees = paymentService.getPaidFees(cmnd, -1);
			allUnpaidFees = paymentService.getUnpaidFees(cmnd, -1);
			
			displayPaidFees(allPaidFees);
			displayUnpaidFees(allUnpaidFees);
		} catch (ClassNotFoundException | SQLException e) {
			showAlert(AlertType.ERROR, "Lỗi", "Lỗi khi tải dữ liệu: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void displayPaidFees(List<Map<String, Object>> fees) {
		ObservableList<Map<String, Object>> data = FXCollections.observableArrayList(fees);
		tablePaidFees.setItems(data);
	}
	
	private void displayUnpaidFees(List<Map<String, Object>> fees) {
		ObservableList<Map<String, Object>> data = FXCollections.observableArrayList(fees);
		tableUnpaidFees.setItems(data);
	}
	
	@FXML
	private void applyFilters(ActionEvent event) {
		String cmnd = UserSession.getInstance().getCmnd();
		
		try {
			int loaiKhoan = getSelectedLoaiKhoan();
			allPaidFees = paymentService.getPaidFees(cmnd, loaiKhoan);
			allUnpaidFees = paymentService.getUnpaidFees(cmnd, loaiKhoan);
			
			displayPaidFees(allPaidFees);
			displayUnpaidFees(allUnpaidFees);
			
			// Clear search fields
			tfSearchPaid.clear();
			tfSearchUnpaid.clear();
		} catch (ClassNotFoundException | SQLException e) {
			showAlert(AlertType.ERROR, "Lỗi", "Lỗi khi tải dữ liệu: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@FXML
	private void resetFilters(ActionEvent event) {
		cbBatBuoc.setSelected(false);
		cbTuNguyen.setSelected(false);
		tfSearchPaid.clear();
		tfSearchUnpaid.clear();
		loadPaymentData();
	}
	
	private int getSelectedLoaiKhoan() {
		boolean batBuoc = cbBatBuoc.isSelected();
		boolean tuNguyen = cbTuNguyen.isSelected();
		
		if (batBuoc && !tuNguyen) {
			return 0; // Chỉ bắt buộc
		} else if (!batBuoc && tuNguyen) {
			return 1; // Chỉ tự nguyện
		} else {
			return -1; // Cả hai
		}
	}
	
	@FXML
	private void searchPaidFees(ActionEvent event) {
		String searchText = tfSearchPaid.getText().trim().toLowerCase();
		if (searchText.isEmpty()) {
			displayPaidFees(allPaidFees);
			return;
		}
		
		List<Map<String, Object>> filtered = new ArrayList<>();
		for (Map<String, Object> fee : allPaidFees) {
			String tenKhoan = ((String) fee.get("TenKhoanThu")).toLowerCase();
			if (tenKhoan.contains(searchText)) {
				filtered.add(fee);
			}
		}
		displayPaidFees(filtered);
	}
	
	@FXML
	private void searchUnpaidFees(ActionEvent event) {
		String searchText = tfSearchUnpaid.getText().trim().toLowerCase();
		if (searchText.isEmpty()) {
			displayUnpaidFees(allUnpaidFees);
			return;
		}
		
		List<Map<String, Object>> filtered = new ArrayList<>();
		for (Map<String, Object> fee : allUnpaidFees) {
			String tenKhoan = ((String) fee.get("TenKhoanThu")).toLowerCase();
			if (tenKhoan.contains(searchText)) {
				filtered.add(fee);
			}
		}
		displayUnpaidFees(filtered);
	}
	
	@FXML
	private void clearSearchPaid(ActionEvent event) {
		tfSearchPaid.clear();
		displayPaidFees(allPaidFees);
	}
	
	@FXML
	private void clearSearchUnpaid(ActionEvent event) {
		tfSearchUnpaid.clear();
		displayUnpaidFees(allUnpaidFees);
	}
	
	@FXML
	private void logout(ActionEvent event) throws IOException {
		UserSession.getInstance().clear();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
		Parent login = loader.load();
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(login, 800, 600));
		stage.setResizable(false);
		stage.show();
	}
	
	private void showAlert(AlertType type, String title, String message) {
		Alert alert = new Alert(type, message);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}

