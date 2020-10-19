package com.apps.movifreak.Home

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apps.movifreak.Adapter.TvAdapter
import com.apps.movifreak.Model.TvShow
import com.apps.movifreak.R
import com.apps.movifreak.Utils.JsonUtils
import com.apps.movifreak.Utils.NetworkUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONException
import java.io.IOException
import java.lang.Exception
import java.net.URL

private val TAG = TvShowFragment::class.java.simpleName

class TvShowFragment : Fragment() {

    /**
     * Type of TvShow -> 1.pop
     *                  2.top
     *                  3.search
     * */

    private val popTvShowConstant: String = "pop"
    private val topTvShowConstant: String = "top"
    private val searchTvShowConstant: String = "search"

    //vars
    private var activeId = 0
    private var returnActivity = ""
    private var displayTvShowsList = ArrayList<TvShow>()
    private var typeOfTvShow = "popular"
    private var isSearchActive = false
    private var searchPage = 1
    private var popCurrentPage = 1
    private var topCurrentPage = 1
    private var searchedTvShow: String? = null
    private var searchPageLimit: Int = 3

    //Movies List
    private val popTvShowList = ArrayList<TvShow>()
    private val topRatedTvShowList = ArrayList<TvShow>()
    private val searchedTvShowList = ArrayList<TvShow>()

    //Widgets
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mContext: Context
    private lateinit var myAdapter: TvAdapter
    private lateinit var pageTitle: TextView
    private lateinit var mainToolbar: Toolbar
    private lateinit var mSearchParam: EditText


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        myAdapter = TvAdapter(mContext)

        val gridLayoutManager = GridLayoutManager(mContext, 3, RecyclerView.VERTICAL, false)
        mRecyclerView?.layoutManager = gridLayoutManager
        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.adapter = myAdapter

