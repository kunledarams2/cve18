package com.kunledarams.cve2018.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kunledarams.cve2018.Activity.MainActivity;
import com.kunledarams.cve2018.Model.Gallerydata;
import com.kunledarams.cve2018.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentResolver.SCHEME_CONTENT;
import static android.content.ContentResolver.SCHEME_FILE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * create an instance of this fragment.
 */
public class ImageAdder extends Fragment {

    private View mRootView;
    private File file;
    private static Uri imageuri = null;
    private ImageView galleryImage;
    private Bitmap thump_bitmap = null;
    byte[] imagebyte = null;
    LinearLayout linearLayout;
    private Button upload;

    private static final int Gallery_Request = 1;
    private StorageReference mstorage;
    private DatabaseReference mdb1;
    private DatabaseReference galleryDB;
    private Gallerydata gallerydata;
    private EditText userComment;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ProgressDialog dialog;

    public ImageAdder() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mstorage= FirebaseStorage.getInstance().getReference();

        galleryDB=FirebaseDatabase.getInstance().getReference().child("CVE-Gallery").push();
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        mdb1= FirebaseDatabase.getInstance().getReference().child("CVE18-RegUser").child(currentUser.getUid());
        gallerydata=new Gallerydata();


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_image_adder, container, false);

        galleryImage =  mRootView.findViewById(R.id.display_image);
        linearLayout=mRootView.findViewById(R.id.galleryLinerlayout);
        upload=mRootView.findViewById(R.id.uploadbuttom);
        userComment=mRootView.findViewById(R.id.mComment);
        dialog=new ProgressDialog(getActivity());


        mRootView.findViewById(R.id.display_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission
                        .READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2000);
                } else {
                    startGallery();
                }

            }

        });

        UploadImage();
        return mRootView;
    }

    public void startGallery() {
        Intent chooseImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        chooseImage.setType("image/");
        startActivityForResult(Intent.createChooser(chooseImage,"selectImage"),Gallery_Request);
    }



    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode==Gallery_Request && resultCode == RESULT_OK)
        {

            imageuri=data.getData();

           // File file;
            File path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            file= new File(path,(System.currentTimeMillis()  +".jpg"));

                  File file=new File(getPathFromUri(imageuri));
                 //thump_file= new File(getPathFromGooglePhotouri(imageuri));

                try {
                    thump_bitmap= new Compressor(getActivity())
                            .setMaxHeight(200)
                            .setMaxHeight(200)
                            .setQuality(50)
                            .setDestinationDirectoryPath(Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                    .getAbsolutePath())
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToBitmap(file);

                }
                catch (Exception  e)
                {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                thump_bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
                imagebyte =byteArrayOutputStream.toByteArray();
                galleryImage.setImageBitmap(thump_bitmap);
                  linearLayout.setVisibility(View.VISIBLE);



            // Toast.makeText(this,"yessss",Toast.LENGTH_SHORT).show();


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

                cursor = getContext().getContentResolver().query(uriPhoto, filePathColumn, null, null, null);
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
    public void UploadImage(){
      // final String enterComment= .trim();

        if(isOnline()){

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog=ProgressDialog.show(getActivity(),"Upload","Please wait.....",false);
                    final  String enterComment=userComment.getText().toString();
                    final String[] GalleryImage = new String[3];
                    final StorageReference storagepath=mstorage.child("CVE18-Gallery").child(imageuri.getLastPathSegment());
                    storagepath.putBytes(imagebyte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // gallerydata.setGImage(taskSnapshot.getDownloadUrl().toString());

                            galleryDB.child("GImage").setValue(taskSnapshot.getDownloadUrl().toString());
                            // GalleryImage[0] =taskSnapshot.getDownloadUrl().toString();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    });


                    mdb1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            gallerydata.setUserImage(dataSnapshot.child("UserImage").getValue().toString());
                            gallerydata.setUserNickname(dataSnapshot.child("userPname").getValue().toString());
                            gallerydata.setUserComment(enterComment);
                            // gallerydata.setGImage(GalleryImage[0]);
                            gallerydata.setPostTime(System.currentTimeMillis());


                            galleryDB.setValue(gallerydata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        dialog.dismiss();
                                        Intent intent=new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                    else {
                                        dialog.dismiss();
                                        Toast.makeText(getContext(),task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }
            });
        }
        else {
            Toast.makeText(getActivity(),"No Internet",Toast.LENGTH_LONG).show();
        }

    }

    private void getCurrentuserInfro(){
        mdb1.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //    gallerydata.setUserId(mAuth.getCurrentUser().getUid());
                gallerydata.setUserNickname(dataSnapshot.child("userNickName").getValue().toString());
                gallerydata.setUserImage(dataSnapshot.child("userImage").getValue().toString());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


}
