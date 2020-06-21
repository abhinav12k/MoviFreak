package com.apps.movifreak.Utils;

import android.util.Log;

import com.apps.movifreak.MainActivity;
import com.apps.movifreak.Model.Movie;

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
            new_movie.setPoster_path(NetworkUtils.getImageUrl(movie.getString("poster_path"),"w342"));
            new_movie.setVote_count(movie.getLong("vote_count"));
            new_movie.setVideo(movie.getBoolean("video"));
            new_movie.setTitle(movie.getString("title"));
            new_movie.setSummary(movie.getString("overview"));
            new_movie.setRelease_date(movie.getString("release_date"));

            JSONArray genreIds = movie.getJSONArray("genre_ids");

            ArrayList<Integer> genreIdList = new ArrayList<>();
            Log.d(TAG,"genreId Size: "+genreIds.length());
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

}
