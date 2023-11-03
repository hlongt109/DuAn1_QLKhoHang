package com.longthph30891.duan1_qlkhohang.Model;

import java.io.Serializable;
import java.util.HashMap;

/*
 * Created by Longthph30891 07/10/2023
 * update by
 * */
public class User implements Serializable {
    String username; // khoa chinh
    String password;
    String numberphone;
    int position; // chuc vu 1 / 0
    String avatar;
    String profile; // gioi thieu tom tat
    String lastLogin; // lan cuoi login
    String createDate; // ngay tao tai khoan
    String lastAction; //hanh dong cuoi tren he thong

    public User() {
    }

    public User(String username, String password, String numberphone, int position, String avatar, String profile, String createDate) {
        this.username = username;
        this.password = password;
        this.numberphone = numberphone;
        this.position = position;
        this.avatar = avatar;
        this.profile = profile;
        this.createDate = createDate;
    }
    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getNumberphone() {
        return numberphone;
    }

    public User setNumberphone(String numberphone) {
        this.numberphone = numberphone;
        return this;
    }

    public int getPosition() {
        return position;
    }

    public User setPosition(int position) {
        this.position = position;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getProfile() {
        return profile;
    }

    public User setProfile(String profile) {
        this.profile = profile;
        return this;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public User setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }

    public String getCreateDate() {
        return createDate;
    }

    public User setCreateDate(String createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getLastAction() {
        return lastAction;
    }

    public User setLastAction(String lastAction) {
        this.lastAction = lastAction;
        return this;
    }

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

