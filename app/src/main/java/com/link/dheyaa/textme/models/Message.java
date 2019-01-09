
/* TextMe Team
 * Jan 2019
 * Message class:
 * containing the attributes and functions of Message
 */

package com.link.dheyaa.textme.models;

import java.util.Objects;

public class Message {
    //attributes of Messages
    private String roomId;
    private String receiverId;
    private String senderId;
    private long time;
    private String value;

    /**
     * default constructor
     */
    public Message() {
    }

    /**
     * secondary constructor
     * @param roomId = String value of the messaging Room's Room Id
     * @param receiverId = String value of the receiver's User Id
     * @param senderId = String value of the sender's User Id
     * @param time = long value of the message's sending time
     * @param value = String value of the message's content
     */
    public Message(String roomId, String receiverId, String senderId, long time, String value) {
        this.roomId = roomId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.time = time;
        this.value = value;
    }

    /**
     * get the room Id
     * @return the String value of the room Id
     */
    public String getRoomId() {
        return roomId;
    }

    /**
     * get the receiver's User Id
     * @return the String value of the receiver's User Id
     */
    public String getReceiverId() {
        return receiverId;
    }

    /**
     * get the sender's User Id
     * @return the String value of the sender's User Id
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * get the sending time of the message
     * @return the long value of message's sending time
     */
    public long getTime() {
        return time;
    }

    /**
     * get the content of the message
     * @return the String value of the message's content
     */
    public String getValue() {
        return value;
    }

    /**
     * set room Id
     * @param roomId = String value of the room Id
     */
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    /**
     * set receiver's User Id
     * @param receiverId = String value of the receiver's User Id
     */
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * set sender's User Id
     * @param senderId = String value of the sender's User Id
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    /**
     * set message's sending time
     * @param time = long value of the message's sending time
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * set message's content
     * @param value = String value of the message's content
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * check if this message is the same as Object o
     * @param o = the object used to compare with this message
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return time == message.time &&
                roomId.equals(message.roomId) &&
                receiverId.equals(message.receiverId) &&
                value.equals(message.value) &&
                senderId.equals(message.senderId);
    }

    /**
     * Builds a String representation of the object for debugging/testing purposes
     * @return a String containing the state of the message
     */
    @Override
    public String toString() {
        return "Message{" +
                "roomId='" + roomId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", time=" + time +
                ", value='" + value + '\'' +
                '}';
    }
}
