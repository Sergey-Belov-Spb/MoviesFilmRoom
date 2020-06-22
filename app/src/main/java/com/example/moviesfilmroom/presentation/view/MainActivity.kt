package com.example.moviesfilmroom.presentation.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.moviesfilmroom.R
import com.example.moviesfilmroom.data.entity.MovieItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MainActivity : AppCompatActivity(), MovieListFragment.MovieListListener,MovieListFavoriteFragment.MovieListListener {
    companion object{
        const val TAG = "MainActiviry"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initButtonListener()
        openAllMoviesList()
    }


    fun CheckNewRequest() : Boolean{

        return false
    }

    fun readLastTime(): Long {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        val timeMsec = sPref.getLong("LastTime",0)
        return timeMsec
    }
    fun storeLastTime(timeMsec :Long   ) {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        val edit = sPref.edit()
        edit.putLong("LastTime",timeMsec)
        edit.commit()
    }
    fun initButtonListener() {
        findViewById<Button>(R.id.debugBtn).setOnClickListener{

            /*val data = Date();
            val curTime = data.time
            val x =(curTime - readLastTime())/1000
            Log.d(TAG,"GetTime = $curTime $x ")
            storeLastTime(curTime)*/
            Log.d(TAG,"Button ")
        }
        findViewById<Button>(R.id.debugBtn2).setOnClickListener {

            Log.d(TAG,"Button2 ")
        }

        findViewById<BottomNavigationView>(R.id.navigationBottom).setOnNavigationItemSelectedListener {menuItem ->
            when(menuItem.itemId){
                R.id.action_allMovies ->{
                    Log.d(TAG,"action_allMovies")
                    openAllMoviesList()
                }
                R.id.action_favoriteMovies ->{
                    Log.d(TAG,"action_favoriteMovies")
                    openFavoriteMoviesList()
                }
            }
            false
        }
    }

    private fun openAllMoviesList(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,MovieListFragment(), MovieListFragment.TAG  )
            //.addToBackStack("Main")
            .commit()
    }

    private fun openFavoriteMoviesList(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,MovieListFavoriteFragment(), MovieListFavoriteFragment.TAG  )
            //.addToBackStack("Main")
            .commit()
    }

    private fun openDetailedFragment(movieItem: MovieItem) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,MovieDetailedFragment.newInstance(movieItem.title,movieItem.gitUrl),MovieDetailedFragment.TAG)
            .addToBackStack("Detaled")
            .commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is MovieListFragment)
        {
            fragment.listener = this
            Log.d(TAG,"onAttachFragment -> MovieListFragment")
        }
        if (fragment is MovieListFavoriteFragment)
        {
            fragment.listener = this
            Log.d(TAG,"onAttachFragment -> MovieListFavoriteFragment")
        }
    }

    override fun onMovieSelected(moviesItemDetailed: MovieItem) {
        Log.d(TAG,"Show Detailed")
        openDetailedFragment(moviesItemDetailed)
    }
}

/*class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}*/
