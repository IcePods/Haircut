package com.example.lu.thebarbershop.MyTools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sweet on 2018/6/3.
 */

public class UserTokenSql extends SQLiteOpenHelper {
    public static String name = "UserTokenSql.db";
    public static Integer version =1;

    public UserTokenSql(Context context) {
        super(context, name, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(id integer primary key autoincrement,useraccount varchar(100),userpassword varchar(100),username varchar(100),usersex varchar(100),userphone varchar(100),userheader varchar(100),usertoken varchar(100))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
