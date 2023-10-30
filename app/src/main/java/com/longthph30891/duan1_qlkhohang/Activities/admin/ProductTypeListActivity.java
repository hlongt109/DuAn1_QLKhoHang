package com.longthph30891.duan1_qlkhohang.Activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.longthph30891.duan1_qlkhohang.R;

public class ProductTypeListActivity extends AppCompatActivity {
    RecyclerView rcvProType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type_list);
        Toolbar toolbar = findViewById(R.id.toolbarProType);
        toolbar.setTitle("ProductType");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sub_menu,menu);
        MenuItem itemAdd = menu.findItem(R.id.item_add);
        itemAdd.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                startActivity(new Intent(ProductTypeListActivity.this, CreateProductTypeActivity.class));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}