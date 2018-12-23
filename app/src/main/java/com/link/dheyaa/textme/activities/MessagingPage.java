package com.link.dheyaa.textme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.models.User;

public class MessagingPage extends AppCompatActivity {

    private String FriendId;
    private String FriendName;
    private FirebaseAuth mAuth;
    private DatabaseReference DBref;
    private User currentUser;

    private android.support.constraint.ConstraintLayout FriendView;
    private ScrollView notFriendView;
    private ScrollView LoadingView;
    
    private EditText message;
    private Toolbar toolbar;
    private Button sendReq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FriendName = getIntent().getStringExtra("friend_name");
        FriendId = getIntent().getStringExtra("friend_id");

        System.out.println("friend id is :"+FriendId);

        DBref = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        updateUI(mAuth.getCurrentUser());


        setContentView(R.layout.activity_messaging_page);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        toolbar2.setTitle(FriendName);


        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        message = (EditText) findViewById(R.id.msg);

        FriendView = findViewById(R.id.friendView);
        notFriendView = findViewById(R.id.notFriendView);
        LoadingView = findViewById(R.id.loadingView);
        sendReq = findViewById(R.id.req_btn);

        sendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFriendRequest();
            }
        });

        setViews(1);


    }

    public void sendFriendRequest(){

    }

    public void setViews(int type) {

        if (type == 1) {
            LoadingView.setVisibility(View.VISIBLE);
            notFriendView.setVisibility(View.GONE);
            FriendView.setVisibility(View.GONE);
        } else if (type == 2) {
            notFriendView.setVisibility(View.VISIBLE);
            LoadingView.setVisibility(View.GONE);
            FriendView.setVisibility(View.GONE);
        } else {
            FriendView.setVisibility(View.VISIBLE);
            notFriendView.setVisibility(View.GONE);
            LoadingView.setVisibility(View.GONE);
        }

    }

    public void freindView(User friendData) {
        message.setText(friendData.getEmail());

        toolbar.setTitle(friendData.getUsername());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //System.out.println("ok ok very Good");
        setViews(3);


    }

    public void errorView() {
        System.out.println("error when retrieving the friend");
    }


    public void updateUI(FirebaseUser user) {
        if (user != null) {
            DBref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                    if(currentUser.getFriends() != null){
                        if (currentUser.getFriends().containsKey(FriendId)) {
                            if (currentUser.getFriends().get(FriendId)) {
                                System.out.println("you are firend with " + FriendName);

                                DBref.child(FriendId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        freindView(dataSnapshot.getValue(User.class));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        errorView();
                                    }
                                });
                            } else {
                                System.out.println("you are not . firend with " + FriendName);
                                setViews(2);
                            }
                        }
                    } else {
                        System.out.println("you are not . firend with 2" + FriendName);
                        setViews(2);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
