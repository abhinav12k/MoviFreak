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
    private final static String MOVIES_DB_BASE_URL = "https://api.themoviedb.org/3/movie";

    private final static String SEARCH_RESULT_LANG = "language";

    private final static String API_KEY = "api_key";

    private final static String PAGE_NO = "page";

    //for getting url ready for main grid layout
    public static URL buildUrlForGrid(String typeOfMovie,String api_key,String searchLanguage,long pageNumber) {
        //typeOfMovie - latest/top_rated/popular

        String searchBaseUrl = MOVIES_DB_BASE_URL+"/"+typeOfMovie;

        Uri builtUri = Uri.parse(searchBaseUrl).buildUpon()
                .appendQueryParameter(API_KEY,api_key)
                .appendQueryParameter(SEARCH_RESULT_LANG,searchLanguage)
                .appendQueryParameter(PAGE_NO,String.valueOf(pageNumber))
                .build();

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

        HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
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
    public static String getImageUrl(String poster_path,String sizeOfImage){

        //w185 for thumbnail size poster image
        String BASE_IMAGE_URL ="https://image.tmdb.org/t/p/"+sizeOfImage+"/";
        String imageUrl = BASE_IMAGE_URL+poster_path;
        return imageUrl;

    }


}

