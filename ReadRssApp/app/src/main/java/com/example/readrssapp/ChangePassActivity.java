package com.example.readrssapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.readrssapp.DAO.UserDAO;
import com.example.readrssapp.DTO.User;

public class ChangePassActivity extends AppCompatActivity {
    UserDAO dao;
    Button btnCancel, btnSave;
    EditText edPassOld, edPass, edRePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        btnCancel=findViewById(R.id.btnCancelUserChange);
        btnSave=findViewById(R.id.btnSaveUserChange);
        edPass=findViewById(R.id.edPassChange);

        edRePass=findViewById(R.id.edRePassChange);
        dao=new UserDAO(this);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edPassOld.setText("");
                edPass.setText("");
                edRePass.setText("");
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // đọc user, pass trong SharedPreferences
                SharedPreferences pref =getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
                String user=pref.getString("USERNAME","");
                if (validate()>0){
                    User userObj=dao.getID(user);
                    userObj.setMatKhau(edPass.getText().toString());
                    dao.updatePass(userObj);
                    if (dao.updatePass(userObj) >0){
                        Toast.makeText(getApplicationContext(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

                        edPass.setText("");
                        edRePass.setText("");
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public int validate(){
        int check=1;
        if (edPass.getText().length()==0 || edRePass.getText().length()==0){
            Toast.makeText(getApplicationContext(), "Bạn phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            check=-1;
        }else{
            // đọc user, pass trong sharepreferences
            SharedPreferences pref=getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
            String passOld=pref.getString("PASSWORD","");
            String pass=edPass.getText().toString();
            String rePass=edRePass.getText().toString();

            if (!pass.equals(rePass)){
                Toast.makeText(getApplicationContext(), "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                check=-1;
            }
        }
        return check;
    }
}