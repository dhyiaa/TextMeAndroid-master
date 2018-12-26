package com.link.dheyaa.textme.adapters;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.models.User;

import java.util.ArrayList;

public class FriendAdapter extends ArrayAdapter<User> {

    private ArrayList<User> friends;
    Context mContext;
    ImageView imageView;

    public FriendAdapter(ArrayList<User> friends, Context context) {
        super(context, R.layout.friends_list_item, friends);
        this.friends = friends;
        this.mContext = context;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.friends_list_item, parent, false);

        User currentFriend = friends.get(position);

        TextView friendName = (TextView) listItem.findViewById(R.id.user_name);
        friendName.setText(currentFriend.getUsername());

        TextView friendEmail = (TextView) listItem.findViewById(R.id.user_email);
        friendEmail.setText(currentFriend.getEmail());

         imageView = (ImageView) listItem.findViewById(R.id.imageView);
        //imageView.setImageBitmap();


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
