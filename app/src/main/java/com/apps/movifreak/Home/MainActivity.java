package com.apps.movifreak.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.movifreak.Adapter.MovieAdapter;
import com.apps.movifreak.Model.Movie;
import com.apps.movifreak.R;
import com.apps.movifreak.Utils.JsonUtils;
import com.apps.movifreak.Utils.MyReceiver;
import com.apps.movifreak.Utils.NetworkUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver myReceiver = null;
    private static final String TAG = MainActivity.class.getSimpleName();

    private ActionBar actionBar;

    private String returnActivity = "";
    private boolean doubleBackToExitPressedOnce = false;

    //Widgets
    private Toolbar main_toolbar;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //For continuously checking internet connection
        myReceiver = new MyReceiver();
        broadcastIntent();

        //setting up toolbar
        main_toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);
        actionBar = getSupportActionBar();

        //Widgets
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_movies);

//        ImageView internetIssue = findViewById(R.id.internet_issue);
//        LinearLayout baseLayout = findViewById(R.id.base_layout);

        //Check for internet connection
//        if(!isInternetAvailable()){
//            internetIssue.setVisibility(View.VISIBLE);
//            baseLayout.setVisibility(View.GONE);
////            Toast.makeText(this, "No Internet Connection please check your internet connection and try again", Toast.LENGTH_SHORT).show();
//        }else{
//            internetIssue.setVisibility(View.GONE);
//            baseLayout.setVisibility(View.VISIBLE);
//        }

        //Creating movie fragment and setting it as default
        final MovieFragment movieFragment = new MovieFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, movieFragment).commit();

        SwitchCompat drawerSwitch = (SwitchCompat) navigationView.getMenu().findItem(R.id.switch_item).getActionView();
        // Saving state of our app
        // using SharedPreferences
        SharedPreferences sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferences.edit();
        final boolean isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);

        // When user reopens the app
        // after applying dark/light mode
        if (isDarkModeOn) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);
            drawerSwitch.setChecked(true);
        } else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
            drawerSwitch.setChecked(false);
        }

        drawerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // if dark mode is off
                    // it will turn it on
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_YES);

                    // it will set isDarkModeOn
                    // boolean to true
                    editor.putBoolean(
                            "isDarkModeOn", true);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Dark Mode On ", Toast.LENGTH_SHORT).show();
                } else {
                    // if dark mode is on
                    // will turn it off
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_NO);
                    // it will set isDarkModeOn
                    // boolean to false
                    editor.putBoolean(
                            "isDarkModeOn", false);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Dark Mode Off", Toast.LENGTH_SHORT).show();

                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_movies:
//                        Toast.makeText(getApplicationContext(), "Movies Clicked", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, movieFragment).commit();
                        return true;
                    case R.id.nav_tv:
//                        Toast.makeText(getApplicationContext(), "Tv Clicked", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TvShowFragment()).commit();
                        return true;
                    case R.id.nav_github:
                        Toast.makeText(getApplicationContext(), "Navigating to github", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_linkedin:
                        Toast.makeText(getApplicationContext(), "Navigating to linkedin", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, main_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorText));
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setDefaultTitle();

    }

    private void setDefaultTitle() {
        main_toolbar.setTitleTextColor(getResources().getColor(R.color.colorRed));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "menu options created");
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.search_icon) {
            //toggle hamburger icon with search bar
            showSearchBar();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSearchBar() {


        // inflate the customized Action Bar View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.layout_action_bar, null);
        final EditText searchParam = v.findViewById(R.id.search);

        if (actionBar != null) {

            // enable the customized view and disable title
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

            actionBar.setCustomView(v);
            // remove Burger Icon
            main_toolbar.setNavigationIcon(null);

            searchParam.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(
                    main_toolbar.getApplicationWindowToken(),
                    InputMethodManager.SHOW_FORCED, 0);

            //initialising text search
            final MovieFragment searchFunction = (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            searchFunction.initSearchTextListener();

            // add click listener to the back arrow icon
            v.findViewById(R.id.ivBackArrow).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    searchParam.setText("");
                    searchFunction.removeSearchResultsAndRefresh();

                    hideSoftKeyboard();
                    setDefaultTitle();

                    // reverse back the show
                    actionBar.setDisplayShowCustomEnabled(false);
                    actionBar.setDisplayShowTitleEnabled(true);
                    //get the Drawer and DrawerToggle from Main Activity
                    // set them back as normal
                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                            MainActivity.this, drawer, main_toolbar, R.string.navigation_drawer_open,
                            R.string.navigation_drawer_close);
                    toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorText));
                    // All that to re-synchronize the Drawer State
                    toggle.syncState();
                }
            });
        }
    }


    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(main_toolbar.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            } else {

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            }
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 3000);
        }
    }

    public void broadcastIntent() {
        registerReceiver(myReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(myReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}