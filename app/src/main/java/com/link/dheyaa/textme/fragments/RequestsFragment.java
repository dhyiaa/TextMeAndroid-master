package com.link.dheyaa.textme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.link.dheyaa.textme.R;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.link.dheyaa.textme.activities.MessagingPage;
import com.link.dheyaa.textme.adapters.FriendAdapter;
import com.link.dheyaa.textme.adapters.RequestsAdapter;
import com.link.dheyaa.textme.utils.Sorting;
import com.link.dheyaa.textme.models.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class RequestsFragment extends android.support.v4.app.Fragment {

    @Nullable
    ListView listView;
    private FirebaseAuth mAuth;
    private DatabaseReference DBref;
    private RequestsAdapter adapter;
    private ArrayList<User> requests = new ArrayList<User>();
    private android.support.constraint.ConstraintLayout noFriends;
    private ProgressBar loading;
    private boolean itemCLicked;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_requests, container, false);
        listView = (ListView) root.findViewById(R.id.friends_list);

        noFriends = (android.support.constraint.ConstraintLayout) root.findViewById(R.id.nofriends);

        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");
        DBref.child(mAuth.getCurrentUser().getUid()).child("friends").orderByValue().equalTo(false).addValueEventListener(userEventListener);


        this.adapter = new RequestsAdapter(new ArrayList(), getContext());
        listView.setAdapter(this.adapter);

        loading = (ProgressBar) root.findViewById(R.id.progressBar);
        //listView.setOnItemClickListener(itemClicked);
        SetViews(false, true);
        Button searchBtn = root.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Search = new Intent(getContext(), com.link.dheyaa.textme.activities.Search.class);
                startActivity(Search);
            }
        });
        itemCLicked = false;

        //return the view from the fragment
        return root;

    }


/*
*     AdapterView.OnItemClickListener itemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //  listView.setClickable(false);
            //  if (itemCLicked == false) {
            Intent Message = new Intent(getActivity(), MessagingPage.class);
            Message.putExtra("friend_name", requests.get(i).getUsername());
            Message.putExtra("friend_id", requests.get(i).getId());
            startActivity(Message);
            listView.setClickable(true);
            itemCLicked = true;
            // }

        }
    };
* */

    public void SetViews(boolean hasFreiends, boolean isLoading) {
        if (isLoading) {
            listView.setVisibility(View.INVISIBLE);
            noFriends.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(hasFreiends ? View.VISIBLE : View.INVISIBLE);
            noFriends.setVisibility(hasFreiends ? View.INVISIBLE : View.VISIBLE);
            loading.setVisibility(View.INVISIBLE);
        }
    }

    ValueEventListener userEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            HashMap<String, Boolean> friendIds = (HashMap<String, Boolean>) dataSnapshot.getValue();
            if (friendIds != null) {
                SetViews(true, false);

                Iterator it = friendIds.entrySet().iterator();
                while (it.hasNext()) {
                    final Map.Entry pair = (Map.Entry) it.next();

                    DBref.child(pair.getKey().toString()).orderByKey().addValueEventListener(new ValueEventListener() {
                        String userId = pair.getKey().toString();

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                user.setId(userId);
                                user.setFriends(null);

                                adapter.removeOld(user, requests);
                                requests.add(user);

                                adapter.clear();
                                adapter.removeAll(requests);

                                Sorting.quickSortByAlphabet(requests);
                                adapter.addAll(requests);

                                //  System.out.println(friends.toString());

                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            SetViews(false, false);
                        }
                    });
                    it.remove();
                }
            } else {
                SetViews(false, false);
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            SetViews(false, false);
        }
    };


}

