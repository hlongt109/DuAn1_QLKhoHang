package com.longthph30891.duan1_qlkhohang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.duan1_qlkhohang.Activities.LoginActivity;
import com.longthph30891.duan1_qlkhohang.Model.Admin;
import com.bumptech.glide.Glide;
import com.longthph30891.duan1_qlkhohang.Model.User;
import com.longthph30891.duan1_qlkhohang.database.DAO.userDAO;
import com.longthph30891.duan1_qlkhohang.databinding.FragmentPersonalFrgBinding;

import java.util.HashMap;

public class PersonalFrg extends Fragment {
    public PersonalFrg() {}
    private FragmentPersonalFrgBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String pickImageUrl = "";
    private String usernameCurrent;
    userDAO dao;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPersonalFrgBinding.inflate(inflater,container,false);
        binding.imgAvatarPersonal.setOnClickListener(v->{
            ImagePicker.with(getActivity())
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080,1080)
                    .start();
        });
        display();
        LogOut();

        return binding.getRoot();
    }
    public void display(){
        SharedPreferences s = getActivity().getSharedPreferences("ReLogin.txt", Context.MODE_PRIVATE);
        String usn = s.getString("usn","");
        usernameCurrent =usn;
        binding.tvUserNamePersonal.setText(usn);
        if(usn.equals("admin")){
            Admin admin = new Admin();
            Glide.with(getActivity()).load(admin.getAvatar())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(binding.imgAvatarPersonal);
        }else {
            dao.getAvatarByUsername(usn, avatarUrl ->
                    Glide.with(getActivity()).load(avatarUrl)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(binding.imgAvatarPersonal));
        }
    }

    public void LogOut(){
        binding.navLogout.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Do you want to log out?");
            builder.setNegativeButton("No",null);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                SharedPreferences s = getActivity().getSharedPreferences("ReLogin.txt", Context.MODE_PRIVATE);
                SharedPreferences.Editor e = s.edit();
                e.clear();e.apply();
                //
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            });
            builder.create().show();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && data != null){
            Uri uri = data.getData();
            if(binding.imgAvatarPersonal != null){
                binding.imgAvatarPersonal.setImageURI(uri);
                pickImageUrl = uri.toString();
                Log.d("Anh","url ="+pickImageUrl);
                //
            }else {
                // Xử lý trường hợp uri bị null (không chọn ảnh)
                Toast.makeText(getActivity(), "Không chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }
}