package com.link.dheyaa.textme.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.link.dheyaa.textme.utils.dataBaeseHelpers;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN: ", refreshedToken);
        dataBaeseHelpers.setToken(refreshedToken);
    }

}