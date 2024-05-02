package com.example.readrssapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.readrssapp.DAO.UserDAO;
import com.example.readrssapp.DTO.User;

public class AddUserActivity extends AppCompatActivity {
    Button btnCancel, btnSave;
    EditText edUser,edHoten,edPass,edRePass;
    UserDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        btnCancel=findViewById(R.id.btnCancelAdd);
        btnSave=findViewById(R.id.btnSaveAdd);
        edUser=findViewById(R.id.edUser);
        edHoten=findViewById(R.id.edHoTen);
        edPass=findViewById(R.id.edPassChange);
        edRePass=findViewById(R.id.edRePassChange);

        dao=new UserDAO(this);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edUser.setText("");
                edHoten.setText("");
                edPass.setText("");
                edRePass.setText("");
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user=new User();
                user.setMaUser(edUser.getText().toString());
                user.setHoTen(edHoten.getText().toString());
                user.setMatKhau(edPass.getText().toString());
                if (validate()>0){
                    if (dao.insert(user)>0){
                        Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        edUser.setText("");
                        edHoten.setText("");
                        edPass.setText("");
                        edRePass.setText("");
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Đăng ký thất bại ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public int validate(){
        int check=1;
        if (edUser.getText().length()==0 || edHoten.getText().length()==0 || edPass.getText().length()==0
                || edRePass.getText().length()==0){
            Toast.makeText(getApplicationContext(), "Bạn phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            check=-1;
        }else{
            String pass= edPass.getText().toString();
            String rePass=edRePass.getText().toString();
            if (!pass.equals(rePass)){
                Toast.makeText(getApplicationContext(), "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                check=-1;
            }
        }
        return  check;
    }
}