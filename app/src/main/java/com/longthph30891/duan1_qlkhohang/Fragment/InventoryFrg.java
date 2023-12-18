package com.longthph30891.duan1_qlkhohang.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.longthph30891.duan1_qlkhohang.Adapter.inventory_Adapter;
import com.longthph30891.duan1_qlkhohang.Model.Product;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.databinding.FragmentInventoryFrgBinding;

import java.util.ArrayList;

public class InventoryFrg extends Fragment {

    ArrayList<Product> list = new ArrayList<>();
    inventory_Adapter adapter;
    FirebaseFirestore database;
    private FragmentInventoryFrgBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInventoryFrgBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        database = FirebaseFirestore.getInstance();
        listenFbFt();

        adapter = new inventory_Adapter(getContext(),list,database);
        binding.rcvInventory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvInventory.setAdapter(adapter);

        return view;
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