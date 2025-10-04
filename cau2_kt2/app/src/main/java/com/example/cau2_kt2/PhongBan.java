package com.example.cau2_kt2;

public class PhongBan {
    private String maPH;
    private String tenPH;
    private String moTa;

    public PhongBan() {
    }

    public PhongBan(String maPH, String tenPH, String moTa) {
        this.maPH = maPH;
        this.tenPH = tenPH;
        this.moTa = moTa;
    }

    public String getMaPH() {
        return maPH;
    }

    public void setMaPH(String maPH) {
        this.maPH = maPH;
    }

    public String getTenPH() {
        return tenPH;
    }

    public void setTenPH(String tenPH) {
        this.tenPH = tenPH;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return tenPH;
    }
}
