package com.link.dheyaa.textme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.activities.MainActivity;

import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat{

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.settings_tab);
        /*
        * View root = inflater.inflate(R.layout.settings_tab, container, false);
        Button signOut = (Button) root.findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                ((MainActivity) getActivity()).updateUI(FirebaseAuth.getInstance().getCurrentUser());
            }
        });
        return root;
        * */

    }

}

