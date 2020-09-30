package com.apps.movifreak.Home

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apps.movifreak.Adapter.MovieAdapter
import com.apps.movifreak.Model.Movie
import com.apps.movifreak.R
import com.apps.movifreak.Utils.JsonUtils
import com.apps.movifreak.Utils.NetworkUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONException
import java.io.IOException
import java.lang.Exception
import java.net.URL

private val TAG = MovieFragment::class.java.simpleName

class MovieFragment : Fragment() {

    //vars
    private var activeId = 0
    private var returnActivity = ""
    private var displayMovieList = ArrayList<Movie>()
    private var typeOfMovie = "popular"
    private var isSearchActive = false
    private var searchPage = 1
    private var popCurrentPage = 1
    private var topCurrentPage = 1
    private var searchedMovie: String? = null

    //Movies List
    private val popMoviesList = ArrayList<Movie>()
    private val topRatedMoviesList = ArrayList<Movie>()
    private val searchedMoviesList = ArrayList<Movie>()

    //Widgets
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mContext: Context
    private lateinit var myAdapter: MovieAdapter
    private lateinit var pageTitle: TextView
    private lateinit var mainToolbar: Toolbar
    private lateinit var mSearchParam: EditText

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        myAdapter = MovieAdapter(mContext)
//        pageTitle.text = "Pop Movies"
        Log.d(TAG, "OnActivityCreated!!!!!!!!!!!!!!!!!!")

        if (savedInstanceState != null && savedInstanceState.containsKey("movies_list")) {
            Log.d(TAG, "Getting data from savedInstance")
            activeId = savedInstanceState.getInt("type_of_movie")
            bottomNavigationView.selectedItemId = activeId
            displayMovieList = savedInstanceState.getParcelableArrayList("movies_list")!!
        } else {
            Log.d(TAG, "Fetching data onStart of app")
            //getting movies list asynchronously
            getMoviesTask().execute(NetworkUtils.buildUrlForMovies(typeOfMovie, getString(R.string.api_key), "", popCurrentPage.toLong()))
        }

        val gridLayoutManager = GridLayoutManager(mContext, 3, RecyclerView.VERTICAL, false)
        mRecyclerView?.layoutManager = gridLayoutManager
        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.adapter = myAdapter

        mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (isSearchActive) {
                        searchPage++
                        Log.d(TAG, "Search Page no: $searchPage")
                        if (searchedMovie != null) {
                            getMoviesTask().execute(NetworkUtils.buildUrlForSearch(getString(R.string.api_key), searchedMovie?.trim(), searchPage.toLong()))
                        }
                    } else {
                        if (typeOfMovie == "popular") {
                            popCurrentPage++
                            Log.d(TAG, "Page no:Pop $popCurrentPage")
                            getMoviesTask().execute(NetworkUtils.buildUrlForMovies(typeOfMovie, getString(R.string.api_key), "", popCurrentPage.toLong()))
                        } else {
                            topCurrentPage++
                            Log.d(TAG, "Page no:Top $topCurrentPage")
                            getMoviesTask().execute(NetworkUtils.buildUrlForMovies(typeOfMovie, getString(R.string.api_key), "", topCurrentPage.toLong()))
                        }
                    }
                }
            }
        })

        bottomNavigationView?.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.most_pop -> {
                    if (typeOfMovie != "popular") {
//                        pageTitle?.text = "Pop Movies"
                        typeOfMovie = "popular"
                        displayMovieList.clear()
                        if (popMoviesList.isEmpty() && popMoviesList.size == 0) {
                            popCurrentPage = 1
                            myAdapter.clearList()
                            getMoviesTask().execute(NetworkUtils.buildUrlForMovies(typeOfMovie, getString(R.string.api_key), "", 1))
                        } else {

                            if (popMoviesList.size > 120) {
                                Log.d(TAG, "PopMovieListSize - " + popMoviesList.size + " Excess data emptying list")
                                popCurrentPage = 1
                                myAdapter.clearList()
                                getMoviesTask().execute(NetworkUtils.buildUrlForMovies(typeOfMovie, getString(R.string.api_key), "", 1))
                            } else {

                                Log.d(TAG, "Getting movies from already fetched list")
                                myAdapter.updateMoviesList(popMoviesList)
                            }
                        }
                    }
                    true
                }
                R.id.top_rated -> {
                    if (typeOfMovie != "top_rated") {
//                        pageTitle?.text = "Top Rated Movies"
                        typeOfMovie = "top_rated"
                        displayMovieList.clear()
                        if (topRatedMoviesList.isEmpty() && topRatedMoviesList.size == 0) {
                            topCurrentPage = 1
                            myAdapter.clearList()
                            getMoviesTask().execute(NetworkUtils.buildUrlForMovies(typeOfMovie, getString(R.string.api_key), "", 1))
                        } else {

                            if (topRatedMoviesList.size > 120) {
                                Log.d(TAG, "TopRatedListSize - " + topRatedMoviesList.size + " Excess data emptying list")
                                topCurrentPage = 1
                                myAdapter.clearList()
                                getMoviesTask().execute(NetworkUtils.buildUrlForMovies(typeOfMovie, getString(R.string.api_key), "", 1))
                            } else {

                                Log.d(TAG, "Getting movies from already fetched list")
                                myAdapter.updateMoviesList(topRatedMoviesList)
                            }
                        }
                    }
                    true
                }
                R.id.fav_movies -> {
                    val favIntent = Intent(mContext, favActivity::class.java)
                    startActivity(favIntent)
                    returnActivity = "favActivity"
                    true
                }
                else -> false
            }
        })

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("movies_list", displayMovieList)
        outState.putInt("type_of_movie", bottomNavigationView!!.selectedItemId)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_movie, container, false)
        mRecyclerView = root.findViewById(R.id.rv_thumbnails)
        mContext = root.context
        bottomNavigationView = root.findViewById(R.id.bottom_navigation)
        mainToolbar = activity?.findViewById(R.id.main_toolbar)!!
        drawerLayout = activity?.findViewById(R.id.drawer_layout)!!
        return root
    }


    private inner class getMoviesTask : AsyncTask<URL?, Void?, ArrayList<Movie?>?>() {
        override fun doInBackground(vararg urls: URL?): ArrayList<Movie?>? {
            val url = urls[0]
            var movieArrayList: ArrayList<Movie?>? = ArrayList()
            try {
                val receivedJson = NetworkUtils.getResponseFromUrl(url)
                movieArrayList = JsonUtils.parseMovieJsonArray(receivedJson)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return movieArrayList
        }

        override fun onPostExecute(movies: ArrayList<Movie?>?) {
            if (movies != null && movies.isNotEmpty()) {
                if (typeOfMovie == "popular") {
                    popMoviesList.addAll(movies as Collection<Movie>)
                    displayMovieList.addAll(movies)
                } else if (typeOfMovie == "top_rated") {
                    topRatedMoviesList.addAll(movies as Collection<Movie>)
                    displayMovieList.addAll(movies)
                } else {
                    searchedMoviesList.addAll(movies as Collection<Movie>)
                    displayMovieList.addAll(movies)
                }
                myAdapter.addMovies(movies)
            }
        }

    }

    fun removeSearchResultsAndRefresh() {

        isSearchActive = false
        if (typeOfMovie == "popular") {

            if (popMoviesList.isNotEmpty() && popMoviesList.size < 121) {
                myAdapter.updateMoviesList(popMoviesList)
            } else {
                Log.d(TAG, "Page no:Pop $popCurrentPage")
                getMoviesTask().execute(NetworkUtils.buildUrlForMovies(typeOfMovie, getString(R.string.api_key), "", popCurrentPage.toLong()))
            }
        } else {

            if (topRatedMoviesList.isNotEmpty() && topRatedMoviesList.size < 121) {
                myAdapter.updateMoviesList(topRatedMoviesList)
            } else {

                Log.d(TAG, "Page no:Top $topCurrentPage")
                getMoviesTask().execute(NetworkUtils.buildUrlForMovies(typeOfMovie, getString(R.string.api_key), "", topCurrentPage.toLong()))
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
                    myAdapter.clearList()
                    Log.d(TAG, "Search Parameter: $s")
                    searchedMovie = s.toString().trim { it <= ' ' }
                    getMoviesTask().execute(NetworkUtils.buildUrlForSearch(getString(R.string.api_key), searchedMovie?.trim(), searchPage.toLong()))
                }
            }
        })
    }

}
