
/* TextMe Team
 * Jan 2019
 * Request adapter:
 * controls the friend requests' listing and layout
 */

package com.link.dheyaa.textme.adapters;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.models.User;

import java.util.ArrayList;

public class RequestsAdapter extends ArrayAdapter<User> {
    //attributes of RequestsAdapter
    private ArrayList<User> friends;
    Context mContext;
    ImageView imageView;
    private FirebaseAuth mAuth;
    private DatabaseReference DBref;

    /**
     * primary constructor
     * @param friends = ArrayList<User> of friends
     * @param context = context value of the activities
     */
    public RequestsAdapter(ArrayList<User> friends, Context context) {
        super(context, R.layout.requests_list_item, friends);
        this.friends = friends;
        this.mContext = context;
    }

    /**
     * get the View at index: position
     * @param position = in value of request's position
     * @param convertView = converting View
     * @param parent = ViewGroup of parent Views
     * @return the View presenting current requests
     */
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            ///if there are no requests
            listItem = LayoutInflater.from(mContext).inflate(R.layout.requests_list_item, parent, false);

        User currentFriend = friends.get(position);

        //generate the TextView presenting applicant's name
        TextView applicantName = (TextView) listItem.findViewById(R.id.user_name);
        applicantName.setText(currentFriend.getUsername());

        //generate the TextView presenting applicant's email
        TextView applicantEmail = (TextView) listItem.findViewById(R.id.user_email);
        applicantEmail.setText(currentFriend.getEmail());

        //generate the ImageView presenting applicant's profile image
        imageView = (ImageView) listItem.findViewById(R.id.imageView);

        //generate the accept and dismiss buttons
        Button requestAccept = (Button) listItem.findViewById(R.id.req_accept);
        Button requestDismiss = (Button) listItem.findViewById(R.id.req_dissime);

        //initialize the button action listeners
        requestAccept.setOnClickListener(AcceptRequestAction);
        requestDismiss.setOnClickListener(dismissRequestAction);

        //get the instance of the database
        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        String urlUser = currentFriend.getImagePath() != null ? currentFriend.getImagePath() : "static/profile.png";

        storageReference.child(urlUser).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext())
                        .load(uri) // the uri you got from Firebase
                        .centerCrop()
                        .into(imageView);            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        //return the new View
        return listItem;
    }

    /**
     * action after accept button is clicked
     */
 View.OnClickListener AcceptRequestAction = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          View parentRow = (View) v.getParent().getParent().getParent().getParent();
          ListView listView = (ListView) parentRow.getParent();
          final int position = listView.getPositionForView(parentRow);
            String currentRequestId = friends.get(position).getId();
          DBref.child(mAuth.getCurrentUser().getUid()).child("friends").child(currentRequestId).setValue(1);
          DBref.child(currentRequestId).child("friends").child(mAuth.getCurrentUser().getUid()).setValue(1);
      }
  };

    /**
     * action after dismiss button is clicked
     */
  View.OnClickListener dismissRequestAction = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          View parentRow = (View) v.getParent().getParent().getParent().getParent();
          ListView listView = (ListView) parentRow.getParent();
          final int position = listView.getPositionForView(parentRow);
          String currentRequestId = friends.get(position).getId();
          DBref.child(mAuth.getCurrentUser().getUid()).child("friends").child(currentRequestId).removeValue();
      }
  };

    /**
     * remove the User friend from the ArrayList<User> friends
     * @param friend = User to be removed from friends
     * @param myFriends = ArrayList<User> of friends
     */
    public void removeOld(User user, ArrayList<User> myFriends) {
        for (int i = 0; i < myFriends.size(); i++) {
            if (myFriends.get(i).getId().equals(user.getId())) {
                this.remove(myFriends.get(i));
                myFriends.remove(myFriends.get(i));
            }
        }
    }

    /**
     * remove all of the Users from the ArrayList<User> myFriends
     * @param myFriends = ArrayList<User> of friends
     */
    public void removeAll(ArrayList<User> myFriends) {
        for (int i = 0; i < myFriends.size(); i++) {
            this.remove(myFriends.get(i));
            //   myFriends.remove(i);
        }
    }

    /**
     * set list of friends
     * @param myFriends = ArrayList<User> of friends
     */
    public void setFriends(ArrayList<User> myFriends) {
        this.friends = myFriends;
    }

}
