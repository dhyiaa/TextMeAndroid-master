
/* TextMe Team
 * Jan 2019
 * dataBaeseHelpers class:
 * this class is used to change the token from the database
 *
 * it is separated to we use it in different placed inside the project
 */


package com.link.dheyaa.textme.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class dataBaeseHelpers {
    /**
     * this function changing the token from database
     *
     * @param token : the token as String
     */
    public static void setToken(String token) {
        // instantiating the FirebaseAuth class
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // getting the database reference to the users node
        DatabaseReference DBref = FirebaseDatabase.getInstance().getReference("Users");
        // update the current user notification token
        DBref.child(mAuth.getUid()).child("registrationToken").setValue(token);
    }
}
