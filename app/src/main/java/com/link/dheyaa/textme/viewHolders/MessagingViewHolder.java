package com.link.dheyaa.textme.viewHolders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
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
import com.link.dheyaa.textme.models.Message;
import com.link.dheyaa.textme.models.User;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessagingViewHolder extends RecyclerView.ViewHolder{

    private final ImageView imageView;
    private final TextView friendName;
    private final TextView timeAgo;
    private String profileImage;

    private User friend;
    private Context context;

    public MessagingViewHolder(Context context, View itemView) {

        super(itemView);

        this.context = context;


        friendName = (TextView) itemView.findViewById(R.id.user_name);
        timeAgo = (TextView) itemView.findViewById(R.id.user_email);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);

    }

    public String getMessageData(Message message, HashMap<String, String>extraData ,boolean you , String key){
        switch (key){
            case "username":
                if (message.getSenderId ().equals (extraData.get ("currentAuthId"))) {
                    return "you";
                } else {
                    return extraData.get ("FriendName");
                }

            case "ago":


            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(message.getTime());
            String date = DateFormat.format("yyyy/MM/dd  hh:mm A", cal).toString();

            return date;


                default: return "";
        }
    }
    public void bindMessage(Message message  , HashMap<String, String>extraData , boolean you) {

        this.friendName.setText(message.getValue ());
        this.timeAgo.setText(getMessageData(message,extraData,you ,"ago"));


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();


        profileImage = "static/profile.png";
        if(you){
            profileImage =  extraData.get("currentAuthImage") != null ?  extraData.get("currentAuthImage") : "static/profile.png";
            System.out.println ("profileImage you -->>>>"+profileImage);

        }else{
            profileImage =  extraData.get("FriendImage") != null ?  extraData.get("FriendImage") : "static/profile.png";
            System.out.println ("profileImage  -->>>>"+profileImage);

        }

        System.out.println ("profileImage ");
        storageReference.child(profileImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.clear(imageView);
                Glide.with(context)
                        .load(uri)
                        .override(100, 100)
                        .centerCrop()
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("imageError ->> "+exception.toString());

            }
        });




    }
}