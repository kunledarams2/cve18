package com.kunledarams.cve2018.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.kunledarams.cve2018.Activity.AComment;
import com.kunledarams.cve2018.Activity.ZoomView;
import com.kunledarams.cve2018.Model.Gallerydata;
import com.kunledarams.cve2018.R;
import com.kunledarams.cve2018.Ultil.DatabaseUtlii;
import com.kunledarams.cve2018.Ultil.Service;

//import static com.google.android.gms.internal.zzs.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * create an instance of this fragment.
 */
public class Gallery extends Fragment {


    private DatabaseReference mdb;
    private FirebaseRecyclerAdapter<Gallerydata,mHolderClass>recyclerAdapter;
    private RecyclerView recyclerView;
    private  View mRootview;
    private DatabaseReference likeDB;
    private FirebaseUser currentUser;
    private DatabaseReference userDb;
    private FirebaseAuth mAuth;
    private DatabaseReference DBLikecount;

    private boolean likeshow= false;
    public Gallery() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Service service= new Service(getActivity());


       if(service.isOnline()){
           mdb= FirebaseDatabase.getInstance().getReference().child("CVE-Gallery");
        }
        else {
            mdb= DatabaseUtlii.getDatabase().getReference().child("CVE-Gallery");
        }

        likeDB=FirebaseDatabase.getInstance().getReference().child("CVE-Likes");
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        userDb=FirebaseDatabase.getInstance().getReference().child("CVE18-RegUser").child(currentUser.getUid());
        DBLikecount=FirebaseDatabase.getInstance().getReference();
        mdb.keepSynced(true);
        likeDB.keepSynced(true);
        userDb.keepSynced(true);
        DBLikecount.keepSynced(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
          mRootview= inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView=mRootview.findViewById(R.id.recyGallery);

        LinearLayoutManager llm= new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        //recyclerView.setRevealOnFocusHint(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        Pupoluate();
        return  mRootview;
    }

    public  void Pupoluate(){
        recyclerAdapter= new FirebaseRecyclerAdapter<Gallerydata, mHolderClass>(Gallerydata.class,R.layout.gallerycontainer,mHolderClass.class,mdb) {
            @Override
            protected void populateViewHolder(final mHolderClass viewHolder, final Gallerydata model, int position) {
                final String postId=getRef(position).getKey();
                mdb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        viewHolder.setUserCommentview(model.getUserComment());
                        viewHolder.setUserImage(getActivity(),model.getUserImage());
                        viewHolder.setGalleryImage(getActivity(),model.getGImage());
                        viewHolder.userNNameview.setText(model.getUserNickname());
                        viewHolder.postTimeview.setText(DateUtils.getRelativeTimeSpanString(model.getPostTime()));


                        if(dataSnapshot.child(postId).hasChild("GImage")){

                            viewHolder.setLikeImage(postId);
                            viewHolder.galleryImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent= new Intent(getContext(), ZoomView.class);
                                    intent.putExtra("ZoomImage",postId);
                                    startActivity(intent);
                                }
                            });

                            viewHolder.layoutComment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent commentAct=new Intent(getContext(), AComment.class);
                                    commentAct.putExtra("CommentId",postId);
                                    startActivity(commentAct);
                                }
                            });

                            viewHolder.userImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final ImageView imageView;
                                    Button call,chat;
                                    TextView textView;
                                    final Dialog dialog= new Dialog(getContext());
                                    View view1= getActivity().getLayoutInflater().inflate(R.layout.zoomprofile,null,false);
                                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);


                                    textView=view1.findViewById(R.id.userName);
                                    textView.setText(model.getUserNickname());

                                    imageView=view1.findViewById(R.id.imageUserProfile);
                                    //imageView.setImageURI(Uri.parse(model.getUserImage()));
                                    Glide.with(getActivity())
                                            .load(model.getUserImage())
                                            .into(imageView);

                                    dialog.setContentView(view1);
                                    //dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                                    dialog.show();
                                }

                            });

                            viewHolder.likeImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    likeshow=true;
                                    final int[] likecount = {0};
                                    DBLikecount.child(postId).child("LikeCount");
                                    DBLikecount.keepSynced(true);
                                    likeDB.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(likeshow){
                                                if(dataSnapshot.child(postId).hasChild(mAuth.getCurrentUser().getUid())){
                                                    likeDB.child(postId).child(mAuth.getCurrentUser().getUid()).removeValue();
                                                    likeDB.child(postId).child("LikeId").removeValue();

                                                    // updateCounter(false);
                                                    likeshow=false;
                                                    Toast.makeText(getActivity(),"unlike",Toast.LENGTH_SHORT).show();
                                                }

                                                else {
                                                    //  likeDB.child(postId).child(mAuth.getCurrentUser().getUid()).setValue(model.getUserNickname());
                                                    userDb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            likeDB.child(postId).child(mAuth.getCurrentUser().getUid()).
                                                                    setValue(dataSnapshot.child("userName").getValue());

                                                            Toast.makeText(getActivity(),"Like",Toast.LENGTH_SHORT).show();
                                                            // updateCounter(true);
                                                            likeshow=false;
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_LONG).show();

                                                        }
                                                    });
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                        }else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void updateCounter(final boolean increment) {
        DBLikecount.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() != null) {
                    int value = mutableData.getValue(Integer.class);
                    if(increment) {
                        value++;
                    } else {
                        value--;
                    }
                    mutableData.setValue(value);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
               // Log.d(TAG, "likeTransaction:onComplete:" + databaseError);
            }
        });
    }

    public static  class mHolderClass extends RecyclerView.ViewHolder{

        private TextView userCommentview,postTimeview,userNNameview;
        private ImageView galleryImage, userImage,likeImage;
        private LinearLayout layoutComment;
        private DatabaseReference datalikes;
        private FirebaseAuth mAuth;

        public mHolderClass(View itemView) {
            super(itemView);

            mAuth=FirebaseAuth.getInstance();
            datalikes=FirebaseDatabase.getInstance().getReference().child("CVE-Likes");
            datalikes.keepSynced(true);

            userCommentview=(TextView)itemView.findViewById(R.id.user_comment);
            postTimeview=(TextView)itemView.findViewById(R.id.tv_post_time);
            userNNameview=(TextView)itemView.findViewById(R.id.tv_post_username);
            galleryImage=(ImageView)itemView.findViewById(R.id.gallery_image_View);
            userImage=(ImageView)itemView.findViewById(R.id.galleryuserImage);
            likeImage=(ImageView)itemView.findViewById(R.id.tv_like_image);

            layoutComment=(LinearLayout)itemView.findViewById(R.id.LinearLayoutComment);

        }
        public void setUserCommentview(String comment){
            userCommentview.setText(comment);
        }
        public void setUserImage(Context context, String userImagev){
            Glide.with(context)
                    .load(userImagev)
                    .fitCenter()
                    .into(userImage);
        }
        public void setGalleryImage(Context context2,String gallery){
            Glide.with(context2)
                    .load(gallery)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(galleryImage);

        }
        public  void setLikeImage(final String PostId){
            datalikes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(PostId).hasChild(mAuth.getCurrentUser().getUid())){

                        likeImage.setImageResource(R.drawable.like);
                    }
                    else {
                        likeImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
