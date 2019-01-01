package com.link.dheyaa.textme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.models.User;
import com.link.dheyaa.textme.utils.Sorting;
import com.link.dheyaa.textme.viewHolders.FriendsViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class FriendAdapter extends RecyclerView.Adapter<FriendsViewHolder> {

    private final  ArrayList<User>  friends;
    private Context context;
    private int itemResource;

    public FriendAdapter(Context context, int itemResource, ArrayList<User> friends) {

        // 1. Initialize our adapter
        this.friends = friends;
        this.context = context;
        this.itemResource = itemResource;
    }

    // 2. Override the onCreateViewHolder method
    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 3. Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_list_item, parent, false);
        return new FriendsViewHolder(this.context, view);
    }

    // 4. Override the onBindViewHolder method
    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {

        // 5. Use position to access the correct Bakery object
        User friend = this.friends.get(position);
        if(friend != null){
            holder.bindFriend(friend);

        }
        // 6. Bind the bakery object to the holder
    }

    public void addFreind(User friend , boolean sortingAccending){
        removeOld(friend);
        friends.add(friend);
        if(sortingAccending ){
            Sorting.quickSortByAlphabet(friends);
        }
        notifyItemInserted(friends.size() - 1 );
    }
    public void removeOld(User friend){
        for(int i = 0 ; i< friends.size() ; i++){
            if(friends.get(i).getId().equals(friend.getId())){
                friends.remove(i);
                break;
            }
        }
    }
    @Override
    public int getItemCount() {
        return this.friends.size();
    }
}



/*
* package com.link.dheyaa.textme.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.models.User;
import com.squareup.picasso.Picasso;

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
        listItem = LayoutInflater.from(mContext).inflate(R.layout.friends_list_item, parent, false);

        final User currentFriend = friends.get(position);

        TextView friendName = (TextView) listItem.findViewById(R.id.user_name);
        friendName.setText(currentFriend.getUsername());

         TextView friendEmail = (TextView) listItem.findViewById(R.id.user_email);
        friendEmail.setText(currentFriend.getEmail());

         imageView = (ImageView) listItem.findViewById(R.id.imageView);
        //imageView.setImageBitmap();
        Glide.clear(imageView);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        String urlUser = currentFriend.getImagePath() != null ? currentFriend.getImagePath() : "static/profile.png";

      //  System.out.println("the user-------  "+currentFriend.getUsername()+"  ----------has the image url of"+urlUser);

        storageReference.child(urlUser).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.clear(imageView);
                Glide.with(getContext())
                        .load(uri) // the uri you got from Firebase
                        .override(100, 100)
                        .centerCrop()
                        .into(imageView);

                System.out.println("user ->> "+currentFriend.getUsername()+" ->> image uri ->> "+uri.toString());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("imageError ->> "+exception.toString());

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

*
* */