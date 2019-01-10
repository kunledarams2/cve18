package com.kunledarams.cve2018.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunledarams.cve2018.Model.Comment;
import com.kunledarams.cve2018.R;

public class AComment extends AppCompatActivity {

    private String PostId;
    private DatabaseReference DR1;
    private TextView userComment;
    private ImageView userImage;
    private DatabaseReference commentDR;
    private FirebaseAuth mAuth;
    private FloatingActionButton sendComment;
    private EditText enterComment;
    private Comment comment;
    private FirebaseUser currentUser;
    private DatabaseReference userDR;
    private DatabaseReference currentUserD;
    private static final String BUNDLE_COMMENT = "comment";
   String path;
    private FirebaseRecyclerAdapter<Comment, mViewholderclass> FRA;
    private FirebaseRecyclerAdapter<Comment,ReHolderClass>FRMRec;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acomment);


        if (savedInstanceState != null) {
            comment = (Comment) savedInstanceState.getSerializable(BUNDLE_COMMENT);
        }

        PostId=getIntent().getExtras().getString("CommentId");
        userComment=findViewById(R.id.PostId_comment);
        userImage=findViewById(R.id.PostuserImage);
        sendComment=findViewById(R.id.fAButtoncomment);
        enterComment=findViewById(R.id.comment_edit);

        DR1= FirebaseDatabase.getInstance().getReference().child("CVE-Gallery");
        comment= new Comment();

        DR1.child(PostId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String commentId=dataSnapshot.child("userComment").getValue().toString();
                String ImageId=dataSnapshot.child("userImage").getValue().toString();

                userComment.setText(commentId);

                Glide.with(AComment.this)
                        .load(ImageId)
                        .into(userImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendComment.setVisibility(View.GONE);
        enterComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isReady=enterComment.getText().length()>0;
                sendComment.setVisibility(View.VISIBLE);
                sendComment.setEnabled(isReady);


            }

            @Override
            public void afterTextChanged(Editable editable) {
               if(enterComment.getText().length()==0){
                   sendComment.setVisibility(View.GONE);
               }
            }
        });


        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        userDR=FirebaseDatabase.getInstance().getReference().child("CVE18-RegUser").child(currentUser.getUid());
        commentDR= FirebaseDatabase.getInstance().getReference("CVE-Comment").child(PostId);


        recyclerView=findViewById(R.id.recyclerComments);
        LinearLayoutManager llm= new LinearLayoutManager(AComment.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
       // llm.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);

        //DisplayView();

    }

    public  static String getUId(){
        String path= FirebaseDatabase.getInstance().getReference().push().toString();
        return path.substring(path.lastIndexOf("/")*1);
    }
    public static  String getCurrentUser(){
        String current=FirebaseAuth.getInstance().getCurrentUser().getUid();
        return current.substring(current.indexOf("/"));
    }

    public void SendComment(View view) {

        final String editComment=enterComment.getText().toString().trim();
        final String uid = getUId();
        comment.setComment(editComment);
        comment.setCommentId(mAuth.getCurrentUser().getUid());
        comment.setTimeCreated(System.currentTimeMillis());

        userDR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // commentDR.child(PostId).child(uid).child(mAuth.getCurrentUser().getUid()).setValue("CurrentUser");
                commentDR.child(PostId).child(uid).setValue(comment);
                commentDR.child(PostId).child(uid).child("userName").setValue(dataSnapshot.child("userPname").getValue());
                commentDR.child(PostId).child(uid).child("userPhoto").setValue(dataSnapshot.child("UserImage").getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        enterComment.setText("");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_COMMENT, comment);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onStart() {
        super.onStart();



            FRA=new FirebaseRecyclerAdapter<Comment, mViewholderclass>(Comment.class,R.layout.commentreceived,mViewholderclass.class, commentDR.child(PostId)) {
                @Override
                protected void populateViewHolder(final mViewholderclass viewHolder, final Comment model, final int position) {
                    final String mPostId= getRef(position).getKey();


                    commentDR.child(PostId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    viewHolder.comment.setText(model.getComment());
                    viewHolder.commentTime.setText(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
                    viewHolder.setImageView(getApplicationContext(),model.getUserPhoto());
                    viewHolder.userName.setText(model.getUserName());

                   /* commentDR.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(PostId).hasChild(mAuth.getCurrentUser().getUid())){
                                viewHolder.commentTime.setText(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
                                viewHolder.comment.setText(model.getComment());
                            }
                            else {

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/




                }

            };
            recyclerView.setAdapter(FRA);
        // recyclerView.smoothScrollToPosition(FRA.getItemCount());




           /* FRMRec=new FirebaseRecyclerAdapter<Comment, ReHolderClass>(Comment.class,R.layout.commentreceived,ReHolderClass.class,commentDR) {
                @Override
                protected void populateViewHolder(ReHolderClass viewHolder, Comment model, int position) {

                }
            };
            recyclerView.setAdapter(FRMRec);*/



    }

    public void DisplayView(){


    }

    public static  class mViewholderclass extends RecyclerView.ViewHolder{

        private TextView comment, commentTime,userName;
        private  ImageView imageView;

        public mViewholderclass(View itemView) {
            super(itemView);

            comment=(TextView)itemView.findViewById(R.id.tv_comment);
            commentTime=(TextView)itemView.findViewById(R.id.tv_commenttime);
            userName=(TextView)itemView.findViewById(R.id.tv_commentusername);
            imageView=(ImageView)itemView.findViewById(R.id.cicleImage_comment);

        }

        public void setImageView(Context context,String imageView1) {
           Glide.with(context)
                   .load(imageView1)
                   .diskCacheStrategy(DiskCacheStrategy.ALL)
                   .into(imageView);
        }
    }

    public static  class ReHolderClass extends RecyclerView.ViewHolder{
        private TextView comment, commentTime,userName;
        private  ImageView imageView;
        public ReHolderClass(View itemView) {
            super(itemView);

            comment=(TextView)itemView.findViewById(R.id.tv_comment);
            commentTime=(TextView)itemView.findViewById(R.id.tv_commenttime);
            userName=(TextView)itemView.findViewById(R.id.tv_commentusername);
            imageView=(ImageView)itemView.findViewById(R.id.cicleImage_comment);

        }

        public void setImageView(Context context,String imageView1) {
            Glide.with(context)
                    .load(imageView1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }

}
