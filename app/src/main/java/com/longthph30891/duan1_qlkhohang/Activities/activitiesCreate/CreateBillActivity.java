package com.longthph30891.duan1_qlkhohang.Activities.activitiesCreate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.longthph30891.duan1_qlkhohang.Activities.SelectProductActivity;
import com.longthph30891.duan1_qlkhohang.Activities.activitiesManagementScreen.BillListActivity;
import com.longthph30891.duan1_qlkhohang.Adapter.BillDetailsAdapter;
import com.longthph30891.duan1_qlkhohang.Model.Bill;
import com.longthph30891.duan1_qlkhohang.Model.BillDetail;
import com.longthph30891.duan1_qlkhohang.Model.Cart;
import com.longthph30891.duan1_qlkhohang.Utilities.CartInterface;
import com.longthph30891.duan1_qlkhohang.Utilities.FormatMoney;
import com.longthph30891.duan1_qlkhohang.databinding.ActivityCreateBillBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CreateBillActivity extends AppCompatActivity {
    private ActivityCreateBillBinding binding;
    private ArrayList<Cart> list = new ArrayList<>();
    private BillDetailsAdapter adapter;
    private FirebaseFirestore database;
    private Cart cart;
    private double totalPriceBill;

    private interface quantityCallBack {
        void onSuccess(long currentQuantity);

        void onFailure();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateBillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        setListener();
    }

    private void setListener() {
        binding.btnChoose.setOnClickListener(v -> {
            startActivity(new Intent(this, SelectProductActivity.class));
        });
        binding.btnCreateTheBill.setOnClickListener(v -> {
            if (isValidDetails()) {
                showCorfirmBill();
            }
        });
        binding.btnBack.setOnClickListener(v -> {
            deleteAllItemsInCart();
            startActivity(new Intent(this, BillListActivity.class));
        });

        ListenerDB();
        adapter = new BillDetailsAdapter(this, list, database);
        binding.rcvProductOder.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvProductOder.setAdapter(adapter);
        adapter.clickUpdateQuantity(new CartInterface() {
            @Override
            public void onIncreaseClick(int position) {
                if (binding.radio.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CreateBillActivity.this, "Chưa chọn trạng thái hóa đơn", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.rdoStockOut.setOnClickListener(v -> {
                    isIncreaseClick(position);
                });
                if (binding.rdoStockOut.isChecked()){
                    isIncreaseClick(position);
                }else {
                    isIncreaseInventory(position);
                }
            }

            @Override
            public void onDecreaseClick(int position) {

                isDecreaseClick(position);
            }

            @Override
            public void onDeleteProduct(int position) {
                Cart cartRemove = list.get(position);
                removeProductFromCart(cartRemove);
            }
        });
    }

    private void showCorfirmBill() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận hóa đơn")
                .setPositiveButton("Lưu", (dialog, which) -> {
                    SharedPreferences s = getSharedPreferences("ReLogin.txt", MODE_PRIVATE);
                    String username = s.getString("usn", "");
                    String id = UUID.randomUUID().toString();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String currentDate = simpleDateFormat.format(new Date());
                    Bill bill = new Bill();
                    bill.setId(id);
                    if (binding.rdoStockOut.isChecked()) {
                        bill.setStatus(0);
                    } else if (binding.rdoInventory.isChecked()) {
                        bill.setStatus(1);
                    }
                    bill.setCreatedByUser(username);
                    bill.setCreatedDate(currentDate);
                    bill.setNote(binding.edNote.getText().toString());
                    bill.setTotalPrice(totalPriceBill);
                    addBill(bill);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(new Date());
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Bill").document(bill.getId()).set(bill)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (Cart item : list) {
                            BillDetail billDetail = new BillDetail();
                            String id = UUID.randomUUID().toString();
                            billDetail.setId(id);
                            billDetail.setBillId(bill.getId());
                            billDetail.setIdProduct(item.getIdProduct());
                            billDetail.setQuantity(item.getQuantity());
                            billDetail.setPrice(item.getPriceProduct());
                            billDetail.setImageProduct(item.getImageProduct());
                            billDetail.setNameProduct(item.getNameProduct());
                            billDetail.setCreatedDate(date);
                            addBillDetails(billDetail);
                        }
                        updateProductQuantities(bill);
                        deleteAllItemsInCart();
                        resetFileds();
                    }
                }).addOnFailureListener(e -> {

                });
    }

    private void addBillDetails(BillDetail billDetail) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("BillDetails").document(billDetail.getId()).set(billDetail)
                .addOnSuccessListener(unused -> {

                }).addOnFailureListener(e -> {

                });
    }

    private void isDecreaseClick(int position) {
        cart = list.get(position);
        int quantity = cart.getQuantity();
        quantity--;
        if (quantity == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bạn có muốn xóa sản phẩm khỏi hóa đơn không ?");
            builder.setNegativeButton("Không", null);
            builder.setPositiveButton("Có", (dialog, which) -> {
                removeProductFromCart(cart);
            });
            builder.create().show();
        } else {
            cart.setQuantity(quantity);
            updateQuantityCart(cart, quantity);
        }
        adapter.notifyDataSetChanged();
    }

    private void isIncreaseInventory(int position) {
        cart = list.get(position);
        int quantity = cart.getQuantity();
        quantity++;
        cart.setQuantity(quantity);
        updateQuantityCart(cart, quantity);
        adapter.notifyDataSetChanged();
    }

    private void isIncreaseClick(int position) {
        cart = list.get(position);
        int quantity = cart.getQuantity();
        quantity++;
        int finalQuantity = quantity;
            currentQuantityOfProduct(cart, new quantityCallBack() {
                @Override
                public void onSuccess(long currentQuantity) {
                    if (finalQuantity > currentQuantity) {
                        Toast.makeText(CreateBillActivity.this, "Đã đạt số lượng tối đa của sản phẩm", Toast.LENGTH_SHORT).show();
                        cart.setQuantity((int) currentQuantity);
                        updateQuantityCart(cart, (int) currentQuantity);
                        adapter.notifyDataSetChanged();
                    } else {
                        cart.setQuantity(finalQuantity);
                        updateQuantityCart(cart, finalQuantity);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure() {
                }
            });


    }

    private void updateQuantityCart(Cart cart, int quantity) {
        SharedPreferences s = getSharedPreferences("ReLogin.txt", MODE_PRIVATE);
        String username = s.getString("usn", "");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Cart")
                .whereEqualTo("idProduct", cart.getIdProduct())
                .whereEqualTo("usernameUser", username)
                .whereEqualTo("checked", true)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            database.collection("Cart").document(document.getId())
                                    .update("quantity", quantity)
                                    .addOnSuccessListener(command -> {

                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(this, "Lỗi cập nhật số lượng", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                });
    }

    private void removeProductFromCart(Cart cart) {
        database.collection("Cart").document(cart.getId())
                .delete()
                .addOnSuccessListener(command -> {
                    Toast.makeText(this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {

                });
    }

    private void currentQuantityOfProduct(Cart cart, quantityCallBack callBack) {
        String id = cart.getIdProduct();
        database.collection("Product").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        long currentQuantity = documentSnapshot.getLong("quantity");
                        callBack.onSuccess(currentQuantity);
                    } else {
                        callBack.onFailure();
                    }
                }).addOnFailureListener(e -> {
                    callBack.onFailure();
                });

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

    private void ListenerDB() {
        database.collection("Cart").addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                int totalQuantity = 0;
                double totalPrice = 0.0;
                ArrayList<Cart> newList = new ArrayList<>(list);

                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Cart cart = dc.getDocument().toObject(Cart.class);
                            newList.add(cart);
                            break;
                        case MODIFIED:
                            Cart cart1 = dc.getDocument().toObject(Cart.class);
                            int oldIndex = dc.getOldIndex();

                            if (!newList.isEmpty() && oldIndex >= 0 && oldIndex < newList.size()) {
                                newList.set(oldIndex, cart1);
                            }
                            break;
                        case REMOVED:
                            int removedIndex = dc.getOldIndex();

                            if (!newList.isEmpty() && removedIndex >= 0 && removedIndex < newList.size()) {
                                newList.remove(removedIndex);
                            }
                            break;
                    }
                }
                list.clear();
                list.addAll(newList);
                for (Cart cart : list) {
                    totalQuantity += cart.getQuantity();
                    totalPrice += cart.getQuantity() * cart.getPriceProduct();
                }
                totalPriceBill = totalPrice;
                binding.tvTotalQuantity.setText(String.valueOf(totalQuantity));
                binding.tvTotalPrice.setText(FormatMoney.formatCurrency(totalPrice));
                if (list.isEmpty()) {
                    binding.imgNothing.setVisibility(View.VISIBLE);
                    binding.tvNothing.setVisibility(View.VISIBLE);
                } else {
                    binding.imgNothing.setVisibility(View.GONE);
                    binding.tvNothing.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        deleteAllItemsInCart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteAllItemsInCart();
    }

    private void deleteAllItemsInCart() {
        SharedPreferences s = getSharedPreferences("ReLogin.txt", MODE_PRIVATE);
        String user = s.getString("usn", "");
        database.collection("Cart")
                .whereEqualTo("usernameUser", user)
                .whereEqualTo("checked", true)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String cartItemId = document.getId();
                            database.collection("Cart").document(cartItemId).delete();
                        }
                    }
                });
    }

    private void updateProductQuantities(Bill bill) {
        for (Cart item : list) {
            if (bill.getStatus() == 0) {
                subtractProductQuantity(item.getIdProduct(), item.getQuantity());
            } else if (bill.getStatus() == 1) {
                addProductQuantity(item.getIdProduct(), item.getQuantity());
            }
        }
    }

    private void subtractProductQuantity(String productId, int quantity) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Product").document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        long currentQuantity = documentSnapshot.getLong("quantity");
                        long newQuantity = currentQuantity - quantity;
                        updateProductQuantityInFirestore(productId, newQuantity);
                    }
                })
                .addOnFailureListener(e -> {

                });
    }

    private void addProductQuantity(String productId, int quantity) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Product").document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        long currentQuantity = documentSnapshot.getLong("quantity");
                        long newQuantity = currentQuantity + quantity;
                        updateProductQuantityInFirestore(productId, newQuantity);
                    }
                })
                .addOnFailureListener(e -> {
                });
    }

    private void updateProductQuantityInFirestore(String productId, long newQuantity) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Product").document(productId)
                .update("quantity", newQuantity)
                .addOnSuccessListener(unused -> {
                })
                .addOnFailureListener(e -> {
                });
    }

    private void resetFileds() {
        binding.rdoInventory.setChecked(false);
        binding.rdoStockOut.setChecked(false);
        binding.edNote.setText("");
    }
}