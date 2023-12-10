package com.longthph30891.duan1_qlkhohang.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesUpdate.UpdateProductTypeActivity;
import com.longthph30891.duan1_qlkhohang.DAO.userDAO;
import com.longthph30891.duan1_qlkhohang.Model.ProductType;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.database.listenerDb;

import java.util.ArrayList;

public class ProTypeAdapter extends RecyclerView.Adapter<ProTypeAdapter.viewHolep> {
    private final Context context;
    private final ArrayList<ProductType> list;
    private userDAO dao = new userDAO();
    FirebaseFirestore database;

    public ProTypeAdapter(Context context, ArrayList<ProductType> list, FirebaseFirestore firebaseFirestore) {
        this.context = context;
        this.list = list;
        this.database = firebaseFirestore;

    }

    @NonNull
    @Override
    public viewHolep onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_producttype, null);
        return new viewHolep(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolep holder, int position) {
        ProductType productType = list.get(position);
        Glide.with(context).load(productType.getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.imgAnh);
        holder.tvName.setText(list.get(position).getName());
        //
        holder.itemView.setOnClickListener(v -> {
           Intent intent = new Intent(context, UpdateProductTypeActivity.class);
           intent.putExtra("productType",productType);
           context.startActivity(intent);
        });
        holder.itemView.setOnLongClickListener(v -> {
            openDialodDelete(productType);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolep extends RecyclerView.ViewHolder {
        ImageView imgAnh;
        TextView  tvName;

        public viewHolep(@NonNull View itemView) {
            super(itemView);
            imgAnh = itemView.findViewById(R.id.imgProductType);
            tvName = itemView.findViewById(R.id.txtNameProType);
        }
    }
    private void openDialodDelete(ProductType productType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông Báo !");
        builder.setMessage("Bạn muốn xóa không ?");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Ok", (dialog, which) -> {
            database.collection("ProductType").document(productType.getId()).delete().addOnSuccessListener(unused -> {
                Toast.makeText(context, "Xóa thành công !", Toast.LENGTH_SHORT).show();
                lastAction(productType.getName());
            }).addOnFailureListener(e ->
                    Toast.makeText(context, "Lỗi xóa", Toast.LENGTH_SHORT).show());
        });
        builder.create().show();
    }
    public void lastAction(String prType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ReLogin.txt", Context.MODE_PRIVATE);
        String usn = sharedPreferences.getString("usn", "");
        dao.lastAction(usn, "Deleted " + prType, unused -> {
        }, e -> {
            Log.d("Action Error", "Action Error");
        });
    }
}
