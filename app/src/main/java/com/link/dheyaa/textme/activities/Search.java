
/* TextMe Team
 * Jan 2019
 * Search class:
 * Search activity of TextMe Program
 */

package com.link.dheyaa.textme.activities;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.link.dheyaa.textme.adapters.FriendAdapter;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.models.User;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    // variable declaration
    private RecyclerView listView;
    private FirebaseAuth mAuth;
    private ArrayList<User> friends = new ArrayList<User>();
    private DatabaseReference DBref;
    private FriendAdapter adapter;
    private EditText searchIput;

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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // setup data from firebase
        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");

        // find predefined views in XML
        listView = (RecyclerView) findViewById(R.id.searched_friends);
        searchIput = (EditText) findViewById(R.id.search_input);

        adapter = new FriendAdapter(this, R.layout.friends_list_item, friends);

        listView.setHasFixedSize(true);
        listView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);

        //search("m");
        // listen for text changing
        searchIput.addTextChangedListener(
            new TextWatcher() { // watch the text
                /* method watches before text changes
                * @param s - text
                * @param start - starting value
                * @param count - amount of chars
                * @param after - overall value
                * */
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                /* method watches when text changes
                 * @param s - text
                 * @param start - starting value
                 * @param count - amount of chars
                 * @param after - overall value
                 * */
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    search(searchIput.getText().toString()); // search user

                }

                /* method watches after text changes
                 * @param s - text
                 * @param start - starting value
                 * @param count - amount of chars
                 * @param after - overall value
                 * */
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        search(searchIput.getText().toString()); // search user

                    } // if input is not empty
                }
        });

        // DBref.child().child("friends").orderByValue().addValueEventListener(userEventListener);

        // back button setup
        FloatingActionButton backBtn = (FloatingActionButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() { // listen for clicking button
            public void onClick(View v) { // launch when button is clicked
                finish();
            }
        });

    }

    /* method to search user
    * @param searchQuery - searching input
    * */
    public void search(String searchQuery) {
        if (!searchQuery.trim().equals("")) {
            //DBref.orderByChild("email").startAt(searchQuery).endAt(searchQuery + "\uf8ff").addValueEventListener(userEventListener);
            DBref.orderByChild("username").startAt(searchQuery).endAt(searchQuery + "\uf8ff").addValueEventListener(userEventListener);
        } // if searching input is not empty
    }

    AdapterView.OnItemClickListener itemClickedSearch = new AdapterView.OnItemClickListener() { // listen for clicking users prompted after search

        /* method launches when item is clicked
        * @param adapterView - current adapterView
        * @param view - current view
        * @param i - the index of friend
        * @param l - position of scrolling
        * */
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //  listView.setClickable(false);

            // add friend's data into intent
            Intent Message = new Intent(getBaseContext(), MessagingPage.class);
            Message.putExtra("friend_name", friends.get(i).getUsername());
            Message.putExtra("friend_id", friends.get(i).getId());
            Message.putExtra("friend_image", friends.get(i).getImagePath ());
            
            startActivity(Message);
            finish();
            //listView.setClickable(true);

        }
    };

    ValueEventListener userEventListener = new ValueEventListener() { // listen for value changing

        /* method launches when data changes
        * @param dataSnapshot - snapshot of current data
        * */
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<User> users = new ArrayList<User>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                User user = postSnapshot.getValue(User.class);
                user.setId(postSnapshot.getKey());
                if(!user.getId().equalsIgnoreCase(mAuth.getCurrentUser().getUid())){
                    users.add(user);
                    adapter.addFriend(user , true);
                    adapter.notifyDataSetChanged();
                } // if user id matches in firebase
            } // loop through children of data snapshot
        }

        /* method lauches when event cancelled
        * @param error - firebase error
        * */
        @Override
        public void onCancelled(DatabaseError error) {
        }
        //SetViews(false , false);
    };
}
