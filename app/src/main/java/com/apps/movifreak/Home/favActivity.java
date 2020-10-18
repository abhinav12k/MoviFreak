package com.apps.movifreak.Home;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.movifreak.Adapter.FavAdapter;
import com.apps.movifreak.Database.AppDatabase;
import com.apps.movifreak.Database.FavMovie;
import com.apps.movifreak.Database.FavTvShow;
import com.apps.movifreak.R;

import java.util.List;

public class favActivity extends AppCompatActivity {

    private static final String TAG = favActivity.class.getSimpleName();
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
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_backarrow);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorText), PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(upArrow);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Setting up recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_thumbnails);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(favActivity.this, 3, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        myAdapter = new FavAdapter(this);
        mRecyclerView.setAdapter(myAdapter);

        Intent incomingIntent = getIntent();
        if(incomingIntent.hasExtra("from_fragment")){
            Log.d(TAG,"from: "+incomingIntent.getStringExtra("from_fragment"));

            if(incomingIntent.getStringExtra("from_fragment").equals("TvShowFragment")){

                actionBar.setTitle("Favorite TvShows");

                //getting fav tvShows list
                LiveData<List<FavTvShow>> tvShowList = mDb.tvShowDao().loadAllTvShowById();


                tvShowList.observe(this, new Observer<List<FavTvShow>>() {
                    @Override
                    public void onChanged(List<FavTvShow> favTvShows) {
                        Log.d(TAG,"tvShowList: "+favTvShows);
                        myAdapter.addTvShows(favTvShows,"tvShow");
                    }
                });

            }else if(incomingIntent.getStringExtra("from_fragment").equals("MovieFragment")){

                actionBar.setTitle("Favorite Movies");

                //getting fav movie list
                LiveData<List<FavMovie>> movieList = mDb.movieDao().loadAllMoviesById();

                movieList.observe(this, new Observer<List<FavMovie>>() {
                    @Override
                    public void onChanged(List<FavMovie> favMovies) {
                        Log.d(TAG,"tvShowList: "+favMovies);
                        myAdapter.addMovies(favMovies,"movie");
                    }
                });

            }

        }


    }
}