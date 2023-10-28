package com.longthph30891.duan1_qlkhohang.Model;

import java.util.HashMap;

public class Admin {
    private String username;
    private String password;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Admin() {
    }

    public String getUsername() {
        return username;
    }

    public Admin setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Admin setPassword(String password) {
        this.password = password;
        return this;
    }
    public HashMap<String,Object> convertHashMap(){
        HashMap<String,Object> admin = new HashMap<>();
        admin.put("username",username);
        admin.put("password",password);
        return admin;
    }
}
