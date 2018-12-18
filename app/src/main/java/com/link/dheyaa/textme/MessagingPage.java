package com.link.dheyaa.textme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FriendName = getIntent().getStringExtra("friend_name");
        FriendId = getIntent().getStringExtra("friend_id");

        DBref = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        updateUI(mAuth.getCurrentUser());


    }

    public void freindView(User friendData) {
        setContentView(R.layout.activity_messaging_page);

        EditText message = (EditText) findViewById(R.id.msg);
        message.setText(friendData.getEmail());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(friendData.getUsername());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        System.out.println("ok ok very Good");

    }

    public void errorView() {
        System.out.println("error when retrieving the friend");
    }

    public void notFriendView(int userId) {
        System.out.println("not friend");
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
                                    freindView(dataSnapshot.getValue(User.class));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    errorView();
                                }
                            });
                        } else {
                            System.out.println("you are not . firend with " + FriendName);
                        }
                    } else {
                        System.out.println("you are not . firend with 2" + FriendName);
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
