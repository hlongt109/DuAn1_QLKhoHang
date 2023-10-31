package com.longthph30891.duan1_qlkhohang.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.firebase.firestore.FirebaseFirestore;
import com.longthph30891.duan1_qlkhohang.Adapter.Products_Adapter;
import com.longthph30891.duan1_qlkhohang.Model.Product;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityLoginBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CreateBillActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);

    }
}