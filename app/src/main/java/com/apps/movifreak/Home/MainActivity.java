package com.apps.movifreak.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.movifreak.Adapter.MovieAdapter;
import com.apps.movifreak.Model.Movie;
import com.apps.movifreak.R;
import com.apps.movifreak.Utils.JsonUtils;
import com.apps.movifreak.Utils.NetworkUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ActionBar actionBar;

    private int pageNo = 1;
    private int activeId;
    private String returnActivity = "";
    private ArrayList<Movie> totalMovies = new ArrayList<>();
    private MovieAdapter myAdapter = new MovieAdapter(this);
    private String typeOfMovie = "popular";
    private boolean isSearchActive = false;
    private int searchPage = 1;
    private String searchedMovie;

    //Widgets
    private BottomNavigationView bottomNavigationView;
    private Toolbar main_toolbar;
    private TextView pageTitle;
    private ImageView searchIcon;
    private RelativeLayout backArrow;
    private EditText mSearchParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up toolbar
        main_toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);
//        main_toolbar.setTitleTextColor(getResources().getColor(R.color.colorRed));

        //bottom navigation view
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        actionBar = getSupportActionBar();
//        actionBar.setTitle("Pop Movies");

        //Widgets
        pageTitle = findViewById(R.id.page_title);
        searchIcon = findViewById(R.id.ic_search);
        mSearchParam = findViewById(R.id.search);
        backArrow = findViewById(R.id.ivBackArrow);

        pageTitle.setText("Pop Movies");

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSearchTextListener();
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchActive = false;
                mSearchParam.setText("");
                totalMovies.clear();
                myAdapter.notifyDataSetChanged();
                new getMoviesTask().execute(NetworkUtils.buildUrlForGrid(typeOfMovie, getString(R.string.api_key), "en-US", pageNo));
                Log.d(TAG, "Page no start: " + pageNo);
                hideSoftKeyboard();
            }
        });


        if (savedInstanceState != null && savedInstanceState.containsKey("movies_list")) {
            activeId = savedInstanceState.getInt("type_of_movie");
            totalMovies = savedInstanceState.getParcelableArrayList("movies_list");
        } else {
            //getting movies list asynchronously
            new getMoviesTask().execute(NetworkUtils.buildUrlForGrid(typeOfMovie, getString(R.string.api_key), "en-US", pageNo));
            Log.d(TAG, "Page no start: " + pageNo);
        }

        //Setting up recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_thumbnails);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 3, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        myAdapter.addMovies(totalMovies);
        mRecyclerView.setAdapter(myAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {

                    if (isSearchActive) {
                        searchPage++;
                        Log.d(TAG, "Search Page no: " + searchPage);
                        new getMoviesTask().execute(NetworkUtils.buildUrlForSearch(getString(R.string.api_key), searchedMovie.trim(), searchPage));
                    } else {

                        pageNo++;
                        Log.d(TAG, "Page no: " + pageNo);
                        new getMoviesTask().execute(NetworkUtils.buildUrlForGrid(typeOfMovie, getString(R.string.api_key), "en-US", pageNo));
                    }
                }

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.most_pop:
                        if (!typeOfMovie.equals("popular")) {
                            pageNo = 1;
                            totalMovies.clear();
                            typeOfMovie = "popular";
                            new getMoviesTask().execute(NetworkUtils.buildUrlForGrid(typeOfMovie, getString(R.string.api_key), "en-US", 1));
//                            actionBar.setTitle("Pop Movies");
                            pageTitle.setText("Pop Movies");
                        }
                        return true;

                    case R.id.top_rated:
                        if (!typeOfMovie.equals("top_rated")) {
                            pageNo = 1;
                            totalMovies.clear();
                            typeOfMovie = "top_rated";
                            new getMoviesTask().execute(NetworkUtils.buildUrlForGrid(typeOfMovie, getString(R.string.api_key), "en-US", 1));
//                            actionBar.setTitle("Top Rated Movies");
                            pageTitle.setText("Top Rated Movies");
                        }
                        return true;

                    case R.id.fav_movies:
                        Intent favIntent = new Intent(MainActivity.this, favActivity.class);
                        startActivity(favIntent);
                        returnActivity = "favActivity";
                        return true;

                    default:
                        return false;
                }
            }
        });

    }

    private void hideSoftKeyboard() {
        pageTitle.setVisibility(View.VISIBLE);
        backArrow.setVisibility(View.GONE);
        mSearchParam.setVisibility(View.GONE);

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(main_toolbar.getApplicationWindowToken(), 0);
    }

    private void initSearchTextListener() {

        pageTitle.setVisibility(View.GONE);
        backArrow.setVisibility(View.VISIBLE);
        mSearchParam.setVisibility(View.VISIBLE);
        mSearchParam.setHint("Search...");
        mSearchParam.requestFocus();
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                main_toolbar.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);

        performSearch();

    }

    private void performSearch() {

        mSearchParam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.toString().trim() != "") {
                    //Perform search operation from MoviesDb
                    isSearchActive = true;
                    searchPage = 1;
                    totalMovies.clear();
                    myAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Search Parameter: " + s.toString());
                    searchedMovie = s.toString().trim();
                    new getMoviesTask().execute(NetworkUtils.buildUrlForSearch(getString(R.string.api_key), searchedMovie, searchPage));


                }
            }
        });

    }

    private class getMoviesTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL url = urls[0];

            ArrayList<Movie> movieArrayList = new ArrayList<>();


            try {
                String receivedJson = NetworkUtils.getResponseFromUrl(url);
                movieArrayList = JsonUtils.parseMovieJsonArray(receivedJson);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return movieArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies != null && !movies.isEmpty()) {
                totalMovies.addAll(movies);
                myAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putParcelableArrayList("movies_list", totalMovies);
        outState.putInt("type_of_movie", bottomNavigationView.getSelectedItemId());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onResume() {
        if (returnActivity.equals("favActivity")) {
            bottomNavigationView.setSelectedItemId(R.id.most_pop);
        } else
            bottomNavigationView.setSelectedItemId(activeId);
        super.onResume();
    }
}