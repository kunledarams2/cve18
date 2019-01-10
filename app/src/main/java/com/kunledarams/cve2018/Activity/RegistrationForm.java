package com.kunledarams.cve2018.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kunledarams.cve2018.Model.User;
import com.kunledarams.cve2018.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import id.zelory.compressor.Compressor;

import static android.content.ContentResolver.SCHEME_CONTENT;
import static android.content.ContentResolver.SCHEME_FILE;

public class RegistrationForm extends AppCompatActivity {

    LinearLayout linearLayout;
    TextView conText;
    private EditText userName, userPhone, userEmail,mybestlecturer,
            myworstday,mybestday,myplans,mybirthday,mynickName,matricNumber,mybestcourse,mFacebook;

    private User user;
    private FirebaseAuth mAuth;
    private DatabaseReference DbReference;
    private StorageReference DBprofileimage;

    private ImageView imageView;
    private Uri imageUri= null;
    private Bitmap compressImage=null;
    private StorageReference storageReference;
    private byte[] imagebyt;
    private ProgressDialog mdialog;

    private static final int Request_code = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);
        linearLayout=(LinearLayout)findViewById(R.id.contLayout);
        conText=(TextView)findViewById(R.id.Continue);
        init();
    }

    public void init(){
        userName=findViewById(R.id.nameView);
        userPhone=findViewById(R.id.phoneView);
        userEmail=findViewById(R.id.emailView);
        mybestlecturer=findViewById(R.id.bestlecturerView);
        mybestday=findViewById(R.id.bestdayView);
        myplans=(EditText)findViewById(R.id.planView);
        mynickName=(EditText)findViewById(R.id.NickNameView);
        matricNumber=(EditText)findViewById(R.id.MatricView);
        myworstday=(EditText)findViewById(R.id.worstdayView);
        mybirthday=(EditText)findViewById(R.id.birthdayViewViewt);
        mybestcourse=(EditText) findViewById(R.id.bestcourseView);
        imageView=(ImageView)findViewById(R.id.Imagerow);
        mFacebook=(EditText)findViewById(R.id.facebookView);
        mdialog=new ProgressDialog(this);



        user= new User();
        mAuth= FirebaseAuth.getInstance();
      //  DbReference= .getReference().child("CVE18-Registration");
       // DbReference= FirebaseDatabase.getInstance().getReference().child("CVE18-User");
        storageReference= FirebaseStorage.getInstance().getReference();
        DbReference.keepSynced(true);
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

    public String getPathFromUri(Uri uriPhoto){
        if(uriPhoto ==null)
            return null ;

        if(SCHEME_FILE.equals(uriPhoto.getScheme())) {
            return uriPhoto.getPath();
        }
        else if(SCHEME_CONTENT.equals(uriPhoto.getScheme())) {
            final String[] filePathColumn = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
            Cursor cursor = null;
            try {

                cursor = getApplicationContext().getContentResolver().query(uriPhoto, filePathColumn, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                   final int columnIdex = (uriPhoto.toString().startsWith("content://com.google.android.gallery3rd")) ? cursor
                            .getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME) : cursor.getColumnIndex(MediaStore.MediaColumns.DATA);


                    if (columnIdex != -1) {
                        String filePath = cursor.getString(columnIdex);
                        if (!TextUtils.isEmpty(filePath)) {
                            return filePath;
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException i) {
                i.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return null;

    }

    @SuppressWarnings("VisibleForTests")
    public void RegMe(View view) {

        final String entername=userName.getText().toString().trim();
        final String enterPhone=userPhone.getText().toString().trim();
        final String enterEmail= userEmail.getText().toString().trim();
        final String enterMBLecturer=mybestlecturer.getText().toString().intern();
        final String enterMBday= mybestday.getText().toString().trim();
        final String enterMPlans=myplans.getText().toString().trim();
        final String entermNickName= mynickName.getText().toString().trim();
        final String enterMatricNo= matricNumber.getText().toString().trim();
        final String enterWorstDay= myworstday.getText().toString().trim();
        final  String enterBirthday=mybirthday.getText().toString().trim();
        final String enterBCousrse= mybestcourse.getText().toString().trim();
        final String enterFacebook=mFacebook.getText().toString().trim();

      /* user.setUserEmail(enterEmail);
        user.setUserBestDay(enterMBday);
        user.setUserName(entername);
        user.setUserMatric(enterMatricNo);
        user.setUserBestCourse(enterBCousrse);
        user.setUserBirthday(enterBirthday);
        user.setUserWorstDay(enterWorstDay);
        user.setUserNickName(entermNickName);
        user.setUserLecturer(enterMBLecturer);
        user.setUserPlans(enterMPlans);
        user.setUserPhone(enterPhone);
        user.setUserFacebook(enterFacebook);*/


        if(imagebyt!=null){
            if(isOnline()){


                StorageReference storegePath= storageReference.child("CVE18Image").child(imageUri.getLastPathSegment());
                storegePath.putBytes(imagebyt).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        user.setUserImage(taskSnapshot.getDownloadUrl().toString());
                        Glide.with(RegistrationForm.this)
                                .load(taskSnapshot.getDownloadUrl())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView);


                        DbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DbReference.child(mAuth.getCurrentUser().getUid()).setValue(user);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Intent intent= new Intent(RegistrationForm.this, LoginForm.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress =
                                (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        mdialog.setMessage("Please wait uploading your details " +  (int)progress + "%" );
                        mdialog.show();
                    }
                });
            }
            else {
                Toast.makeText(RegistrationForm.this,"No Internet",Toast.LENGTH_LONG).show();

            }

        }else {Toast.makeText(RegistrationForm.this,"Image error",Toast.LENGTH_LONG).show();}


    }

    public void Continue(View view) {
        linearLayout.setVisibility(View.VISIBLE);
        conText.setVisibility(View.GONE);
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
