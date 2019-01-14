
/* TextMe Team
 * Jan 2019
 * Friend fragment:
 * controls the friends' listing and layout
 */

package com.link.dheyaa.textme.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.adapters.FriendAdapter;
import com.link.dheyaa.textme.itemDecorators.friendsItemDecorator;
import com.link.dheyaa.textme.models.User;

import java.util.ArrayList;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FriendsFragment extends Fragment {
    //attributes of FriendFragment
    @Nullable
    private RecyclerView listView;
    private FirebaseAuth mAuth;
    private DatabaseReference DBref;
    private FriendAdapter adapter;
    private ArrayList<User> friends = new ArrayList<User>();
    private ConstraintLayout noFriends;
    private ProgressBar loading;
    private boolean itemCLicked;
    private boolean sortingAscending;

    /**
     * create the view presenting current friends
     * @param inflater= LayoutInflater to generate layouts
     * @param container = ViewGroup of container Views
     * @param savedInstanceState = Bundle storing the current instance's state
     * @return the View presenting current friends
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.friends_tab, container, false);
        listView = (RecyclerView) root.findViewById(R.id.friends_list);
        sortingAscending = true;
        noFriends = (ConstraintLayout) root.findViewById(R.id.nofriends);
        loading = (ProgressBar) root.findViewById(R.id.progressBar);
        //generate the search button
        AppCompatButton searchBtn = (AppCompatButton) root.findViewById(R.id.search_btn);

        //set the click listener
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Search = new Intent(getContext(), com.link.dheyaa.textme.activities.Search.class);
                startActivity(Search);
            }
        });
        SetViews(false, false);

        //get the instance of the database
        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");
        DBref.child(mAuth.getCurrentUser().getUid()).child("friends").orderByValue().equalTo(1).addChildEventListener(FriendsChildEventListner);

        DBref.child(mAuth.getCurrentUser().getUid()).child("friends").orderByValue().equalTo(1).addValueEventListener (new ValueEventListener () {
            @Override
            //action after change in database occurs
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue () == null){
                    SetViews(false, false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //set the alphabetical sorting switch button
        final ToggleButton switchSorting = (ToggleButton) root.findViewById(R.id.switchSorting);
        switchSorting.setOnClickListener(new View.OnClickListener() {
            @Override
            //set the click action
            public void onClick(View v) {
                if (switchSorting.getText().equals("A-Z")) {
                    sortingAscending = true;
                    adapter.sortFriends(sortingAscending);
                } else {
                    sortingAscending = false;
                    adapter.sortFriends(sortingAscending);
                }
            }
        });

        //initiate a new friendAdapter
        adapter = new FriendAdapter(getContext(), R.layout.friends_list_item, friends);

        listView.setHasFixedSize(true);
        listView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layoutManager);

        listView.addItemDecoration(new friendsItemDecorator(0));

        //return the new view
        return root;

    }

    /**
     * action after a child's event occurs
     */
    ChildEventListener FriendsChildEventListner =  new ChildEventListener() {
        //child added event
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            System.out.println("child->>>>Added : key->>> "+dataSnapshot.getKey());
            if(dataSnapshot.getValue(Integer.class) == 1){
                addFriendData(dataSnapshot.getKey());
                SetViews(true, false);
            }else{
                adapter.removeOldByID(dataSnapshot.getKey());
                System.out.println ("child->>removeById->>1->"+dataSnapshot.getKey());

            }

        }

        //child changed event
        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            System.out.println ("child->cahnges->important");

            if(dataSnapshot.getValue(Integer.class) == 1){
                addFriendData(dataSnapshot.getKey());
                SetViews(true, false);
            }else{
                System.out.println ("child->>removeById->>2->"+dataSnapshot.getKey());
                adapter.removeOldByID(dataSnapshot.getKey());
            }
        }

        //child removed event
        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            adapter.removeOldByID(dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    /**
     * add a new friend
     * @param friendId = String value of the friend's User Id
     */
    public void addFriendData(final String friendId){
        DBref.child(friendId).orderByKey().addValueEventListener(new ValueEventListener() {
            String userId = friendId;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setId(userId);
                        user.setFriends(null);
                       // friends.add (user);
                        System.out.println("userAdded ->> user ->>" + user);

                        adapter.addFriend(user, sortingAscending);
                        adapter.notifyDataSetChanged();
                    }
                }catch (Exception err){
                    System.out.println ("err123 ->>> "+err.toString ());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                SetViews(false, false);
            }
        });
    }

    /**
     * set the view layout presenting current friends
     * @param hasFriends = boolean value of whether the user has friends
     * @param isLoading = boolean value of whether the user is loading
     */
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




}
