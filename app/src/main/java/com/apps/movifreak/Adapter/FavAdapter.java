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

import com.apps.movifreak.Database.FavMovie;
import com.apps.movifreak.Home.DetailActivity;
import com.apps.movifreak.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by abhinav on 12/7/20.
 */
public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private static final String TAG = FavAdapter.class.getSimpleName();

    private List<FavMovie> favMovieList;
    private Context mContext;

    public void addMovies(List<FavMovie> favMovies){
        favMovieList = favMovies;
        notifyDataSetChanged();
    }

    public FavAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public FavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.single_movie_tile,parent,false);
        FavAdapter.ViewHolder viewHolder = new FavAdapter.ViewHolder(listItem);
        mContext = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FavAdapter.ViewHolder holder, final int position) {
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
                                Log.d(TAG,"Error in loading images");
                            }
                        });
            }
        });

        holder.movie_tile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FavMovie currentMovie = favMovieList.get(position);
                Intent DetailIntent = new Intent(mContext, DetailActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("fav_movie_details",currentMovie);
                DetailIntent.putExtra("fav_movie_bundle",b);
                mContext.startActivity(DetailIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(favMovieList!=null)
            return favMovieList.size();
        else
            return 0;
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
