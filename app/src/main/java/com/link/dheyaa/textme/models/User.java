package com.link.dheyaa.textme.models;

import java.util.HashMap;

public class User {
    private String username;
    private String password;
    private String email;
    private HashMap<String, Boolean> friends;
    private String Id;
    private String imagePath;
    private String registrationToken;

    public User(String username, String password, String email, HashMap<String, Boolean> friends, String id, String imagePath, String registrationToken) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.friends = friends;
        Id = id;
        this.imagePath = imagePath;
        this.registrationToken = registrationToken;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public User(String username, String email, HashMap<String, Boolean> friends, String id) {
        this.username = username;
        this.email = email;
        this.friends = friends;
        Id = id;
    }

    public User(String username, String password, String email, HashMap<String, Boolean> friends, String id) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.friends = friends;
        Id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() {
        return new User(this.username, this.password, this.email, this.friends, this.Id);
    }

    public User() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public User(String username, String password, String email, HashMap<String, Boolean> friends) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.friends = friends;
    }


    public User(String username, String email, HashMap<String, Boolean> friends) {
        this.username = username;
        this.email = email;
        this.friends = friends;
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

    public HashMap<String, Boolean> getFriends() {
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

    public void setFriends(HashMap<String, Boolean> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "User {" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", friends=" + friends +
                ", Id='" + Id + '\'' +
                '}';
    }
}