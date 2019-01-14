
/* TextMe Team
 * Jan 2019
 * MessagingPage class:
 * Messaging activity of TextMe Program
 */

package com.link.dheyaa.textme.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.adapters.MessagingRecyclingAdapter;
import com.link.dheyaa.textme.itemDecorators.friendsItemDecorator;
import com.link.dheyaa.textme.models.Message;
import com.link.dheyaa.textme.models.User;
import com.link.dheyaa.textme.utils.MessagesHelpers;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagingPage extends AppCompatActivity {

    // variable declaration
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

    /* onCreate method for activity
     * @param savedInstanceState - data bundle for activity
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("textMeSP", MODE_PRIVATE);

        String isDark = prefs.getString("isDark", null);
        if (isDark != null) {
            setTheme (R.style.ActivityTheme_Primary_Base_Dark);

        }else{
            setTheme (R.style.ActivityTheme_Primary_Base_Light);
        }

        super.onCreate (savedInstanceState);

        // get intents from search activity
        FriendName = getIntent ().getStringExtra ("friend_name");
        FriendId = getIntent ().getStringExtra ("friend_id");
        FriendImage = getIntent ().getStringExtra ("friend_image");

        layoutManager = new LinearLayoutManager (_context);

        System.out.println ("msg->friend->id->" + FriendId);
        System.out.println ("msg->friend->id->" + FriendName);
        System.out.println ("msg->friend->id->" + FriendImage);

        // setup data from firebase
        DBref = FirebaseDatabase.getInstance ().getReference ("Users");
        DBrefMessages = FirebaseDatabase.getInstance ().getReference ("Messages");
        mAuth = FirebaseAuth.getInstance ();

        System.out.println ("msg->getCurrentUser->id->" + mAuth.getCurrentUser ().getUid ());

        // add a Value Event Listener in user reference in firebase
        DBref.child (mAuth.getCurrentUser ().getUid ()).addValueEventListener (new ValueEventListener () { // listen for value changing

            /* method launches when data changes
             * @param dataSnapshot - snapshot of current data
             * */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue (User.class);
                currentUser.setId (dataSnapshot.getKey ());

                System.out.println ("msg->userChanged->id->" + dataSnapshot.getKey ());
                System.out.println ("msg->userChanged->user->" + currentUser.toString ());

                // put data
                extraDAta.put ("currentAuthImage", currentUser.getImagePath ());
                extraDAta.put ("FriendName", FriendName);
                extraDAta.put ("FriendId", FriendId);
                extraDAta.put ("FriendImage", FriendImage);
                extraDAta.put ("currentAuthId", mAuth.getUid ());

                // adapt messages
                // messageAdapter = new MessageAdapter(new ArrayList<Message>(), _context, extraDAta);
                ArrayList<Message> msgs = new ArrayList<Message> ();
                messageAdapter = new MessagingRecyclingAdapter (_context, R.layout.message_list_item_you, msgs, extraDAta);

                // toolbar setup
                setContentView (R.layout.activity_messaging_page);
                toolbar = (Toolbar) findViewById (R.id.toolbar);
                Toolbar toolbar2 = (Toolbar) findViewById (R.id.toolbar2);
                toolbar2.setTitle (FriendName);

                setSupportActionBar (toolbar2);
                getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
                getSupportActionBar ().setDisplayShowHomeEnabled (true);

                // find views in XML
                messageList = (RecyclerView) findViewById (R.id.message_list);

                message = (EditText) findViewById (R.id.msg);
                noMsg = (TextView) findViewById (R.id.noMsg);
                FriendView = findViewById (R.id.friendView);
                notFriendView = findViewById (R.id.notFriendView);
                LoadingView = findViewById (R.id.loadingView);
                sendReq = findViewById (R.id.req_btn);
                sendMsgBtn = findViewById (R.id.sendMsgBtn);
                msgNoFriend = findViewById (R.id.mesgNoFriend);

                sendMsgBtn.setOnClickListener (new View.OnClickListener () { // listen for clicking send message btn
                    @Override
                    public void onClick(View v) { // launches when btn is clicked
                        sendMessage(); // send message when btn is clicked
                    }
                });
                sendReq.setOnClickListener (new View.OnClickListener () { // listen for clicking request btn
                    @Override
                    public void onClick(View v) { // launches when btn is clicked
                        sendFriendRequest(); // send request when btn is clicked
                    }
                });

                setViews (1);


                updateUI(); // update UI
            }

            /* method when event is cancelled
            * @param error - error in firebase
            * */
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }

    /* method to create options in toolbar
    * @param menu - the message menu
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.messaging_page, menu);
        return true;
    }

    /* method for selected items' actions
    * @param item - items of the menu
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.blockFriend) { // if the user chooses to block the friend
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder (this, android.R.style.Theme_Material_Dialog_Alert);
            } // if platform version is higher than or equals to Lollipop's version
            else {
                builder = new AlertDialog.Builder (this);
            } // if platform version is lower than Lollipop
            builder.setTitle ("Blocking a friend").setMessage ("Are you sure you want to block " + FriendName + " ?") // set title and message
                    .setPositiveButton (android.R.string.yes, new DialogInterface.OnClickListener () { // set positive button and listener for the button

                        /* method launches when button is clicked
                        * @param dialog - the dialog interface
                        * @param which - id of identification
                        * */
                        public void onClick(DialogInterface dialog, int which) {
                            // setting value of blocking
                            DBref.child (mAuth.getUid ()).child ("friends").child (FriendId).setValue (-1);
                            DBref.child (FriendId).child ("friends").child (FriendId).setValue (-1);
                            finish ();
                        }
                    })
                    .setNegativeButton (android.R.string.no, new DialogInterface.OnClickListener () { // set positive button and listener for the button

                        /* method launches when button is clicked
                         * @param dialog - the dialog interface
                         * @param which - id of identification
                         * */
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon (android.R.drawable.ic_dialog_alert) // set icon
                    .show(); // enable the window
        }

        return super.onOptionsItemSelected (item);
    }

    /* method to send message
    * no params
    * */
    public void sendMessage() {
        String value = message.getText ().toString ();
        if (!value.trim ().equals ("")) { // if value is not empty
            message.setText ("");
            // get id
            String senderId = mAuth.getCurrentUser ().getUid ();
            String receiverId = FriendId;
            System.out.println ("receiverId=" + receiverId + "   \n   senderId=" + senderId);

            Long time = System.currentTimeMillis ();

            Message message = new Message (
                    MessagesHelpers.getRoomId (FriendId, mAuth.getUid ()),
                    receiverId,
                    senderId,
                    time,
                    value
            );
            DBrefMessages.child (MessagesHelpers.getRoomId (FriendId, mAuth.getUid ())).child ("values").push ().setValue (message); // push message to database
        }
    }

    /* method to send friend request
    * no params
    * */
    public void sendFriendRequest() {
        DBref.child (FriendId).child ("friends").child (mAuth.getCurrentUser ().getUid ()).setValue (0); // update request to database
        Snackbar.make (notFriendView, "Your request has been successfully sent to " + FriendName + ".", Snackbar.LENGTH_LONG).show (); // show snack bar
        setViews (10); // set request sent view
    }

    /* method to set types of views
    * @param type - type identification number
    * */
    public void setViews(int type) {

        if (type == 1) { // loading view
            LoadingView.setVisibility (View.VISIBLE);
            notFriendView.setVisibility (View.GONE);
            FriendView.setVisibility (View.GONE);
            System.out.println ("type ->> 1");
        } else if (type == 2) { // no friends view
            sendReq.setActivated (true);
            msgNoFriend.setText ("you are not friends with " + FriendName);
            notFriendView.setVisibility (View.VISIBLE);
            LoadingView.setVisibility (View.GONE);
            FriendView.setVisibility (View.GONE);
            System.out.println ("type ->> 2");

        } else if (type == 3) { // view when having friend
            FriendView.setVisibility (View.VISIBLE);

            notFriendView.setVisibility (View.GONE);
            LoadingView.setVisibility (View.GONE);
            messageList.setVisibility (View.VISIBLE);
            noMsg.setVisibility (View.GONE);
            System.out.println ("type ->> 3");

        } else if (type == 10) { // view when a request is sent
            msgNoFriend.setText ("You have already sent a request to " + FriendName);
            sendReq.setActivated (false);
            sendReq.setVisibility (View.INVISIBLE);
            System.out.println ("type ->> 10");

        } else { // view when have no messages
            noMsg.setVisibility (View.VISIBLE);
            FriendView.setVisibility (View.VISIBLE);

            messageList.setVisibility (View.GONE);
            notFriendView.setVisibility (View.GONE);
            LoadingView.setVisibility (View.GONE);

            System.out.println ("type ->> else");

        }

    }

    /* method for friend view
    * @param currentUser - current user logged in
    * */
    public void friendView(User currentUser) {
                                                                                                                                // message.setText(friendData.getEmail());

        // preparing recycled view for messages
        messageList.setAdapter (messageAdapter);
        messageList.setHasFixedSize (true);
        messageList.setLayoutManager (new LinearLayoutManager (_context));
        messageList.addItemDecoration (new friendsItemDecorator (0));
        messageList.getRecycledViewPool ().setMaxRecycledViews (1, 0);
        messageList.getRecycledViewPool ().setMaxRecycledViews (0, 0);

        // toolbar setup
        toolbar.setTitle (currentUser.getUsername ());
        setSupportActionBar (toolbar);
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        getSupportActionBar ().setDisplayShowHomeEnabled (true);

        // get messages from database by room id
        DBrefMessages.child (MessagesHelpers.getRoomId (FriendId, mAuth.getUid ())).child ("values").addChildEventListener (new ChildEventListener () { // listen for children

            /* method launches when a child is added
            * @param newMessage - snapshot of new message data
            * @param s - message id
            * */
            @Override
            public void onChildAdded(DataSnapshot newMessage, String s) {

                System.out.println ("msg->friendView->onChildAdded ");

                // add message and scroll to its position
                Message message = newMessage.getValue (Message.class);
                messageAdapter.addMessage (message, layoutManager);
                messageList.scrollToPosition(messageAdapter.messages.size() - 1);

                long numsOfChildren = newMessage.getChildrenCount ();
                if (numsOfChildren < 1) { // if has no message
                    setViews (4); // set no message view

                } else { // if has message
                    setViews (3);
                    // set friend & message view
                }
                // messageAdapter.addMessage(message);
            }

            /* method launches when a child is changed
            * @param dataSnapshot - snapshot of data
            * @param s - message id
            * */
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            /* method launches when a child is removed
            * @param dataSnapshot - snapshot of data
            * */
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            /* method launches when a child is moved
            * @param dataSnapshot - snapshot of data
            * @param s - message id
            * */
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            /* method launches when event is cancelled
            * @param databaseError - error on firebase
            * */
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        setViews (3);

    }

    /* method to update UI
    * no params
    * */
    public void updateUI() {
        System.out.println ("msg->updateUi");

        if (currentUser != null && currentUser.getFriends () != null) { // if there is an user and the user has at least a friend
            System.out.println ("msg->updateUi->currentUser != null ");

            if (currentUser.getFriends ().containsKey (FriendId)) { // if friend is in user's node
                System.out.println ("msg->updateUi->currentUser.getFriends ().containsKey : true ");

                if (currentUser.getFriends ().get (FriendId) == 1) { // if the two users are friends
                    System.out.println ("msg->updateUi->isFriend->true ");

                    // update id to database
                    DBref.child (FriendId).addValueEventListener (new ValueEventListener () { // listen for value changing

                        /* method launches when data is changed
                        * @param dataSnapshot - snapshot of data
                        * */
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            friendView (dataSnapshot.getValue (User.class)); // set friend view
                        }

                        /* method launches when event is cancelled
                        * @param databaseError - error on firebase
                        * */
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            setViews (2); // set no friend view
                        }
                    });
                } else if (currentUser.getFriends ().get (FriendId) == -1) { // if user blocked friend
                    System.out.println ("msg->updateUi->isBLocked->true ");
                    finish (); // close activity
                } else { // if request is sent but not confirmed or rejected by the user
                    System.out.println ("msg->updateUi->isFriend->false ");
                    setViews (2); // set no friend view
                }
            } else { // if the friend is not in user's node
                setViews (2); // set no friend view

            }
        } else { // if there is no user or the user has no friends
            System.out.println ("msg->updateUi->currentUser.getFriends ().containsKey : false");
            setViews (2); // set no friend view
        }
    }

    /* method to go back to previous activity
    * return true when function is completed
    * */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed (); // go back to previous activity
        return true;
    }

}