package com.example.pulopo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pulopo.R;


import com.example.pulopo.Utils.UserUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.net.HttpURLConnection;
import java.net.URL;


public class RegisterActivity extends AppCompatActivity {
    EditText textPersonName, textEmailAddress, textPassword, textPasswordConfirm;
    Button btnRegister,btnBack;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    creatAccountUser();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void creatAccountUser() {
        String email_str,perSonUser_str,pass_str,passConfirm_str;

        email_str = textEmailAddress.getText().toString().trim();
        perSonUser_str = textPersonName.getText().toString().trim();
        pass_str = textPassword.getText().toString().trim();
        passConfirm_str = textPasswordConfirm.getText().toString().trim();
        if(TextUtils.isEmpty(email_str) || TextUtils.isEmpty(perSonUser_str)
          || TextUtils.isEmpty(pass_str) || TextUtils.isEmpty(passConfirm_str)){
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }else{
            if(pass_str.equals(passConfirm_str)){
                callAPICreate();
            }else{
                Toast.makeText(this, "Xác minh mật khẩu chưa chính xác", Toast.LENGTH_SHORT).show();
            }
        }



    }

    private void callAPICreate() {
        new NetworkTask().execute();
    }

    private class NetworkTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // Thực hiện các tác vụ mạng ở đây
            try {
                // Tạo URL của API
                URL url = new URL("https://localhost:5001/api/User/Register?UserName=dvthongTest&Password=dvthong&HoTen=dvthong&Email=dvthong");

                // Tạo kết nối HTTP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.connect();

                Log.e("check",connection.toString());

                // Đóng kết nối

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

// Gọi AsyncTask

        private void writeUtilsUserName() {
        String email_login = textEmailAddress.getText().toString().trim();
        String pass_login = textPassword.getText().toString().trim();
        String userNamePerson  = textPersonName.getText().toString().trim();
        String userID  = firebaseAuth.getCurrentUser().getUid();
        UserUtil.emailLogin = email_login;
        UserUtil.passwordLogin = pass_login;
        UserUtil.userName = userNamePerson;
        UserUtil.UID = userID;



    }
    


    private void initView() {
        textEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        textPersonName = findViewById(R.id.editTextPersonName);
        textPassword = findViewById(R.id.editTextTextPassword);
        textPasswordConfirm= findViewById(R.id.editTextTextPasswordConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBackToLogin);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}