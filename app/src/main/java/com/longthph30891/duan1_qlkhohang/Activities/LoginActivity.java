package com.longthph30891.duan1_qlkhohang.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.longthph30891.duan1_qlkhohang.database.DAO.userDAO;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    userDAO uDAO = new userDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //
        validate();
        binding.btnLogin.setOnClickListener(v -> {
            checkLogin();
        });
        binding.btnExit.setOnClickListener(v -> {
            finish();
        });
    }
    private void checkLogin() {
        String usn = binding.edUsername.getText().toString();
        String pass = binding.edPasswordLg.getText().toString();
        if(usn.isEmpty()||pass.isEmpty()){
            if(usn.isEmpty()){
                binding.ilUsername.setError("Không để trống username");
            }else {
                binding.ilUsername.setError(null);
            }
            if(pass.isEmpty()){
                binding.ilPasswordLg.setError("Không để trống mật khẩu");
            }else {
                binding.ilPasswordLg.setError(null);
            }
        }else {
           uDAO.checkUser(usn, pass, (isUser, position) -> {
               if(isUser){
                   if("admin".equals(position)){
                       SharePre(usn,0,true);
                       uDAO.lastLogin(usn, unused -> {}, e -> {
                           Log.d("Last Login Error","Last Login Error");
                       });
                       Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                       startActivity(intent);
                       finish();
                       Toast.makeText(this, "Welcome "+usn, Toast.LENGTH_SHORT).show();
                   } else if ("user".equals(position)) {
                       SharePre(usn,1,true);
                       uDAO.lastLogin(usn, unused -> {}, e -> {
                           Log.d("Last Login Error","Last Login Error");
                       });
                       Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                       startActivity(intent);
                       finish();
                       Toast.makeText(this, "Welcome "+usn, Toast.LENGTH_SHORT).show();
                   }
               }else {
                   Toast.makeText(this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
               }
           });
        }
    }

    private void validate() {
        binding.edUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = s.toString().trim();
                if(username.isEmpty()){
                    binding.ilUsername.setError("Không để trống username");
                }else {
                    binding.ilUsername.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edPasswordLg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String pass = s.toString();
                if(pass.isEmpty()){
                    binding.ilPasswordLg.setError("Không để trống mật khẩu");
                }else {
                    binding.ilPasswordLg.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void SharePre(String usn, int pos,boolean isLogin){
        SharedPreferences s = getSharedPreferences("ReLogin.txt",MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.putString("usn",usn);
        e.putInt("pos",pos);
        e.putBoolean("isLogin",isLogin);
        e.apply();
    }
}