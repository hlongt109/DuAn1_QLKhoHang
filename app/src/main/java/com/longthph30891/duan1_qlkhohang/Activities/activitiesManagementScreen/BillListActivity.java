package com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.duan1_qlkhohang.Activities.MainActivity;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesCreate.CreateBillActivity;
import com.longthph30891.duan1_qlkhohang.Adapter.billAdapter;
import com.longthph30891.duan1_qlkhohang.Model.Bill;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityBillListBinding;
import java.util.ArrayList;

public class BillListActivity extends AppCompatActivity {
    private ActivityBillListBinding binding;
    private FirebaseFirestore database;
    private ArrayList<Bill> list = new ArrayList<>();
    billAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setSupportActionBar(binding.toolbarBill);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        database = FirebaseFirestore.getInstance();
        ListenerDB();
        adapter = new billAdapter(this,list,database);
        binding.rcvBills.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvBills.setAdapter(adapter);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sub_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.item_add){
            Intent intent = new Intent(BillListActivity.this, CreateBillActivity.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.item_search){
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
    private void ListenerDB(){
        database.collection("Bill").addSnapshotListener((value, error) -> {
            if(error != null){
                return;
            }
            if(value != null){
                for(DocumentChange dc : value.getDocumentChanges()){
                    switch (dc.getType()){
                        case ADDED:
                            Bill bill = dc.getDocument().toObject(Bill.class);
                            list.add(bill);
                            break;
                        case MODIFIED:
                            Bill updateBill = dc.getDocument().toObject(Bill.class);
                            if (dc.getOldIndex() == dc.getNewIndex()) {
                                list.set(dc.getOldIndex(), updateBill);
                                adapter.notifyItemChanged(dc.getOldIndex());
                            } else {
                                list.remove(dc.getOldIndex());
                                list.add(updateBill);
                                adapter.notifyItemMoved(dc.getOldIndex(),dc.getNewIndex());
                            }
                            break;
                        case REMOVED:
                            dc.getDocument().toObject(Bill.class);
                            list.remove(dc.getOldIndex());
                            adapter.notifyItemRemoved(dc.getOldIndex());
                            break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}