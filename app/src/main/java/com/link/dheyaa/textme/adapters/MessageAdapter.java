
/* TextMe Team
 * Jan 2019
 * Message adapter:
 * controls Messages' listing and layout
 */

package com.link.dheyaa.textme.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.models.Message;
import com.link.dheyaa.textme.models.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MessageAdapter extends ArrayAdapter<Message> {

    private ArrayList<Message> messages;
    private Context mContext;
    private ImageView imageView;
    private HashMap<String, String> extraData;

    private String friendProfile;
    private String authUserProfile;

    private DatabaseReference DBref;

    public MessageAdapter(ArrayList<Message> messages, Context context, HashMap<String, String> extraData) {
        super (context, R.layout.message_list_item, messages);
        this.messages = messages;
        this.mContext = context;
        this.extraData = extraData;
        this.DBref = FirebaseDatabase.getInstance ().getReference ("Users");

    }

    public String getSenderName(String senderId) {
        if (senderId.equals (extraData.get ("currentAuthId"))) {
            return "you";
        } else {
            return extraData.get ("FriendName");
        }

    }

    public String getAgo(int days, int hours, int minutes) {
        String msg = "";
        if (minutes >= 60) {
            msg = (hours >= 24) ? days + " days" : hours + " hours";
        } else {
            msg = minutes + " mins";
        }
        return (minutes == 0) ? "just now ." : msg + " ago.";
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        Message currentMessage = messages.get (position);


        View listItem = convertView;
        String profileUrl = "static/profile.png";

        if (currentMessage.getSenderId ().equals (extraData.get ("currentAuthId"))) {
            listItem = LayoutInflater.from (mContext).inflate (R.layout.message_list_item_you, parent, false);
            profileUrl = extraData.get ("currentAuthImage") != null ? extraData.get ("currentAuthImage") : profileUrl;

        } else {
            listItem = LayoutInflater.from (mContext).inflate (R.layout.message_list_item, parent, false);
            profileUrl = extraData.get ("FriendImage") != null ? extraData.get ("FriendImage") : profileUrl;

        }

        TextView friendName = (TextView) listItem.findViewById (R.id.user_name);
        friendName.setText (currentMessage.getValue ());
        //friendName.setText("message value");

        TextView friendEmail = (TextView) listItem.findViewById (R.id.user_email);

        Date now = new Date ();
        // long diff = now.getTime() - currentMessage.getTime();


        Date date = new Date ();
        java.sql.Timestamp timestamp1 = new Timestamp (date.getTime ());

        // create a calendar and assign it the same time
        Calendar cal = Calendar.getInstance ();
        cal.setTimeInMillis (timestamp1.getTime ());

        // create a  second time stamp
        Timestamp timestamp2 = new Timestamp (currentMessage.getTime ());

        // get time difference in seconds
        long milliseconds = timestamp1.getTime () - timestamp2.getTime ();
        int seconds = (int) milliseconds / 1000;

        // calculate hours minutes and seconds
        int days = seconds / (24 * 3600);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = (seconds % 3600) % 60;

        friendEmail.setText (getAgo (days, hours, minutes));

        imageView = (ImageView) listItem.findViewById (R.id.imageView);


        FirebaseStorage storage = FirebaseStorage.getInstance ();
        StorageReference storageReference = storage.getReference ();


        storageReference.child (profileUrl).getDownloadUrl ().addOnSuccessListener (new OnSuccessListener<Uri> () {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println ("profileImage " + uri);
                Glide.with (getContext ())
                        .load (uri)
                        .centerCrop ()
                        .into (imageView);
            }
        });


        return listItem;
    }


}


