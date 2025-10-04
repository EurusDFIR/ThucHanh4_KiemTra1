package com.example.cau1_kt1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private TextView tvPosition, tvValue;
    private Button btnDong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        tvPosition = findViewById(R.id.tvPosition);
        tvValue = findViewById(R.id.tvValue);
        btnDong = findViewById(R.id.btnDong);


        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        String value = intent.getStringExtra("value");

        tvPosition.setText(String.valueOf(position));
        tvValue.setText(value != null ? value : "");

        btnDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
