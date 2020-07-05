package com.apps.movifreak.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by abhinav on 21/6/20.
 */
public class Movie implements Serializable {

    private String title;
    private String overview;  //summary
    private int vote_average;     //rating
    private String release_date;
    private boolean adult;
    private long id;
    private String poster_path;
    private long vote_count;
    private double popularity;
    private boolean video;
    private ArrayList<Integer> genre_ids;
    private String backdrop_path;
    private String original_language;
    private String original_title;
    private String landscapeImageUrl;

    public String getLandscapeImageUrl() {
        return landscapeImageUrl;
    }

    public void setLandscapeImageUrl(String landscapeImageUrl) {
        this.landscapeImageUrl = landscapeImageUrl;
    }

    //No arg constructor
    public Movie(){

    }

    public Movie(String title, String summary, int rating, String release_date, boolean adult, long id, String poster_path, long vote_count, double popularity, boolean video, String backdrop_path, String original_language, String original_title, ArrayList<Integer> genre_ids,String landscapeImageUrl) {
        this.title = title;
        this.overview = summary;
        this.vote_average = rating;
        this.release_date = release_date;
        this.adult = adult;
        this.id = id;
        this.poster_path = poster_path;
        this.vote_count = vote_count;
        this.popularity = popularity;
        this.video = video;
        this.backdrop_path = backdrop_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.genre_ids = genre_ids;
        this.landscapeImageUrl = landscapeImageUrl;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", vote_average=" + vote_average +
                ", release_date='" + release_date + '\'' +
                ", adult=" + adult +
                ", id=" + id +
                ", poster_path='" + poster_path + '\'' +
                ", vote_count=" + vote_count +
                ", popularity=" + popularity +
                ", video=" + video +
                ", genre_ids=" + genre_ids +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", original_language='" + original_language + '\'' +
                ", original_title='" + original_title + '\'' +
                '}';
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return overview;
    }

    public void setSummary(String summary) {
        this.overview = summary;
    }

    public int getRating() {
        return vote_average;
    }

    public void setRating(int rating) {
        this.vote_average = rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public long getVote_count() {
        return vote_count;
    }

    public void setVote_count(long vote_count) {
        this.vote_count = vote_count;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(ArrayList<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }
}
