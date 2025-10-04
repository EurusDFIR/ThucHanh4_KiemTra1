package com.example.cau2_kt2;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView lvNhanVien;
    private Spinner spinnerPhongBan;
    private Button btnApply, btnAll, btnClose;
    private ArrayAdapter<String> adapterNhanVien;
    private ArrayAdapter<PhongBan> adapterPhongBan;
    private List<NhanVien> danhSachNhanVien;
    private List<PhongBan> danhSachPhongBan;

    private int selectedPosition = -1;

    private ActivityResultLauncher<Intent> nhanVienActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvNhanVien = findViewById(R.id.lvNhanVien);
        spinnerPhongBan = findViewById(R.id.spnChonPhongBan);
        btnApply = findViewById(R.id.btnApply);
        btnAll = findViewById(R.id.btnAll);
        btnClose = findViewById(R.id.btnClose);

        dbHelper = new DatabaseHelper(this);

        nhanVienActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadAllNhanVien();
                    }
                });

        loadPhongBan();

        loadAllNhanVien();

        registerForContextMenu(lvNhanVien);

        lvNhanVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhongBan phongBanSelected = (PhongBan) spinnerPhongBan.getSelectedItem();
                if (phongBanSelected != null) {
                    loadNhanVienTheoPhong(phongBanSelected.getMaPH());
                }
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAllNhanVien();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadPhongBan() {
        danhSachPhongBan = dbHelper.layDanhSachPhongBan();
        adapterPhongBan = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachPhongBan);
        adapterPhongBan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhongBan.setAdapter(adapterPhongBan);
    }

    private void loadAllNhanVien() {
        danhSachNhanVien = dbHelper.layTatCaNhanVien();
        hienThiDanhSachNhanVien();
    }

    private void loadNhanVienTheoPhong(String maPhong) {
        danhSachNhanVien = dbHelper.layNhanVienTheoPhong(maPhong);
        hienThiDanhSachNhanVien();
    }

    private void hienThiDanhSachNhanVien() {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < danhSachNhanVien.size(); i++) {
            NhanVien nv = danhSachNhanVien.get(i);
            items.add((i + 1) + "-" + nv.getTenNV());
        }
        adapterNhanVien = new ArrayAdapter<>(this, R.layout.item_nhanvien, R.id.tvItemNhanVien, items);
        lvNhanVien.setAdapter(adapterNhanVien);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lvNhanVien) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            selectedPosition = info.position;
            menu.setHeaderTitle("Chọn thao tác");
            menu.add(0, 1, 0, "Thêm");
            menu.add(0, 2, 1, "Sửa");
            menu.add(0, 3, 2, "Xoá");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == 1) {
            // Thêm
            Intent intent = new Intent(MainActivity.this, NhanVienActivity.class);
            nhanVienActivityLauncher.launch(intent);
            return true;
        } else if (id == 2) {
            // Sửa
            if (selectedPosition >= 0 && selectedPosition < danhSachNhanVien.size()) {
                NhanVien nv = danhSachNhanVien.get(selectedPosition);
                Intent intent = new Intent(MainActivity.this, NhanVienActivity.class);
                intent.putExtra("MANV", nv.getMaNV());
                nhanVienActivityLauncher.launch(intent);
            }
            return true;
        } else if (id == 3) {
            // Xoá
            if (selectedPosition >= 0 && selectedPosition < danhSachNhanVien.size()) {
                NhanVien nv = danhSachNhanVien.get(selectedPosition);
                xacNhanXoaNhanVien(nv);
            }
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void xacNhanXoaNhanVien(final NhanVien nv) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xoá")
                .setMessage("Bạn có chắc chắn muốn xoá nhân viên " + nv.getTenNV() + " không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    boolean result = dbHelper.xoaNhanVien(nv.getMaNV());
                    if (result) {
                        Toast.makeText(MainActivity.this, "Xoá nhân viên thành công", Toast.LENGTH_SHORT).show();
                        loadAllNhanVien();
                    } else {
                        Toast.makeText(MainActivity.this, "Xoá nhân viên thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}