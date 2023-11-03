package com.longthph30891.duan1_qlkhohang.Activities.activitiesCreate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen.UserListActivity;
import com.longthph30891.duan1_qlkhohang.Model.User;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.database.DAO.userDAO;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityCreateUserBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CreateUserActivity extends AppCompatActivity {
    private ActivityCreateUserBinding binding;
    private String urlImg = "";
    private FirebaseFirestore database;
    String createdDate;
    private userDAO uDao = new userDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        createdDate = sdf.format(new Date());
        binding.btnExitUserCreate.setOnClickListener(v -> {
            Intent intent = new Intent(CreateUserActivity.this, UserListActivity.class);
            startActivity(intent);
        });
        database = FirebaseFirestore.getInstance();
        binding.imgUserCreate.setOnClickListener(v -> {
            ImagePicker.with(CreateUserActivity.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
        binding.btnSaveUserCreate.setOnClickListener(v -> {
            OpenDialogAdd();
        });
        binding.btnExitUserCreate.setOnClickListener(v -> {
            Intent intent = new Intent(CreateUserActivity.this, UserListActivity.class);
            startActivity(intent);
        });
    }

    private void OpenDialogAdd() {
        String username = binding.edUserNameCreate.getText().toString();
        String password = binding.edPassCreate.getText().toString();
        String phone = binding.edPhoneNumberCreate.getText().toString();
        String position = binding.edPositionCreate.getText().toString();
        String profile = binding.edProfileCreate.getText().toString();

        if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || position.isEmpty() || profile.isEmpty() || urlImg.isEmpty()) {
            if (username.isEmpty()) {
                binding.tilUserNameCreate.setError("Không được để trống username");
            } else {
                binding.tilUserNameCreate.setError(null);
            }
            if (password.isEmpty()) {
                binding.tilPassCreate.setError("Không được để trống password");
            } else {
                binding.tilPassCreate.setError(null);
            }
            if (position.isEmpty()) {
                binding.tilPositionCreate.setError("Không được để trống position");
            } else {
                binding.tilPositionCreate.setError(null);
            }
            if (phone.isEmpty()) {
                binding.tilPhoneNumberCreate.setError("Không được để trống phonenumber");
            } else {
                binding.tilPhoneNumberCreate.setError(null);
            }
            if (profile.isEmpty()) {
                binding.tilProfileCreate.setError("Không được để trống profile");
            } else {
                binding.tilProfileCreate.setError(null);
            }
            if (urlImg.isEmpty()) {
                Toast.makeText(this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        } else {
            uDao.checkUserNameExist(username, exists -> {
                if(exists){
                    binding.tilUserNameCreate.setError("username đã tồn tại !");
                }else {
                    try {
                        int positionUser = Integer.parseInt(position);
                        if (positionUser < 0 || positionUser > 1) {
                            binding.tilPositionCreate.setError("Position là số 0 - 1");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        binding.tilPositionCreate.setError("Position là số 0 - 1");
                        return;
                    }
                    int positionUser = Integer.parseInt(position);
                    User user = new User(username, password, phone, positionUser, urlImg, profile, createdDate);
                    HashMap<String, Object> map = user.convertHashMap();
                    database.collection("User").document(username).set(map).addOnSuccessListener(unused -> {
                        Toast.makeText(CreateUserActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        lastAction(username);
                    }).addOnFailureListener(e ->
                            Toast.makeText(CreateUserActivity.this, "Lỗi thêm", Toast.LENGTH_SHORT).show());
                    clearAll();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (binding.imgUserCreate != null) {
                binding.imgUserCreate.setImageURI(uri);
                urlImg = uri.toString();
            }
        }
    }

    public void clearAll() {
        binding.imgUserCreate.setImageResource(R.drawable.img);
        binding.edUserNameCreate.setText("");
        binding.edPassCreate.setText("");
        binding.edPhoneNumberCreate.setText("");
        binding.edPositionCreate.setText("");
        binding.edProfileCreate.setText("");
    }

    public void lastAction(String tk) {
        SharedPreferences sharedPreferences = getSharedPreferences("ReLogin.txt", Context.MODE_PRIVATE);
        String usn = sharedPreferences.getString("usn", "");
        uDao.lastAction(usn, "Created " + tk + " account", unused -> {
        }, e -> {
            Log.d("Action Error", "Action Error");
        });
    }
}