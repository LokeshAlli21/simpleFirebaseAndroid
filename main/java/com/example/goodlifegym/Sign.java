package com.example.goodlifegym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign extends AppCompatActivity {

    private TextView signUp;
    private TextInputEditText userET,passET;
    private Button login;

    private FirebaseAuth auth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        signUp = findViewById(R.id.signUp);
        userET = findViewById(R.id.userET);
        passET = findViewById(R.id.passET);
        login = findViewById(R.id.login);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading data...");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Sign.this, SignUp.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = userET.getText().toString();
                String pass = passET.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                    Toast.makeText(Sign.this,"Enter email and password both", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.show();
                    loginAcc(email,pass);
                }
            }
        });

    }

    private void loginAcc(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(Sign.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialog.dismiss();
                Toast.makeText(Sign.this,"Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Sign.this,MainActivity.class));
            }
        }).addOnFailureListener(Sign.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Sign.this,"Login Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}