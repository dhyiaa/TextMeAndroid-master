package com.link.dheyaa.textme.models;

import java.util.ArrayList;

public class Room {
    private String roomId;
    private ArrayList<Message> messages;

    public Room(String roomId, ArrayList<Message> messages) {
        this.roomId = roomId;
        this.messages = messages;
    }

    public Room() {
    }

    public String getRoomId() {
        return roomId;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", messages=" + messages +
                '}';
    }
}
