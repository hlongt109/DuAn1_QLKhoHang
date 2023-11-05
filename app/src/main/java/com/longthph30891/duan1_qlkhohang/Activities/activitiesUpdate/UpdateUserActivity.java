package com.longthph30891.duan1_qlkhohang.Activities.activitiesUpdate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen.UserListActivity;
import com.longthph30891.duan1_qlkhohang.Model.User;
import com.longthph30891.duan1_qlkhohang.database.DAO.userDAO;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityUpdateUserBinding;

import java.util.UUID;


public class UpdateUserActivity extends AppCompatActivity {
    private ActivityUpdateUserBinding binding;
    private FirebaseFirestore database;
    private userDAO dao = new userDAO();
    private Uri SelectedImgUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnExitUserUpdate.setOnClickListener(v->{
            Intent intent = new Intent(UpdateUserActivity.this, UserListActivity.class);
            startActivity(intent);
        });
        binding.btnSaveUserUpdate.setOnClickListener(v -> {
          openDialogUpadte();
        });
        binding.imgUserUpdate.setOnClickListener(v -> {
            ImagePicker.with(UpdateUserActivity.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080,1080)
                    .start();
        });
        initData();
    }

    private void initData() {
        User user = (User) getIntent().getSerializableExtra("user");
        if (user!= null){
            String avatarUser = user.getAvatar();
            Glide.with(this).load(avatarUser).into(binding.imgUserUpdate);
            binding.edUserNameUpdate.setText(user.getUsername());
            binding.edPassUpdate.setText(user.getPassword());
            binding.edPhoneNumberUpdate.setText(user.getNumberphone());
            binding.edPositionUpdate.setText(String.valueOf(user.getPosition()));
            binding.edProfileUpdate.setText(user.getProfile());
        }
    }

    private void openDialogUpadte() {
        User user = (User) getIntent().getSerializableExtra("user");
        database = FirebaseFirestore.getInstance();
        String username = binding.edUserNameUpdate.getText().toString();
        String password = binding.edPassUpdate.getText().toString();
        String phone = binding.edPhoneNumberUpdate.getText().toString();
        String position = binding.edPositionUpdate.getText().toString();
        String profile = binding.edProfileUpdate.getText().toString();
        if (SelectedImgUri == null){
            SelectedImgUri = Uri.parse(user.getAvatar());
        }
        if(username.isEmpty() || password.isEmpty() || phone.isEmpty()|| position.isEmpty()||profile.isEmpty()){
            if(username.isEmpty()){
                binding.tilUserNameUpdate.setError("Không để trống username");
            }else {
                binding.tilUserNameUpdate.setError(null);
            }
            if(password.isEmpty()){
                binding.tilPassUpdate.setError("Không để trống password");
            }else {
                binding.tilPassUpdate.setError(null);
            }
            if(phone.isEmpty()){
                binding.tilPhoneNumberUpdate.setError("Không để trống phone number");
            }else {
                binding.tilPhoneNumberUpdate.setError(null);
            }
            if(position.isEmpty()){
                binding.tilPositionUpdate.setError("Không để trống position");
            }else {
                binding.tilPositionUpdate.setError(null);
            }
            if(profile.isEmpty()){
                binding.tilProfileUpdate.setError("Không để trống profile");
            }else {
                binding.tilProfileUpdate.setError(null);
            }
        }else {
            try {
                int pst = Integer.parseInt(position);
                if(pst < 0 || pst >1){
                    binding.tilPositionUpdate.setError("Position chỉ từ 0 - 1");
                    return;
                }else {
                    binding.tilPositionUpdate.setError(null);
                }
            }catch (NumberFormatException e){
                binding.tilPositionUpdate.setError("Position là số từ 0 - 1");
                return;
            }
            if(!username.equals(user.getUsername())){
                dao.checkUserNameExist(username, exists -> {
                    if (exists){
                        binding.tilUserNameUpdate.setError("username đã tồn tại !");
                    }else {
                       update(user,username,password,phone,position,profile);
                    }
                });
            }else {
                update(user,username,password,phone,position,profile);
            }
        }
    }
    private void update(User user,String username,String pass, String phone,String position,String profile){
        if(SelectedImgUri != null){
            String imgFileName = UUID.randomUUID().toString()+".jpg";
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference();
            StorageReference imagesRef = reference.child("image user");
            StorageReference image = imagesRef.child(imgFileName);
            image.putFile(SelectedImgUri).addOnSuccessListener(taskSnapshot -> {
                image.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    int posi = Integer.parseInt(position);
                    user.setUsername(username);user.setPassword(pass);
                    user.setNumberphone(phone);
                    user.setPosition(posi);
                    user.setProfile(profile);
                    user.setAvatar(imageUrl);
                    database.collection("User").document(user.getUsername()).update(user.convertHashMap()).addOnSuccessListener(unused ->{
                        Toast.makeText(UpdateUserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        lastAction(username);
                    }).addOnFailureListener(e ->
                            Toast.makeText(UpdateUserActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show());
                    Intent intent = new Intent(UpdateUserActivity.this, UserListActivity.class);
                    startActivity(intent);
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Lỗi tải hình ảnh lên", Toast.LENGTH_SHORT).show();
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!= null){
            SelectedImgUri = data.getData();
            if(binding.imgUserUpdate != null){
                binding.imgUserUpdate.setImageURI(SelectedImgUri);
            }
        }
    }
    public void lastAction(String tk){
        SharedPreferences sharedPreferences = getSharedPreferences("ReLogin.txt", Context.MODE_PRIVATE);
        String usn = sharedPreferences.getString("usn","");
        dao.lastAction(usn, "Updated "+tk+" account", unused -> {
        }, e -> {
            Log.d("Action Error", "Action Error");
        });
    }
}