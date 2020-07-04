package com.apps.movifreak.Home;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.movifreak.Model.Movie;
import com.apps.movifreak.Model.Review;
import com.apps.movifreak.Model.Trailer;
import com.apps.movifreak.R;
import com.apps.movifreak.Utils.JsonUtils;
import com.apps.movifreak.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

//    private TextView movie_title;
    private TextView movie_releaseDate;
    private TextView movie_language;
    private TextView movie_rating;
    private TextView movie_synopsis;

    private ArrayList<Trailer> trailerArrayList;
    private ArrayList<Review> reviewArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //getting intent from main activity
        Intent incomingIntent = getIntent();
        Movie clickedMovie = (Movie) incomingIntent.getSerializableExtra("movie_details");

        //setting up widgets
//        movie_title =(TextView) findViewById(R.id.movie_title);
        movie_synopsis = (TextView) findViewById(R.id.movie_synopsis);
        movie_releaseDate = (TextView) findViewById(R.id.movie_releaseDate);
        movie_rating = (TextView) findViewById(R.id.movie_rating);
        movie_language = (TextView) findViewById(R.id.movie_language);

        //Movie Details
        String title = clickedMovie.getTitle();
        String synopsis = clickedMovie.getSummary();
        String releaseDate = clickedMovie.getRelease_date();
        String rating = String.valueOf(clickedMovie.getRating());
        String lang = clickedMovie.getOriginal_language();
        long id = clickedMovie.getId();

        if(lang==null){
            lang = "en";
        }

        //filling up details
//        movie_title.setText(title);
        movie_language.setText(lang);
        movie_rating.setText(rating);
        movie_releaseDate.setText(releaseDate);
        movie_synopsis.setText(synopsis);

        //setting toolbar
        Toolbar main_toolbar = findViewById(R.id.main_toolbar);
        main_toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(main_toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);

        String movie_url = clickedMovie.getPoster_path();

        ImageView background = findViewById(R.id.detail_bg);
        Picasso.with(this).load(movie_url).into(background);

        getTrailer_and_reviews(id);

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
        }
    }

}