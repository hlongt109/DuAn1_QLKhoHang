package com.longthph30891.duan1_qlkhohang.Adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.longthph30891.duan1_qlkhohang.databinding.ItemBillBinding;

public class billAdapter {
    static class myViewHolder extends RecyclerView.ViewHolder{
        private ItemBillBinding binding;
        myViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
