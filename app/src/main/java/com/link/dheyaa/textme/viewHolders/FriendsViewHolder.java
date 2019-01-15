/* TextMe Team
 * Jan 2019
 * FriendsViewHolder class:
 * this class make showing the items possible.
 * */
package com.link.dheyaa.textme.viewHolders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.activities.MessagingPage;
import com.link.dheyaa.textme.models.User;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /* declaring some attributes*/
    private final ImageView profileImage;
    private final TextView email;
    private final TextView username;
    private User friend;
    private Context context;

    /**
     * main constructor which take some important info
     *
     * @param context  : the context of the activity
     * @param itemView : the item that should be viewed
     */
    public FriendsViewHolder(Context context, View itemView) {
        super(itemView);
        /** setting thr values of some attributes by the foreign variables*/
        this.context = context;
        this.profileImage = (ImageView) itemView.findViewById(R.id.imageView);
        this.email = (TextView) itemView.findViewById(R.id.user_email);
        this.username = (TextView) itemView.findViewById(R.id.user_name);
        // setting the onClick event listener on the item that shown
        itemView.setOnClickListener(this);
    }

    public void bindFriend(User friend) {
        //setting the friend data
        this.friend = friend;
        // setting the field values by the friend data
        this.email.setText(friend.getEmail());
        this.username.setText(friend.getUsername());

        //instantiating the FirebaseStorage class
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        // getting the profile image url
        String profileImageUrl = friend.getImagePath() != null ? friend.getImagePath() : "static/profile.png";

        //download the image from firebase
        storageReference.child(profileImageUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //wehen downloading is done seccessfully show the image
                Glide.clear(profileImage);
                Glide.with(context)
                        .load(uri) // the uri you got from Firebase
                        .override(150, 150)
                        .centerCrop()
                        .into(profileImage);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // when the image downloading field clean the image source
                Glide.clear(profileImage);

            }

        });


    }

    @Override
    public void onClick(View v) {
        if (this.friend != null) {
            Intent Message = new Intent(v.getContext(), MessagingPage.class);
            Message.putExtra("friend_name", friend.getUsername());
            Message.putExtra("friend_id", friend.getId());
            Message.putExtra("friend_image", friend.getImagePath());
            context.startActivity(Message);
        }
    }
}