package com.apps.movifreak.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apps.movifreak.Home.DetailActivity
import com.apps.movifreak.Model.TvShow
import com.apps.movifreak.R
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.lang.Exception

/**
 * Created by abhinav on 3/10/20.
 */


private val TAG: String? = TvAdapter::class.java.simpleName

class TvAdapter(mContext: Context) : RecyclerView.Adapter<TvAdapter.ViewHolder>(){

    private var tvList = ArrayList<TvShow>()
    private lateinit var mContext:Context

    fun addTvShows(mList: ArrayList<TvShow?>?){
        this.tvList.addAll(mList as Collection<TvShow>)
        notifyDataSetChanged()
    }

    fun clearList(){
        this.tvList.clear()
        notifyDataSetChanged()
    }

    fun updateTvShows(mList: ArrayList<TvShow>){
        this.tvList.clear()
        this.tvList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        mContext = parent.context
        val listItem: View = layoutInflater.inflate(R.layout.single_movie_tile, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: TvAdapter.ViewHolder, position: Int) {

        val tileUrl: String? = tvList[position]?.poster_path

        Picasso.with(mContext).load(tileUrl).networkPolicy(NetworkPolicy.OFFLINE).into(holder.tv_title, object : Callback {
            override fun onSuccess() {
                holder.mProgressBar!!.visibility = View.INVISIBLE
            }

            override fun onError() {
                //Try again online if cache failed
                Picasso.with(mContext)
                        .load(tileUrl)
                        .into(holder.tv_title, object : Callback {
                            override fun onSuccess() {
                                holder.mProgressBar!!.visibility = View.INVISIBLE
                            }

                            override fun onError() {
                                Log.d(TAG, "Error in loading images")
                            }
                        })
            }
        })

        try {
            val tvRating: String = tvList[position]?.vote_average.toString()
            val movieYear: String? = tvList[position]?.first_air_date?.substring(0, 4)

            holder.tv_rating!!.text = tvRating
            holder.tv_year!!.text = movieYear
        }catch (e : Exception){
            e.printStackTrace()
        }



        holder.tv_title!!.setOnClickListener {

            //Take to the detailed activity
            val currentTvShow: TvShow = tvList[position]
            val DetailIntent = Intent(mContext, DetailActivity::class.java)
            val b = Bundle()
            b.putParcelable("tvShow_details", currentTvShow)
            DetailIntent.putExtra("tvShow_bundle", b)
            DetailIntent.putExtra("tvShow_details", "from TvShow Fragment")
            mContext.startActivity(DetailIntent)
        }


    }

    override fun getItemCount(): Int {
        return tvList.size
    }

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_title: ImageView? = itemView.findViewById(R.id.movie_thumbnail)
        var tv_rating: TextView? = itemView.findViewById(R.id.tv_rating)
        var tv_year:TextView? = itemView.findViewById(R.id.tv_rating_year)
        var mProgressBar: ProgressBar? = itemView.findViewById(R.id.progress_bar)

    }

}