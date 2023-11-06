package com.longthph30891.duan1_qlkhohang.Activities.activitiesUpdate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesCreate.CreateProduct_Activity;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen.ProductListActivity;
import com.longthph30891.duan1_qlkhohang.Model.Product;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityUpdateProduct2Binding;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityUpdateProductBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class UpdateProduct_Activity extends AppCompatActivity {
    private ActivityUpdateProduct2Binding binding;
    FirebaseFirestore database;
    Context context = this;
    private String imgUrlProduct = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product2);
        database = FirebaseFirestore.getInstance();
        binding = ActivityUpdateProduct2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgUpdateProduct.setOnClickListener(v -> {
            ImagePicker.with(UpdateProduct_Activity.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080,1080)
                    .start();
        });
        binding.btnUpdateProductBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateProduct_Activity.this, ProductListActivity.class));
            }
        });

        binding.updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_Product();
            }
        });
        initData();
    }

    private void update_Product() {
        database = FirebaseFirestore.getInstance();
        Product product = new Product();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String name = binding.edUpdateName.getText().toString();
        String priceStr = binding.edUpdatePrice.getText().toString();
        String quantityStr = binding.edUpdateQuantity.getText().toString();
        String date = dateFormat.format(new Date());

        if (name.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
            if (name.isEmpty()) {
                binding.inUpdateName.setError("Vui lòng không để trống tên sản phẩm!");
            } else {
                binding.inUpdateName.setError(null);
            }

            if (priceStr.isEmpty()) {
                binding.inUpdatePrice.setError("Vui lòng không để trống giá sản phẩm!");
            } else {
                binding.inUpdatePrice.setError(null);
            }

            if (quantityStr.isEmpty()) {
                binding.inUpdateQuantity.setError("Vui lòng không để trống số lượng");
            } else {
                binding.inUpdateQuantity.setError(null);
            }
        } else {
            int price, quantity;
            try {
                price = Integer.parseInt(priceStr);
            } catch (NumberFormatException e) {
                binding.inUpdatePrice.setError("Giá phải là số nguyên");
                return;
            }

            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                binding.inUpdateQuantity.setError("Số lượng phải là số nguyên");
                return;
            }

            if (price < 0) {
                binding.inUpdatePrice.setError("Giá sản phẩm phải lớn hơn 0");
            } else if (quantity < 0) {
                binding.inUpdateQuantity.setError("Số lượng sản phẩm phải lớn hơn 0");
            } else {
                product.setName(name);
                product.setQuantity(quantity);
                product.setPrice(price);
                product.setDate(date);
                product.setPhoto(imgUrlProduct);
                database.collection("Product").document(product.getName())
                        .update(product.converHashMap())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Cập nhật " + name + " Thành công", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Cập nhật " + name + " Thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private void initData(){
        Product product = (Product) getIntent().getSerializableExtra("product");
        if(product != null){
            binding.imgUpdateProduct.setImageURI(Uri.parse(product.getPhoto()));
            binding.edUpdateName.setText(product.getName());
            binding.edUpdateQuantity.setText(String.valueOf(product.getQuantity()));
            binding.edUpdatePrice.setText(String.valueOf(product.getPrice()));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            if(binding.imgUpdateProduct != null){
                binding.imgUpdateProduct.setImageURI(uri);
                imgUrlProduct = uri.toString();
            }
        }
    }
}