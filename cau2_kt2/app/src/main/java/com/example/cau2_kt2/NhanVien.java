package com.example.cau2_kt2;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private String phong;

    public NhanVien() {
    }

    public NhanVien(String maNV, String tenNV, String phong) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.phong = phong;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getPhong() {
        return phong;
    }

    public void setPhong(String phong) {
        this.phong = phong;
    }
}
