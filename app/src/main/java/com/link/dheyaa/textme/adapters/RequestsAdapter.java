package com.link.dheyaa.textme.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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


    private ArrayList<User> friends;
    Context mContext;
    ImageView imageView;
    private FirebaseAuth mAuth;
    private DatabaseReference DBref;

    public RequestsAdapter(ArrayList<User> friends, Context context) {
        super(context, R.layout.requests_list_item, friends);
        this.friends = friends;
        this.mContext = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.requests_list_item, parent, false);

        User currentFriend = friends.get(position);

        TextView friendName = (TextView) listItem.findViewById(R.id.user_name);
        friendName.setText(currentFriend.getUsername());

        TextView friendEmail = (TextView) listItem.findViewById(R.id.user_email);
        friendEmail.setText(currentFriend.getEmail());

        imageView = (ImageView) listItem.findViewById(R.id.imageView);

         Button requestAccept = (Button) listItem.findViewById(R.id.req_accept);
        Button requestDissime = (Button) listItem.findViewById(R.id.req_dissime);

        requestAccept.setOnClickListener(AcceptRequestAcction);
        requestDissime.setOnClickListener(dissimeRequestAcction);

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


        //Your imageView variable

        return listItem;
    }


 View.OnClickListener AcceptRequestAcction = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          View parentRow = (View) v.getParent();
          ListView listView = (ListView) parentRow.getParent();
          final int position = listView.getPositionForView(parentRow);
            String currentRequestId = friends.get(position).getId();
          DBref.child(mAuth.getCurrentUser().getUid()).child("friends").child(currentRequestId).setValue(true);
          DBref.child(currentRequestId).child("friends").child(mAuth.getCurrentUser().getUid()).setValue(true);
      }
  };

  View.OnClickListener dissimeRequestAcction = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          View parentRow = (View) v.getParent();
          ListView listView = (ListView) parentRow.getParent();
          final int position = listView.getPositionForView(parentRow);
          String currentRequestId = friends.get(position).getId();
          DBref.child(mAuth.getCurrentUser().getUid()).child("friends").child(currentRequestId).removeValue();
      }
  };


    public void removeOld(User user, ArrayList<User> Myfriends) {
        for (int i = 0; i < Myfriends.size(); i++) {
            if (Myfriends.get(i).getId().equals(user.getId())) {
                this.remove(Myfriends.get(i));
                Myfriends.remove(Myfriends.get(i));
            }
        }
    }

    public void removeAll(ArrayList<User> Myfriends) {
        for (int i = 0; i < Myfriends.size(); i++) {
            this.remove(Myfriends.get(i));
            //   Myfriends.remove(i);
        }
    }

    public void setFriends(ArrayList<User> Myfriends) {
        this.friends = Myfriends;
    }

}
