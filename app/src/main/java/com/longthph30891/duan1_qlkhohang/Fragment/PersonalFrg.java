package com.longthph30891.duan1_qlkhohang.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.duan1_qlkhohang.Activities.LoginActivity;
import com.longthph30891.duan1_qlkhohang.Model.Admin;
import com.bumptech.glide.Glide;
import com.longthph30891.duan1_qlkhohang.Model.User;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.database.DAO.userDAO;
import com.longthph30891.duan1_qlkhohang.database.listenerDb;
import com.longthph30891.duan1_qlkhohang.databinding.FragmentPersonalFrgBinding;

import java.util.HashMap;

public class PersonalFrg extends Fragment {
    public PersonalFrg() {
    }
    private FragmentPersonalFrgBinding binding;
    userDAO dao = new userDAO();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPersonalFrgBinding.inflate(inflater, container, false);
        display();
        LogOut();
        return binding.getRoot();
    }
    public void display() {
        SharedPreferences s = getActivity().getSharedPreferences("ReLogin.txt", Context.MODE_PRIVATE);
        String usn = s.getString("usn", "");
        binding.tvUserNamePersonal.setText(usn);
        dao.getAvatarByUsername(usn, avatarUrl -> {
            if (getActivity() != null){
                getActivity().runOnUiThread(()->{
                    Glide.with(getActivity())
                            .load(avatarUrl)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(binding.imgAvatarPersonal);
                });
            }
        });
    }
    public void LogOut() {
        binding.navLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Do you want to log out?");
            builder.setNegativeButton("No", null);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                SharedPreferences s = getActivity().getSharedPreferences("ReLogin.txt", Context.MODE_PRIVATE);
                SharedPreferences.Editor e = s.edit();
                e.clear();
                e.apply();
                //
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            });
            builder.create().show();
        });
    }
}