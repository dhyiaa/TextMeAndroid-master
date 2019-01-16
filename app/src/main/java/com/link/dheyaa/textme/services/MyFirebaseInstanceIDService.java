
/* TextMe Team
 * Jan 2019
 * MyFirebaseInstanceIDService class:
 * this class maintain how the token for each device is loaded and checked
 */

package com.link.dheyaa.textme.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.link.dheyaa.textme.utils.dataBaeseHelpers;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    /**
     * onTokenRefresh : this method fired whenever the token is changed from the server ,
     * which will try to save it on device
     */
    @Override
    public void onTokenRefresh() {
        // parsing the token to String type
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // saving the token to the database
        dataBaeseHelpers.setToken(refreshedToken);
    }


}