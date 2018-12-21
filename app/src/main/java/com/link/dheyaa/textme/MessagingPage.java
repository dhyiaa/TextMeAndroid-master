package com.link.dheyaa.textme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessagingPage extends AppCompatActivity {

    private String FriendId;
    private String FriendName;
    private FirebaseAuth mAuth;
    private DatabaseReference DBref;
    private User currentUser;

    private LinearLayout FriendView;
    private LinearLayout notFriendView;
    private LinearLayout LoadingView;
    private EditText message;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FriendName = getIntent().getStringExtra("friend_name");
        FriendId = getIntent().getStringExtra("friend_id");

        DBref = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        updateUI(mAuth.getCurrentUser());


        setContentView(R.layout.activity_messaging_page);

        toolbar = findViewById(R.id.toolbar);
        message = findViewById(R.id.msg);

        FriendView = findViewById(R.id.friendView);
        notFriendView = findViewById(R.id.notFriendView);
        LoadingView = findViewById(R.id.loadingView);
        setViews(1);


    }

    public void setViews(int type) {

        if (type == 1) {
            LoadingView.setVisibility(View.VISIBLE);
            notFriendView.setVisibility(View.INVISIBLE);
            FriendView.setVisibility(View.INVISIBLE);
        } else if (type == 2) {
            notFriendView.setVisibility(View.VISIBLE);
            LoadingView.setVisibility(View.INVISIBLE);
            FriendView.setVisibility(View.INVISIBLE);
        } else {
            FriendView.setVisibility(View.VISIBLE);
            notFriendView.setVisibility(View.INVISIBLE);
            LoadingView.setVisibility(View.INVISIBLE);
        }

    }

    public void friendView(User friendData) {
        message.setText(friendData.getEmail());

        toolbar.setTitle(friendData.getUsername());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        System.out.println("ok ok very Good");
        setViews(3);


    }

    public void errorView() {
        System.out.println("error when retrieving the friend");
    }

    public void notFriendView(int userId) {
        System.out.println("not friend error !!!");
    }


    public void updateUI(FirebaseUser user) {
        if (user != null) {
            DBref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                    if (currentUser.getFriends().containsKey(FriendId)) {
                        if (currentUser.getFriends().get(FriendId)) {
                            System.out.println("you are firend with " + FriendName);

                            DBref.child(FriendId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    friendView(dataSnapshot.getValue(User.class));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    errorView();
                                }
                            });
                        } else {
                            System.out.println("you are not . firend with " + FriendName);
                            setViews(1);
                        }
                    } else {
                        System.out.println("you are not . firend with 2" + FriendName);
                        setViews(1);
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
