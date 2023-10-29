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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class SignUp extends AppCompatActivity {

    private TextView loginTV;
    private TextInputEditText userETS,passETS,nameETS;
    private Button signUpBtn;
    String userID;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private static final String KEY_ = "";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginTV = findViewById(R.id.loginTV);
        signUpBtn = findViewById(R.id.signUpBtn);
        userETS = findViewById(R.id.userETS);
        passETS = findViewById(R.id.passETS);
        nameETS=findViewById(R.id.nameETS);

        auth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading data...");
//        progressDialog.show();

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Sign.class));
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userETS.getText().toString();
                String pass = passETS.getText().toString();
                String name=nameETS.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                    Toast.makeText(SignUp.this,"Enter email and password both", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.show();
                    register(email,pass,name);
                }
            }
        });
    }

    private void register(String email, String pass,String name) {
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
//                    progressDialog.dismiss();
//                    Toast.makeText(SignUp.this, "Successfully Resistered the USER", Toast.LENGTH_SHORT).show();

                    if (auth.getCurrentUser()!=null){

                        userID=auth.getCurrentUser().getUid();

                    }
                    DocumentReference userInfo=firestore.collection("admin auth data").document(userID);
                    AdminAuthModel model=new AdminAuthModel(name,email,pass,userID);
                    userInfo.set(model, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this, "UserResisteredSuccessfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this, "Failed to upload, "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(SignUp.this, Sign.class));
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}