package com.apps.movifreak;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.apps.movifreak.Adapter.MovieAdapter;
import com.apps.movifreak.Model.Movie;
import com.apps.movifreak.Utils.JsonUtils;
import com.apps.movifreak.Utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up toolbar
        Toolbar main_toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Pop Movies");

        //getting movies list asynchronously
        new getMoviesTask().execute(NetworkUtils.buildUrlForGrid("popular",getString(R.string.api_key),"en-US",1));

    }

    public class getMoviesTask extends AsyncTask<URL,Void, ArrayList<Movie>>{

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
            //Setting up recycler view
            mRecyclerView = (RecyclerView) findViewById(R.id.rv_thumbnails);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,3,RecyclerView.VERTICAL,false);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            MovieAdapter myAdapter = new MovieAdapter(movies);
            mRecyclerView.setAdapter(myAdapter);

        }
    }

}