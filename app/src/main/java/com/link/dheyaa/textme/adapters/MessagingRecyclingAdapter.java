
/* TextMe Team
 * Jan 2019
 * Message adapter:
 * controls Messages' listing and layout
 */

package com.link.dheyaa.textme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.firebase.database.DatabaseReference;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.models.Message;
import com.link.dheyaa.textme.viewHolders.MessagingViewHolder;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessagingRecyclingAdapter extends RecyclerView.Adapter<MessagingViewHolder> {
    //attributes of MessagingRecyclingAdapter
    private Context context;
    private int itemResource;

    public ArrayList<Message> messages;
    private Context mContext;
    private ImageView imageView;
    private HashMap<String, String> extraData;
    private String friendProfile;
    private String authUserProfile;
    private DatabaseReference DBref;
    private boolean you;

    /**
     * primary constructor
     * @param context = context value of the activities
     * @param itemResource = int value of the layouts
     * @param messages = ArrayList<Message> of Messages
     * @param extraData = HashMap list of Message's extra information
     */
    public MessagingRecyclingAdapter(Context context, int itemResource, ArrayList<Message> messages, HashMap<String, String> extraData) {

        this.messages = messages;
        this.context = context;
        this.itemResource = itemResource;
        this.extraData = extraData;

    }

    /**
     * get the View's type at index: position
     * @param position = int value of the message's index
     * @return 0 if the message is sent by the User, and 1 if by the friend
     */
    @Override
    public int getItemViewType(int position) {
        if (messages.get (position).getSenderId ().equals (extraData.get ("currentAuthId"))) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Create a new MessagingViewHolder
     * Override the onCreateViewHolder method
     * @param parent = ViewGroup of parent Views
     * @param viewType = int value of the view type
     * @return a new MessagingViewHolder based on the MessagingRecyclingAdapter
     */
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

    /**
     * bind the position-th Message to the MessagingViewHolder
     * Override the onBindViewHolder method
     * @param holder = MessagingViewHolder to bind the Message
     * @param position = int value of the position index
     */
    @Override
    public void onBindViewHolder(MessagingViewHolder holder, int position) {

        Message message = this.messages.get (position);
        if (message != null) {
            holder.bindMessage (message,extraData,you);
        }
    }

    /**
     * add a new friend to the the ArrayList<Message> messages
     * @param message = Message to be added
     * @param layoutManager = LayoutManager to control the scrolling position
     */
    public void addMessage(Message message, RecyclerView.LayoutManager layoutManager) {
        removeOld (message);
        messages.add (message);
        layoutManager.scrollToPosition(messages.size () - 1);

        notifyItemInserted (messages.size () - 1);
    }

    /**
     * remove the Message from the ArrayList<Message> messages
     * @param message = Message to be removed
     */
    public void removeOld(Message message) {
        for (int i = 0; i < messages.size (); i++) {
            if (messages.get (i).getTime () == message.getTime ()) {
                messages.remove (i);
                break;
            }
        }
    }

    /**
     * set list of Messages
     * @param messages = ArrayList<Message> of Messages
     */
    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    /**
     * get the number of Message
     * @return the size of ArrayList<Message> messages
     */
    @Override
    public int getItemCount() {
        return this.messages.size ();
    }
}

