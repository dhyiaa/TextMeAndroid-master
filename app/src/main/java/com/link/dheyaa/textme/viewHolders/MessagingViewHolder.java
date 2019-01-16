/* TextMe Team
 * Jan 2019
 * MessagingViewHolder class:
 * this class make showing the messages possible.
 * */
package com.link.dheyaa.textme.viewHolders;

import android.content.Context;
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
import com.link.dheyaa.textme.models.Message;
import com.link.dheyaa.textme.models.User;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessagingViewHolder extends RecyclerView.ViewHolder {

    /* declaring some attributes*/
    private final ImageView imageView;
    private final TextView friendName;
    private final TextView timeAgo;
    private String profileImage;
    private User friend;
    private Context context;

    /**
     * main constructor
     *
     * @param context  : the context of the activity
     * @param itemView : the item that should be viewed
     */
    public MessagingViewHolder(Context context, View itemView) {
        super(itemView);
        /*setting thr values of some attributes by the foreign variables*/
        this.context = context;
        friendName = (TextView) itemView.findViewById(R.id.user_name);
        timeAgo = (TextView) itemView.findViewById(R.id.user_email);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }

    /**
     * getMessageData : return the most important message data to show it later
     *
     * @param message   : the message object from the server
     * @param extraData : extra user data like [id, friendId ...]
     * @param you       : whether it is your message or your friends' one.
     * @param key       : the type of data should be returned
     * @return : the ordered type of menage data
     */
    public String getMessageData(Message message, HashMap<String, String> extraData, boolean you, String key) {
        switch (key) {
            case "username":
                if (message.getSenderId().equals(extraData.get("currentAuthId"))) {
                    return "you";
                } else {
                    return extraData.get("FriendName");
                }

            case "ago":

                // parsing the time from timeStamp to form of "yyyy/MM/dd hh:mm A"
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(message.getTime());
                String date = DateFormat.format("yyyy/MM/dd  hh:mm A", cal).toString();
                return date;

            default:
                //if the ordered data is not found return blank
                return "";
        }
    }

    /**
     * this method bind the message object to the list item to be shown to the user
     *
     * @param message   : the message object
     * @param extraData : extra user data like [id, friendId ...]
     * @param you       : whether it is your message or your friends' one.
     */
    public void bindMessage(Message message, HashMap<String, String> extraData, boolean you) {

        /* setting the item data to the received ones. */
        this.friendName.setText(message.getValue());
        this.timeAgo.setText(getMessageData(message, extraData, you, "ago"));
        /* instantiating firebase storage . */
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        /* setting the default profile image path. */
        profileImage = "static/profile.png";
        if (you) {
            /* if the message owner is you put your image in it . */
            profileImage = extraData.get("currentAuthImage") != null ? extraData.get("currentAuthImage") : "static/profile.png";
        } else {
            /* else put your friend's image in it */
            profileImage = extraData.get("FriendImage") != null ? extraData.get("FriendImage") : "static/profile.png";

        }
        //download the image from firebase
        storageReference.child(profileImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //when downloading is done successfully show the image
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
                // when the image downloading field clean the image source
                Glide.clear(imageView);
            }
        });
    }

}