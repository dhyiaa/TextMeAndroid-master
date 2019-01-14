
/* TextMe Team
 * Jan 2019
 * SignUp class:
 * SignUp activity of TextMe Program
 */

package com.link.dheyaa.textme.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.models.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class SignUp extends AppCompatActivity {

    // variable declaration
    private FirebaseAuth mAuth;
    public ProgressBar loading;
    View parentLayout;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ImageView userPhoto;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;

    /* onCreate method for activity
     * @param savedInstanceState - data bundle for activity
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        SharedPreferences prefs = getSharedPreferences ("textMeSP", MODE_PRIVATE);

        String isDark = prefs.getString ("isDark", null);
        if (isDark != null) {
            setTheme (R.style.ActivityTheme_Primary_Base_Dark);

        } else {
            setTheme (R.style.ActivityTheme_Primary_Base_Light);
        }

        super.onCreate (savedInstanceState);

        // firebase setup
        mAuth = FirebaseAuth.getInstance ();
        updateUI (mAuth.getCurrentUser ());

        // set view
        setContentView (R.layout.activity_sign_up);
        parentLayout = findViewById (R.id.container_main);

        // set storage
        storage = FirebaseStorage.getInstance ();
        storageReference = storage.getReference ();

        // toolbar setup
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById (R.id.toolbar2);
        setSupportActionBar (toolbar);
        toolbar.setNavigationOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);

        // Sign Up Button Setup
        Button button = (Button) findViewById (R.id.btn_signUp);
        loading = (ProgressBar) findViewById (R.id.progressBar);
        setTitle ("Sign Up");

        button.setOnClickListener (new View.OnClickListener () {
            public void onClick(View v) {
                SignUp ();
            }
        });

        // user photo setup
        ImageView userPhoto = parentLayout.findViewById (R.id.userPhoto);
        userPhoto.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                chooseImage ();
            }
        });
    }

    /* method to update UI
     * @param currentUser - the current user got from firebase
     * */
    protected void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startActivity (new Intent (SignUp.this, MainActivity.class));
            finish ();
        }

    }

    /* method for signing up an account
     * no param
     * */
    public void SignUp() {
        loading.setVisibility (View.VISIBLE);

        final EditText username = (EditText) findViewById (R.id.username);
        final EditText email = (EditText) findViewById (R.id.input_email);
        final EditText password = (EditText) findViewById (R.id.input_password);

        if (email.getText ().length () != 0 && password.getText ().length () != 0 && username.getText ().length () != 0) { // check if fields are valid
            // create user
            mAuth.createUserWithEmailAndPassword (email.getText ().toString (), password.getText ().toString ())
                    .addOnCompleteListener (this, new OnCompleteListener<AuthResult> () {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful ()) { // if task is successful
                                loading.setVisibility (View.INVISIBLE);
                                final FirebaseUser authUser = mAuth.getCurrentUser ();

                                User userObject = new User (
                                        username.getText ().toString ().toLowerCase (),
                                        email.getText ().toString ()
                                );

                                String ImagePath = uploadImage (authUser);
                                userObject.setImagePath (ImagePath);

                                FirebaseDatabase database = FirebaseDatabase.getInstance ();
                                DatabaseReference myRef = database.getReference ("Users");
                                myRef.child (authUser.getUid ()).setValue (userObject).addOnCompleteListener (new OnCompleteListener<Void> () {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful ()) {
                                            loading.setVisibility (View.INVISIBLE);

                                            FirebaseUser authUser = mAuth.getCurrentUser ();
                                            updateUI (authUser);
                                        } else {
                                            Snackbar.make (parentLayout, "saving  failed", Snackbar.LENGTH_LONG).show ();
                                        }
                                    }
                                });
                            } else { // if task is not successful
                                loading.setVisibility (View.INVISIBLE);
                                String errorMsg = task.getException ().getMessage ();
                                Snackbar.make (parentLayout, errorMsg, Snackbar.LENGTH_LONG).show ();
                                updateUI (null);
                            }
                        }
                    });
        } else {
            loading.setVisibility (View.INVISIBLE);
            Snackbar.make (parentLayout, "Fill all of the fields ", Snackbar.LENGTH_LONG).show ();
        }
    }

    /* method to switch between auth and sign in page
     * @param v - current view
     */
    public void SignInPage(View v) {

        // startActivity(new Intent(getApplicationContext(), SignIn.class));
        finish ();
    }

    /* method to choose image
     * no param
     * */
    private void chooseImage() {
        Intent intent = new Intent ();
        intent.setType ("image/*");
        intent.setAction (Intent.ACTION_GET_CONTENT);
        startActivityForResult (Intent.createChooser (intent, "Select your profile picture"), PICK_IMAGE_REQUEST);
    }

    /* method to get real path from URI
     * @param context - current context
     * @param contentUri - URI of current content
     * */
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver ().query (contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow (MediaStore.Images.Media.DATA);
            cursor.moveToFirst ();
            return cursor.getString (column_index);
        } finally {
            if (cursor != null) { // if cursor is changed
                cursor.close ();
            }
        }
    }

    /* method to get data
     * @param requestCode - request code
     * @param resultCode - result code
     * @param data - the data being processed
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData () != null) {
            filePath = data.getData ();
            System.out.println ("file path " + filePath.toString ());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap (this.getContentResolver (), filePath);
            } catch (IOException e) {
                e.printStackTrace ();
            }

        }
    }

    /* method to get power of two for sample ratio
     * @param ratio - ratio in double
     */
    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit ((int) Math.floor (ratio));
        if (k == 0) return 1;
        else return k;
    }

    /* method to upload image
     * @param authUser - the current User
     * */
    private String uploadImage(FirebaseUser authUser) {
        final FirebaseUser authUser_ = authUser;
        if (filePath != null) { // if filePath is Valid
            final ProgressDialog progressDialog = new ProgressDialog (this);
            progressDialog.setTitle ("Uploading...");
            progressDialog.show ();
            String uniqueImagePath = "images/" + UUID.randomUUID ().toString () + authUser_.getUid ();
            StorageReference ref = storageReference.child (uniqueImagePath);
            ref.putFile (filePath)
                    .addOnSuccessListener (new OnSuccessListener<UploadTask.TaskSnapshot> () {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            updateUI (authUser_);
                            Toast.makeText (SignUp.this, "Uploaded", Toast.LENGTH_SHORT).show ();
                        }
                    })
                    .addOnFailureListener (new OnFailureListener () {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss ();
                            Snackbar.make (parentLayout, "Failed " + e.getMessage (), Snackbar.LENGTH_LONG).show ();

                            Toast.makeText (SignUp.this, "Failed " + e.getMessage (), Toast.LENGTH_SHORT).show ();
                        }
                    })
                    .addOnProgressListener (new OnProgressListener<UploadTask.TaskSnapshot> () {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred () / taskSnapshot
                                    .getTotalByteCount ());
                            progressDialog.setMessage ("Uploaded " + (int) progress + "%");
                        }
                    });
            return uniqueImagePath;
        }
        return null;
    }

}
