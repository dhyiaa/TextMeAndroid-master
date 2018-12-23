package com.link.dheyaa.textme.models;

import java.util.HashMap;

public class Room {
    private String roomId;
    private HashMap<String , Message> values;

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setValues(HashMap<String, Message> values) {
        this.values = values;
    }

    public String getRoomId() {
        return roomId;
    }

    public HashMap<String, Message> getValues() {
        return values;
    }

    public Room(String roomId, HashMap<String, Message> values) {
        this.roomId = roomId;
        this.values = values;
    }

    public Room() {
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", values=" + values +
                '}';
    }
}
