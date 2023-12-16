package com.longthph30891.duan1_qlkhohang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.longthph30891.duan1_qlkhohang.Model.Cart;
import com.longthph30891.duan1_qlkhohang.Model.Product;
import com.longthph30891.duan1_qlkhohang.Utilities.FormatMoney;
import com.longthph30891.duan1_qlkhohang.Utilities.checkBoxInterface;
import com.longthph30891.duan1_qlkhohang.databinding.ItemSelectProductBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class SelectProductAdapter extends RecyclerView.Adapter<SelectProductAdapter.myViewHolder> {
    private final Context context;
    private final ArrayList<Product> list;
    FirebaseFirestore database;
    checkBoxInterface checkBoxInterface;
    private ArrayList<Boolean> checkedItem = new ArrayList<>();


    public SelectProductAdapter(Context context, ArrayList<Product> list, FirebaseFirestore database) {
        this.context = context;
        this.list = list;
        this.database = database;
        checkedItem = new ArrayList<>(Collections.nCopies(list.size(),false));
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSelectProductBinding binding = ItemSelectProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Product product = list.get(position);
        holder.setData(product);
        holder.binding.chkCheck.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                saveDataToCart(product);
            } else {
                removeProductFromCart(product);
            }
            if (checkedItem != null && checkedItem.size() > position) {
                checkedItem.set(position, isChecked);
                if (checkBoxInterface != null) {
                    checkBoxInterface.onCheckBoxChanged(checkedItem);
                }
            }
        }));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ItemSelectProductBinding binding;

        myViewHolder(ItemSelectProductBinding itemSelectProductBinding) {
            super(itemSelectProductBinding.getRoot());
            binding = itemSelectProductBinding;
        }

        void setData(Product product) {
            Glide.with(context).load(product.getPhoto()).into(binding.imgSelectProduct);
            binding.txtProductName.setText(product.getName());
            binding.txtProductPrice.setText(FormatMoney.formatCurrency(product.getPrice()));
            binding.tvQuantity.setText(String.valueOf(product.getQuantity()));
        }
    }
    private void saveDataToCart(Product selectedProduct) {
        String id = UUID.randomUUID().toString();
        Cart cartItem = new Cart(id,
                selectedProduct.getId(),
                selectedProduct.getUserID(),
                selectedProduct.getPhoto(),
                selectedProduct.getName(),
                selectedProduct.getPrice(),
                1, true);
        database.collection("Cart").document(id).set(cartItem).addOnSuccessListener(command -> {

        }).addOnFailureListener(e -> {

        });
    }
    private void removeProductFromCart(Product selectedProduct) {
        database.collection("Cart")
                .whereEqualTo("idProduct",selectedProduct.getId())
                .whereEqualTo("usernameUser",selectedProduct.getUserID())
                .whereEqualTo("checked",true)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document: task.getResult()){
                            database.collection("Cart").document(document.getId())
                                    .delete()
                                    .addOnSuccessListener(command -> {

                                    }).addOnFailureListener(e -> {

                                    });
                        }
                    }
                });
    }
}
