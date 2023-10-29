package com.longthph30891.duan1_qlkhohang.Model;

import java.util.HashMap;

/*
 * Created by Longthph30891 07/10/2023
 * update by
 * */
public class Product {
    int id; // khoa chinh, auto 0 - n
    String name; // ten sp
    int quantity;// so luong sp
    String price; // gia sp
    String photo; // anh cua san pham khi vao kho
    String userID; // id cua nguoi tao san pham
    //
    public Product() {
    }

    public Product(int id, String name, int quantity, String price, String photo, String userID) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.photo = photo;
        this.userID = userID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public HashMap<String, Object> converHashMap(){
        HashMap<String, Object> work = new HashMap<>();
        work.put("id", id);
        work.put("name", name);
        work.put("quantity", quantity);
        work.put("price", price);
        work.put("photo", photo);
        work.put("userID", userID);
        return work;
    }
}
