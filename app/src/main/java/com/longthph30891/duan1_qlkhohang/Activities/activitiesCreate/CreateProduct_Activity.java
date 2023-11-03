package com.longthph30891.duan1_qlkhohang.Activities.activitiesCreate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen.ProductListActivity;
import com.longthph30891.duan1_qlkhohang.Model.Product;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityCreateProductBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class CreateProduct_Activity extends AppCompatActivity {
    private ActivityCreateProductBinding binding;
    FirebaseFirestore database;
    Context context = this;
    private String selectedImageUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        database = FirebaseFirestore.getInstance();
        binding = ActivityCreateProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        validate();

        binding.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_product();
            }
        });

        binding.btnAddProductBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateProduct_Activity.this, ProductListActivity.class));
            }
        });

        binding.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(CreateProduct_Activity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

    }

    private void add_product() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String name = binding.edAddName.getText().toString();
        String priceStr = binding.edAddPrice.getText().toString();
        String quantityStr = binding.edAddQuantity.getText().toString();
        String date = dateFormat.format(new Date());

        if(name.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()){
            if(name.isEmpty()){
                binding.inAddName.setError("Vui lòng không để trống tên sản phẩm!");
            }else{
                binding.inAddName.setError(null);
            }

            if(priceStr.isEmpty()){
                binding.inAddPrice.setError("Vui lòng không để trống giá sản phẩm!");
            }else{
                binding.inAddPrice.setError(null);
            }

            if(quantityStr.isEmpty()){
                binding.inAddQuantity.setError("Vui lòng không để trống số lượng");
            }else{
                binding.inAddQuantity.setError(null);
            }
        }else{
            try {
                int price = Integer.parseInt(priceStr);
                int quantity = Integer.parseInt(quantityStr);

                if (price < 0) {
                    binding.inAddPrice.setError("Giá sản phẩm phải lớn hơn 0");
                } else if (quantity < 0) {
                    binding.inAddQuantity.setError("Số lượng sản phẩm phải lớn hơn 0");
                } else {
                    String id = UUID.randomUUID().toString();
                    Product pd = new Product(id,name,quantity,price,selectedImageUrl, date);
                    HashMap<String, Object> mapPD = pd.converHashMap();
                    database.collection("Product").document(id).set(mapPD).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Thêm sản phẩm " + name + " thành công", Toast.LENGTH_SHORT).show();
                            binding.edAddName.setText("");
                            binding.edAddPrice.setText("");
                            binding.edAddQuantity.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } catch (NumberFormatException e) {
                // Xử lý lỗi khi giá trị không phải số nguyên
                binding.inAddPrice.setError("Giá và số lượng phải là số nguyên");
                binding.inAddQuantity.setError("Giá và số lượng phải là số nguyên");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Thay đổi đoạn mã trong phương thức onActivityResult
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (binding.imgProduct != null) {
                binding.imgProduct.setImageURI(uri);
                selectedImageUrl = uri.toString(); // Chuyển đổi sang chuỗi, vì bạn đang sử dụng uri.toString() ở phía trên
            }
        }
    }

    private void validate(){
        binding.edAddName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    binding.inAddName.setError("Vui lòng không để trống tên của sản phẩm");
                }else{
                    binding.inAddName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.edAddPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    binding.inAddPrice.setError("Vui lòng không để trống giá của sản phẩm");
                }else{
                    binding.inAddPrice.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.edAddQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    binding.inAddQuantity.setError("Vui lòng không để trống số lượng của sản phẩm");
                }else{
                    binding.inAddQuantity.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}