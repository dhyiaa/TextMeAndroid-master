package com.link.dheyaa.textme.adapters;

import android.content.Context;
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
import com.link.dheyaa.textme.models.Message;
import com.link.dheyaa.textme.models.User;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageAdapter extends ArrayAdapter<Message> {

    private ArrayList<Message> messages;
    Context mContext;
    ImageView imageView;
    HashMap<String , String> extraData;

    public MessageAdapter(ArrayList<Message> messages, Context context ,     HashMap<String , String>  extraData) {
        super(context, R.layout.message_list_item, messages);
        this.messages = messages;
        this.mContext = context;
        this.extraData = extraData;
    }
    public String getSenderName( String senderId){
        if (senderId.equals(extraData.get("currentAuthId")) ){
            return "you";
        }else {
            return extraData.get("FriendName");
        }

    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        Message currentMessage = messages.get(position);


        View listItem = convertView;

        if (currentMessage.getSenderId().equals(extraData.get("currentAuthId")) ){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.message_list_item_you, parent, false);
        }
        else {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.message_list_item, parent, false);
        }
        TextView friendName = (TextView) listItem.findViewById(R.id.user_name);
        friendName.setText(currentMessage.getValue());
        //friendName.setText("message value");

        TextView friendEmail = (TextView) listItem.findViewById(R.id.user_email);
        friendEmail.setText(getSenderName(currentMessage.getSenderId()));
       // friendEmail.setText("time");

       //  imageView = (ImageView) listItem.findViewById(R.id.imageView);
        //imageView.setImageBitmap();


        /*
        *
        *
        * FirebaseStorage storage = FirebaseStorage.getInstance();
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
        *
        * */


       //Your imageView variable

        return listItem;
    }

/*
*
*
*     public void addIfNotExist(Message msg){
        boolean found = false;
        for(int i = 0 ; i< this.messages.size() ; i++){
            if(this.messages.get(i).equals(msg)){
                found =true;
                break;
            }
        }

        if(!found){
            this.add(msg);
            this.notifyDataSetChanged();
            this.messages.add(msg);
            System.out.println("called add"+msg.toString());
            System.out.println(this.messages.toString());

        }


    }
* */



}
