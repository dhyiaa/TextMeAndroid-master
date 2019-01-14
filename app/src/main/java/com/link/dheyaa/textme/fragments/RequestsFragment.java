
/* TextMe Team
 * Jan 2019
 * Request fragment:
 * controls the request' listing and layout
 */

package com.link.dheyaa.textme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.link.dheyaa.textme.R;

import androidx.annotation.Nullable;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.link.dheyaa.textme.adapters.RequestsAdapter;
import com.link.dheyaa.textme.utils.Sorting;
import com.link.dheyaa.textme.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


public class RequestsFragment extends Fragment {
    //attributes of RequestFragment
    @Nullable
    ListView listView;
    private FirebaseAuth mAuth;
    private DatabaseReference DBref;
    private RequestsAdapter adapter;
    private ArrayList<User> requests = new ArrayList<User>();
    private ConstraintLayout noFriends;
    private ProgressBar loading;
    private boolean itemCLicked;
   // private Button requestAccept;
   // private Button requestDissime;

    /**
     * create the view presenting current requests
     * @param inflater= LayoutInflater to generate layouts
     * @param container = ViewGroup of container Views
     * @param savedInstanceState = Bundle storing the current instance's state
     * @return the View presenting current requests
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_requests, container, false);
        listView = (ListView) root.findViewById(R.id.friends_list);

        noFriends = (ConstraintLayout) root.findViewById(R.id.nofriends);

       // requestAccept = (Button) root.findViewById(R.id.req_accept);
        //requestDissime = (Button) root.findViewById(R.id.req_dissime);

        //requestAccept.setOnClickListener(AcceptRequestAcction);
       // requestDissime.setOnClickListener(dissimeRequestAcction);

        //get the instance of the database
        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");
        DBref.child(mAuth.getCurrentUser().getUid()).child("friends").orderByValue().equalTo(0).addValueEventListener(userEventListener);

        //instantiate a new requestAdapter
        this.adapter = new RequestsAdapter(new ArrayList(), getContext());
        listView.setAdapter(this.adapter);

        loading = (ProgressBar) root.findViewById(R.id.progressBar);
        SetViews(false, true);

        //return the new view
        return root;

    }
  /*
  *   View.OnClickListener AcceptRequestAcction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DBref.child(mAuth.getCurrentUser().getUid()).child("friends").setValue("");
        }
    };

    View.OnClickListener dissimeRequestAcction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
  * */

    /**
     * set the view layout presenting current requests
     * @param hasFriends = boolean value of whether the user has requests
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

    /**
     * action after a user's event occurs
     */
    ValueEventListener userEventListener = new ValueEventListener() {
        //data changed event
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            HashMap<String, Integer> friendIds = (HashMap<String, Integer>) dataSnapshot.getValue();
            if (friendIds != null) {
                //if the user has friends
                SetViews(true, false);

                Iterator it = friendIds.entrySet().iterator();
                while (it.hasNext()) {
                    final Map.Entry pair = (Map.Entry) it.next();

                    DBref.child(pair.getKey().toString()).orderByKey().addValueEventListener(new ValueEventListener() {
                        String userId = pair.getKey().toString();
                        //data changed event
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                //if the user exist
                                user.setId(userId);
                                user.setFriends(null);
                                adapter.removeOld(user, requests);
                                requests.add(user);
                                adapter.removeAll(requests);
                                Sorting.quickSortByAlphabet(requests , true);
                                adapter.addAll(requests);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        //cancelled event
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
        //cancelled event
        @Override
        public void onCancelled(DatabaseError error) {
            SetViews(false, false);
        }
    };


}

