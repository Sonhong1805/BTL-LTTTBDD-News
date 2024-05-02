package com.example.readrssapp.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.readrssapp.DTO.User;
import com.example.readrssapp.Database.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private SQLiteDatabase db;

    public UserDAO(Context context){
        DBHelper dbHelper=new DBHelper(context);
        db=dbHelper.getWritableDatabase();
    }

    public long insert(User user){
        ContentValues contentValues=new ContentValues();
        contentValues.put("maUser", user.getMaUser());
        contentValues.put("hoTen",user.getHoTen());
        contentValues.put("matKhau",user.getMatKhau());

        return db.insert("User",null,contentValues);
    }

    public int updatePass(User user){
        ContentValues contentValues=new ContentValues();
        contentValues.put("hoTen",user.getHoTen());
        contentValues.put("matKhau",user.getMatKhau());

        return db.update("User",contentValues,"maUser=?", new String[]{user.getMaUser()});
    }

    public int delete(String id){
        return db.delete("User","maUser=?",new String[]{id});
    }


    //get data nhieu tham so
    @SuppressLint("Range")
    public List<User> getData(String sql, String...selectionArgs){

        List<User> list=new ArrayList<>();
        Cursor c=db.rawQuery(sql, selectionArgs);
        while (c.moveToNext()){
            User obj=new User();
            obj.setMaUser(c.getString(c.getColumnIndex("maUser")));
            obj.setHoTen(c.getString(c.getColumnIndex("hoTen")));
            obj.setMatKhau(c.getString(c.getColumnIndex("matKhau")));

            list.add(obj);
        }
        return list;
    }

    //get tat ca data
    public List<User> getAll(){
        String sql="SELECT * FROM ThuThu";
        return getData(sql);
    }

    //get data theo id
    public User getID(String id){
        String sql="SELECT * FROM User WHERE maUser=?";
        List<User> list =getData(sql,id);
        return list.get(0);
    }


    //check login
    public int checkLogin(String id,String password){
        String sql="SELECT * FROM User WHERE maUser=? AND matKhau=?";
        List<User> list=getData(sql,id,password);
        if (list.size() == 0)
            return -1;
        return 1;
    }

}