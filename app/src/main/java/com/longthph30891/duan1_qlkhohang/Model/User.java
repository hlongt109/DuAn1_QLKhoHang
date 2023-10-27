package com.longthph30891.duan1_qlkhohang.Model;

import java.util.HashMap;

/*
 * Created by Longthph30891 07/10/2023
 * update by
 * */
public class User {
    String username; // khoa chinh
    String password;
    String numberphone;
    String position; // chuc vu 1 / 0
    String avatar;
    String profile; // gioi thieu tom tat
    String lastLogin; // lan cuoi login
    String createDate; // ngay tao tai khoan
    String lastAction; //hanh dong cuoi tren he thong
    public HashMap<String,Object> convertHashMap(){
        HashMap<String,Object> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password);
        user.put("numberphone", numberphone);
        user.put("position", position);
        user.put("avatar",avatar);
        user.put("profile",profile);
        user.put("lastLogin",lastLogin);
        user.put("createDate",createDate);
        user.put("lastAction",lastAction);
        return user;
    }
}

