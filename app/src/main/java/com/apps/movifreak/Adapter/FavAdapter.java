package com.apps.movifreak.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.movifreak.Database.FavMovie;
import com.apps.movifreak.Database.FavTvShow;
import com.apps.movifreak.Home.DetailActivity;
import com.apps.movifreak.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhinav on 12/7/20.
 */
public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private static final String TAG = FavAdapter.class.getSimpleName();

    //for favorite movies
    private List<FavMovie> favMovieList = new ArrayList<>();

    //for favorite tvShows
    private List<FavTvShow> favTvShowList = new ArrayList<>();

    private Context mContext;
    private String movieOrTvShow;

    public void addTvShows(List<FavTvShow> favTvShows, String movieOrTvShow) {
        this.favMovieList.clear();
        this.favTvShowList = favTvShows;
        this.movieOrTvShow = movieOrTvShow;
        notifyDataSetChanged();
    }

    public void addMovies(List<FavMovie> favMovies, String movieOrTvShow) {
        this.favTvShowList.clear();
        this.favMovieList = favMovies;
        this.movieOrTvShow = movieOrTvShow;
        notifyDataSetChanged();
    }

    public FavAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public FavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.single_movie_tile, parent, false);
        FavAdapter.ViewHolder viewHolder = new FavAdapter.ViewHolder(listItem);
        mContext = parent.getContext();
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final FavAdapter.ViewHolder holder, final int position) {

        if (movieOrTvShow.equals("movie")) {


            final String tileUrl = favMovieList.get(position).getPoster_path();

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

            final String movieRating = String.valueOf(favMovieList.get(position).getVote_average());
            final String movieYear = favMovieList.get(position).getRelease_date().substring(0, 4);

            holder.movie_rating.setText(movieRating);
            holder.movie_year.setText(movieYear);

            holder.movie_tile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FavMovie currentMovie = favMovieList.get(position);
                    Intent DetailIntent = new Intent(mContext, DetailActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelable("fav_movie_details", currentMovie);
                    DetailIntent.putExtra("fav_movie_bundle", b);
                    mContext.startActivity(DetailIntent);

                }
            });


        } else if (movieOrTvShow.equals("tvShow")) {


            Log.d(TAG, favTvShowList.get(position).toString());

            final String tileUrl = favTvShowList.get(position).getPoster_path();

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

            final String tvShowRating = String.valueOf(favTvShowList.get(position).getVote_average());
            final String firstAirDate = favTvShowList.get(position).getFirst_air_date().substring(0, 4);

            holder.movie_rating.setText(tvShowRating);
            holder.movie_year.setText(firstAirDate);

            holder.movie_tile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FavTvShow currentTvShow = favTvShowList.get(position);
                    Intent DetailIntent = new Intent(mContext, DetailActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelable("fav_tvShow_details", currentTvShow);
                    DetailIntent.putExtra("fav_tvShow_bundle", b);
                    mContext.startActivity(DetailIntent);

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        if (favMovieList.size() != 0)
            return favMovieList.size();
        else if (favTvShowList.size() != 0)
            return favTvShowList.size();
        else
            return 0;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView movie_tile;
        public ProgressBar mProgressBar;
        public TextView movie_rating, movie_year;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            movie_tile = itemView.findViewById(R.id.movie_thumbnail);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
            movie_rating = itemView.findViewById(R.id.tv_rating);
            movie_year = itemView.findViewById(R.id.tv_rating_year);

        }
    }

}
