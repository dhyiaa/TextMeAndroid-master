
/* TextMe Team
 * Jan 2019
 * MyFirebaseInstanceIDService class:
 * Subclass of FirebaseInstanceIdService that provide functions to work for instance ID
* */

package com.link.dheyaa.textme.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.link.dheyaa.textme.utils.dataBaseHelpers;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    /* method launches when token is refreshed
    * no params
    * */
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN: ", refreshedToken);
        dataBaseHelpers.setToken(refreshedToken);
    }

}