        getTvShowsTask().execute(NetworkUtils.buildUrlForTV(typeOfTvShow, getString(R.string.api_key), "", popCurrentPage.toLong()))

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {

                    if (isSearchActive) {
                        if (searchPage < searchPageLimit) {
                            searchPage++
                            Log.d(TAG, "Search Page no: $searchPage")
                            if (searchedTvShow != null) {
                                getTvShowsTask().execute(NetworkUtils.buildUrlForSearch(getString(R.string.api_key), searchedTvShow?.trim(), searchPage.toLong(),"tvShow"))
                            }
                        }
                    } else {

                        if (typeOfTvShow == "popular") {
                            popCurrentPage++
                            Log.d(TAG, "Page no:Pop $popCurrentPage")
                            getTvShowsTask().execute(NetworkUtils.buildUrlForTV(typeOfTvShow, getString(R.string.api_key), "", popCurrentPage.toLong()))
                        } else {
                            topCurrentPage++
                            Log.d(TAG, "Page no:Top $topCurrentPage")
                            getTvShowsTask().execute(NetworkUtils.buildUrlForTV(typeOfTvShow, getString(R.string.api_key), "", topCurrentPage.toLong()))
                        }

                    }

                }
            }

        })

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.most_pop -> {
                    if (typeOfTvShow != "popular") {
//                        pageTitle?.text = "Pop Tv Shows"
                        typeOfTvShow = "popular"
                        displayTvShowsList.clear()
                        if (popTvShowList.isEmpty() && popTvShowList.size == 0) {
                            popCurrentPage = 1
                            myAdapter.clearPopTvShowList()
                            getTvShowsTask().execute(NetworkUtils.buildUrlForTV(typeOfTvShow, getString(R.string.api_key), "", 1))
                        } else {

                            if (popTvShowList.size > 120) {
                                Log.d(TAG, "PopTvShowListSize - " + popTvShowList.size + " Excess data emptying list")
                                popCurrentPage = 1
                                myAdapter.clearPopTvShowList()
                                getTvShowsTask().execute(NetworkUtils.buildUrlForTV(typeOfTvShow, getString(R.string.api_key), "", 1))
                            } else {

                                Log.d(TAG, "Getting tv shows from already fetched list")
                                myAdapter.updatePopTvShowList(popTvShowList,popTvShowConstant)
                            }
                        }
                    }
                    true
                }

                R.id.top_rated -> {
                    if (typeOfTvShow != "top_rated") {
//                        pageTitle?.text = "Top Rated TvShows"
                        typeOfTvShow = "top_rated"
                        displayTvShowsList.clear()
                        if (topRatedTvShowList.isEmpty() && topRatedTvShowList.size == 0) {
                            topCurrentPage = 1
                            myAdapter.clearTopTvShowList()
                            getTvShowsTask().execute(NetworkUtils.buildUrlForTV(typeOfTvShow, getString(R.string.api_key), "", 1))
                        } else {

                            if (topRatedTvShowList.size > 120) {
                                Log.d(TAG, "TopRatedListSize - " + topRatedTvShowList.size + " Excess data emptying list")
                                topCurrentPage = 1
                                myAdapter.clearTopTvShowList()
                                getTvShowsTask().execute(NetworkUtils.buildUrlForTV(typeOfTvShow, getString(R.string.api_key), "", 1))
                            } else {

                                Log.d(TAG, "Getting tv shows from already fetched list")
                                myAdapter.updateTopTvShowList(topRatedTvShowList,topTvShowConstant)
                            }
                        }
                    }
                    true
                }

                R.id.fav_movies -> {
                    val favIntent = Intent(mContext, favActivity::class.java)
                    favIntent.putExtra("from_fragment", "TvShowFragment")
                    startActivity(favIntent)
                    returnActivity = "favActivity"
                    true
                }

                else -> false
            }
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater.inflate(R.layout.fragment_movie, container, false)

        mRecyclerView = root.findViewById(R.id.rv_thumbnails)
        mContext = root.context
        bottomNavigationView = root.findViewById(R.id.bottom_navigation)
        mainToolbar = activity?.findViewById(R.id.main_toolbar)!!
        drawerLayout = activity?.findViewById(R.id.drawer_layout)!!
        return root
    }

    //getting tvShows
    private inner class getTvShowsTask : AsyncTask<URL?, Void?, ArrayList<TvShow>>() {

        override fun doInBackground(vararg urls: URL?): ArrayList<TvShow> {

            val url = urls[0]
            var tvShowList: ArrayList<TvShow> = ArrayList()

            try {
                val receivedJson = NetworkUtils.getResponseFromUrl(url)
                tvShowList = JsonUtils.parseTvShowJsonArray(receivedJson)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return tvShowList

        }

        override fun onPostExecute(result: ArrayList<TvShow>) {

            if (!result.isNullOrEmpty()) {
                when (typeOfTvShow) {
                    "popular" -> {
                        popTvShowList.addAll(result)
                        myAdapter.addPopTvShowList(result,popTvShowConstant)
                    }
                    "top_rated" -> {
                        topRatedTvShowList.addAll(result as Collection<TvShow>)
                        myAdapter.addTopTvShowList(result,topTvShowConstant)
                    }
                    else ->{
                        searchedTvShowList.addAll(result as Collection<TvShow>)
                        myAdapter.addSearchTvShowList(result,searchTvShowConstant)
                    }
                }
                displayTvShowsList.addAll(result as Collection<TvShow>)
            }

        }

    }

    fun initSearchTextListener() {
        //Enabled when search icon is clicked on MainActivity
        try {
            mSearchParam = activity?.findViewById(R.id.search)!!
            performSearch()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun performSearch() {
        mSearchParam?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty() && s.toString().trim { it <= ' ' } !== "") {
                    //Perform search operation from MoviesDb
                    isSearchActive = true
                    searchPage = 1
                    myAdapter.clearAllTvShowList()
                    Log.d(TAG, "Search Parameter: $s")
                    searchedTvShow = s.toString().trim { it <= ' ' }
                    getTvShowsTask().execute(NetworkUtils.buildUrlForSearch(getString(R.string.api_key), searchedTvShow?.trim(), searchPage.toLong(),"tvShow"))
                }
            }
        })
    }


    fun removeSearchResultsAndRefresh() {

        isSearchActive = false
        myAdapter.clearSearchTvShowList()
        if (typeOfTvShow == "popular") {

            Log.d(TAG,"refresh after search")

            if (popTvShowList.isNotEmpty() && popTvShowList.size < 121) {

                myAdapter.updatePopTvShowList(popTvShowList,popTvShowConstant)

            } else {

                Log.d(TAG, "Page no:Pop $popCurrentPage")
                getTvShowsTask().execute(NetworkUtils.buildUrlForTV(typeOfTvShow, getString(R.string.api_key), "", popCurrentPage.toLong()))

            }
        } else {

            if (topRatedTvShowList.isNotEmpty() && topRatedTvShowList.size < 121) {

                myAdapter.updateTopTvShowList(topRatedTvShowList,topTvShowConstant)

            } else {

                Log.d(TAG, "Page no:Top $topCurrentPage")
                getTvShowsTask().execute(NetworkUtils.buildUrlForTV(typeOfTvShow, getString(R.string.api_key), "", topCurrentPage.toLong()))

            }
        }

    }


}