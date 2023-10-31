package com.longthph30891.duan1_qlkhohang.database.DAO;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginDao {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public interface AdminCheckCallback {
        void onCheckAdmin(boolean isAdmin);


    }
    public interface UserCheckCallback {
        void onCheckUser(boolean isUser);
    }

    public boolean checkAdmin(String usn, String pass, AdminCheckCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Admin")
                .whereEqualTo("username", usn)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot dc : task.getResult()) {
                            String storedUsn = dc.getString("username");
                            String storedPass = dc.getString("password");
                            if (usn.equals(storedUsn) && pass.equals(storedPass)) {
                                callback.onCheckAdmin(true);
                                return;
                            }
                        }
                    }
                    callback.onCheckAdmin(false);
                });
        return false;
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
                            if (usn.equals(storedUsn) && pass.equals(storedPass)) {
                                callback.onCheckUser(true);
                                return;
                            }
                        }
                    }
                    callback.onCheckUser(false);
                });
        return false;
    }
}
