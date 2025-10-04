package com.example.cau2_kt2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class NhanVienActivity extends AppCompatActivity {

    private EditText edtMaNV, edtTenNV;
    private Spinner spinnerPhong;
    private Button btnLuu, btnHuy;
    private DatabaseHelper dbHelper;
    private boolean isEdit = false;
    private String maNVCu = "";
    private List<PhongBan> danhSachPhongBan;
    private ArrayAdapter<PhongBan> adapterPhongBan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhanvien);

        edtMaNV = findViewById(R.id.edtMaNV);
        edtTenNV = findViewById(R.id.edtTenNV);
        spinnerPhong = findViewById(R.id.spinnerPhong);
        btnLuu = findViewById(R.id.btnLuu);
        btnHuy = findViewById(R.id.btnHuy);

        dbHelper = new DatabaseHelper(this);

        loadPhongBan();

        String maNV = getIntent().getStringExtra("MANV");
        if (maNV != null && !maNV.isEmpty()) {
            isEdit = true;
            maNVCu = maNV;
            loadThongTinNhanVien(maNV);
            edtMaNV.setEnabled(false);
        }

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luuNhanVien();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void loadPhongBan() {
        danhSachPhongBan = dbHelper.layDanhSachPhongBan();
        adapterPhongBan = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachPhongBan);
        adapterPhongBan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhong.setAdapter(adapterPhongBan);
    }

    private void loadThongTinNhanVien(String maNV) {
        NhanVien nv = dbHelper.layNhanVienTheoMa(maNV);
        if (nv != null) {
            edtMaNV.setText(nv.getMaNV());
            edtTenNV.setText(nv.getTenNV());

            for (int i = 0; i < danhSachPhongBan.size(); i++) {
                if (danhSachPhongBan.get(i).getMaPH().equals(nv.getPhong())) {
                    spinnerPhong.setSelection(i);
                    break;
                }
            }
        }
    }

    private void luuNhanVien() {
        String maNV = edtMaNV.getText().toString().trim();
        String tenNV = edtTenNV.getText().toString().trim();

        if (maNV.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã nhân viên", Toast.LENGTH_SHORT).show();
            edtMaNV.requestFocus();
            return;
        }

        if (tenNV.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên nhân viên", Toast.LENGTH_SHORT).show();
            edtTenNV.requestFocus();
            return;
        }

        PhongBan phongBanSelected = (PhongBan) spinnerPhong.getSelectedItem();
        if (phongBanSelected == null) {
            Toast.makeText(this, "Vui lòng chọn phòng ban", Toast.LENGTH_SHORT).show();
            return;
        }
        //tao doi tuong nhan vien
        NhanVien nv = new NhanVien(maNV, tenNV, phongBanSelected.getMaPH());

        boolean result;
        if (isEdit) {
            // Cap nhat
            result = dbHelper.capNhatNhanVien(nv);
            if (result) {
                Toast.makeText(this, "Cập nhật nhân viên thành công", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Cập nhật nhân viên thất bại", Toast.LENGTH_SHORT).show();
            }
        } else {
            // them moi
            result = dbHelper.themNhanVien(nv);
            if (result) {
                Toast.makeText(this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Thêm nhân viên thất bại (có thể mã đã tồn tại)", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
