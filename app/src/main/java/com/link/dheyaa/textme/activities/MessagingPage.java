package com.link.dheyaa.textme.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.adapters.MessagingRecyclingAdapter;
import com.link.dheyaa.textme.itemDecorators.friendsItemDecorator;
import com.link.dheyaa.textme.models.Message;
import com.link.dheyaa.textme.models.User;
import com.link.dheyaa.textme.utils.MessagesHelpers;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagingPage extends AppCompatActivity {

    private String FriendId;
    private String FriendName;
    private FirebaseAuth mAuth;
    private DatabaseReference DBref, DBrefMessages;
    private User currentUser;

    private ConstraintLayout FriendView;
    private ScrollView notFriendView;
    private ScrollView LoadingView;

    private EditText message;
    private Toolbar toolbar;
    private Button sendReq;
    private TextView noMsg;
    private com.google.android.material.floatingactionbutton.FloatingActionButton sendMsgBtn;
    private RecyclerView messageList;
    private MessagingRecyclingAdapter messageAdapter;
    private TextView msgNoFriend;
    private String FriendImage;
    public HashMap<String, String> extraDAta = new HashMap<String, String> ();
    public Context _context = this;
    public RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        FriendName = getIntent().getStringExtra ("friend_name");
        FriendId = getIntent().getStringExtra ("friend_id");
        FriendImage = getIntent().getStringExtra ("friend_image");

        layoutManager = new LinearLayoutManager (_context);

        System.out.println ("friend id is :" + FriendId);

        DBref = FirebaseDatabase.getInstance ().getReference ("Users");
        DBrefMessages = FirebaseDatabase.getInstance ().getReference ("Messages");
        mAuth = FirebaseAuth.getInstance ();

        updateUI (mAuth.getCurrentUser ());


        DBrefMessages.child (MessagesHelpers.getRoomId (FriendId, mAuth.getUid ())).child ("values").addChildEventListener (new ChildEventListener () {
            @Override
            public void onChildAdded(DataSnapshot newMessage, String s) {
                Message message = newMessage.getValue (Message.class);
                messageAdapter.addMessage (message, layoutManager);
                long numsOfChildren = newMessage.getChildrenCount ();
                if (numsOfChildren < 0) {
                    setViews (4);

                } else {
                    setViews (3);

                }
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


        setContentView (R.layout.activity_messaging_page);
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        Toolbar toolbar2 = (Toolbar) findViewById (R.id.toolbar2);
        toolbar2.setTitle (FriendName);

        setSupportActionBar (toolbar2);
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        getSupportActionBar ().setDisplayShowHomeEnabled (true);


        //extraDAta.put("currentAuthProfile", mAuth.getCurrentUser().getUid());


        messageList = (RecyclerView) findViewById (R.id.message_list);


        message = (EditText) findViewById (R.id.msg);
        noMsg = (TextView) findViewById (R.id.noMsg);
        FriendView = findViewById (R.id.friendView);
        notFriendView = findViewById (R.id.notFriendView);
        LoadingView = findViewById (R.id.loadingView);
        sendReq = findViewById (R.id.req_btn);
        sendMsgBtn = findViewById (R.id.sendMsgBtn);
        msgNoFriend = findViewById (R.id.mesgNoFriend);

        sendMsgBtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                sendMessage ();
            }
        });
        sendReq.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                sendFriendRequest ();
            }
        });


        setViews (1);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.messaging_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.blockFriend) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder (this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder (this);
            }
            builder.setTitle ("Blocking a friend ")
                    .setMessage ("Are you sure you want to block " + FriendName)
                    .setPositiveButton (android.R.string.yes, new DialogInterface.OnClickListener () {
                        public void onClick(DialogInterface dialog, int which) {
                            DBref.child (mAuth.getUid ()).child ("friends").child (FriendId).setValue (-1);
                            DBref.child (FriendId).child ("friends").child (FriendId).setValue (-1);
                            finish ();
                        }
                    })
                    .setNegativeButton (android.R.string.no, new DialogInterface.OnClickListener () {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon (android.R.drawable.ic_dialog_alert)
                    .show ();
        } else if (id == R.id.muteFriend) {

        }

        return super.onOptionsItemSelected (item);
    }

    public void sendMessage() {
        String value = message.getText ().toString ();
        if (!value.trim ().equals ("")) {
            message.setText ("");
            String senderId = mAuth.getCurrentUser ().getUid ();
            String reciverId = FriendId;
            System.out.println ("reciverId=" + reciverId + "   \n   senderId=" + senderId);

            Long time = System.currentTimeMillis ();

            Message message = new Message (
                    MessagesHelpers.getRoomId (FriendId, mAuth.getUid ()),
                    reciverId,
                    senderId,
                    time,
                    value
            );
            DBrefMessages.child (MessagesHelpers.getRoomId (FriendId, mAuth.getUid ())).child ("values").push ().setValue (message);
        }
    }


    public void sendFriendRequest() {
        DBref.child (FriendId).child ("friends").child (mAuth.getCurrentUser ().getUid ()).setValue (0);
        Snackbar.make (notFriendView, "your request has been sent to " + FriendName + ".", Snackbar.LENGTH_LONG).show ();
        setViews (10);
    }

    public void setViews(int type) {

        if (type == 1) {
            LoadingView.setVisibility (View.VISIBLE);
            notFriendView.setVisibility (View.GONE);
            FriendView.setVisibility (View.GONE);
        } else if (type == 2) {
            sendReq.setActivated (true);
            msgNoFriend.setText ("you are not friends with " + FriendName);
            notFriendView.setVisibility (View.VISIBLE);
            LoadingView.setVisibility (View.GONE);
            FriendView.setVisibility (View.GONE);
        } else if (type == 3) {
            FriendView.setVisibility (View.VISIBLE);

            notFriendView.setVisibility (View.GONE);
            LoadingView.setVisibility (View.GONE);
            messageList.setVisibility (View.VISIBLE);
            noMsg.setVisibility (View.GONE);


        } else if (type == 10) {
            msgNoFriend.setText ("You have already sent a request to " + FriendName);
            sendReq.setActivated (false);
            sendReq.setVisibility (View.INVISIBLE);
        } else {
            noMsg.setVisibility (View.VISIBLE);
            messageList.setVisibility (View.GONE);
        }

    }

    public void freindView(User friendData) {
        // message.setText(friendData.getEmail());

        toolbar.setTitle (friendData.getUsername ());
        setSupportActionBar (toolbar);
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        getSupportActionBar ().setDisplayShowHomeEnabled (true);
        setViews (3);


    }

    public void errorView() {
        System.out.println ("error when retrieving the friend");
    }


    public void updateUI(FirebaseUser user) {
        if (user != null) {
            DBref.child (user.getUid ()).addValueEventListener (new ValueEventListener () {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    System.out.println ("checking from m p : -->>> "+FriendImage);
                    System.out.println ("checking from m p I: -->>> "+getIntent().getStringExtra ("friend_image"));



                    currentUser = dataSnapshot.getValue (User.class);
                    extraDAta.put ("currentAuthImage", currentUser.getImagePath ());
                    extraDAta.put ("FriendName", FriendName);
                    extraDAta.put ("FriendId", FriendId);
                    extraDAta.put ("FriendImage", FriendImage);
                    extraDAta.put ("currentAuthId", mAuth.getUid ());

                    // messageAdapter = new MessageAdapter(new ArrayList<Message>(), _context, extraDAta);
                    ArrayList<Message> msgs = new ArrayList<Message> ();
                    messageAdapter = new MessagingRecyclingAdapter (_context, R.layout.message_list_item_you, msgs, extraDAta);

                    messageList.setAdapter (messageAdapter);
                    messageList.setHasFixedSize (true);
                    messageList.setLayoutManager (layoutManager);
                    messageList.addItemDecoration (new friendsItemDecorator (0));
                    messageList.getRecycledViewPool().setMaxRecycledViews(1, 0);
                    messageList.getRecycledViewPool().setMaxRecycledViews(0, 0);


                    if (currentUser.getFriends () != null) {
                        if (currentUser.getFriends ().containsKey (FriendId)) {
                            if (currentUser.getFriends ().get (FriendId) == 1) {
                                System.out.println ("you are firend with " + FriendName);

                                DBref.child (FriendId).addValueEventListener (new ValueEventListener () {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        freindView (dataSnapshot.getValue (User.class));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        errorView ();
                                    }
                                });
                            } else if (currentUser.getFriends ().get (FriendId) == -1) {
                                finish ();
                            } else {
                                System.out.println ("you are not . firend with " + FriendName);
                                setViews (2);
                            }
                        }
                    } else {
                        System.out.println ("you are not . firend with " + FriendName);
                        setViews (2);
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
        onBackPressed ();
        return true;
    }

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