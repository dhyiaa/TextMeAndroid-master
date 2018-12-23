package com.link.dheyaa.textme.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

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

    ListView listView;
    private FirebaseAuth mAuth;
    private ArrayList<User> friends = new ArrayList<User>();
    private DatabaseReference DBref;
    FriendAdapter adapter;
    EditText searchIput;

    public void Search(MainActivity main) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");

        listView = (ListView) findViewById(R.id.searched_friends);
        searchIput = (EditText) findViewById(R.id.search_input);
        listView.setOnItemClickListener(itemClickedSearch);

        adapter = new FriendAdapter(friends, this);
        listView.setAdapter(adapter);

        //search("m");

        searchIput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //System.out.println("searching event");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //System.out.println("searching event");
                search(searchIput.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    search(searchIput.getText().toString());
                   // System.out.println("searching event");
                }
            }
        });


        // DBref.child().child("friends").orderByValue().addValueEventListener(userEventListener);

        FloatingActionButton backBtn = (FloatingActionButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void search(String searchQuery) {
        if (!searchQuery.trim().equals("")) {
            //DBref.orderByChild("email").startAt(searchQuery).endAt(searchQuery + "\uf8ff").addValueEventListener(userEventListener);
            DBref.orderByChild("username").startAt(searchQuery).endAt(searchQuery + "\uf8ff").addValueEventListener(userEventListener);

        } else {
            adapter.clear();
            adapter.removeAll(friends);
        }
       // System.out.println("searching with " + searchQuery);
    }


    AdapterView.OnItemClickListener itemClickedSearch = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //  listView.setClickable(false);
            Intent Message = new Intent(getBaseContext(), MessagingPage.class);
            Message.putExtra("friend_name", friends.get(i).getUsername());
            Message.putExtra("friend_id", friends.get(i).getId());
            startActivity(Message);
            finish();
            //listView.setClickable(true);

        }
    };
    ValueEventListener userEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<User> users = new ArrayList<User>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                User user = postSnapshot.getValue(User.class);
                user.setId(postSnapshot.getKey());
                if(!user.getId().equalsIgnoreCase(mAuth.getCurrentUser().getUid())){
                    users.add(user);
                }
            }
            //System.out.println(users.toString());
            adapter.removeAll(friends);
            adapter.addAll(users);
         //   System.out.println(users.toString());
            adapter.notifyDataSetChanged();
            friends = users;
        }

        @Override
        public void onCancelled(DatabaseError error) {
        }
        //SetViews(false , false);
    };
}
