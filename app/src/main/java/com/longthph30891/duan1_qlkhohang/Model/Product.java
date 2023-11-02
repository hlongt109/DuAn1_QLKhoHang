package com.longthph30891.duan1_qlkhohang.Model;

import java.util.HashMap;

/*
 * Created by Longthph30891 07/10/2023
 * update by
 * */
public class Product {
    String id; // khoa chinh, auto 0 - n
    String name; // ten sp
    int quantity;// so luong sp
    int price; // gia sp
    String photo; // anh cua san pham khi vao kho
    String Date;
    String userID; // id cua nguoi tao san pham
    //
    public Product() {
    }

    public Product(String id, String name, int quantity, int price, String photo, String date, String userID) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.photo = photo;
        Date = date;
        this.userID = userID;
    }

    public Product(String id, String name, int quantity, int price, String photo, String date) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.photo = photo;
        Date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
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
        work.put("Date", Date);
        work.put("photo", photo);
        return work;
    }
}
