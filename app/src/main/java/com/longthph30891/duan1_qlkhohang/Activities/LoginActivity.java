package com.longthph30891.duan1_qlkhohang.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.duan1_qlkhohang.database.DAO.LoginDao;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LoginDao dao = new LoginDao();

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
            if (dao.checkAdmin(usn, pass, isAdmin -> {
                if (isAdmin) {
                    SharePre(usn,pass,true);
                    Toast.makeText(LoginActivity.this, "Welcome " + usn, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
                }
            }));
            if(dao.checkUser(usn, pass, isUser -> {
                if(isUser){
                    SharePre(usn,pass,true);
                    Toast.makeText(LoginActivity.this, "Welcome " + usn, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
                }
            }));
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
    public void SharePre(String usn, String pass,boolean isLogin){
        SharedPreferences s = getSharedPreferences("ReLogin.txt",MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.putString("usn",usn);
        e.putString("pass",pass);
        e.putBoolean("isLogin",isLogin);
        e.apply();
    }
}