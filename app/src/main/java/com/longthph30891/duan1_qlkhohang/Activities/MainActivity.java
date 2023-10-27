package com.longthph30891.duan1_qlkhohang.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.longthph30891.duan1_qlkhohang.Activities.admin.UserListActivity;
import com.longthph30891.duan1_qlkhohang.Fragment.BillListFrg;
import com.longthph30891.duan1_qlkhohang.Fragment.admin.CreateUserFrg;
import com.longthph30891.duan1_qlkhohang.Fragment.admin.ProductListFrg;
import com.longthph30891.duan1_qlkhohang.Fragment.admin.ProductTypeFrg;
import com.longthph30891.duan1_qlkhohang.Fragment.admin.StatisticsFrg;
import com.longthph30891.duan1_qlkhohang.Fragment.admin.UserListFrg;
import com.longthph30891.duan1_qlkhohang.R;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SearchView searchView;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    public void initView(){
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("");
        navigationView = findViewById(R.id.nvMenu);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // mặc định
        BillListFrg billListFrg = new BillListFrg();
        replate(billListFrg);
        toolbar.setTitle("Phiếu xuất kho");
        // menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_Nguoidung){
                    UserListFrg ufm = new UserListFrg();
                    replate(ufm);
                    toolbar.setTitle("Thành viên");
                }else if(item.getItemId() == R.id.nav_LoaiSanpham){
                    ProductTypeFrg pt = new ProductTypeFrg();
                    replate(pt);
                    toolbar.setTitle("Loại sản phẩm");
                }else if(item.getItemId() == R.id.nav_Sanpham){
                    ProductListFrg productListFrg = new ProductListFrg();
                    replate(productListFrg);
                    toolbar.setTitle("Sản phẩm");
                }else if(item.getItemId()==R.id.nav_HoaDon){
                    BillListFrg billListFrg = new BillListFrg();
                    replate(billListFrg);
                    toolbar.setTitle("Phiếu xuất kho");
                }else if(item.getItemId()==R.id.nav_CreateUser){
                    CreateUserFrg createUserFrg = new CreateUserFrg();
                    replate(createUserFrg);
                    toolbar.setTitle("Tạo tài khoản người dùng");
                }else if(item.getItemId()==R.id.nav_Statistics){
                    StatisticsFrg statisticsFrg = new StatisticsFrg();
                    replate(statisticsFrg);
                    toolbar.setTitle("Thống kê");
                }else if(item.getItemId()==R.id.nav_Logout){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Log out");
                    builder.setTitle("Bạn muốn đăng xuất ?");
                    builder.setNegativeButton("No",null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent1);
                            finish();
                        }
                    });
                    builder.create().show();
                }
                drawerLayout.closeDrawer(navigationView); // đóng menu sau khi chọn một item
                return true;
            }
        });
    }
    public void replate(Fragment frg){
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout,frg).commit();

    }
}