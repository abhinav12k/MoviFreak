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

    private ArrayList<Movie> movieList = new ArrayList<>();
    private Context mContext;

    public MovieAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void addMovies(ArrayList<Movie> mList){
        this.movieList = mList;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.single_movie_tile,parent,false);
        return new MovieAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieAdapter.ViewHolder holder, final int position) {

        final String tileUrl = movieList.get(position).getPoster_path();

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
                                Log.d(TAG,"Error in loading images");
                            }
                        });
            }
        });

        holder.movie_tile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Movie currentMovie = movieList.get(position);
                Intent DetailIntent = new Intent(mContext, DetailActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("movie_details",currentMovie);
                DetailIntent.putExtra("movie_bundle",b);
                DetailIntent.putExtra("movie_details","from Main Activity");
                mContext.startActivity(DetailIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView movie_tile;
        public ProgressBar mProgressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            movie_tile = itemView.findViewById(R.id.movie_thumbnail);
            mProgressBar = itemView.findViewById(R.id.progress_bar);

        }
    }

}
