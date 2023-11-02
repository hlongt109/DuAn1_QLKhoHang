package com.longthph30891.duan1_qlkhohang.Activities.activitiesUpdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesCreate.CreateProduct_Activity;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen.ProductListActivity;
import com.longthph30891.duan1_qlkhohang.Model.Product;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityUpdateProduct2Binding;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityUpdateProductBinding;

import java.util.ArrayList;

public class UpdateProduct_Activity extends AppCompatActivity {
    private ActivityUpdateProduct2Binding binding;
    FirebaseFirestore database;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product2);
        database = FirebaseFirestore.getInstance();
        binding = ActivityUpdateProduct2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Product pd = getIntent().getParcelableExtra("product");

        binding.edUpdateName.setText(pd.getName());
        binding.edUpdatePrice.setText(String.valueOf(pd.getPrice()));
        binding.edUpdateQuantity.setText(String.valueOf(pd.getQuantity()));
        
        binding.btnUpdateProductBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateProduct_Activity.this, ProductListActivity.class));
            }
        });
        
        binding.updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_Product(pd);
            }
        });
    }

    private void update_Product(Product pd) {

    }
}