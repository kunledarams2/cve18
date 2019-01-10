package com.kunledarams.cve2018.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.kunledarams.cve2018.R;

public class UpdateProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference updTDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
    }
}
