package com.link.dheyaa.textme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.activities.MainActivity;


public class SettingsFragment extends android.support.v4.app.Fragment {

    public SettingsFragment() {
        //root.update();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.setting_tab, container, false);
        Button signOut = (Button) root.findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                ((MainActivity) getActivity()).updateUI(FirebaseAuth.getInstance().getCurrentUser());
            }
        });


        return root;

    }
}

