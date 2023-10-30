package com.longthph30891.duan1_qlkhohang.Model;
/*
* Create by phongndph36760 29/10/2023
* */
public class ProductType {
    int id;//khóa
    String name;//tên loại
    String photo;//ảnh loại

    public ProductType() {
    }

    public ProductType(int id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
