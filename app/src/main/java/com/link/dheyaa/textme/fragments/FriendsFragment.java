package com.link.dheyaa.textme.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.link.dheyaa.textme.activities.MessagingPage;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.adapters.FriendAdapter;
import com.link.dheyaa.textme.utils.Sorting;
import com.link.dheyaa.textme.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class FriendsFragment extends android.support.v4.app.Fragment {

    @Nullable
    ListView listView;
    private FirebaseAuth mAuth;
    private DatabaseReference DBref;
    private FriendAdapter adapter;
    private ArrayList<User> friends = new ArrayList<User>();
    private android.support.constraint.ConstraintLayout noFriends;
    private ProgressBar loading;
    private boolean itemCLicked;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.friends_tab, container, false);
        listView = (ListView) root.findViewById(R.id.friends_list);

        noFriends = (android.support.constraint.ConstraintLayout) root.findViewById(R.id.nofriends);

        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");
        DBref.child(mAuth.getCurrentUser().getUid()).child("friends").orderByValue().equalTo(true).addValueEventListener(userEventListener);

        this.adapter = new FriendAdapter(new ArrayList(), getContext());
        listView.setAdapter(this.adapter);

        loading = (ProgressBar) root.findViewById(R.id.progressBar);
        listView.setOnItemClickListener(itemClicked);
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
        return root;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        itemCLicked = false;

    }

    AdapterView.OnItemClickListener itemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent Message = new Intent(getActivity(), MessagingPage.class);
            Message.putExtra("friend_name", friends.get(i).getUsername());
            Message.putExtra("friend_id", friends.get(i).getId());
            startActivity(Message);
            listView.setClickable(true);
            itemCLicked = true;

        }
    };

    public void SetViews(boolean hasFriends, boolean isLoading) {
        if (isLoading) {
            listView.setVisibility(View.INVISIBLE);
            noFriends.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(hasFriends ? View.VISIBLE : View.INVISIBLE);
            noFriends.setVisibility(hasFriends ? View.INVISIBLE : View.VISIBLE);
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
                                adapter.removeOld(user, friends);
                                friends.add(user);

                                adapter.clear();
                                adapter.removeAll(friends);
                                Sorting.quickSortByAlphabet(friends);
                                adapter.addAll(friends);
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
