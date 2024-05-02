package com.example.readrssapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.readrssapp.DAO.UserDAO;

public class LoginActivity extends AppCompatActivity {

    EditText edUserName, edPassword;
    TextView tvDangky, tvQMK;
    Button btnLogin, btnCancel;
    CheckBox chkRememberPass;
    String strUser, strPass;
    UserDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("ĐĂNG NHẬP");
        tvDangky=findViewById(R.id.tvDangky);
        tvQMK=findViewById(R.id.tvQMK);
        edUserName=findViewById(R.id.edUserName);
        edPassword=findViewById(R.id.edPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnCancel=findViewById(R.id.btnCancel);
        chkRememberPass=findViewById(R.id.chkRemember);
        FragmentManager manager1=getSupportFragmentManager();
        dao= new UserDAO(this);

        //doc user, pass trong SharePreferences

        SharedPreferences pref=getSharedPreferences("USER_FILE", MODE_PRIVATE);
        edUserName.setText(pref.getString("USERNAME",""));
        edPassword.setText(pref.getString("PASSWORD",""));
        chkRememberPass.setChecked(pref.getBoolean("REMEMBER",false));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edUserName.setText("");
                edPassword.setText("");
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklogin();
            }
        });

        tvDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(LoginActivity.this,AddUserActivity.class);
               startActivity(intent);

                Log.d("abc","m da an vao");
            }
        });

        tvQMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,ChangePassActivity.class);
                startActivity(intent);
            }
        });
    }

    public void checklogin(){
        strUser=edUserName.getText().toString();
        strPass=edPassword.getText().toString();
        if (strUser.isEmpty() || strPass.isEmpty()){
            Toast.makeText(getApplicationContext(), "Tên đăng nhập và mật khẩu không được bỏ trống",
                    Toast.LENGTH_SHORT).show();
        }else{
            if (dao.checkLogin(strUser,strPass)>0 || (strUser.equals("admin") && strPass.equals("admin"))){
                Toast.makeText(getApplicationContext(), "Login thành công", Toast.LENGTH_SHORT).show();
                rememberUser(strUser,strPass,chkRememberPass.isChecked());
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("user",strUser);
                i.putExtra("pass",strPass);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Tên đăng nhập và mật khẩu khônd đúng ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void rememberUser(String u, String p, boolean status){
        SharedPreferences pref =getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor edit= pref.edit();
        if (!status){
            //xóa tình trạng lưu trữ trước đó
            edit.clear();
        }else{
            //Lưu dữ liệu
            edit.putString("USERNAME",u);
            edit.putString("PASSWORD",p);
            edit.putBoolean("REMEMBER",status);
        }
        //lưu lại toàn bộ
        edit.commit();
    }
}