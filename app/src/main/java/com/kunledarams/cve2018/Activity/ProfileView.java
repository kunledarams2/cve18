package com.kunledarams.cve2018.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunledarams.cve2018.Model.User;
import com.kunledarams.cve2018.R;
import com.kunledarams.cve2018.Ultil.Service;

public class ProfileView extends AppCompatActivity {

    private DatabaseReference mDB;
    private String profileId;
    private LinearLayout linearLayout;
    private TextView name, birthday,phone,hobbies,quote,email,socialMedia,plans,food,color,partingWord,likesdislikes;
    private ImageView imageView;
    TextView SeeallTextview;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Service service= new Service(this);

        profileId=getIntent().getStringExtra("profileID");
        mDB= FirebaseDatabase.getInstance().getReference().child("CVESort-User");
     /*   if(service.isOnline()){

        }else {
            mDB= DatabaseUtlii.getDatabase().getReference().child("CVE-User");
        }*/

        user= new User();
        Init();
        linearLayout=findViewById(R.id.seeall);
        SeeallTextview=findViewById(R.id.textViewD);
        mDB.keepSynced(true);
        DisplayToView();

    }


    public void Init(){
        name=findViewById(R.id.nameView);
        phone=findViewById(R.id.phoneView);
        birthday=findViewById(R.id.birthdayView);
        plans=findViewById(R.id.planView);
        email=findViewById(R.id.emailView);
        food=findViewById(R.id.FoodView);
        color=findViewById(R.id.colorView);
        hobbies=findViewById(R.id.HobbView);
        socialMedia=findViewById(R.id.facebookView);
       partingWord=findViewById(R.id.partWordView);
       quote=findViewById(R.id.QuoteView);
       likesdislikes=findViewById(R.id.likesdislikesView);
        imageView=findViewById(R.id.Image);




    }
    private void DisplayToView(){
       mDB.child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nameView=(String)dataSnapshot.child("userName").getValue();
                name.setText(nameView);
                String foodView=(String)dataSnapshot.child("userFood").getValue();
                food.setText(foodView);
                String colorView=(String)dataSnapshot.child("userColor").getValue();
                color.setText(colorView);
                String QuoteView=(String)dataSnapshot.child("userQuote").getValue();
                quote.setText(QuoteView);
                String PartWordsView=(String)dataSnapshot.child("userPartingword").getValue();
               partingWord.setText(PartWordsView);
                String hoobiesView=(String)dataSnapshot.child("userHobbies").getValue();
                hobbies.setText(hoobiesView);
               long phoneV=(long)dataSnapshot.child("userPhone").getValue();
                phone.setText(String.valueOf(phoneV));
                String birthdayv=(String)dataSnapshot.child("userBirthday").getValue();
                String FacebookView=(String)dataSnapshot.child("userSocialmedia").getValue();
                socialMedia.setText(FacebookView);
                String likesView=(String)dataSnapshot.child("userLikes").getValue();
                likesdislikes.setText(likesView);

                birthday.setText(birthdayv);
                String emailv= (String)dataSnapshot.child("userEmail").getValue();
                email.setText(emailv);
                String plansv=(String)dataSnapshot.child("userPlan").getValue();
                plans.setText(plansv);
                final String profileimage=(String)dataSnapshot.child("userImage").getValue();

                Glide.with(ProfileView.this)
                        .load(profileimage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(ProfileView.this,ZoomView.class);
                        intent.putExtra("ProfileV",profileimage);
                        startActivity(intent);

                    }
                });

              // String nicknamev=dataSnapshot.child("userNickName").getValue().toString();
               // nickname.setText(nicknamev);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void Seeall(View view) {
        linearLayout.setVisibility(View.VISIBLE);
        SeeallTextview.setVisibility(View.GONE);
    }

    public void SeeLess(View view) {
        linearLayout.setVisibility(View.GONE);
        SeeallTextview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slidein_back,R.anim.slideout_back);
    }
}
