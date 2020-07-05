package com.apps.movifreak.Database;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by abhinav on 5/7/20.
 */
@Entity(tableName = "favMovie")
public class FavMovie {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long movieId;
    private String title;
    private String overview;  //summary
    private int vote_average;     //rating
    private String release_date;
    private boolean adult;
    private String poster_path;
    private long vote_count;
    private double popularity;
    private String backdrop;    //landscape picture
    private Date dateSaved;

    @Ignore
    public FavMovie(String title, String overview, int vote_average, String release_date, boolean adult, String poster_path, long vote_count, double popularity, String backdrop,Date dateSaved,long movieId) {
        this.title = title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.adult = adult;
        this.poster_path = poster_path;
        this.vote_count = vote_count;
        this.popularity = popularity;
        this.backdrop = backdrop;
        this.dateSaved = dateSaved;
        this.movieId = movieId;
    }

    public FavMovie(int id, String title, String overview, int vote_average, String release_date, boolean adult, String poster_path, long vote_count, double popularity, String backdrop, Date dateSaved,long movieId) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.adult = adult;
        this.poster_path = poster_path;
        this.vote_count = vote_count;
        this.popularity = popularity;
        this.backdrop = backdrop;
        this.dateSaved = dateSaved;
        this.movieId = movieId;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public Date getDateSaved() {
        return dateSaved;
    }

    public void setDateSaved(Date dateSaved) {
        this.dateSaved = dateSaved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getVote_average() {
        return vote_average;
    }

    public void setVote_average(int vote_average) {
        this.vote_average = vote_average;
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

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

}
