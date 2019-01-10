package com.kunledarams.cve2018.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunledarams.cve2018.Activity.ProfileView;
import com.kunledarams.cve2018.Activity.ZoomView;
import com.kunledarams.cve2018.Model.User;
import com.kunledarams.cve2018.R;


public class Profile extends Fragment {


    private DatabaseReference mDB;
    private FirebaseRecyclerAdapter<User,MViewHokderClass>FRA;
    private RecyclerView recyclerView;
    private View view;
    private LinearLayout progLayout;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDB= FirebaseDatabase.getInstance().getReference().child("CVESort-User");
       /* if(isOnline()){


     }else {
            mDB= DatabaseUtlii.getDatabase().getReference().child("CVE18-User");
        }*/
        mDB.keepSynced(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_profile,container,false);

        progLayout=view.findViewById(R.id.progLoading);
        recyclerView=view.findViewById(R.id.profileRecyc);

        DividerItemDecoration itemDecoration=new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.HORIZONTAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.dividerline));
        LinearLayoutManager llm= new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(itemDecoration);
        setHasOptionsMenu(true);

        pupoluation();
        return view;
    }

    public  void  pupoluation(){

        FRA= new FirebaseRecyclerAdapter<User, MViewHokderClass>(User.class,R.layout.profilecontainer,MViewHokderClass.class,mDB) {
            @Override
            protected void populateViewHolder(MViewHokderClass viewHolder, final User model, int position) {

                final String profileId=getRef(position).getKey();
                viewHolder.setName(model.getUserName());
                viewHolder.setNickname(model.getUserNickName());
                viewHolder.setImageView(getActivity(),model.getUserImage());

                progLayout.setVisibility(View.GONE);
             viewHolder.profilelayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity(), ProfileView.class);
                        intent.putExtra("profileID",profileId);
                        startActivity(intent);
                        //Toast.makeText(getActivity(),profileId,Toast.LENGTH_LONG).show();
                        getActivity().overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                    }
                });
                viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ImageView imageView;
                        Button call,chat;
                        TextView textView;

                        final Dialog dialog= new Dialog(getContext());

                        View view1= dialog.getLayoutInflater().inflate(R.layout.zoomprofile,null);
                        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(view1);

                        dialog.setTitle(model.getUserName());
                        textView=view1.findViewById(R.id.userName);
                        textView.setText(model.getUserName());

                        imageView=view1.findViewById(R.id.imageUserProfile);
                        //imageView.setImageURI(Uri.parse(model.getUserImage()));
                        Glide.with(getActivity())
                                .load(model.getUserImage())
                                .into(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent= new Intent(getContext(), ZoomView.class);
                              //  ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()
                                      //  ,imageView,"profileImage");

                                intent.putExtra("profileImage",model.getUserImage());
                                //startActivity(intent,optionsCompat.toBundle());
                                startActivity(intent);
                            }
                        });



                        call=view1.findViewById(R.id.buttoncall);
                        call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent= new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + model.getUserPhone()));

                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }

                                startActivity(intent);
                            }

                        });
                        chat=view1.findViewById(R.id.chatbutton);

                        chat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //String url="https://api.whatsapp.com/send?phone="+ "+234"+model.getUserPhone();
                                String getNumber= String.valueOf(model.getUserPhone());
                                Uri uri= Uri.parse("smsto:"+ getNumber);
                                Intent intent= new Intent(Intent.ACTION_SENDTO,uri);
                                intent.setPackage("com.whatsapp");
                                startActivity(intent);

                            }
                        });

                       //setLayout(WindowManager.LayoutParams.,WindowManager.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                    }

                });


            }
        };
        recyclerView.setAdapter(FRA);
        FRA.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public static class MViewHokderClass extends RecyclerView.ViewHolder {
        private TextView name,nickname;
        private LinearLayout profilelayout;
        private ImageView imageView;
        public MViewHokderClass(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.userName);
            nickname=itemView.findViewById(R.id.userNickName);
            profilelayout=itemView.findViewById(R.id.layourProfile);
            imageView=itemView.findViewById(R.id.userImage);
        }
        public void setName(String userName){
            name.setText(userName);
        }
        public void setNickname(String userNick){
            nickname.setText(userNick);
        }

        public void setImageView(Context context,String s){
            Glide.with(context)
                    .load(s)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }

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
