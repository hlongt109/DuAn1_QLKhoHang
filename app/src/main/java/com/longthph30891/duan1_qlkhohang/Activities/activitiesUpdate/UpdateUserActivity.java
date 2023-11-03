package com.longthph30891.duan1_qlkhohang.Activities.activitiesUpdate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen.UserListActivity;
import com.longthph30891.duan1_qlkhohang.Model.User;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityUpdateUserBinding;


public class UpdateUserActivity extends AppCompatActivity {
    private ActivityUpdateUserBinding binding;
    private String imgUrl = "";
    FirebaseFirestore database;
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
            binding.imgUserUpdate.setImageURI(Uri.parse(user.getAvatar()));
            binding.edUserNameUpdate.setText(user.getUsername());
            binding.edPassUpdate.setText(user.getPassword());
            binding.edPhoneNumberUpdate.setText(user.getNumberphone());
            binding.edPositionUpdate.setText(String.valueOf(user.getPosition()));
            binding.edProfileUpdate.setText(user.getProfile());
        }
    }

    private void openDialogUpadte() {
        User user = new User();
        database = FirebaseFirestore.getInstance();
        String username = binding.edUserNameUpdate.getText().toString();
        String password = binding.edPassUpdate.getText().toString();
        String phone = binding.edPhoneNumberUpdate.getText().toString();
        String position = binding.edPositionUpdate.getText().toString();
        String profile = binding.edProfileUpdate.getText().toString();
        if(username.isEmpty() || password.isEmpty() || phone.isEmpty()|| position.isEmpty()||profile.isEmpty()|| imgUrl.isEmpty()){
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
            if (imgUrl.isEmpty()){
                Toast.makeText(this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
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
            int posi = Integer.parseInt(position);
            user.setUsername(username);user.setPassword(password);user.setNumberphone(phone);
            user.setPosition(posi);user.setProfile(profile);user.setAvatar(imgUrl);
            database.collection("User").document(user.getUsername()).update(user.convertHashMap()).addOnSuccessListener(unused ->
                    Toast.makeText(UpdateUserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
            ).addOnFailureListener(e ->
                    Toast.makeText(UpdateUserActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show());
            Intent intent = new Intent(UpdateUserActivity.this, UserListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!= null){
            Uri uri = data.getData();
            if(binding.imgUserUpdate != null){
                binding.imgUserUpdate.setImageURI(uri);
                imgUrl = uri.toString();
            }
        }
    }
}