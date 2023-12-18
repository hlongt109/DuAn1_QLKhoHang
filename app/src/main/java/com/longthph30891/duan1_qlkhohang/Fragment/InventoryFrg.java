package com.longthph30891.duan1_qlkhohang.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.longthph30891.duan1_qlkhohang.Adapter.inventory_Adapter;
import com.longthph30891.duan1_qlkhohang.Model.BillDetail;
import com.longthph30891.duan1_qlkhohang.Model.Product;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.databinding.FragmentInventoryFrgBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class InventoryFrg extends Fragment {

    ArrayList<BillDetail> list = new ArrayList<>();
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

        binding.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(R.layout.dialog__selectdate);

                Window window = dialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(window.getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    window.setAttributes(layoutParams);
                }

                TextView StartDay = dialog.findViewById(R.id.StartDay);
                TextView EndDay = dialog.findViewById(R.id.EndDay);
                Button btnLatch = dialog.findViewById(R.id.btn_Latch);

                btnLatch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                StartDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                String selectedStartDay = i2 + "/" + (i1 + 1) + "/" + i;
                                StartDay.setText(selectedStartDay);
                            }
                        }, Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH),
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });

                EndDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                String selectedStartDay = i2 + "/" + (i1 + 1) + "/" + i;
                                EndDay.setText(selectedStartDay);
                            }
                        }, Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH),
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });

                dialog.show();
            }
        });

        return view;
    }

    private void listenFbFt(){
        database.collection("BillDetails").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }
                if(value != null){
                    for(DocumentChange dc : value.getDocumentChanges()){
                        switch (dc.getType()){
                            case ADDED: // thêm 1 document
                                dc.getDocument().toObject(BillDetail.class);
                                list.add(dc.getDocument().toObject(BillDetail.class));
                                adapter.notifyItemInserted(list.size() - 1);
                                break;
                            case MODIFIED: // update 1 document
                                BillDetail pdUpdate = dc.getDocument().toObject(BillDetail.class);
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