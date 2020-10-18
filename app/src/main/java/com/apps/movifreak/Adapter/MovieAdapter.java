package com.apps.movifreak.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.movifreak.Home.DetailActivity;
import com.apps.movifreak.Model.Movie;
import com.apps.movifreak.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abhinav on 21/6/20.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    /**
     * Type of Movie -> 1.pop
     *                  2.top
     *                  3.search
     * */

    private ArrayList<Movie> topMovieList = new ArrayList<>();
    private ArrayList<Movie> popMovieList = new ArrayList<>();
    private ArrayList<Movie> searchMovieList = new ArrayList<>();
    private String typeOfMovie = "pop";

    private ArrayList<Movie> listInUse;
    private Context mContext;

    public MovieAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addTopMovies(ArrayList<Movie> mList,String type) {
        this.topMovieList.addAll(mList);
        this.typeOfMovie = type;
        listInUse = this.topMovieList;
        notifyDataSetChanged();
    }

    public void updateTopMovieList(ArrayList<Movie> newList,String type) {
        this.topMovieList.clear();
        this.topMovieList = newList;
        this.typeOfMovie = type;
        listInUse = this.topMovieList;
        notifyDataSetChanged();
    }

    public void addPopMovies(ArrayList<Movie> popList,String type){
        this.popMovieList.addAll(popList);
        this.typeOfMovie = type;
        clearSearchMovieList();
        listInUse = this.popMovieList;
        notifyDataSetChanged();
    }

    public void updatePopMovieList(ArrayList<Movie> popList,String type){
        this.popMovieList.clear();
        this.popMovieList = popList;
        this.typeOfMovie = type;
        listInUse = this.popMovieList;
        notifyDataSetChanged();
    }

    public void addSearchMovieList(ArrayList<Movie> searchMovieList,String type){
        this.searchMovieList.addAll(searchMovieList);
        this.typeOfMovie = type;
        listInUse = this.searchMovieList;
        notifyDataSetChanged();
    }

    public void updateSearchMovieList(ArrayList<Movie> searchMovieList,String type){
        this.searchMovieList.clear();
        this.searchMovieList = searchMovieList;
        this.typeOfMovie = type;
        listInUse = this.searchMovieList;
        notifyDataSetChanged();
    }

    public void clearTopMovieList(){
        this.topMovieList.clear();
        notifyDataSetChanged();
    }

    public void clearSearchMovieList(){
        this.searchMovieList.clear();
        notifyDataSetChanged();
    }

    public void clearPopMovieList(){
        this.popMovieList.clear();
        notifyDataSetChanged();
    }

    public void clearAllLists() {
        this.topMovieList.clear();
        this.popMovieList.clear();
        this.searchMovieList.clear();
        this.listInUse.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.single_movie_tile, parent, false);
        return new MovieAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieAdapter.ViewHolder holder, final int position) {

        switch (typeOfMovie){
            case "pop":
                listInUse = popMovieList;
                break;
            case "top":
                listInUse = topMovieList;
                break;
            case "search":
                listInUse = searchMovieList;
                break;
        }

        final String tileUrl = listInUse.get(position).getPoster_path();

        Picasso.with(mContext).load(tileUrl).networkPolicy(NetworkPolicy.OFFLINE).into(holder.movie_tile, new Callback() {
            @Override
            public void onSuccess() {
                holder.mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {
                //Try again online if cache failed
                Picasso.with(mContext)
                        .load(tileUrl)
                        .into(holder.movie_tile, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.mProgressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError() {
                                Log.d(TAG, "Error in loading images");
                            }
                        });
            }
        });

        try {
            final String movieRating = String.valueOf(listInUse.get(position).getRating());
            final String movieYear = listInUse.get(position).getRelease_date().substring(0, 4);

            holder.movie_rating.setText(movieRating);
            holder.movie_year.setText(movieYear);
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.movie_tile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Movie currentMovie = listInUse.get(position);
                Intent DetailIntent = new Intent(mContext, DetailActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("movie_details", currentMovie);
                DetailIntent.putExtra("movie_bundle", b);
                DetailIntent.putExtra("movie_details", "from Movie Fragment");
                mContext.startActivity(DetailIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        switch (typeOfMovie){
            case "pop":
                listInUse = popMovieList;
                break;
            case "top":
                listInUse = topMovieList;
                break;
            case "search":
                listInUse = searchMovieList;
                break;
        }
        return listInUse.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView movie_tile;
        public TextView movie_rating, movie_year;
        public ProgressBar mProgressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            movie_tile = itemView.findViewById(R.id.movie_thumbnail);
            movie_rating = itemView.findViewById(R.id.tv_rating);
            movie_year = itemView.findViewById(R.id.tv_rating_year);
            mProgressBar = itemView.findViewById(R.id.progress_bar);

        }
    }

}
