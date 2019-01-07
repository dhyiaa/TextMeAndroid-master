
/* TextMe Team
* Jan 2019
* MainActivity class:
* Main activity of TextMe Program
*/

package com.link.dheyaa.textme.activities;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.link.dheyaa.textme.R;

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
