package com.kunledarams.cve2018.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunledarams.cve2018.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ZoomView extends AppCompatActivity {

    private ImageView imageView;
    private DatabaseReference DR;
    private String PostId;
    private String ProfileImage;
    private String profileVId;
    private PhotoViewAttacher photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_view);

        imageView=(ImageView)findViewById(R.id.zoomImage);
        PostId=getIntent().getExtras().getString("ZoomImage");
        ProfileImage=getIntent().getExtras().getString("profileImage");
        profileVId=getIntent().getExtras().getString("ProfileV");
        DR= FirebaseDatabase.getInstance().getReference().child("CVE-Gallery");
        if(ProfileImage==null && PostId!=null&&profileVId==null){
            ZoomGalleryImage();
        }
        if(ProfileImage!=null && PostId==null && profileVId==null) {
            Glide.with(ZoomView.this)

                    .load(ProfileImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .crossFade()
                    .into(imageView);

            photoView=new PhotoViewAttacher(imageView);

        }
        if(ProfileImage==null && PostId==null&&profileVId!=null){
            Glide.with(ZoomView.this)
                    .load(profileVId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .crossFade()

                    .into(imageView);

            photoView=new PhotoViewAttacher(imageView);
        }


    }

    public void ZoomGalleryImage(){

        DR.child(PostId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageContainer=dataSnapshot.child("GImage").getValue().toString();

                Glide.with(ZoomView.this)
                        .load(imageContainer)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter().crossFade()
                        .into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        photoView=new PhotoViewAttacher(imageView);

    }
    public void ProfileZoom(){}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slidein_back,R.anim.slideout_back);
        finish();

    }
}
