
/* TextMe Team
 * Jan 2019
 * MainActivity class:
 * Main activity of TextMe Program
 */

package com.link.dheyaa.textme.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.link.dheyaa.textme.fragments.FriendsFragment;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.fragments.RequestsFragment;
import com.link.dheyaa.textme.fragments.SettingsFragment;
import com.link.dheyaa.textme.models.User;
import com.link.dheyaa.textme.utils.dataBaeseHelpers;

import java.io.IOException;

public class MainActivity extends AppCompatActivity   {

    // variable declaration
    private FirebaseAuth mAuth;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public User currentAuthUser;
    private DatabaseReference DBref;
    private Toolbar toolbar;
    private TextView toolBarTitle;
    private ImageView profileView;
    public MainActivity _context;

    /* onCreate method for activity
     * @param savedInstanceState - data bundle for activity
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // set theme
        SharedPreferences prefs = getSharedPreferences("textMeSP", MODE_PRIVATE);
        String isDark = prefs.getString("isDark", null);
        if (isDark != null) { // if user chooses dark mode
            setTheme (R.style.ActivityTheme_Primary_Base_Dark);
        }else{ // if user does not choose dark mode
            setTheme (R.style.ActivityTheme_Primary_Base_Light);
        }

        super.onCreate (savedInstanceState); // call super method

        // database setup
        mAuth = FirebaseAuth.getInstance ();
        DBref = FirebaseDatabase.getInstance ().getReference ("Users");

        updateUI (mAuth.getCurrentUser ()); // update UI with current user
        showContent (); // show content
    }

    /* method to show contents
     * no params
     * */
    private void showContent() {

        setContentView (R.layout.activity_main);

        // find views
        _context = this;
        profileView = (ImageView) findViewById (R.id.profileView);
        toolBarTitle = (TextView) findViewById (R.id.toolBarTitle);
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);

        // create bottom navigation items
        final AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById (R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem (R.string.tab_text_1, R.drawable.friends, R.color.colorPrimaryText_dark);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem (R.string.tab_text_3, R.drawable.requests, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem (R.string.tab_text_4, R.drawable.settings, R.color.colorPrimaryDark);

        com.mikhaellopez.circularimageview.CircularImageView profile = findViewById (R.id.profileView);

        // add items to bottom navigation
        bottomNavigation.addItem (item1);
        bottomNavigation.addItem (item2);
        bottomNavigation.addItem (item3);

        // set theme 1
        TypedValue typedValue = new TypedValue ();
        Resources.Theme theme = this.getTheme ();
        theme.resolveAttribute (R.attr.colorPrimary, typedValue, true);
        @ColorInt int color = typedValue.data;

        // set theme 2
        TypedValue typedValue2 = new TypedValue ();
        Resources.Theme theme2 = this.getTheme ();
        theme.resolveAttribute (R.attr.colorPrimary, typedValue, true);
        @ColorInt int color2 = typedValue.data;

        // set attributes of bottom navigation
        bottomNavigation.setDefaultBackgroundColor (color2);
        bottomNavigation.setBehaviorTranslationEnabled (false);
        bottomNavigation.setAccentColor ( Color.parseColor ("#FFFFFF"));
        bottomNavigation.setInactiveColor (Color.argb (100 , 255 ,255 ,255));
        bottomNavigation.setOnTabSelectedListener (new AHBottomNavigation.OnTabSelectedListener () {

            /* method launches when tab is selected
             * @param position - position of tab
             * @param wasSelected - true if selected
             * */
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                mViewPager.setCurrentItem (position);
                return true;
            }
        });

        // set view pager
        mSectionsPagerAdapter = new SectionsPagerAdapter (getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById (R.id.container);
        mViewPager.setAdapter (mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener () {

            /* method launches when page is scrolled
             * @param position - position on page
             * @param positionOffset - offset of position
             * @param positionOffsetPixels - pixels of offset
             * */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            /* method launches when page is selected
             * @param position - position on page
             * */
            @Override
            public void onPageSelected(int position) {
                bottomNavigation.setCurrentItem (position); // set item by position
            }

            /* method launches when scroll state of page changes
             * @param state - state of scroll
             * */
            @Override
            public void onPageScrollStateChanged(int state) {}

        });

        mViewPager.setOffscreenPageLimit (4);

        getSupportActionBar ().setDisplayShowTitleEnabled (false); // disable display

        profile.setOnClickListener (new View.OnClickListener(){ // listen for clicking on profile image

            /** method launches when profile image is clicked
             * @param v - current view
             */
            @Override
            public void onClick(View v) {
                // go to settings page
                mViewPager.setCurrentItem (2);
                bottomNavigation.setCurrentItem (2);
            }
        });

    }

    // listen for changes of authentication state
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener () {

        /* method launches when auth state changes
         * @param firebaseAuth - auth of firebase
         * */
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser ();
            if (user == null) { // if there is no user logged in
                startActivity (new Intent (MainActivity.this, SignIn.class)); // start sign in activity
                finish (); // terminate activity
            }
        }
    };

    /* method to update UI
     * @param currentUser - current user logged in
     * */
    public void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) { // if there is no user logged in
            startActivity (new Intent (getApplicationContext(), SignIn.class)); // start sign in activity
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId (); // try to delete instance id
            } catch (IOException e) { // catch error
                e.printStackTrace (); // print error
            }
            this.finish (); // finish activity

        } else { // if there is a user logged in

            // listen for successful instance id result to firebase
            FirebaseInstanceId.getInstance().getInstanceId ().addOnSuccessListener (new OnSuccessListener<InstanceIdResult> () {

                /* method launches when instance id result update is successful
                 * @param instanceIdResult - result of instance id
                 * */
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String token = FirebaseInstanceId.getInstance ().getToken ();
                    DBref.child(mAuth.getUid()).child("registrationToken").setValue(token);
                }
            });

            DBref.child (mAuth.getCurrentUser ().getUid ()).addValueEventListener (new ValueEventListener () {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentAuthUser = dataSnapshot.getValue (User.class);
                    toolBarTitle.setText (currentAuthUser.getUsername ());

                    FirebaseStorage storage = FirebaseStorage.getInstance ();
                    StorageReference storageReference = storage.getReference ();

                    storageReference.child (currentAuthUser.getImagePath () != null ? currentAuthUser.getImagePath () : "static/profile.png").getDownloadUrl ().addOnSuccessListener (new OnSuccessListener<Uri> () {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with (_context)
                                    .load (uri) // the uri you got from Firebase
                                    .centerCrop ()
                                    .into (profileView);
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
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.search_btn) {

            Intent Search = new Intent (this, Search.class);
            startActivity (Search);

        }

        return super.onOptionsItemSelected (item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super (fm);
        }
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new FriendsFragment ();
            } else if (position == 1) {
                return new RequestsFragment ();
            } else {
                return new SettingsFragment ();
            }
        }
        @Override
        public int getCount() {
            return 3;
        }
    }
}
