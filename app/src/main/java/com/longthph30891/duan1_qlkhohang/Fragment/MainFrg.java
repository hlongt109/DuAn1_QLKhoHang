package com.longthph30891.duan1_qlkhohang.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.longthph30891.duan1_qlkhohang.Activities.admin.ProductTypeListActivity;
import com.longthph30891.duan1_qlkhohang.R;
public class MainFrg extends Fragment {
    public MainFrg() {
        // Required empty public constructor
    }
    ImageView imgProType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_frg,container,false);
        imgProType = view.findViewById(R.id.productType);
        imgProType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductTypeListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}