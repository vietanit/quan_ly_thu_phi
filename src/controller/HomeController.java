package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import models.UserSession;

public class HomeController implements Initializable {
	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnLogout;
	
	public void setNhanKhau(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/views/NhanKhau.fxml"));
		Pane nhankhauPane = (Pane) loader.load();
		borderPane.setCenter(nhankhauPane);
		if (btnLogout != null) btnLogout.setVisible(false);
	}

	public void setHoKhau(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/views/HoKhau.fxml"));
		Pane hokhauPane = (Pane) loader.load();
		borderPane.setCenter(hokhauPane);
		if (btnLogout != null) btnLogout.setVisible(false);

	}

	public void setKhoanPhi(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/views/KhoanThu.fxml"));
		Pane khoanphiPane = (Pane) loader.load();
		borderPane.setCenter(khoanphiPane);
		if (btnLogout != null) btnLogout.setVisible(false);
	}
	
	public void setDongPhi(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/NopTien.fxml"));
		Pane dongphiPane = (Pane) loader.load();
		borderPane.setCenter(dongphiPane);
		if (btnLogout != null) btnLogout.setVisible(false);
	}
	
	public void setThongKe(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/views/ThongKe.fxml"));
		Pane thongkePane = (Pane) loader.load();
		borderPane.setCenter(thongkePane);
		if (btnLogout != null) btnLogout.setVisible(false);

	}
	
	public void setTrangChu(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/views/Main.fxml"));
		Pane trangchuPane = (Pane) loader.load();
		borderPane.setCenter(trangchuPane);
		if (btnLogout != null) btnLogout.setVisible(true);

	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			Pane login = FXMLLoader.load(getClass().getResource("/views/Main.fxml"));
			borderPane.setCenter(login);
			if (btnLogout != null) btnLogout.setVisible(true);
		} catch (Exception e) {
			// Catch any exception to avoid breaking the whole home view when center fails to load
			e.printStackTrace();
			// Fallback: show a simple message so the UI is not empty
			Pane fallback = new Pane();
			Label msg = new Label("Không thể tải trang chủ");
			msg.setLayoutX(20);
			msg.setLayoutY(20);
			fallback.getChildren().add(msg);
			borderPane.setCenter(fallback);
			if (btnLogout != null) btnLogout.setVisible(true);
		}

	}

	@FXML
	public void handleLogout(ActionEvent event) throws IOException {
		UserSession.getInstance().clear();
		Parent login = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(login,800,600));
		stage.setResizable(false);
		stage.show();
	}

}
