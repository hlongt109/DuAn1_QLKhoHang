package com.longthph30891.duan1_qlkhohang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.longthph30891.duan1_qlkhohang.database.DBHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHelper dbHelper = new DBHelper(this);
    }
}