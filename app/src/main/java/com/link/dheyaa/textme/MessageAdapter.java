package com.link.dheyaa.textme;

import android.content.Context;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {
    private ArrayList<Message> messages;
    Context mContext;
    public MessageAdapter(ArrayList<Message> messages, Context context) {
        super(context,R.layout.activity_messaging_page, messages);
        this.messages = messages;
        this.mContext = context;

    }

}
