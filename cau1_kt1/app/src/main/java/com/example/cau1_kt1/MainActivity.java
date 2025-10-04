package com.example.cau1_kt1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etMoiNhapTen;
    private Button btnNhap, btnXemChiTiet;
    private ListView lvDanhSach;

    private ArrayList<String> danhSachItems;
    private ArrayAdapter<String> adapter;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etMoiNhapTen = findViewById(R.id.etMoiNhapTen);
        btnNhap = findViewById(R.id.btnNhap);
        btnXemChiTiet = findViewById(R.id.btnXemChiTiet);
        lvDanhSach = findViewById(R.id.lvDanhSach);


        danhSachItems = new ArrayList<>();


        danhSachItems.add("Hòa");
        danhSachItems.add("Việt");
        danhSachItems.add("Hoài");
        danhSachItems.add("Hoàng");


        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                danhSachItems);


        lvDanhSach.setAdapter(adapter);


        btnNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = etMoiNhapTen.getText().toString().trim();

                if (!ten.isEmpty()) {

                    danhSachItems.add(ten);


                    adapter.notifyDataSetChanged();


                    etMoiNhapTen.setText("");

                    Toast.makeText(MainActivity.this, "Đã thêm: " + ten, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        lvDanhSach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                btnXemChiTiet.setEnabled(true);

                Toast.makeText(MainActivity.this,
                        "Đã chọn: " + danhSachItems.get(position) + " (vị trí: " + position + ")",
                        Toast.LENGTH_SHORT).show();
            }
        });


        btnXemChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition != -1) {

                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);


                    intent.putExtra("position", selectedPosition);
                    intent.putExtra("value", danhSachItems.get(selectedPosition));


                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng chọn một item!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}