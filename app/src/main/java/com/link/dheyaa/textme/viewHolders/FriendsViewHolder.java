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

    private final ImageView profileImage;
    private final TextView email;
    private final TextView username;

    private User friend;
    private Context context;

    public FriendsViewHolder(Context context, View itemView) {

        super(itemView);

        this.context = context;

        this.profileImage = (ImageView) itemView.findViewById(R.id.imageView);
        this.email = (TextView) itemView.findViewById(R.id.user_email);
        this.username = (TextView) itemView.findViewById(R.id.user_name);

        itemView.setOnClickListener(this);
    }

    public void bindFriend(User friend ) {
        this.friend = friend;
        this.email.setText(friend.getEmail());
        this.username.setText(friend.getUsername());



        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        String profileImageUrl = friend.getImagePath() != null ? friend.getImagePath() : "static/profile.png";

        storageReference.child(profileImageUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.clear(profileImage);
                Glide.with(context)
                        .load(uri) // the uri you got from Firebase
                        .override(150, 150)
                        .centerCrop()
                        .into(profileImage);

            //    System.out.println("user ->> "+friend.getUsername()+" ->> image uri ->> "+uri.toString());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("imageError ->> "+exception.toString());

            }
        });


    }

    @Override
    public void onClick(View v) {
        if (this.friend != null) {
            Intent Message = new Intent(v.getContext(), MessagingPage.class);
            Message.putExtra("friend_name", friend.getUsername());
            Message.putExtra("friend_id", friend.getId());
            context.startActivity(Message);
        }
    }
}