
/* TextMe Team
* Jan 2019
* SignIn class:
* SignIn activity of TextMe Program
*/

package com.link.dheyaa.textme.activities;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
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

    /* onCreate method for activity
     * @param savedInstanceState - data bundle for activity
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("textMeSP", MODE_PRIVATE);

        String isDark = prefs.getString("isDark", null);
        if (isDark != null) {
            setTheme (R.style.ActivityTheme_Primary_Base_Dark);

        }else{
            setTheme (R.style.ActivityTheme_Primary_Base_Light);
        }

        super.onCreate(savedInstanceState);


        // firebase setup
        mAuth = FirebaseAuth.getInstance();
        updateUI(mAuth.getCurrentUser());

        // set view
        setContentView(R.layout.activity_sign_in);

        // toolbar setup
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        // set parent layout
        parentLayout = findViewById(R.id.container_main);

        // button setup
        Button button = (Button) findViewById(R.id.btn_signIn);
        loading = (ProgressBar) findViewById(R.id.progressBar);
        button.setOnClickListener(new View.OnClickListener() { // listen for clicking button
            public void onClick(View v) {
                SignIn(); // run sign in function
            }
        });

    }

    /* method to update UI
    * @param currentUser - current user logged in
    * */
    protected void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) { // if user is not null
            startActivity(new Intent(SignIn.this, MainActivity.class)); // start activity
            finish(); // finish activity
        }

    }

    /* method to sign in
    * no params
    * */
    public void SignIn() {

        loading.setVisibility(View.VISIBLE); // enable loading view
        EditText email = findViewById(R.id.input_email);
        EditText password = findViewById(R.id.input_password);
        if (email.getText().length() != 0 && password.getText().length() != 0) { // if email and password is not empty
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { // listen for completion of signing in

                        /* method launches when sign in is completed
                        * @param task - non-null task
                        * */
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) { // if task is successful
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user); // update UI
                            } else { // if task is failed
                                loading.setVisibility(View.INVISIBLE); // disable loading view
                                String errorMsg = task.getException().getMessage(); // get error message
                                Snackbar.make(parentLayout, errorMsg, Snackbar.LENGTH_LONG).show(); // show error message in snackbar
                                updateUI(null); // update UI
                            }
                        }
                    });
        } else { // if either email or password is empty

            Snackbar.make(parentLayout, "Please fill in all of the fields required", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show(); // show instruction in snackbar
            loading.setVisibility(View.INVISIBLE); // disable loading view

        }
    }
    /* method to show sign up page
    * @param v - current view
    * */
    public void SignUpPage(View v) {
        startActivity(new Intent(getApplicationContext(), SignUp.class)); // start activity
        //   finish();
    }
}
