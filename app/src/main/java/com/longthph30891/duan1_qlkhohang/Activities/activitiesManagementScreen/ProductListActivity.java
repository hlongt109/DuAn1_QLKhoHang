package com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.duan1_qlkhohang.R;

public class ProductListActivity extends AppCompatActivity {
    Toolbar toolbar;
    FirebaseFirestore database;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        toolbar = findViewById(R.id.toolbar_Product);
        database = FirebaseFirestore.getInstance();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView toolbarTitle = new TextView(this);
        toolbarTitle.setText("Sản Phẩm");
        toolbarTitle.setTextSize(18);
        toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
        toolbar.addView(toolbarTitle);

    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sub_menu, menu); // Sử dụng `getMenuInflater()` thay vì `getLayoutInflater()`
        return true;
    }
}