
/* TextMe Team
 * Jan 2019
 * User class:
 * containing the attributes and functions of User
 */

package com.link.dheyaa.textme.models;

import java.util.HashMap;

public class User {
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
     * @param friends = HashMap list of the user's friend
     */
    public User(String username, String email, HashMap<String, Integer> friends) {
        this.username = username;
        this.email = email;
        this.friends = friends;
    }

    /**
     * tertiary constructor
     * @param username = String value the user's user name
     * @param password = String value of the password
     * @param email = String value of the user's email
     * @param friends = HashMap list of the user's friend
     */
    public User(String username, String password, String email, HashMap<String, Integer> friends) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.friends = friends;
    }

    /**
     * quaternary constructor
     * @param username = String value the user's user name
     * @param password = String value of the password
     * @param email = String value of the user's email
     * @param id = String value of the user's user id
     */
    public User(String username, String email, HashMap<String, Integer> friends, String id) {
        this.username = username;
        this.email = email;
        this.friends = friends;
        Id = id;
    }

    /**
     * quinary constructor
     * @param username = String value the user's user name
     * @param password = String value of the password
     * @param email = String value of the user's email
     * @param friends = HashMap list of the user's friend
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
     * senary constructor
     * @param username = String value the user's user name
     * @param password = String value of the password
     * @param email = String value of the user's email
     * @param friends = HashMap list of the user's friend
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
     * get the registration token
     * @return = the String value of the registration token
     */
    public String getRegistrationToken() {
        return registrationToken;
    }

    /**
     * set the registration token
     * @param registrationToken
     */
    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    /**
     * set the user's image
     * @param imagePath = the String value of the user image's path
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Integer> getFriends() {
        return friends;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFriends(HashMap<String, Integer> friends) {
        this.friends = friends;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() {
        return new User(this.username, this.password, this.email, this.friends, this.Id);
    }

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