package com.example.cau2_kt2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuanLyNhanVien.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PHONGBAN = "PHONGBAN";
    private static final String TABLE_NHANVIEN = "NHANVIEN";

    //  PHONGBAN
    private static final String PB_MAPH = "MAPH";
    private static final String PB_TENPH = "TENPH";
    private static final String PB_MOTA = "MOTA";

    //  NHANVIEN
    private static final String NV_MANV = "MANV";
    private static final String NV_TENNV = "TENNV";
    private static final String NV_PHONG = "PHONG";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // tao bang PHONGBAN
        String CREATE_TABLE_PHONGBAN = "CREATE TABLE " + TABLE_PHONGBAN + "("
                + PB_MAPH + " TEXT PRIMARY KEY,"
                + PB_TENPH + " TEXT,"
                + PB_MOTA + " TEXT)";
        db.execSQL(CREATE_TABLE_PHONGBAN);

        // tao bang NHANVIEN
        String CREATE_TABLE_NHANVIEN = "CREATE TABLE " + TABLE_NHANVIEN + "("
                + NV_MANV + " TEXT PRIMARY KEY,"
                + NV_TENNV + " TEXT,"
                + NV_PHONG + " TEXT,"
                + "FOREIGN KEY(" + NV_PHONG + ") REFERENCES " + TABLE_PHONGBAN + "(" + PB_MAPH + "))";
        db.execSQL(CREATE_TABLE_NHANVIEN);

        // them du lieu mau
        themDuLieuMau(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NHANVIEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONGBAN);
        onCreate(db);
    }

    private void themDuLieuMau(SQLiteDatabase db) {
        // them phong ban mau
        db.execSQL("INSERT INTO " + TABLE_PHONGBAN + " VALUES ('PB01', 'Phòng Hành chính', 'Quản lý hành chính')");
        db.execSQL("INSERT INTO " + TABLE_PHONGBAN + " VALUES ('PB02', 'Phòng Kế toán', 'Quản lý tài chính')");
        db.execSQL("INSERT INTO " + TABLE_PHONGBAN + " VALUES ('PB03', 'Phòng Kinh doanh', 'Kinh doanh và bán hàng')");
        db.execSQL("INSERT INTO " + TABLE_PHONGBAN + " VALUES ('PB04', 'Phòng IT', 'Công nghệ thông tin')");

        // Them nhan vien mau
        db.execSQL("INSERT INTO " + TABLE_NHANVIEN + " VALUES ('NV01', 'Le Van Hoang', 'PB01')");
        db.execSQL("INSERT INTO " + TABLE_NHANVIEN + " VALUES ('NV02', 'Nguyen Ngoc Hoa', 'PB01')");
        db.execSQL("INSERT INTO " + TABLE_NHANVIEN + " VALUES ('NV03', 'Tran Thi B', 'PB02')");
        db.execSQL("INSERT INTO " + TABLE_NHANVIEN + " VALUES ('NV04', 'Le Van C', 'PB03')");
        db.execSQL("INSERT INTO " + TABLE_NHANVIEN + " VALUES ('NV05', 'Pham Thi D', 'PB04')");
        db.execSQL("INSERT INTO " + TABLE_NHANVIEN + " VALUES ('NV06', 'Hoang Van E', 'PB02')");
    }

    // cac phuong thuc cua phong ban
    public boolean themPhongBan(PhongBan pb) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PB_MAPH, pb.getMaPH());
        values.put(PB_TENPH, pb.getTenPH());
        values.put(PB_MOTA, pb.getMoTa());
        long result = db.insert(TABLE_PHONGBAN, null, values);
        return result != -1;
    }

    public List<PhongBan> layDanhSachPhongBan() {
        List<PhongBan> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PHONGBAN, null);

        if (cursor.moveToFirst()) {
            do {
                PhongBan pb = new PhongBan();
                pb.setMaPH(cursor.getString(0));
                pb.setTenPH(cursor.getString(1));
                pb.setMoTa(cursor.getString(2));
                list.add(pb);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // cac phuong thuc cua nhan vien
    public boolean themNhanVien(NhanVien nv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NV_MANV, nv.getMaNV());
        values.put(NV_TENNV, nv.getTenNV());
        values.put(NV_PHONG, nv.getPhong());
        long result = db.insert(TABLE_NHANVIEN, null, values);
        return result != -1;
    }

    public boolean capNhatNhanVien(NhanVien nv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NV_TENNV, nv.getTenNV());
        values.put(NV_PHONG, nv.getPhong());
        int result = db.update(TABLE_NHANVIEN, values, NV_MANV + " = ?", new String[] { nv.getMaNV() });
        return result > 0;
    }

    public boolean xoaNhanVien(String maNV) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NHANVIEN, NV_MANV + " = ?", new String[] { maNV });
        return result > 0;
    }

    public List<NhanVien> layTatCaNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NHANVIEN, null);

        if (cursor.moveToFirst()) {
            do {
                NhanVien nv = new NhanVien();
                nv.setMaNV(cursor.getString(0));
                nv.setTenNV(cursor.getString(1));
                nv.setPhong(cursor.getString(2));
                list.add(nv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<NhanVien> layNhanVienTheoPhong(String maPhong) {
        List<NhanVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NHANVIEN + " WHERE " + NV_PHONG + " = ?",
                new String[] { maPhong });

        if (cursor.moveToFirst()) {
            do {
                NhanVien nv = new NhanVien();
                nv.setMaNV(cursor.getString(0));
                nv.setTenNV(cursor.getString(1));
                nv.setPhong(cursor.getString(2));
                list.add(nv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public NhanVien layNhanVienTheoMa(String maNV) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NHANVIEN + " WHERE " + NV_MANV + " = ?",
                new String[] { maNV });

        NhanVien nv = null;
        if (cursor.moveToFirst()) {
            nv = new NhanVien();
            nv.setMaNV(cursor.getString(0));
            nv.setTenNV(cursor.getString(1));
            nv.setPhong(cursor.getString(2));
        }
        cursor.close();
        return nv;
    }
}
