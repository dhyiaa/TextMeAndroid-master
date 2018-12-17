 package com.link.dheyaa.textme;

import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

 public class FriendAdapter extends ArrayAdapter<User>{

     private ArrayList<User> friends;
     Context mContext;

     public FriendAdapter(ArrayList<User> friends, Context context) {
         super(context, R.layout.friends_list_item, friends);
         this.friends = friends;
         this.mContext=context;
     }


     @Override
     public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         View listItem = convertView;
         if(listItem == null)
             listItem = LayoutInflater.from(mContext).inflate(R.layout.friends_list_item,parent,false);

         User currentFriend = friends.get(position);

         TextView friendName = (TextView) listItem.findViewById(R.id.user_name);
         friendName.setText(currentFriend.getUsername());

         TextView friendEmail = (TextView) listItem.findViewById(R.id.user_email);
         friendEmail.setText(currentFriend.getEmail());

         return listItem;
     }
     public void removeOld(User user , ArrayList<User> Myfriends){
         for(int i = 0 ; i < Myfriends.size() ; i++){
            if( Myfriends.get(i).getId().equals(user.getId()) ){
                this.remove(Myfriends.get(i));
                Myfriends.remove(Myfriends.get(i));
            }
         }
     }
     public void removeAll( ArrayList<User> Myfriends){
         for(int i = 0 ; i < Myfriends.size() ; i++ ){
             this.remove(Myfriends.get(i));
         }
     }
     public void setFriends( ArrayList<User> Myfriends){
         this.friends = Myfriends;
     }

 }
