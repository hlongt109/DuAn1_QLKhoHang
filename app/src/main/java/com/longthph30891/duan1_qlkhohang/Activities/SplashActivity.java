package com.longthph30891.duan1_qlkhohang.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.database.DAO.userDAO;

public class SplashActivity extends AppCompatActivity {
    private userDAO dao = new userDAO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences s = getSharedPreferences("ReLogin.txt",MODE_PRIVATE);
        String usn = s.getString("usn","");
        boolean isLoggedIn = s.getBoolean("isLogin", false);

        if (isLoggedIn) {
            Toast.makeText(this, "Welcome "+usn, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            dao.lastLogin(usn, unused -> {
            }, e -> {});
            startActivity(intent);
            finish();
        } else {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }, 2000);
        }
    }
}