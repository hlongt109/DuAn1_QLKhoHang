package com.longthph30891.duan1_qlkhohang.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen.BillListActivity;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen.ProductListActivity;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen.ProductTypeListActivity;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen.UserListActivity;
import com.longthph30891.duan1_qlkhohang.databinding.FragmentMainFrgBinding;

public class MainFrg extends Fragment {
    private FragmentMainFrgBinding binding;
    public MainFrg() {
        // Required empty public constructor
    }
    ImageView imgProType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainFrgBinding.inflate(inflater,container,false);
        bindingInit();
        return binding.getRoot();
    }
    public void bindingInit(){
        binding.navQlUsers.setOnClickListener(v ->{
            Intent intent = new Intent(getActivity(), UserListActivity.class);
            startActivity(intent);
        });
        binding.navQlProductType.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductTypeListActivity.class);
            startActivity(intent);
        });
        binding.navQlProducts.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), ProductListActivity.class);
            startActivity(intent);
        });
        binding.navQlBill.setOnClickListener(v ->{
            Intent intent = new Intent(getActivity(), BillListActivity.class);
            startActivity(intent);
        });
    }
}