package com.link.dheyaa.textme;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Search extends AppCompatActivity {

    ListView listView;
    private FirebaseAuth mAuth;
    private ArrayList<User> friends = new ArrayList<User>();
    private DatabaseReference DBref;
    FriendAdapter adapter;
    EditText searchIput ;
    public void Search(MainActivity main){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");

        listView = (ListView) findViewById(R.id.searched_friends);
        searchIput = (EditText) findViewById(R.id.search_input) ;

        adapter = new FriendAdapter(friends , this);
        listView.setAdapter(adapter);

        //search("m");

        searchIput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("searching event");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("searching event");

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0){
                   search(searchIput.getText().toString());
                    System.out.println("searching event");
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

    public void search(String searchQuery){
        DBref.orderByChild("username")
                .startAt(searchQuery)
                .addValueEventListener(userEventListener);
        //.endAt(searchQuery+"\uf8ff")
        System.out.println("searching with "+searchQuery);
    }

    ValueEventListener userEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            HashMap<String, User> users = (HashMap<String, User>) dataSnapshot.getValue();

            ArrayList<User> results = new ArrayList<User>();

            if (users != null) {

                //------------------

                Iterator it = users.entrySet().iterator();
                while (it.hasNext()) {
                     Map.Entry pair = (Map.Entry) it.next();
                     results.add((User)pair.getValue());
                    it.remove();
                }

                //------------
                System.out.println("results is : "+ results.toString());


            } else {
                //SetViews(false , false);
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {  }
        //SetViews(false , false);
    };
}
