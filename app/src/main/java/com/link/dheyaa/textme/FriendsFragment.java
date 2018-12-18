package com.link.dheyaa.textme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
                Intent Search = new Intent(getContext(), Search.class);
                startActivity(Search);
            }
        });
        itemCLicked = false;

        //return the view from the fragment
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
            //  listView.setClickable(false);
            if (itemCLicked == false) {
                Intent Message = new Intent(getActivity(), MessagingPage.class);
                Message.putExtra("Friend_name", friends.get(i).getUsername());
                startActivity(Message);
                listView.setClickable(true);
                itemCLicked = true;
            }

        }
    };

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

                                adapter.removeOld(user, friends);
                                friends.add(user);

                                adapter.clear();
                                adapter.removeAll(friends);

                                Sorting.quickSortByAlphabet(friends);
                                adapter.addAll(friends);

                                System.out.println(friends.toString());

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
