package com.kunledarams.cve2018.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kunledarams.cve2018.Model.User;
import com.kunledarams.cve2018.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import id.zelory.compressor.Compressor;

public class Sign_UpForm extends AppCompatActivity {



    private EditText userName, userPassword1, userPassword2, userEmail;
    final static  String passwordChecker="cve13";
    private DatabaseReference dataCve2018;
    private FirebaseDatabase CVE18DB;
    private FirebaseAuth mAuth;
    private User user;
    private ProgressDialog dialog;

    private ImageView imageView;
    private Uri imageUri= null;
    private Bitmap compressImage=null;
    private StorageReference storageReference;
    private byte[] imagebyt;
    private static final int Request_code = 1000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up_form);

        userName=findViewById(R.id.userName);
        userPassword1=findViewById(R.id.userPassword1);
        userPassword2=findViewById(R.id.userPassword2);
        userEmail=findViewById(R.id.userEmail);
        imageView=findViewById(R.id.Imagerow);
        mAuth= FirebaseAuth.getInstance();
        dataCve2018=FirebaseDatabase.getInstance().getReference().child("CVE18-RegUser");
        storageReference= FirebaseStorage.getInstance().getReference();

        user= new User();
        dialog=new ProgressDialog(this);
        dataCve2018.keepSynced(true);
    }


    public void selectImage(View view) {
        Intent getImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getImage.setType("image/");
        startActivityForResult(Intent.createChooser(getImage,"selectImage"),Request_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Request_code) {
            if (resultCode == RESULT_OK) {
                imageUri = data.getData();

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);


                // File file = new File(getPathFromUri(imageUri));

            }

        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                Uri uri=result.getUri();

                File file=new File(uri.getPath());
                // imageView.setImageURI(uri);


                try {
                    compressImage = new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxHeight(200)
                            .setQuality(50)
                            .setDestinationDirectoryPath(Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                    .getAbsolutePath())
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToBitmap(file);
                    imageView.setImageBitmap(compressImage);

                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                compressImage.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
                imagebyt =byteArrayOutputStream.toByteArray();
                imageView.setImageBitmap(compressImage);

            }

        }


    }


    public void SignMEUP(View view) {

        final String enterName= userName.getText().toString().trim();
        final String enterEmail = userEmail.getText().toString().trim();

        final String enterPassword1= userPassword1.getText().toString().trim();
        final String entertPassword2= userPassword2.getText().toString().trim();

        boolean checker1=enterPassword1.contains(passwordChecker);
        boolean checker2= entertPassword2.contains(passwordChecker);

        if(isOnline()){

            if(TextUtils.isEmpty(enterName)||TextUtils.isEmpty(entertPassword2)){
                Toast.makeText(this,"Please enter your dettails",Toast.LENGTH_LONG).show();
            }
            else {
                if(checker1==checker2){

                    if(imagebyt!=null){
                        dialog= ProgressDialog.show(Sign_UpForm.this,"Authication in progress","Please wait",false,false);
                        mAuth.createUserWithEmailAndPassword(enterEmail,enterPassword1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    // String Device_Token= FirebaseInstanceId.getInstance().getToken();
                                    final String user_id= mAuth.getCurrentUser().getUid();
                                    final DatabaseReference currentUserId=dataCve2018.child(user_id);
                                    currentUserId.child("userPname").setValue(enterName);
                                    currentUserId.child("userPEmail").setValue(enterEmail);
                                    currentUserId.child("userPassword").setValue(enterPassword1);
                                    // currentUserId.child("userDeviceToken").setValue(Device_Token);

                                    StorageReference storegePath= storageReference.child("CVE18Image").child(imageUri.getLastPathSegment());
                                    storegePath.putBytes(imagebyt).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            currentUserId.child("UserImage").setValue(taskSnapshot.getDownloadUrl().toString());

                                        }
                                    });

                                    user.setUserid(user_id);


                                    dialog.dismiss();
                                    Intent intent = new Intent(Sign_UpForm.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(Sign_UpForm.this, e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        Toast.makeText(this, "Please Add Your Image ",Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(this,"Please check your password ",Toast.LENGTH_LONG).show();
                }

        }

        }else {
            Toast.makeText(this,"No Internet",Toast.LENGTH_LONG).show();
        }




    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
