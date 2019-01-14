package com.link.dheyaa.textme.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class dataBaseHelpers {
    public static void setToken(String token){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference DBref = FirebaseDatabase.getInstance().getReference("Users");
        DBref.child(mAuth.getUid()).child("registrationToken").setValue(token);
    }
}
