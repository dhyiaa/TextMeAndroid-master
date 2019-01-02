package com.link.dheyaa.textme.models;

import java.util.ArrayList;
import java.util.List;

public class Friend {

    private String name;
    private String email;

    public Friend(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
