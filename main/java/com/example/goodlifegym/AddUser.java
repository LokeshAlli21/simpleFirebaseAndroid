package com.example.goodlifegym;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddUser extends AppCompatActivity {

    EditText name,work,age;
    Button btn;
    FirebaseFirestore db;

    MaterialCardView selectImg;
    Uri imgUri;
    Bitmap bitmap;
    ImageView cardImg;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    private String photoUrl;
    //
    private FirebaseAuth firebaseAuth;
    private String currentUserID,docId;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

//        if (firebaseAuth.getCurrentUser().getUid().toString()==null){
//            startActivity(new Intent(MainActivity.this,Sign.class));
//        }

        name = findViewById(R.id.titleET);
        work = findViewById(R.id.descET);
        age = findViewById(R.id.ageET);
        btn = findViewById(R.id.btnPost);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Adding user...");


        selectImg=findViewById(R.id.selectImg);
        cardImg=findViewById(R.id.cardImg);

        db = FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        mStorageRef=storage.getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        currentUserID=firebaseAuth.getCurrentUser().getUid();

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CheckStoragePermission();
                pickImgFromGallary();
//                Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                uploadImage();

            }
        });


    }

    private void pickImgFromGallary() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        launcher.launch(intent);

    }


    ActivityResultLauncher<Intent> launcher
            =registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK){
                    Intent data=result.getData();
                    if (data!=null && data.getData()!=null){

                        imgUri=data.getData();
                        try {
                            bitmap= MediaStore.Images.Media.getBitmap(
                                    getContentResolver(),
                                    imgUri
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (imgUri!=null){
                        cardImg.setImageBitmap(bitmap);
                    }
                }
            }
    );

    private void uploadImage(){


        if (imgUri==null){
            Toast.makeText(this, "Please Select Thumbnail", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }else{
            final StorageReference mRef=mStorageRef.child("photo/"+imgUri.getLastPathSegment());
            mRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
                    mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (uri!=null){
                                photoUrl=uri.toString();
                                uploadInfo();
//                                Toast.makeText(MainActivity.this, "Uploaded Image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddUser.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddUser.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void uploadInfo(){
        String Name = name.getText().toString();
        String Work = work.getText().toString();
        String Age = age.getText().toString();
        String date,time;
        long currentDateAndTimeInLong= Calendar.getInstance().getTimeInMillis();
        date=millisToDateInString(currentDateAndTimeInLong);
        time=millisToTimeInString(currentDateAndTimeInLong);

        String currentDateAndTimeInStr=String.valueOf(currentDateAndTimeInLong);
        if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(Work) ){
            progressDialog.dismiss();
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
        }else {
            DocumentReference documentReference=db.collection("user data").document();

            UserDataModel newsModel=new UserDataModel(Name,Work,Age,"",currentUserID,photoUrl,currentDateAndTimeInStr,date,time);
            documentReference.set(newsModel, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        docId=documentReference.getId();
                        newsModel.setDocId(docId);
                        documentReference.set(newsModel,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(AddUser.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddUser.this,"Failed"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddUser.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private String millisToTimeInString(long millis){
        SimpleDateFormat timeFormat=new SimpleDateFormat("hh:mm a");
        return timeFormat.format(millis);
    }
    private String millisToDateInString(long millis){
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(millis);
    }

}