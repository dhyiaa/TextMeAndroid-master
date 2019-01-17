
/* TextMe Team
 * Jan 2019
 * Setting fragment:
 * controls the setting page's listing and layout
 */

package com.link.dheyaa.textme.fragments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
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

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends Fragment {
    //attributes of SettingFragment
    private DatabaseReference DBref;
    private FirebaseAuth mAuth;

    private EditText usernameInput;
    private EditText inputPasswordOld;
    private EditText passwordInput;
    private ImageView imageView;

    public String username;
    public String email;
    public LinearLayout parentView;
    public String userToken;

    /**
     * default constructor
     */
    public SettingsFragment() {

    }

    public void recreate()
    {
        ((MainActivity) getActivity ()).finish();
        startActivity( ((MainActivity) getActivity ()).getIntent());

    }

    /**
     * create the view presenting current settings
     * @param inflater= LayoutInflater to generate layouts
     * @param container = ViewGroup of container Views
     * @param savedInstanceState = Bundle storing the current instance's state
     * @return the View presenting current settings
     */
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.setting_tab, container, false);

        MainActivity parent = (MainActivity) getActivity ();
        usernameInput = (EditText) root.findViewById (R.id.input_name);
        inputPasswordOld = (EditText) root.findViewById (R.id.input_password_old);
        passwordInput = (EditText) root.findViewById (R.id.input_password);
        imageView = (ImageView) root.findViewById (R.id.imageView);
        parentView = (LinearLayout) root.findViewById (R.id.container_main);

        //generate the update button
        final Button updateBtn = (Button) root.findViewById (R.id.updateBtn);
        updateBtn.setOnClickListener (UpdateData);

        //generate the sign out button
        Button signOut = (Button) root.findViewById (R.id.sign_out);
        signOut.setOnClickListener (SignOut);

        //get the instance of the database
        mAuth = FirebaseAuth.getInstance ();
        DBref = FirebaseDatabase.getInstance ().getReference ("Users");
        DBref.child (mAuth.getCurrentUser ().getUid ()).addValueEventListener (getUserData);


        Switch darkModeSwitch = root.findViewById (R.id.darkMOdeSwitch);

        SharedPreferences prefs = (getActivity ()).getSharedPreferences("textMeSP", MODE_PRIVATE);

        String isDark = prefs.getString("isDark", null);
        if (isDark != null) {
            darkModeSwitch.setChecked (true);
        }else{
            darkModeSwitch.setChecked (false);
        }


        darkModeSwitch.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = inflater.getContext ().getSharedPreferences("textMeSP", MODE_PRIVATE).edit();
                if(isChecked){
                    editor.putString("isDark", "true");
                }else{
                    editor.putString("isDark", null);
                }
                editor.apply();
                recreate();
            }
        });
        //return the new view
        return root;
    }

    //action after clicking on the update button
    View.OnClickListener UpdateData = new View.OnClickListener () {

        @Override
        public void onClick(View v) {
            if (
                    email.trim ().equals ("") ||
                            inputPasswordOld.getText ().toString ().trim ().equals ("") ||
                            passwordInput.getText ().toString ().trim ().equals ("")
                    ) {
                //if the new user information is invalid
                Snackbar.make (parentView, "Please full all the fields", Snackbar.LENGTH_LONG).show ();
            } else {
                //authorize the user again
                AuthCredential credential = EmailAuthProvider.getCredential (email, inputPasswordOld.getText ().toString ());
                FirebaseAuth.getInstance ().getCurrentUser ().reauthenticate (credential).addOnCompleteListener (new OnCompleteListener<Void> () {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful ()) {
                            //get the current user
                            final FirebaseUser user = mAuth.getInstance ().getCurrentUser ();
                            //update the user's email and add a complete listener
                            user.updateEmail (email)
                                    .addOnCompleteListener (new OnCompleteListener<Void> () {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful ()) {
                                                //update the user's password and adda complete listener
                                                user.updatePassword (passwordInput.getText ().toString ()).addOnCompleteListener (new OnCompleteListener<Void> () {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful ()) {
                                                            if (!usernameInput.getText ().toString ().equals (null)) {
                                                                DBref.child (mAuth.getUid ()).child ("username").setValue (usernameInput.getText ().toString ());
                                                            }
                                                            if (inputPasswordOld.getText ().toString ().equals (passwordInput.getText ().toString ())) {
                                                                DBref.child (mAuth.getUid ()).child ("password").setValue (passwordInput.getText ().toString ());
                                                            }

                                                            //restart the application
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
                        }
                    }
                });
            }
        }
    };

    /**
     * action after clicking on the sign out button
     */
    View.OnClickListener SignOut = new View.OnClickListener () {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance ().signOut ();
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId ();
                FirebaseInstanceId.getInstance().deleteToken(userToken != null ? userToken : "" , "FCM");
            } catch (IOException e) {
                e.printStackTrace ();
            }


            DBref.child (mAuth.getUid ()).child ("registrationToken").setValue ("").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        ((MainActivity) getActivity ()).updateUI (FirebaseAuth.getInstance ().getCurrentUser ());
                    }
                }
            });
            //update the main activity's UI
        }
    };

    /**
     * action after value event triggered
     */
    ValueEventListener getUserData = new ValueEventListener () {
        @Override
        //data changed event
        public void onDataChange(DataSnapshot dataSnapshot) {
            //get the User's data
            User currentAuthUser = dataSnapshot.getValue (User.class);
           // usernameInput.setText (currentAuthUser.getUsername ());
            email = currentAuthUser.getEmail ();
            username = currentAuthUser.getUsername ();
            userToken = currentAuthUser.getRegistrationToken();
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
                //failure event
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        //cancelled event
        @Override
        public void onCancelled(DatabaseError error) {
        }
    };
}

