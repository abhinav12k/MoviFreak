package com.apps.movifreak.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.movifreak.Home.YoutubePlayerActivity;
import com.apps.movifreak.Model.Trailer;
import com.apps.movifreak.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abhinav on 5/7/20.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private ArrayList<Trailer> trailersList;
    private Context mContext;

    //consts
    private static final String TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";

    public TrailerAdapter(Context context,ArrayList<Trailer> trailersList){
        this.trailersList = trailersList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.single_trailer_layout,parent,false);
        TrailerAdapter.ViewHolder viewHolder = new TrailerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final String key = trailersList.get(position).getKey();
        final String youtubeLink = TRAILER_BASE_URL+key;

        String thumbnailLink = "https://img.youtube.com/vi/"+key+"/sddefault.jpg";
        Picasso.with(mContext)
                .load(thumbnailLink)
                .into(holder.trailerThumbnail, new Callback() {
                    @Override
                    public void onSuccess() {
                        //loading thumbnail complete disable progress bar

                        holder.mProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        //error in loading thumbnail
                        Log.d(TAG,"Error in loading trailer images");
                    }
                });

        holder.trailerThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri youtubeUri = Uri.parse(youtubeLink);
//                Intent trailerIntent = new Intent(Intent.ACTION_VIEW,youtubeUri);
//                if(trailerIntent.resolveActivity(mContext.getPackageManager())!=null){
//                    mContext.startActivity(trailerIntent);
//                }

                Intent playerIntent = new Intent(mContext, YoutubePlayerActivity.class);
                playerIntent.putExtra("key",key);
                mContext.startActivity(playerIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView trailerThumbnail;
        public ProgressBar mProgressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trailerThumbnail = itemView.findViewById(R.id.trailerThumbnail);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
