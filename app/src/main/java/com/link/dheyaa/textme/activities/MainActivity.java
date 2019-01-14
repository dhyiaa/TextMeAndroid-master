
/* TextMe Team
 * Jan 2019
 * MainActivity class:
 * Main activity of TextMe Program
 */

package com.link.dheyaa.textme.activities;

import android.content.Intent;
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
import com.link.dheyaa.textme.utils.dataBaseHelpers;

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
        // setTheme(R.style.DarkTheme);

        super.onCreate (savedInstanceState);

        mAuth = FirebaseAuth.getInstance ();
        DBref = FirebaseDatabase.getInstance ().getReference ("Users");

        updateUI (mAuth.getCurrentUser ());
        showContent ();

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
        final AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById (R.id.bottom_navigation);

        // create bottom navigation items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem (R.string.tab_text_1, R.drawable.friends, R.color.colorPrimaryText_dark);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem (R.string.tab_text_3, R.drawable.requests, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem (R.string.tab_text_4, R.drawable.settings, R.color.colorPrimaryDark);

        // add items to bottom navigation
        bottomNavigation.addItem (item1);
        bottomNavigation.addItem (item2);
        bottomNavigation.addItem (item3);

        // set theme
        TypedValue typedValue = new TypedValue ();
        Resources.Theme theme = this.getTheme ();
        theme.resolveAttribute (R.attr.colorPrimary, typedValue, true);
        @ColorInt int color = typedValue.data;

        // set attributes of bottom navigation
        bottomNavigation.setDefaultBackgroundColor (Color.parseColor ("#FFFFFF"));
        bottomNavigation.setBehaviorTranslationEnabled (false);
        bottomNavigation.setAccentColor (color);
        bottomNavigation.setInactiveColor (Color.parseColor ("#747474"));
        bottomNavigation.setOnTabSelectedListener (new AHBottomNavigation.OnTabSelectedListener () { // listen for tab selecting

            /* method lauches when tab is selected
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
        mSectionsPagerAdapter = new SectionsPagerAdapter (getSupportFragmentManager ()); // instantiate section pager adapter
        mViewPager = (ViewPager) findViewById (R.id.container);
        mViewPager.setAdapter (mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener () { // listen for page change

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

    }

    // listen for changes of authentication state
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener () {

        /* method launches when auth state changes
        * @param firebaseAuth - auth of firebase
        * */
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) { // if there is a user logged in
                startActivity (new Intent (MainActivity.this, SignIn.class)); // start activity
                finish (); // terminate activity
            }
        }
    };

    /* method to update UI
    * @param currentUser - current user logged in
    * */
    public void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) { // if there is a user logged in
            startActivity (new Intent (getApplicationContext (), SignIn.class)); // start activity
            this.finish (); // finish activity

        } else {

            // listen for successful instance id result to firebase
            FirebaseInstanceId.getInstance().getInstanceId ().addOnSuccessListener (new OnSuccessListener<InstanceIdResult> () {

                /* method launches when instance id result update is successful
                * @param instanceIdResult - result of instance id
                * */
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String token = FirebaseInstanceId.getInstance().getToken();
                    DBref.child(mAuth.getUid()).child("registrationToken").setValue(token); // set value for registration token
                    //dataBaseHelpers.setToken (FirebaseInstanceId.getInstance ().getToken ());
                }
            });

            DBref.child (mAuth.getCurrentUser ().getUid ()).addValueEventListener (new ValueEventListener () { // listen for value changing

                /* method launches when data is changed
                * @param dataSnapshot - snapshot of data
                * */
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // get user and display username
                    currentAuthUser = dataSnapshot.getValue (User.class);
                    //toolbar.setTitle(currentAuthUser.getUsername());
                    toolBarTitle.setText (currentAuthUser.getUsername ());
                    //profileView.setImageBitmap();

                    // set storage
                    FirebaseStorage storage = FirebaseStorage.getInstance ();
                    StorageReference storageReference = storage.getReference ();
                    storageReference.child (currentAuthUser.getImagePath () != null ? currentAuthUser.getImagePath () : "static/profile.png")
                    .getDownloadUrl().addOnSuccessListener (new OnSuccessListener<Uri> () { // listen for success storage of uri

                        /* method launches when uri is successfully stored
                        * @param uri - URI of image
                        * */
                        @Override
                        public void onSuccess(Uri uri) {
                            // set image
                            Glide.with (_context)
                                    .load (uri) // the uri you got from Firebase
                                    .centerCrop ()
                                    .into (profileView);
                        }
                    }).addOnFailureListener (new OnFailureListener () { // listen for process failure

                        /* method launches when process fails
                        * @param exception - non-null exception made process failed
                        * */
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                }

                /* method launches when event is cancelled
                * @param error - error from firebase
                * */
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }

    }

    /* method to create options in toolbar
     * @param menu - the message menu
     * return true when completed
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.menu_main, menu);
        return true;
    }

    /* method launches when item is selected
    * @param item - menu item
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.action_settings) { // if user clicked action settings
            return true;
        } else if (id == R.id.search_btn) { // if user clicked search button

            // activate search activity
            Intent Search = new Intent (this, Search.class);
            startActivity (Search);

        }

        return super.onOptionsItemSelected (item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        /* primary constructor
        * @param fm - fragment manager
        * */
        public SectionsPagerAdapter(FragmentManager fm) {
            super (fm); // super constructor
        }

        /* method to get fragments
        * @param position - fragment number
        * */
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

        /* method to get counts for fragments
        * no params
        * */
        @Override
        public int getCount() {
            return 3;
        }
    }
}
