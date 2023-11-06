package com.longthph30891.duan1_qlkhohang.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesUpdate.UpdateProduct_Activity;
import com.longthph30891.duan1_qlkhohang.Model.Product;
import com.longthph30891.duan1_qlkhohang.R;

import java.util.ArrayList;

public class Products_Adapter extends RecyclerView.Adapter<Products_Adapter.ViewHoldel>{
    private final Context context;
    private final ArrayList<Product> list;
    FirebaseFirestore database;

    public Products_Adapter(Context context, ArrayList<Product> list, FirebaseFirestore database) {
        this.context = context;
        this.list = list;
        this.database = database;
    }

    @NonNull
    @Override
    public ViewHoldel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_product,parent,false);
        return new ViewHoldel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoldel holder, int position) {
        Product pd = list.get(position);
        holder.tvProductName.setText(list.get(position).getName());
        holder.tvProductPrice.setText(String.valueOf(list.get(position).getPrice()));
        holder.tvProductQuantity.setText(String.valueOf(list.get(position).getQuantity()));
        holder.tvProductDate.setText(list.get(position).getDate());
        Glide.with(context).load(pd.getPhoto())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(holder.img_Product);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Product!");
                builder.setMessage("Are you sure ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.collection("Product").document(pd.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Product deletion" + " was successful", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Product deletion failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", null);
                builder.create().show();
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateProduct_Activity.class);
                intent.putExtra("product", pd);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHoldel extends RecyclerView.ViewHolder{
        ImageView img_Product;
        TextView tvProductName, tvProductPrice, tvProductQuantity, tvProductDate;
        public ViewHoldel(@NonNull View itemView) {
            super(itemView);
            img_Product = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductDate = itemView.findViewById(R.id.tvProductDate);
        }
    }
}
