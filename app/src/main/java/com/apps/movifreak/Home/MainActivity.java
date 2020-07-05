package com.apps.movifreak.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

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
    private ArrayList<Movie> totalMovies = new ArrayList<>();
    private MovieAdapter myAdapter;
    private String typeOfMovie = "popular";

    //Bottom navigation view
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up toolbar
        Toolbar main_toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);
        main_toolbar.setTitleTextColor(getResources().getColor(R.color.colorRed));

        //bottom navigation view
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Pop Movies");

        //getting movies list asynchronously
        new getMoviesTask().execute(NetworkUtils.buildUrlForGrid(typeOfMovie,getString(R.string.api_key),"en-US",pageNo));

        //Setting up recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_thumbnails);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,3,RecyclerView.VERTICAL,false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        myAdapter = new MovieAdapter(totalMovies);
        mRecyclerView.setAdapter(myAdapter);


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){
                    pageNo++;
                    Log.d(TAG,"Page no: "+pageNo);
                    new getMoviesTask().execute(NetworkUtils.buildUrlForGrid(typeOfMovie,getString(R.string.api_key),"en-US",pageNo));
                    myAdapter.updateDataSet(totalMovies);
                }

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.most_pop:
                        if(!typeOfMovie.equals("popular")) {
                            totalMovies.clear();
                            typeOfMovie = "popular";
                            new getMoviesTask().execute(NetworkUtils.buildUrlForGrid(typeOfMovie, getString(R.string.api_key), "en-US", 1));
                            actionBar.setTitle("Pop Movies");
                            myAdapter.updateDataSet(totalMovies);
                        }
                        return true;

                    case R.id.top_rated:
                        if(!typeOfMovie.equals("top_rated")) {
                            totalMovies.clear();
                            typeOfMovie = "top_rated";
                            new getMoviesTask().execute(NetworkUtils.buildUrlForGrid(typeOfMovie, getString(R.string.api_key), "en-US", 1));
                            actionBar.setTitle("Top Rated Movies");
                            myAdapter.updateDataSet(totalMovies);
                        }
                        return true;


                    default:
                        return false;
                }
            }
        });

    }


    private class getMoviesTask extends AsyncTask<URL,Void, ArrayList<Movie>>{

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL url = urls[0];

            ArrayList<Movie> movieArrayList = new ArrayList<>();


            try {
                String receivedJson =  NetworkUtils.getResponseFromUrl(url);
                movieArrayList = JsonUtils.parseMovieJsonArray(receivedJson);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return movieArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {

            totalMovies.addAll(movies);
        }
    }
}