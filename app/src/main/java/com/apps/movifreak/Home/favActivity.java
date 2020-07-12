package com.apps.movifreak.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.apps.movifreak.Adapter.FavAdapter;
import com.apps.movifreak.Adapter.MovieAdapter;
import com.apps.movifreak.Database.AppDatabase;
import com.apps.movifreak.Database.FavMovie;
import com.apps.movifreak.R;
import com.apps.movifreak.Utils.AppExecutors;
import com.apps.movifreak.Utils.NetworkUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class favActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FavAdapter myAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        mDb = AppDatabase.getInstance(getApplicationContext());

        //setting up toolbar
        Toolbar main_toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);
        main_toolbar.setContentInsetStartWithNavigation(0);
        main_toolbar.setTitleTextColor(getResources().getColor(R.color.colorRed));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Favorite Movies");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Setting up recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_thumbnails);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(favActivity.this, 3, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        myAdapter = new FavAdapter(favActivity.this);
        mRecyclerView.setAdapter(myAdapter);

        //getting fav movie list
        LiveData<List<FavMovie>> movieList = mDb.movieDao().loadAllMoviesById();
        movieList.observe(this, new Observer<List<FavMovie>>() {
            @Override
            public void onChanged(List<FavMovie> favMovies) {
                myAdapter.addMovies(favMovies);
            }
        });


    }
}