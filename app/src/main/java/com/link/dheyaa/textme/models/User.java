
/* TextMe Team
 * Jan 2019
 * User class:
 * containing the attributes and functions of User
 */

package com.link.dheyaa.textme.models;

import java.util.HashMap;

public class User {
    //attributes of User
    private String username;
    private String password;
    private String email;
    private HashMap<String, Integer> friends;
    private String Id;
    private String imagePath;
    private String registrationToken;

    /**
     * default constructor
     */
    public User() {
    }

    /**
     * secondary constructor
     * @param username = String value the user's user name
     * @param email = String value of the user's email
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * tertiary constructor
     * @param username = String value the user's user name
     * @param email = String value of the user's email
     * @param friends = HashMap list of the user's friends
     */
    public User(String username, String email, HashMap<String, Integer> friends) {
        this.username = username;
        this.email = email;
        this.friends = friends;
    }

    /**
     * quaternary constructor
     * @param username = String value the user's user name
     * @param password = String value of the password
     * @param email = String value of the user's email
     * @param friends = HashMap list of the user's friends
     */
    public User(String username, String password, String email, HashMap<String, Integer> friends) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.friends = friends;
    }

    /**
     * quinary constructor
     * @param username = String value the user's user name
     * @param email = String value of the user's email
     * @param friends = HashMap list of the user's friends
     * @param id = String value of the user's user id
     */
    public User(String username, String email, HashMap<String, Integer> friends, String id) {
        this.username = username;
        this.email = email;
        this.friends = friends;
        Id = id;
    }

    /**
     * senary constructor
     * @param username = String value the user's user name
     * @param password = String value of the password
     * @param email = String value of the user's email
     * @param friends = HashMap list of the user's friends
     * @param id = String value of the user's user id
     */
    public User(String username, String password, String email, HashMap<String, Integer> friends, String id) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.friends = friends;
        Id = id;
    }

    /**
     * septenary constructor
     * @param username = String value the user's user name
     * @param password = String value of the password
     * @param email = String value of the user's email
     * @param friends = HashMap list of the user's friends
     * @param id = String value of the user's user id
     * @param imagePath = String value of the user image's path
     * @param registrationToken = String value of the registration token
     */
    public User(String username, String password, String email, HashMap<String, Integer> friends, String id, String imagePath, String registrationToken) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.friends = friends;
        Id = id;
        this.imagePath = imagePath;
        this.registrationToken = registrationToken;
    }

    /**
     * set registration token
     * @param registrationToken = String value of the registration token
     */
    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    /**
     * get the registration token
     * @return the String value of the registration token
     */
    public String getRegistrationToken() {
        return registrationToken;
    }

    /**
     * set user's image
     * @param imagePath = String value of the user image's path
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * get the user's image
     * @return the String value of the user's image path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * set user's user id
     * @param id = String value of the user's user id
     */
    public void setId(String id) {
        Id = id;
    }

    /**
     * get the user's user id
     * @return the String value of the user's user id
     */
    public String getId() {
        return Id;
    }

    /**
     * set user's user name
     * @param username = String value of the user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * get the user's user name
     * @return the String value of the user's user name
     */
    public String getUsername() {
        return username;
    }

    /**
     * set user's password
     * @param password = String value of the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * get the user's password
     * @return the String value of the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * set user's email
     * @param email = String value of the user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get the user's email
     * @return the String value of the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * set user's friends
     * @param friends = HashMap list of the user's friends
     */
    public void setFriends(HashMap<String, Integer> friends) {
        this.friends = friends;
    }

    /**
     * get the user's friends
     * @return the HashMap list of the user's friends
     */
    public HashMap<String, Integer> getFriends() {
        return friends;
    }

    /**
     * check if this user is the same as Object obj
     * @param obj = the object used to compare with this user
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * create a clone of the user
     * @return an Object clone of the user
     */
    @Override
    protected Object clone() {
        return new User(this.username, this.password, this.email, this.friends, this.Id);
    }

    /**
     * Builds a String representation of the object for debugging/testing purposes
     * @return a String containing the state of the user
     */
    @Override
    public String toString() {
        return "User {" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", friends=" + friends +
                ", Id='" + Id + '\'' +
                '}';
    }
}