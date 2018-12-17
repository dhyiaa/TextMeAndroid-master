package com.link.dheyaa.textme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressBar loading;
    private View parentLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setTheme(R.style.DarkTheme);


        mAuth = FirebaseAuth.getInstance();
        updateUI(mAuth.getCurrentUser());


        setContentView(R.layout.activity_sign_in);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        parentLayout = findViewById(R.id.container_main);



        Button button = (Button) findViewById(R.id.btn_signIn);
        loading = (ProgressBar) findViewById(R.id.progressBar);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SignIn();
            }
        });

    }



    protected void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startActivity(new Intent(SignIn.this, MainActivity.class));
            finish();
        }

    }

    public void SignIn() {

        loading.setVisibility(View.VISIBLE);

        EditText email = (EditText) findViewById(R.id.input_email);
        EditText password = (EditText) findViewById(R.id.input_password);

        if (email.getText().length() != 0 && password.getText().length() != 0) {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                loading.setVisibility(View.INVISIBLE);

                                String errorMsg = task.getException().getMessage();

                                Snackbar.make(parentLayout, errorMsg, Snackbar.LENGTH_LONG).show();

                                updateUI(null);
                            }
                        }
                    });
        } else {

            Snackbar.make(parentLayout, "Fill all of the fields", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            loading.setVisibility(View.INVISIBLE);

        }
    }

    public void SignUpPage(View v) {
        startActivity(new Intent(getApplicationContext(), SignUp.class));
        //   finish();
    }

   /*
   *
   *
   *  @Override
    public void onBackPressed() {
        finish();
    }

   * */


}
