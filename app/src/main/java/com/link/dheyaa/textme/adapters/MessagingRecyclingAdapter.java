package com.link.dheyaa.textme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.models.Message;
import com.link.dheyaa.textme.models.User;
import com.link.dheyaa.textme.utils.Sorting;
import com.link.dheyaa.textme.viewHolders.FriendsViewHolder;
import com.link.dheyaa.textme.viewHolders.MessagingViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessagingRecyclingAdapter extends RecyclerView.Adapter<MessagingViewHolder> {

    private Context context;
    private int itemResource;

    private ArrayList<Message> messages;
    private Context mContext;
    private ImageView imageView;
    private HashMap<String, String> extraData;
    private String friendProfile;
    private String authUserProfile;
    private DatabaseReference DBref;
    private boolean you;

    public MessagingRecyclingAdapter(Context context, int itemResource, ArrayList<Message> messages, HashMap<String, String> extraData) {

        this.messages = messages;
        this.context = context;
        this.itemResource = itemResource;
        this.extraData = extraData;

    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get (position).getSenderId ().equals (extraData.get ("currentAuthId"))) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public MessagingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.message_list_item_you, parent, false);
        switch (viewType) {
            case 0:
                view = LayoutInflater.from (parent.getContext ())
                        .inflate (R.layout.message_list_item_you, parent, false);
                you = true;
                break;
            case 1:
                view = LayoutInflater.from (parent.getContext ())
                        .inflate (R.layout.message_list_item, parent, false);
                you = false;
                break;
        }
        return new MessagingViewHolder (this.context, view);
    }


    @Override
    public void onBindViewHolder(MessagingViewHolder holder, int position) {

        Message message = this.messages.get (position);
        if (message != null) {
            holder.bindMessage (message,extraData,you);
        }
    }

    public void addMessage(Message message, RecyclerView.LayoutManager layoutManager) {
        removeOld (message);
        messages.add (message);
        layoutManager.scrollToPosition(messages.size () - 1);

        notifyItemInserted (messages.size () - 1);
    }

    public void removeOld(Message message) {
        for (int i = 0; i < messages.size (); i++) {
            if (messages.get (i).getTime () == message.getTime ()) {
                messages.remove (i);
                break;
            }
        }
    }


    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemCount() {
        return this.messages.size ();
    }
}

