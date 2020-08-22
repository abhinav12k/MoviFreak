package com.apps.movifreak.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.movifreak.Adapter.ReviewAdapter;
import com.apps.movifreak.Adapter.TrailerAdapter;
import com.apps.movifreak.Database.AppDatabase;
import com.apps.movifreak.Database.FavMovie;
import com.apps.movifreak.Model.Movie;
import com.apps.movifreak.Model.Review;
import com.apps.movifreak.Model.Trailer;
import com.apps.movifreak.R;
import com.apps.movifreak.Utils.AppExecutors;
import com.apps.movifreak.Utils.JsonUtils;
import com.apps.movifreak.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

//    private TextView movie_title;
    private TextView movie_releaseDate;
    private TextView movie_language;
    private TextView movie_rating;
    private TextView movie_synopsis;
    private TextView trailerHeading;
    private TextView reviewHeading;
    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewsRecyclerView;
    private ImageView fav_image_empty;
    private ImageView fav_image_filled;
    private ImageView posterImage;

    private ArrayList<Trailer> trailerArrayList;
    private ArrayList<Review> reviewArrayList;

    //Database
    private AppDatabase mDb;

    //getting movie
    private Movie clickedMovie;
    private FavMovie clickedFavMovie;

    //Movie Details
    private String title,synopsis,releaseDate,rating,lang,movie_url,poster_path;
    private long id;

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //instantiating database
        mDb = AppDatabase.getInstance(getApplicationContext());

        //setting up widgets
//        movie_title =(TextView) findViewById(R.id.movie_title);
        movie_synopsis = (TextView) findViewById(R.id.movie_synopsis);
        movie_releaseDate = (TextView) findViewById(R.id.movie_releaseDate);
        movie_rating = (TextView) findViewById(R.id.movie_rating);
//        movie_language = (TextView) findViewById(R.id.movie_language);
        trailerHeading = (TextView) findViewById(R.id.trailerHeading);
        reviewHeading = (TextView) findViewById(R.id.reviewHeading);
        trailerRecyclerView = (RecyclerView) findViewById(R.id.trailerList);
        reviewsRecyclerView = (RecyclerView) findViewById(R.id.reviewsList);
        fav_image_empty = (ImageView) findViewById(R.id.fav_btn_empty);
        fav_image_filled = (ImageView) findViewById(R.id.fav_btn_filled);
        posterImage = (ImageView) findViewById(R.id.poster);

        //getting intent from main activity
        Intent incomingIntent = getIntent();
        if(incomingIntent.hasExtra("movie_details")) {
            Bundle bundle = incomingIntent.getBundleExtra("movie_bundle");
            clickedMovie = bundle.getParcelable("movie_details");

            Log.d(TAG, clickedMovie.toString());

            //Movie Details
            title = clickedMovie.getTitle();
            synopsis = clickedMovie.getSummary();
            releaseDate = clickedMovie.getRelease_date();
            rating = String.valueOf(clickedMovie.getRating());
            lang = clickedMovie.getOriginal_language();
            id = clickedMovie.getId();
            movie_url = clickedMovie.getLandscapeImageUrl();
            poster_path = clickedMovie.getPoster_path();

            if(lang==null){
                lang = "en";
            }

        }else{

            try {
                Bundle bundle = incomingIntent.getBundleExtra("fav_movie_bundle");
                clickedFavMovie = bundle.getParcelable("fav_movie_details");

                Log.d(TAG, clickedFavMovie.toString());

                //Movie Details
                title = clickedFavMovie.getTitle();
                synopsis = clickedFavMovie.getOverview();
                releaseDate = clickedFavMovie.getRelease_date();
                rating = String.valueOf(clickedFavMovie.getVote_average());
                lang = clickedFavMovie.getOriginal_language();
                id = clickedFavMovie.getMovieId();
                movie_url = clickedFavMovie.getBackdrop();
                poster_path = clickedFavMovie.getPoster_path();

            }catch (Exception e){
                e.printStackTrace();
            }
            if(lang==null){
                lang = "en";
            }

        }

        //filling up details
