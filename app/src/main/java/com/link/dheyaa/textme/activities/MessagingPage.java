package com.link.dheyaa.textme.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.adapters.MessageAdapter;
import com.link.dheyaa.textme.models.Message;
import com.link.dheyaa.textme.models.Room;
import com.link.dheyaa.textme.models.User;
import com.link.dheyaa.textme.utils.MessagesHelpers;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MessagingPage extends AppCompatActivity {

    private String FriendId;
    private String FriendName;
    private FirebaseAuth mAuth;
    private DatabaseReference DBref, DBrefMessages;
    private User currentUser;

    private android.support.constraint.ConstraintLayout FriendView;
    private ScrollView notFriendView;
    private ScrollView LoadingView;

    private EditText message;
    private Toolbar toolbar;
    private Button sendReq;
    private TextView noMsg;
    private Button sendMsgBtn;
    private ListView messageList;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FriendName = getIntent().getStringExtra("friend_name");
        FriendId = getIntent().getStringExtra("friend_id");

        System.out.println("friend id is :" + FriendId);

        DBref = FirebaseDatabase.getInstance().getReference("Users");
        DBrefMessages = FirebaseDatabase.getInstance().getReference("Messages");
        mAuth = FirebaseAuth.getInstance();

        DBrefMessages.child(MessagesHelpers.getRoomId(FriendId, mAuth.getUid())).child("values").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot newMessage, String s) {
                Message message = newMessage.getValue(Message.class);
                messageAdapter.add(message);
               // messageAdapter.addMessage(message);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateUI(mAuth.getCurrentUser());

        setContentView(R.layout.activity_messaging_page);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        toolbar2.setTitle(FriendName);

        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        messageAdapter = new MessageAdapter(new ArrayList<Message>(), this);

        messageList = findViewById(R.id.message_list);
        messageList.setAdapter(messageAdapter);

        message = (EditText) findViewById(R.id.msg);
        noMsg = (TextView) findViewById(R.id.noMsg);
        FriendView = findViewById(R.id.friendView);
        notFriendView = findViewById(R.id.notFriendView);
        LoadingView = findViewById(R.id.loadingView);
        sendReq = findViewById(R.id.req_btn);
        sendMsgBtn = findViewById(R.id.sendMsgBtn);
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        sendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFriendRequest();
            }
        });

        setViews(1);
    }

    public void sendMessage() {
        String value = message.getText().toString();
        String senderId = mAuth.getUid();
        String reciverId = FriendId;

        Long time = System.currentTimeMillis() / 1000;

        Message message = new Message(
                MessagesHelpers.getRoomId(FriendId, mAuth.getUid()),
                reciverId,
                senderId,
                time,
                value
        );
        //DBrefMessages.getKey(MessagesHelpers.getRoomId(FriendId, mAuth.getUid()).setValue(message);
        DBrefMessages.child(MessagesHelpers.getRoomId(FriendId, mAuth.getUid())).child("values").push().setValue(message);
    }

    /*
               *  DBrefMessages.child(MessagesHelpers.getRoomId(FriendId, mAuth.getUid())).setValue(new Room()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            noMsg.setVisibility(View.VISIBLE);
                        } else {
                        }
                    }
                });

     ValueEventListener roomEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            HashMap<String, Message> newMessage = (HashMap<String, Message>) dataSnapshot.getValue();





            if (newMessage.size() < 1) {
                setViews(4);
                System.out.println("room == null");

            } else {

                Iterator it = newMessage.entrySet().iterator();
                while (it.hasNext()) {
                    final Map.Entry pair = (Map.Entry) it.next();
                    Message myMessage = (Message) pair.getValue();
                    System.out.println(pair.getValue().toString() +"--------------------------------------------- \n ---------------------------------------------");
                    messageAdapter.addIfNotExist((Message) pair.getValue());
                    messageAdapter.addMessage((Message) pair.getValue());
                }

                System.out.println(_messages.toString());
                setViews(3);



               /*

                for (int i = 0; i < _messages.size(); i++) {
                    System.out.println(_messages.toString() +"--------------------------------------------- \n ---------------------------------------------");
                    messageAdapter.addIfNotExist(_messages.get(i));
                    messageAdapter.addMessage(_messages.get(i));
                }

               *  if (room.getValues() != null && room.getValues().values() != null) {
                    HashMap<String, Message> messages = room.getValues();
                   // messageAdapter.addAll();
                    noMsg.setVisibility(View.VISIBLE);
                } else {
                    noMsg.setVisibility(View.GONE);

                    //System.out.println(room.toString());
                }


}
        }

@Override
public void onCancelled(DatabaseError databaseError) {
        System.out.println(databaseError.toString());
        }
        };
*
*
* */

    public void sendFriendRequest() {
        DBref.child(FriendId).child("friends").child(mAuth.getCurrentUser().getUid()).setValue(false);
        Snackbar.make(notFriendView, "your request has been sent to" + FriendName, Snackbar.LENGTH_LONG).show();
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
        } else if (type == 3) {
            FriendView.setVisibility(View.VISIBLE);

            notFriendView.setVisibility(View.GONE);
            LoadingView.setVisibility(View.GONE);
            messageList.setVisibility(View.VISIBLE);
            noMsg.setVisibility(View.GONE);


        } else {
            noMsg.setVisibility(View.VISIBLE);
            messageList.setVisibility(View.GONE);
        }

    }

    public void freindView(User friendData) {
       // message.setText(friendData.getEmail());

        toolbar.setTitle(friendData.getUsername());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                    if (currentUser.getFriends() != null) {
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
