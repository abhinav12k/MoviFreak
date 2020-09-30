package com.apps.movifreak.Utils;

import android.net.Uri;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by abhinav on 21/6/20.
 */
public class NetworkUtils {

    //base url for movies
    private final static String BASE_URL_MOVIES = "https://api.themoviedb.org/3/movie";

    //base url for tv shows
    private final static String BASE_URL_TV = "https://api.themoviedb.org/3/tv";

    private final static String SEARCH_RESULT_LANG = "language";

    private final static String API_KEY = "api_key";

    private final static String PAGE_NO = "page";

    private final static String QUERY_PARAMETER = "query";

    private final static String INCLUDE_ADULT = "include_adult";

    private final static String REGION = "region";

    private final static String YEAR = "year";

    //for getting url ready for main grid movies
    public static URL buildUrlForMovies(String typeOfMovie, String api_key, String searchLanguage, long pageNumber) {
        //typeOfMovie - latest/top_rated/popular

        String searchBaseUrl = BASE_URL_MOVIES + "/" + typeOfMovie;
        Uri builtUri;
        if(searchLanguage.equals("")){
            builtUri = Uri.parse(searchBaseUrl).buildUpon()
                    .appendQueryParameter(API_KEY, api_key)
                    .appendQueryParameter(PAGE_NO, String.valueOf(pageNumber))
                    .build();
        }else {

            builtUri = Uri.parse(searchBaseUrl).buildUpon()
                    .appendQueryParameter(API_KEY, api_key)
                    .appendQueryParameter(SEARCH_RESULT_LANG, searchLanguage)
                    .appendQueryParameter(PAGE_NO, String.valueOf(pageNumber))
                    .build();
        }

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }

    //for getting url ready for trailers/reviews/images
    public static URL buildUrlForDetailActivity(long Id, String type, String api_key) {
        //type - videos/reviews

        String searchUrl = BASE_URL_MOVIES + "/" + Id + "/" + type;

        Uri buildUri = Uri.parse(searchUrl).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .build();

        URL url = null;

        try {
            url = new URL(buildUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }

    //for getting url ready for searching movie
    public static URL buildUrlForSearch(String api_key, String query, long pageNumber) {
        String searchUrl = "https://api.themoviedb.org/3/search/movie";

        //normal search without filters - only parameter search
        Uri buildUri = Uri.parse(searchUrl).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .appendQueryParameter(QUERY_PARAMETER, query)
                .appendQueryParameter(PAGE_NO, String.valueOf(pageNumber))
                .build();

        URL url = null;

        try {
            url = new URL(buildUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }

    //for generating url for tvShows
    public static URL buildUrlForTV(String typeOfTvShow, String api_key, String searchLanguage, long pageNumber){
        //typeOfTvShow - latest/top_rated/popular

        String searchBaseUrl = BASE_URL_MOVIES + "/" + typeOfTvShow;
        Uri builtUri;
        if(searchLanguage.equals("")){
            builtUri = Uri.parse(searchBaseUrl).buildUpon()
                    .appendQueryParameter(API_KEY, api_key)
                    .appendQueryParameter(PAGE_NO, String.valueOf(pageNumber))
                    .build();
        }else {

            builtUri = Uri.parse(searchBaseUrl).buildUpon()
                    .appendQueryParameter(API_KEY, api_key)
                    .appendQueryParameter(SEARCH_RESULT_LANG, searchLanguage)
                    .appendQueryParameter(PAGE_NO, String.valueOf(pageNumber))
                    .build();
        }

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }

    //for getting json response
    public static String getResponseFromUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    //for getting poster image url
    public static String getPosterImageUrl(String poster_path, String sizeOfImage) {

        //w185 for thumbnail size poster image
        String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/" + sizeOfImage + "/";
        String imageUrl = BASE_IMAGE_URL + poster_path;
        return imageUrl;

    }

    //for getting poster for detail activity
    public static String getLandscapeImageUrl(String backdrop_path, String sizeOfImage) {

        //w780 for landscape size image
        String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/" + sizeOfImage + "/";
        String imageUrl = BASE_IMAGE_URL + backdrop_path;
        return imageUrl;

    }
}

