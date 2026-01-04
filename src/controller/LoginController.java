package controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.UserSession;
import services.MysqlConnection;

public class LoginController {
	@FXML
	private TextField tfUsername;
	@FXML
	private PasswordField tfPassword;
	@FXML
	private RadioButton rbAdmin;
	@FXML
	private RadioButton rbUser;
	
	@FXML
	public void initialize() {
		ToggleGroup group = new ToggleGroup();
		rbAdmin.setToggleGroup(group);
		rbUser.setToggleGroup(group);
		rbAdmin.setSelected(true); // Default to Admin
	}
	
	public void Login(ActionEvent event) throws IOException {
		String name = tfUsername.getText();
		String pass = tfPassword.getText();
		boolean isAdmin = rbAdmin.isSelected();
		
		// Kiểm tra xem người dùng có chọn loại tài khoản chưa
		if (rbAdmin.getToggleGroup().getSelectedToggle() == null) {
			Alert alert = new Alert(AlertType.WARNING, "Vui lòng chọn loại tài khoản (Admin hoặc User)!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}
		
		// Nếu chọn Admin
		if (isAdmin) {
			if (!name.equals("admin") || !pass.equals("1")) {
				Alert alert = new Alert(AlertType.WARNING, "Tên tài khoản admin hoặc mật khẩu không đúng!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				return;
			}
			UserSession.getInstance().setUserInfo(null, true);
			loadAdminHome(event);
		} else {
			// Nếu chọn User - username là số CMND
			if (name.equals("admin")) {
				Alert alert = new Alert(AlertType.WARNING, "Tài khoản này chỉ dành cho Admin! Vui lòng chọn Admin hoặc sử dụng tài khoản khác!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				return;
			}
			
			// Kiểm tra CMND từ database
			try {
				if (validateUserCMND(name, pass)) {
					UserSession.getInstance().setUserInfo(name, false);
					loadUserHome(event);
				} else {
					Alert alert = new Alert(AlertType.WARNING, "Số CMND hoặc mật khẩu không đúng!", ButtonType.OK);
					alert.setHeaderText(null);
					alert.showAndWait();
				}
			} catch (ClassNotFoundException | SQLException e) {
				Alert alert = new Alert(AlertType.ERROR, "Lỗi khi kết nối database: " + e.getMessage(), ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				e.printStackTrace();
			}
		}
	}
	
	private boolean validateUserCMND(String cmnd, String password) throws ClassNotFoundException, SQLException {
		java.sql.Connection connection = MysqlConnection.getMysqlConnection();
		String query = "SELECT ID FROM nhan_khau WHERE CMND = ?";
		java.sql.PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, cmnd);
		java.sql.ResultSet rs = preparedStatement.executeQuery();
		
		boolean exists = rs.next();
		preparedStatement.close();
		connection.close();
		return exists;
	}
	
	private void loadAdminHome(ActionEvent event) throws IOException {
		Parent home = FXMLLoader.load(getClass().getResource("/views/Home3.fxml"));
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(home,800,600));
        stage.setResizable(false);
        stage.show();
	}
	
	private void loadUserHome(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserPayment.fxml"));
		Parent home = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(home,1000,700));
        stage.setResizable(false);
        stage.show();
	}

}
