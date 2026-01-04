package models;

public class OwnerInfo {
    private int maHo;
    private String ten;
    private String cmnd;
    private String sdt;

    public OwnerInfo() {}

    public OwnerInfo(int maHo, String ten, String cmnd, String sdt) {
        this.maHo = maHo;
        this.ten = ten;
        this.cmnd = cmnd;
        this.sdt = sdt;
    }

    public int getMaHo() {
        return maHo;
    }

    public void setMaHo(int maHo) {
        this.maHo = maHo;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
