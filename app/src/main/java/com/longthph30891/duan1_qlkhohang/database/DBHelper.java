package com.longthph30891.duan1_qlkhohang.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

// trong 1 du an nen co 1 file DB
// 1file DBHelper
public class DBHelper extends SQLiteOpenHelper {
    // tao bang Bill
    String create_table_bill = "Create table Bill (id integer primary key autoincrement, quantity integer, createdByUser text, createdDate text, note text)";
    // tao bang user
    String CREATE_USER_TABLE = "CREATE TABLE " + "USER" +" (" +
            "username" + " TEXT PRIMARY KEY," +
            "password" + " TEXT," +
            "numberphone" + " TEXT," +
            "position" + " INTEGER," +
            "avatar" + " TEXT," +
            "profile" + " TEXT," +
            "lastLogin" + " TEXT," +
            "createDate" + " TEXT," +
            "lastAction" + " TEXT" +
            ")";
    String dataus = "INSERT INTO USER VALUES('longthph30891','longthph30891','0869070822','1','Long - admin','14/10/2023','13/10/2023','Thêm hóa đơn')";

    // tao bang product
    String create_table_product = "Create table Product(id integer primary key autoincrement, name text , quantity integer, price text, photo text, userID text)";
    // tao bang billDetail
    String create_table_billDetail = "Create table BillDetail(id integer primary key autoincrement, billID integer, quantity text)";
    public DBHelper(@Nullable Context context) {
        super(context, "file.db",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table_bill);
        db.execSQL(create_table_product);
        db.execSQL(create_table_billDetail);
        db.execSQL(CREATE_USER_TABLE);
        // tạo dữ liệu
        db.execSQL(dataus);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

