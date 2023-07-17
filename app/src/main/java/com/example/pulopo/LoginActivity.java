package com.example.pulopo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin,btnRegister;
    EditText edtmail, edtPass;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Login();
            }
        });
    }

    private void Login() {
        String email_tr, pass_str;
        email_tr = edtmail.getText().toString();
        pass_str = edtPass.getText().toString();
        if(TextUtils.isEmpty(email_tr) && TextUtils.isEmpty(pass_str)){
            Toast.makeText(this, "Chua nhap email hoac mat khau", Toast.LENGTH_SHORT).show();
        }else{
            firebaseAuth.signInWithEmailAndPassword(email_tr,pass_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                       startActivity(intent);
                       Toast.makeText(LoginActivity.this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                   }
                    Toast.makeText(LoginActivity.this, "Dang nhap that bai", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    public void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        edtmail = findViewById(R.id.edtMail);
        edtPass = findViewById(R.id.edtPassword);
    }
}