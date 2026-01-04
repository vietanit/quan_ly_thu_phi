package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.HoKhauModel;
import models.NopTienModel;
import models.OwnerInfo;
import models.NhanKhauModel;
import services.ChuHoService;
import services.HoKhauService;
import services.NhanKhauService;
import services.NopTienService;
import services.QuanHeService;

public class ThongKeDetailsController {
    @FXML
    TableView<OwnerInfo> tvDaDong;
    @FXML
    TableColumn<OwnerInfo, Integer> colMaHoDaDong;
    @FXML
    TableColumn<OwnerInfo, String> colTenDaDong;
    @FXML
    TableColumn<OwnerInfo, String> colCmndDaDong;
    @FXML
    TableColumn<OwnerInfo, String> colSdtDaDong;

    @FXML
    TableView<OwnerInfo> tvChuaDong;
    @FXML
    TableColumn<OwnerInfo, Integer> colMaHoChuaDong;
    @FXML
    TableColumn<OwnerInfo, String> colTenChuaDong;
    @FXML
    TableColumn<OwnerInfo, String> colCmndChuaDong;
    @FXML
    TableColumn<OwnerInfo, String> colSdtChuaDong;

    public void loadData(int maKhoanThu) throws ClassNotFoundException, SQLException {
        List<NopTienModel> listNop = new NopTienService().getListNopTien();
        Set<Integer> maHoDaDong = listNop.stream().filter(n -> n.getMaKhoanThu() == maKhoanThu)
                .map(n -> {
                    try {
                        return new QuanHeService().getMaHoByIdThanhVien(n.getIdNopTien());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return -1;
                    }
                }).filter(m -> m != -1).collect(Collectors.toSet());

        List<HoKhauModel> allHo = new HoKhauService().getListHoKhau();

        // MaHo -> IDChuHo
        Map<Integer, Integer> maHoToChu = new ChuHoService().getListChuHo().stream()
                .collect(Collectors.toMap(ch -> ch.getMaHo(), ch -> ch.getIdChuHo()));

        // ID -> NhanKhau
        Map<Integer, NhanKhauModel> idToNhan = new NhanKhauService().getListNhanKhau().stream()
                .collect(Collectors.toMap(n -> n.getId(), n -> n));

        List<OwnerInfo> daDongOwners = allHo.stream().filter(h -> maHoDaDong.contains(h.getMaHo())).map(h -> {
            int maHo = h.getMaHo();
            int idChu = maHoToChu.getOrDefault(maHo, -1);
            NhanKhauModel owner = idToNhan.get(idChu);
            if (owner != null) {
                return new OwnerInfo(maHo, owner.getTen(), owner.getCmnd(), owner.getSdt());
            } else {
                return new OwnerInfo(maHo, "", "", "");
            }
        }).collect(Collectors.toList());

        List<OwnerInfo> chuaDongOwners = allHo.stream().filter(h -> !maHoDaDong.contains(h.getMaHo())).map(h -> {
            int maHo = h.getMaHo();
            int idChu = maHoToChu.getOrDefault(maHo, -1);
            NhanKhauModel owner = idToNhan.get(idChu);
            if (owner != null) {
                return new OwnerInfo(maHo, owner.getTen(), owner.getCmnd(), owner.getSdt());
            } else {
                return new OwnerInfo(maHo, "", "", "");
            }
        }).collect(Collectors.toList());

        colMaHoDaDong.setCellValueFactory(new PropertyValueFactory<>("maHo"));
        colTenDaDong.setCellValueFactory(new PropertyValueFactory<>("ten"));
        colCmndDaDong.setCellValueFactory(new PropertyValueFactory<>("cmnd"));
        colSdtDaDong.setCellValueFactory(new PropertyValueFactory<>("sdt"));

        colMaHoChuaDong.setCellValueFactory(new PropertyValueFactory<>("maHo"));
        colTenChuaDong.setCellValueFactory(new PropertyValueFactory<>("ten"));
        colCmndChuaDong.setCellValueFactory(new PropertyValueFactory<>("cmnd"));
        colSdtChuaDong.setCellValueFactory(new PropertyValueFactory<>("sdt"));

        tvDaDong.setItems(FXCollections.observableArrayList(daDongOwners));
        tvChuaDong.setItems(FXCollections.observableArrayList(chuaDongOwners));
    }
}
