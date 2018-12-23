package com.link.dheyaa.textme.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.models.Message;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {

    private ArrayList<Message> messages;
    Context mContext;

    public MessageAdapter(ArrayList<Message> messages, Context context) {

        super(context,R.layout.activity_messaging_page, messages);
        this.messages = messages;
        this.mContext = context;

    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.activity_messaging_page, parent, false);

        Message currentMessage = messages.get(position);

        //TextView messageValue = listItem.findViewById(R.id.?);
        //messageValue.setText(currentMessage.getValue());

        //TextView messageTime = listItem.findViewById(R.id.?);
        //friendEmail.setText(currentMessage.getTime());

        return listItem;
    }

    public void removeAll(ArrayList<Message> messages) {
        for (int i = 0; i < messages.size(); i++) {
            this.remove(messages.get(i));
        }
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

}
