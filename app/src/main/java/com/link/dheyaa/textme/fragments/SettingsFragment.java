package com.link.dheyaa.textme.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.activities.MainActivity;
import com.link.dheyaa.textme.models.User;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class SettingsFragment extends Fragment {

    public String userName;
    public String email;
    private DatabaseReference DBref;
    private FirebaseAuth mAuth;

    public SettingsFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.setting_tab, container, false);

        MainActivity parent = (MainActivity) getActivity();
        final EditText username = (EditText) root.findViewById(R.id.input_name);
        final EditText email = (EditText) root.findViewById(R.id.input_email);
        final EditText password = (EditText) root.findViewById(R.id.input_password);
        final ImageView imageView = (ImageView) root.findViewById(R.id.imageView);

        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");
        DBref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentAuthUser = dataSnapshot.getValue(User.class);
                username.setText(currentAuthUser.getUsername());
                email.setText(currentAuthUser.getEmail());

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();

                storageReference.child(currentAuthUser.getImagePath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext())
                                .load(uri) // the uri you got from Firebase
                                .centerCrop()
                                .into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });



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

