package com.longthph30891.duan1_qlkhohang.database.DAO;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class userDAO {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface onAvatarCallBack {
        void onAvatarUrl(String avatarUrl);
    }
    public interface UserCheckCallback {
        void onCheckUser(boolean isUser, String position);
    }
    public void getAvatarByUsername(String username, onAvatarCallBack callback) {
        db.collection("User").document(username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            String avatarUrl = documentSnapshot.getString("avatar");
                            if (avatarUrl != null) {
                                callback.onAvatarUrl(avatarUrl);
                            } else {
                                Log.e("userDAO", "Không tìm thấy URL ảnh.");
                            }
                        } else {
                            Log.e("userDAO", "Không tìm thấy tài liệu với username.");
                        }
                    } else {
                        Log.e("userDAO", "Lỗi truy vấn dữ liệu FireStore.");
                    }
                });
    }
    public boolean checkUser(String usn, String pass, UserCheckCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User")
                .whereEqualTo("username", usn)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot dc : task.getResult()) {
                            String storedUsn = dc.getString("username");
                            String storedPass = dc.getString("password");
                            int position = dc.getLong("position").intValue();
                            if (usn.equals(storedUsn) && pass.equals(storedPass)) {
                                if(position == 0){
                                    callback.onCheckUser(true,"admin");
                                } else if (position == 1){
                                    callback.onCheckUser(true, "user");
                                }
                                return;
                            }
                        }
                    }
                    callback.onCheckUser(false, "");
                });
        return false;
    }
}
