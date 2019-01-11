package com.link.dheyaa.textme.fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.activities.MainActivity;
import com.link.dheyaa.textme.models.User;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class SettingsFragment extends Fragment {

    private DatabaseReference DBref;
    private FirebaseAuth mAuth;

    private EditText usernameInput;
    private EditText inputPasswordOld;
    private EditText passwordInput;
    private ImageView imageView;

    public String username;
    public String email;
    public LinearLayout parentView;


    public SettingsFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.setting_tab, container, false);

        MainActivity parent = (MainActivity) getActivity ();
        usernameInput = (EditText) root.findViewById (R.id.input_name);
        inputPasswordOld = (EditText) root.findViewById (R.id.input_password_old);
        passwordInput = (EditText) root.findViewById (R.id.input_password);
        imageView = (ImageView) root.findViewById (R.id.imageView);
        parentView = (LinearLayout) root.findViewById (R.id.container_main);

        final Button updateBtn = (Button) root.findViewById (R.id.updateBtn);
        updateBtn.setOnClickListener (UpdateData);


        Button signOut = (Button) root.findViewById (R.id.sign_out);
        signOut.setOnClickListener (SignOut);


        mAuth = FirebaseAuth.getInstance ();
        DBref = FirebaseDatabase.getInstance ().getReference ("Users");
        DBref.child (mAuth.getCurrentUser ().getUid ()).addValueEventListener (getUserData);

        return root;
    }

    View.OnClickListener UpdateData = new View.OnClickListener () {

        @Override
        public void onClick(View v) {
            if (
                    email.trim ().equals ("") ||
                            inputPasswordOld.getText ().toString ().trim ().equals ("") ||
                            passwordInput.getText ().toString ().trim ().equals ("")

                    ) {
                Snackbar.make (parentView, "Please full all the fields", Snackbar.LENGTH_LONG).show ();
            } else {
                AuthCredential credential = EmailAuthProvider.getCredential (email, inputPasswordOld.getText ().toString ());
                FirebaseAuth.getInstance ().getCurrentUser ().reauthenticate (credential).addOnCompleteListener (new OnCompleteListener<Void> () {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful ()) {
                            final FirebaseUser user = mAuth.getInstance ().getCurrentUser ();
                            user.updateEmail (email)
                                    .addOnCompleteListener (new OnCompleteListener<Void> () {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful ()) {
                                                System.out.println ("email updated correctly");
                                                user.updatePassword (passwordInput.getText ().toString ()).addOnCompleteListener (new OnCompleteListener<Void> () {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful ()) {
                                                            System.out.println ("password updated correctly");
                                                            if (!usernameInput.getText ().toString ().equals (null)) {
                                                                DBref.child (mAuth.getUid ()).child ("username").setValue (usernameInput.getText ().toString ());
                                                            }
                                                            if (inputPasswordOld.getText ().toString ().equals (passwordInput.getText ().toString ())) {
                                                                DBref.child (mAuth.getUid ()).child ("password").setValue (passwordInput.getText ().toString ());
                                                            }

                                                            getActivity ().finish ();
                                                            startActivity (getActivity ().getIntent ());
                                                        } else {
                                                            String errorMsg = task.getException ().getMessage ();
                                                            Snackbar.make (parentView, errorMsg, Snackbar.LENGTH_LONG).show ();
                                                        }
                                                    }
                                                });
                                            } else {
                                                String errorMsg = task.getException ().getMessage ();
                                                Snackbar.make (parentView, errorMsg, Snackbar.LENGTH_LONG).show ();
                                            }
                                        }
                                    });
                        } else {

                        }
                    }
                });
            }
        }
    };
    View.OnClickListener SignOut = new View.OnClickListener () {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance ().signOut ();
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId ();
            } catch (IOException e) {
                e.printStackTrace ();
            }

            ((MainActivity) getActivity ()).updateUI (FirebaseAuth.getInstance ().getCurrentUser ());
        }
    };


    ValueEventListener getUserData = new ValueEventListener () {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            User currentAuthUser = dataSnapshot.getValue (User.class);
            usernameInput.setText (currentAuthUser.getUsername ());
            email = currentAuthUser.getEmail ();
            username = currentAuthUser.getUsername ();
            FirebaseStorage storage = FirebaseStorage.getInstance ();
            StorageReference storageReference = storage.getReference ();

            storageReference.child (currentAuthUser.getImagePath () != null ? currentAuthUser.getImagePath () : "static/profile.png").getDownloadUrl ().addOnSuccessListener (new OnSuccessListener<Uri> () {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with (getContext ())
                            .load (uri) // the uri you got from Firebase
                            .centerCrop ()
                            .into (imageView);
                }
            }).addOnFailureListener (new OnFailureListener () {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

        @Override
        public void onCancelled(DatabaseError error) {
        }
    };
}

