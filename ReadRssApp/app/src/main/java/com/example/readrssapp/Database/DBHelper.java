package com.example.readrssapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    static final String DB_Name="ReadRSS";
    static final int DB_Version=1;

    public DBHelper(Context context){
        super(context,DB_Name,null,DB_Version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       String createTableUser="create table User (" +
               "maUser TEXT PRIMARY KEY, " +
               "hoTen TEXT NOT NULL, " +
               "matKhau TEXT NOT NULL)";
       db.execSQL(createTableUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String dropTableUser ="drop table if exists User";
        db.execSQL(dropTableUser);
    }
}
