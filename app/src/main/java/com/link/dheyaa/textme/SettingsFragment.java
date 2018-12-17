package com.link.dheyaa.textme;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.EventListener;


public class SettingsFragment extends android.support.v4.app.Fragment {

    public SettingsFragment(){
        //root.update();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_tab, container, false);
        return root;

    }
}

