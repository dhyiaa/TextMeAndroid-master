package com.link.dheyaa.textme.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.link.dheyaa.textme.fragments.FriendsFragment;
import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.fragments.RequestsFragment;
import com.link.dheyaa.textme.fragments.SettingsFragment;
import com.link.dheyaa.textme.models.User;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private User currentAuthUser;
    private DatabaseReference DBref;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setTheme(R.style.DarkTheme);

        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");
        updateUI(mAuth.getCurrentUser());
        showContent();

    }

    private void showContent() {

        // DBref.child(mAuth.getCurrentUser().getUid()).Searchchild("friends").push().setValue("friend1");

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("loading ...");
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.setOffscreenPageLimit(4);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        /*
        *
        * FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        * */

    }


    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                startActivity(new Intent(MainActivity.this, SignIn.class));
                finish();
            }
        }
    };

    public void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            startActivity(new Intent(getApplicationContext(), SignIn.class));
            this.finish();

        } else {
            DBref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentAuthUser = dataSnapshot.getValue(User.class);
                    toolbar.setTitle(currentAuthUser.getUsername());

                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.search_btn) {

            Intent Search = new Intent(this, Search.class);
            startActivity(Search);

            // mAuth.signOut();
            // updateUI(mAuth.getCurrentUser());
        }

        return super.onOptionsItemSelected(item);
    }

/*
*
*     public static class PlaceholderFragment extends Fragment {
        private static String sectionId;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(sectionId, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            if (getArguments().getInt(sectionId) == 1) {
                rootView = inflater.inflate(R.layout.friends_tab, container, false);
            } else if (getArguments().getInt(sectionId) == 2) {
                rootView = inflater.inflate(R.layout.requests_tab, container, false);
            } else if (getArguments().getInt(sectionId) == 3) {
                rootView = inflater.inflate(R.layout.settings_tab, container, false);
            } else {
                rootView = inflater.inflate(R.layout.friends_tab, container, false);
            }
            return rootView;
        }
    }


* */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new FriendsFragment();
            }
            else if (position == 1) {
                return new RequestsFragment();
            }
            else  {
                return new SettingsFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
