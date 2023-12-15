package com.longthph30891.duan1_qlkhohang.Activities.activitiesCreate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen.ProductListActivity;
import com.longthph30891.duan1_qlkhohang.Adapter.CustomSpinnerAdapter;
import com.longthph30891.duan1_qlkhohang.Model.Product;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityCreateProductBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreateProduct_Activity extends AppCompatActivity {
    private ActivityCreateProductBinding binding;
    FirebaseFirestore database;
    private CustomSpinnerAdapter adapter;
    Context context = this;
    String username, idUser;
    String selectedProductType;
    private String selectedImageUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        database = FirebaseFirestore.getInstance();
        binding = ActivityCreateProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        validate();

        username = getUsnFromSharedPreferences();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("User");
        Query query = usersCollection.whereEqualTo("username", username);
        query.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : task.getResult()){
                    idUser = document.getId();

                }
            }else{

            }
        });

        FirebaseFirestore ProductTypeDb = FirebaseFirestore.getInstance();
        ProductTypeDb.collection("ProductType").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<String> productTypes = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        String name = documentSnapshot.getString("name");
                        productTypes.add(name);
                    }
                    adapter = new CustomSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, productTypes);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spnProductType.setAdapter(adapter);

                    binding.spnProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedProductType = adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }
        });

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

        if (name.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty() ||  selectedImageUrl.isEmpty()) {
            if (name.isEmpty()) {
                binding.inAddName.setError("Vui lòng không để trống tên sản phẩm!");
            } else {
                binding.inAddName.setError(null);
            }

            if (priceStr.isEmpty()) {
                binding.inAddPrice.setError("Vui lòng không để trống giá sản phẩm!");
            } else {
                binding.inAddPrice.setError(null);
            }

            if (quantityStr.isEmpty()) {
                binding.inAddQuantity.setError("Vui lòng không để trống số lượng");
            } else {
                binding.inAddQuantity.setError(null);
            }

            if (selectedImageUrl.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ảnh sản phẩm", Toast.LENGTH_SHORT).show();
            }
        } else {
            int price, quantity;
            try {
                price = Integer.parseInt(priceStr);
            } catch (NumberFormatException e) {
                binding.inAddPrice.setError("Giá phải là số nguyên");
                return;
            }

            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                binding.inAddQuantity.setError("Số lượng phải là số nguyên");
                return;
            }

            if (price < 0) {
                binding.inAddPrice.setError("Giá sản phẩm phải lớn hơn 0");
            } else if (quantity < 0) {
                binding.inAddQuantity.setError("Số lượng sản phẩm phải lớn hơn 0");
            } else {
                String id = UUID.randomUUID().toString();
                Product pd = new Product(id, name, quantity, price, selectedImageUrl, date, idUser,selectedProductType);
                HashMap<String, Object> mapPD = pd.converHashMap();
                database.collection("Product").document(id).set(mapPD).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Thêm sản phẩm " + name + " thành công", Toast.LENGTH_SHORT).show();
                        binding.imgProduct.setImageResource(R.drawable.img);
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

    private String getUsnFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("ReLogin.txt", Context.MODE_PRIVATE);
        return sharedPreferences.getString("usn", "");
    }

}