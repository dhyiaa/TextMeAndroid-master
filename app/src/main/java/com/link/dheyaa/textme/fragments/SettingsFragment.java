package com.link.dheyaa.textme.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    public String email;
    private DatabaseReference DBref;
    private FirebaseAuth mAuth;

    private EditText usernameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private ImageView imageView;

    public SettingsFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.setting_tab, container, false);

        MainActivity parent = (MainActivity) getActivity ();
        usernameInput = (EditText) root.findViewById (R.id.input_name);
        emailInput = (EditText) root.findViewById (R.id.input_email);
        passwordInput = (EditText) root.findViewById (R.id.input_password);
        imageView = (ImageView) root.findViewById (R.id.imageView);

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
            AuthCredential credential = EmailAuthProvider.getCredential (emailInput.getText ().toString (), passwordInput.getText ().toString ());
            mAuth.getCurrentUser ().reauthenticate (credential).addOnCompleteListener (new OnCompleteListener<Void> () {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    FirebaseUser user = mAuth.getInstance ().getCurrentUser ();
                    user.updatePassword (passwordInput.getText ().toString ()).addOnCompleteListener (new OnCompleteListener<Void> () {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful ()) {
                                System.out.println ("password updated correctly");
                            }
                        }
                    });
                    user.updateEmail (emailInput.getText ().toString ())
                            .addOnCompleteListener (new OnCompleteListener<Void> () {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful ()) {
                                        System.out.println ("email updated correctly");
                                    }
                                }
                            });
                }
            });
        }
    };
    View.OnClickListener SignOut = new View.OnClickListener () {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance ().signOut ();
            ((MainActivity) getActivity ()).updateUI (FirebaseAuth.getInstance ().getCurrentUser ());
        }
    };


    ValueEventListener getUserData = new ValueEventListener () {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            User currentAuthUser = dataSnapshot.getValue (User.class);
            usernameInput.setText (currentAuthUser.getUsername ());
            emailInput.setText (currentAuthUser.getEmail ());

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

