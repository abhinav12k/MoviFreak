package com.apps.movifreak.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.movifreak.Model.Review;
import com.apps.movifreak.R;

import java.util.ArrayList;

/**
 * Created by abhinav on 5/7/20.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private ArrayList<Review> reviewArrayList;
    private Context mContext;

    public ReviewAdapter(Context context,ArrayList<Review> reviews){
        this.reviewArrayList = reviews;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.single_review_layout,parent,false);
        ReviewAdapter.ViewHolder holder = new ReviewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {

        holder.authorName.setText(reviewArrayList.get(position).getAuthor());
        holder.comment.setText(reviewArrayList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView authorName;
        public TextView comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            authorName=itemView.findViewById(R.id.author_name);
            comment = itemView.findViewById(R.id.review);
        }
    }

}
