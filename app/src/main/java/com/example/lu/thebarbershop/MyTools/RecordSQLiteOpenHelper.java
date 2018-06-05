package com.example.lu.thebarbershop.MyTools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sweet on 2018/5/21.
 */

public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    private static String name = "searchHistory.db";
    private static Integer version = 1;

    public RecordSQLiteOpenHelper(Context context) {

        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table searchhistory(id integer primary key autoincrement,history varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