//        movie_title.setText(title);
//        movie_language.setText(lang);
        movie_rating.setText(rating);
        movie_releaseDate.setText(releaseDate);
        movie_synopsis.setText(synopsis);

        //setting toolbar
        final Toolbar main_toolbar = findViewById(R.id.main_toolbar);
        main_toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(main_toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView background = findViewById(R.id.detail_bg);

        try {
            Picasso.with(this).load(movie_url).placeholder(R.drawable.bg).into(background);
            Picasso.with(this).load(poster_path).into(posterImage);
        }catch (Exception e){
            e.printStackTrace();
        }

        getTrailer_and_reviews(id);

        //checking if the movie is already added to favorite
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                FavMovie movie = mDb.movieDao().loadMovieById(id);
                if(movie!=null) {
                    fav_image_filled.setVisibility(View.VISIBLE);
                    fav_image_empty.setVisibility(View.GONE);
                }else{
                    fav_image_empty.setVisibility(View.VISIBLE);
                    fav_image_filled.setVisibility(View.GONE);
                }
            }
        });

        fav_image_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fav_image_empty.setVisibility(View.GONE);
                fav_image_filled.setVisibility(View.VISIBLE);

                //getting fav movie details
                Date date = new Date();

                final FavMovie favMovie = new FavMovie(clickedMovie.getTitle(),clickedMovie.getSummary(),clickedMovie.getRating(),clickedMovie.getRelease_date(),clickedMovie.isAdult(),clickedMovie.getPoster_path(),clickedMovie.getVote_count(),clickedMovie.getPopularity(),clickedMovie.getLandscapeImageUrl(),date,clickedMovie.getId(),clickedMovie.getOriginal_language());

                //save details to the database
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.movieDao().insertMovie(favMovie);
                    }
                });
                Toast.makeText(DetailActivity.this, "Added to collection!", Toast.LENGTH_SHORT).show();
            }
        });

        fav_image_filled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fav_image_filled.setVisibility(View.GONE);
                fav_image_empty.setVisibility(View.VISIBLE);

                //delete details from the database
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        FavMovie deleteMovie = mDb.movieDao().loadMovieById(id);
                        mDb.movieDao().deleteMovie(deleteMovie);
                    }
                });
                Toast.makeText(DetailActivity.this, "Removed from collection!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupReviewsList() {

        //setting up reviews list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailActivity.this,RecyclerView.VERTICAL,false);
        reviewsRecyclerView.setLayoutManager(linearLayoutManager);
        reviewsRecyclerView.setHasFixedSize(true);

        //setting up adapter
        ReviewAdapter reviewAdapter = new ReviewAdapter(DetailActivity.this,reviewArrayList);
        reviewsRecyclerView.setAdapter(reviewAdapter);
    }

    private void setupTrailerList() {
        //setting up trailer's list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailActivity.this,RecyclerView.HORIZONTAL,false);
        trailerRecyclerView.setLayoutManager(linearLayoutManager);
        trailerRecyclerView.setHasFixedSize(true);

        //setting up adapter
        TrailerAdapter adapter = new TrailerAdapter(DetailActivity.this,trailerArrayList);
        trailerRecyclerView.setAdapter(adapter);

    }

    private void getTrailer_and_reviews(long id) {

        new getTrailersTask().execute(NetworkUtils.buildUrlForDetailActivity(id,"videos",getString(R.string.api_key)));
        new getReviewsTask().execute(NetworkUtils.buildUrlForDetailActivity(id,"reviews",getString(R.string.api_key)));

    }

    private class getTrailersTask extends AsyncTask<URL,Void, ArrayList<Trailer>>{

        @Override
        protected ArrayList<Trailer> doInBackground(URL... urls) {

            ArrayList<Trailer> trailers = new ArrayList<>();

            URL url = urls[0];

            try{
                String receivedJson = NetworkUtils.getResponseFromUrl(url);
                trailers = JsonUtils.parseTrailerJsonArray(receivedJson);
            }catch (Exception e){
                e.printStackTrace();
            }

            return trailers;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailers) {
            trailerArrayList = trailers;
            if(!trailerArrayList.isEmpty()&&trailerArrayList.size()!=0)
                trailerHeading.setVisibility(View.VISIBLE);
            setupTrailerList();
        }
    }

    private class getReviewsTask extends AsyncTask<URL,Void,ArrayList<Review>>{

        @Override
        protected ArrayList<Review> doInBackground(URL... urls) {

            ArrayList<Review> reviews = new ArrayList<>();

            URL url = urls[0];

            try{

                String receivedJson = NetworkUtils.getResponseFromUrl(url);
                reviews = JsonUtils.parseReviewJsonArray(receivedJson);

            }catch (Exception e){
                e.printStackTrace();
            }

            return reviews;
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            reviewArrayList = reviews;
            if(reviewArrayList.size()!=0&&!reviews.isEmpty())
                reviewHeading.setVisibility(View.VISIBLE);
            setupReviewsList();
        }
    }

}