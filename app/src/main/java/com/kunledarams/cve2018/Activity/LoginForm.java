package com.kunledarams.cve2018.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kunledarams.cve2018.R;

//import com.google.firebase.iid.FirebaseInstanceId;

public class LoginForm extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDB;
    private EditText username,password;
    private ProgressDialog dialog;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser= mAuth.getCurrentUser();
        if( firebaseUser!=null){
            Intent intent= new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        username= findViewById(R.id.editEmail);
        password=findViewById(R.id.userMatricno);
        mAuth=FirebaseAuth.getInstance();
        mDB= FirebaseDatabase.getInstance().getReference().child("CVE18-RegUser");
        mDB.keepSynced(true);
        dialog=new ProgressDialog(this);

    }

    public void RegForm(View view) {
        Button Regect, Accept;
        Dialog dialog= new Dialog(this);
        View view1= dialog.getLayoutInflater().inflate(R.layout.activity_welecome_note,null);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        Regect=view1.findViewById(R.id.reject);
        Accept=view1.findViewById(R.id.accept);

        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registForm= new Intent(LoginForm.this,Sign_UpForm.class);
                startActivity(registForm);
            }
        });
        Regect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dialog.setContentView(view1);
        dialog.show();


    }

    public void SubmitBut(View view) {

        String enternamr=username.getText().toString();
        String enterpassword=password.getText().toString().trim();
        if(TextUtils.isEmpty(enternamr)||TextUtils.isEmpty(enterpassword)){
            Toast.makeText(LoginForm.this,"Please check your Email 0r Password",Toast.LENGTH_LONG).show();
        }

        if(!TextUtils.isEmpty(enternamr) && !TextUtils.isEmpty(enterpassword)){
            dialog=ProgressDialog.show(LoginForm.this,"Login","Please wait",false,false);
            mAuth.signInWithEmailAndPassword(enternamr,enterpassword).addOnCompleteListener(LoginForm.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        String onlineUser=mAuth.getCurrentUser().getUid();
                        String deviceToken= FirebaseInstanceId.getInstance().getToken();

                        mDB.child(onlineUser).child("deviceToken").setValue(deviceToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    CheckIdExitance();
                                }
                            }
                        });

                    } else if(!task.isSuccessful()) {
                        dialog.dismiss();
                        Toast.makeText(LoginForm.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }

    public  void CheckIdExitance(){
        final  String user_id = mAuth.getCurrentUser().getUid();
        final FirebaseUser user = mAuth.getCurrentUser();

        mDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    user.isEmailVerified();


                    dialog.dismiss();
                    Intent GOTAB = new Intent(getApplication(), MainActivity.class);
                    startActivity(GOTAB);
                    Toast.makeText(getApplicationContext(),"Welcome",Toast.LENGTH_SHORT).show();
                    finish();


                }else if(!dataSnapshot.hasChild(user_id)){
                    Toast.makeText(getApplicationContext(),"Please Register And check your email for verification" ,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}
