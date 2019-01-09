
/* TextMe Team
 * Jan 2019
 * Room class:
 * containing the attributes and functions of Messaging Room
 */

package com.link.dheyaa.textme.models;

import java.util.HashMap;

public class Room {
    //attributes of Rooms
    private String roomId;
    private HashMap<String , Message> values;

    /**
     * default constructor
     */
    public Room() {
    }

    /**
     * secondary constructor
     * @param roomId = String value of the room's Id
     * @param values = HashMap list of the room's Messages
     */
    public Room(String roomId, HashMap<String, Message> values) {
        this.roomId = roomId;
        this.values = values;
    }

    /**
     * set room's Id
     * @param roomId = String value of the room's Id
     */
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    /**
     * get the room's Id
     * @return the String value of the room's Id
     */
    public String getRoomId() {
        return roomId;
    }

    /**
     * set room's Messages
     * @param values = HashMap list of the room's Messages
     */
    public void setValues(HashMap<String, Message> values) {
        this.values = values;
    }

    /**
     * get the room's Messages
     * @return the HashMap list of the room's Messages
     */
    public HashMap<String, Message> getValues() {
        return values;
    }

    /**
     * Builds a String representation of the object for debugging/testing purposes
     * @return a String containing the state of the room
     */
    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", values=" + values +
                '}';
    }
}
