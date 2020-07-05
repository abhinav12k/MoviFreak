package com.apps.movifreak.Utils;

import android.util.Log;

import com.apps.movifreak.Model.Movie;
import com.apps.movifreak.Model.Review;
import com.apps.movifreak.Model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abhinav on 21/6/20.
 */
public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    public static ArrayList<Movie> parseMovieJsonArray(String json) throws JSONException {

        JSONObject searchResult = new JSONObject(json);

        int pageNo = searchResult.getInt("page");
        long total_results = searchResult.getLong("total_results");
        long total_pages = searchResult.getLong("total_pages");

        JSONArray results = searchResult.getJSONArray("results");

        ArrayList<Movie> movie_results = new ArrayList<>();

        for(int i=0;i<results.length();i++){
            JSONObject movie = results.getJSONObject(i);
            Movie new_movie = new Movie();

            new_movie.setAdult(movie.getBoolean("adult"));
            new_movie.setId(movie.getLong("id"));
            new_movie.setRating(movie.getInt("vote_average"));
            new_movie.setPopularity(movie.getDouble("popularity"));
            new_movie.setPoster_path(NetworkUtils.getPosterImageUrl(movie.getString("poster_path"),"w342"));
            new_movie.setLandscapeImageUrl(NetworkUtils.getLandscapeImageUrl(movie.getString("backdrop_path"),"w780"));
            new_movie.setVote_count(movie.getLong("vote_count"));
            new_movie.setVideo(movie.getBoolean("video"));
            new_movie.setTitle(movie.getString("title"));
            new_movie.setSummary(movie.getString("overview"));
            new_movie.setRelease_date(movie.getString("release_date"));

            JSONArray genreIds = movie.getJSONArray("genre_ids");

            ArrayList<Integer> genreIdList = new ArrayList<>();
//            Log.d(TAG,"genreId Size: "+genreIds.length());
            for(int j=0;j<genreIds.length();j++){
                try {
                    genreIdList.add(j,genreIds.getInt(j));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            new_movie.setGenre_ids(genreIdList);

            movie_results.add(i, new_movie);
        }

        return movie_results;
    }

    public static ArrayList<Trailer> parseTrailerJsonArray(String json) throws JSONException {

        JSONObject searchResult = new JSONObject(json);
        long id = searchResult.getLong("id");

        JSONArray resultArray = searchResult.getJSONArray("results");

        ArrayList<Trailer> trailers = new ArrayList<>();

        for(int i=0;i<resultArray.length();i++){

            JSONObject trailer = resultArray.getJSONObject(i);

            Trailer new_trailer = new Trailer();

            new_trailer.setId(trailer.getString("id"));
            new_trailer.setKey(trailer.getString("key"));
            new_trailer.setName(trailer.getString("name"));

//            Log.d(TAG,new_trailer.toString());
            trailers.add(i,new_trailer);
        }

        return trailers;
    }

    public static ArrayList<Review> parseReviewJsonArray(String json) throws JSONException {

        JSONObject searchResult = new JSONObject(json);
        long id = searchResult.getLong("id");
        int page = searchResult.getInt("page");
        int totalPages = searchResult.getInt("total_pages");

        JSONArray resultArray = searchResult.getJSONArray("results");

        ArrayList<Review> reviews = new ArrayList<>();

        for(int i=0;i<resultArray.length();i++){

            JSONObject review = resultArray.getJSONObject(i);

            Review new_review = new Review();

            new_review.setAuthor(review.getString("author"));
            new_review.setContent(review.getString("content"));
            new_review.setId(review.getString("id"));
            new_review.setUrl(review.getString("url"));

//            Log.d(TAG,new_review.toString());
            reviews.add(i,new_review);
        }

        return reviews;
    }

}
