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

import com.longthph30891.duan1_qlkhohang.Model.ProductType;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.database.listenerDb;

import java.util.ArrayList;

public class ProTypeAdapter extends RecyclerView.Adapter<ProTypeAdapter.viewHolep> {
    Context context;
    ArrayList<ProductType> list;
    listenerDb listenerDb;

    public ProTypeAdapter(Context context, ArrayList<ProductType> list) {
        this.context = context;
        this.list = list;
        listenerDb = new listenerDb();
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
//        holder.imgAnh.set
        holder.tvID.setText(list.get(position).getId());
        holder.tvName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolep extends RecyclerView.ViewHolder {
        ImageView imgAnh;
        TextView tvID, tvName;

        public viewHolep(@NonNull View itemView) {
            super(itemView);
            imgAnh = itemView.findViewById(R.id.imgProductType);
            tvID = itemView.findViewById(R.id.txtIDProType);
            tvName = itemView.findViewById(R.id.txtNameProType);
        }
    }

}
