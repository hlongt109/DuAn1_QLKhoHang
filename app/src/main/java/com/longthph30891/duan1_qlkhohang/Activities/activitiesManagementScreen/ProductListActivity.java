package com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.longthph30891.duan1_qlkhohang.Activities.MainActivity;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesCreate.CreateProduct_Activity;
import com.longthph30891.duan1_qlkhohang.Adapter.Products_Adapter;
import com.longthph30891.duan1_qlkhohang.Model.Product;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityProductBinding;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {
    private ActivityProductBinding binding;
    ArrayList<Product> list = new ArrayList<>();
    Products_Adapter adapter;
    Context context = this;
    FirebaseFirestore database;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        initView();

        listenFbFt();

        adapter = new Products_Adapter(context, list, database);

        binding.rcvProduct.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvProduct.setAdapter(adapter);
    }

    private void initView(){
        setSupportActionBar(binding.toolbarProduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        return true;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sub_menu, menu); // Sử dụng `getMenuInflater()` thay vì `getLayoutInflater()`
        MenuItem item_add = menu.findItem(R.id.item_add);

        item_add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                startActivity(new Intent(ProductListActivity.this, CreateProduct_Activity.class));
                return false;
            }
        });
        return true;
    }

    private void listenFbFt(){
        database.collection("Product").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }
                if(value != null){
                    for(DocumentChange dc : value.getDocumentChanges()){
                        switch (dc.getType()){
                            case ADDED: // thêm 1 document
                                dc.getDocument().toObject(Product.class);
                                list.add(dc.getDocument().toObject(Product.class));
                                adapter.notifyItemInserted(list.size() - 1);
                                break;
                            case MODIFIED: // update 1 document
                                Product pdUpdate = dc.getDocument().toObject(Product.class);
                                if(dc.getOldIndex() == dc.getNewIndex()){
                                    list.set(dc.getOldIndex(), pdUpdate);
                                    adapter.notifyItemChanged(dc.getOldIndex());
                                }else{
                                    list.remove(dc.getOldIndex());
                                    list.add(pdUpdate);
                                    adapter.notifyItemMoved(dc.getOldIndex(), dc.getNewIndex());
                                }
                                break;
                            case REMOVED: // xóa 1 document
                                dc.getDocument().toObject(Product.class);
                                int removedIndex = dc.getOldIndex();
                                if(removedIndex >= 0 && removedIndex < list.size()){
                                    list.remove(removedIndex);
                                    adapter.notifyItemRemoved(removedIndex);
                                }
                                break;
                        }
                    }
                }
            }
        });
    }
}
