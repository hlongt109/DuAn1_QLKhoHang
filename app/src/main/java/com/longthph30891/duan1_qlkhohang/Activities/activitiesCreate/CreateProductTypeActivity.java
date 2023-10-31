package com.longthph30891.duan1_qlkhohang.Activities.activitiesCreate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen.ProductTypeListActivity;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.database.listenerDb;

public class CreateProductTypeActivity extends AppCompatActivity {
    ImageView imgProType;
    EditText edID, edName;
    Button btnAdd,btnBack;
    listenerDb listenerDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product_type);
        imgProType = findViewById(R.id.imageProductType);
        edID = findViewById(R.id.ed_addIDProType);
        edName = findViewById(R.id.ed_addNameProType);
        btnAdd = findViewById(R.id.btn_add_ProductType);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateProductTypeActivity.this, ProductTypeListActivity.class));
            }
        });
    }
}