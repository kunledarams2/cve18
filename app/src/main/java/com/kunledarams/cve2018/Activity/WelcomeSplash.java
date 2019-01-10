package com.kunledarams.cve2018.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.kunledarams.cve2018.R;
import com.kunledarams.cve2018.Ultil.DatabaseUtlii;
import com.kunledarams.cve2018.Ultil.Service;

public class WelcomeSplash extends AppCompatActivity {
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_splash);
        Service service= new Service(this);
        DatabaseUtlii.getDatabase().getReference().keepSynced(true);

        Thread timer= new Thread(){
            @Override
            public void run() {
                super.run();
                try {

                    sleep(4000);

                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Intent intent= new Intent(WelcomeSplash.this, LoginForm.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}
