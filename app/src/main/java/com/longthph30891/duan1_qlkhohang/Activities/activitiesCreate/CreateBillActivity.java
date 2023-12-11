package com.longthph30891.duan1_qlkhohang.Activities.activitiesCreate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.longthph30891.duan1_qlkhohang.Model.Bill;
import com.longthph30891.duan1_qlkhohang.Model.BillDetail;
import com.longthph30891.duan1_qlkhohang.R;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityCreateBillBinding;

import java.util.ArrayList;

public class CreateBillActivity extends AppCompatActivity {
    private ActivityCreateBillBinding binding;
    private ArrayList<BillDetail> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateBillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
    }

    private void setListener() {
        if (list.isEmpty()) {
            binding.imgNothing.setVisibility(View.VISIBLE);
            binding.tvNothing.setVisibility(View.VISIBLE);
        } else {
            binding.imgNothing.setVisibility(View.GONE);
            binding.tvNothing.setVisibility(View.GONE);
        }
        binding.btnChooseProduct.setOnClickListener(v -> {
            // open list of products to choose some
        });
        binding.btnCreateTheBill.setOnClickListener(v -> {
            if (isValidDetails()) {
                showCorfirmBill();
            }
        });
    }

    private void showCorfirmBill() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận hóa đơn")
                .setPositiveButton("Lưu", (dialog, which) -> {
                    // save bill
                })
                .setNegativeButton("Hủy (20)", (dialog, which) -> {
                    dialog.cancel();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText("Hủy (" + millisUntilFinished / 1000 + ")");
            }

            @Override
            public void onFinish() {
                alertDialog.dismiss();
            }
        }.start();
    }

    private void addBill(Bill bill) {

    }

    private void addBillDetails(BillDetail billDetail) {

    }

    private Boolean isValidDetails() {

        if (list.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất 1 sản phẩm", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.radio.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Chưa chọn trạng thái hóa đơn", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}