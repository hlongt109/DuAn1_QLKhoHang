package com.longthph30891.duan1_qlkhohang.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.duan1_qlkhohang.Model.Product;
import com.longthph30891.duan1_qlkhohang.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class inventory_Adapter extends RecyclerView.Adapter<inventory_Adapter.inventory_ViewHolder>{

    private Context context;
    private ArrayList<Product> list;
    private FirebaseFirestore database;

    public inventory_Adapter(Context context, ArrayList<Product> list, FirebaseFirestore database) {
        this.context = context;
        this.list = list;
        this.database = database;
    }

    @NonNull
    @Override
    public inventory_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_inventory,parent,false);
        return new inventory_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull inventory_ViewHolder holder, int position) {
        Product pd = list.get(position);

        Glide.with(context).load(pd.getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.imgIventory);

        holder.name_Inventory.setText(pd.getName());
        holder.quantity_Inventory.setText(String.valueOf(pd.getQuantity()));

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalRevenue = formatter.format(list.get(position).getPrice());
        holder.price_Inventory.setText(formattedTotalRevenue);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class inventory_ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgIventory;
        TextView name_Inventory,quantity_Inventory,price_Inventory;
        public inventory_ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIventory = itemView.findViewById(R.id.imgIventory);
            name_Inventory = itemView.findViewById(R.id.name_Inventory);
            quantity_Inventory = itemView.findViewById(R.id.quantity_Inventory);
            price_Inventory = itemView.findViewById(R.id.price_Inventory);
        }
    }

}
