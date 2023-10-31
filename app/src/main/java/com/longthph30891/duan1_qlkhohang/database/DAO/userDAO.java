package com.longthph30891.duan1_qlkhohang.database.DAO;

import android.util.Log;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class userDAO {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface onAvatarCallBack {
        void onAvatarUrl(String avatarUrl);
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
}
