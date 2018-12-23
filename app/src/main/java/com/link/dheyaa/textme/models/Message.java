package com.link.dheyaa.textme.models;

import java.util.Objects;

public class Message {
    private String roomId;
    private String reciverId;
    private String senderId;
    private long time;
    private String value;

    public Message() {
    }

    public Message(String roomId, String reciverId, String senderId, long time, String value) {
        this.roomId = roomId;
        this.reciverId = reciverId;
        this.senderId = senderId;
        this.time = time;
        this.value = value;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getReciverId() {
        return reciverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public long getTime() {
        return time;
    }

    public String getValue() {
        return value;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return time == message.time &&
                roomId.equals(message.roomId) &&
                reciverId.equals(message.reciverId) &&
                value.equals(message.value) &&
                senderId.equals(message.senderId);
    }

}
