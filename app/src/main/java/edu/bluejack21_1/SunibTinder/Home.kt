package edu.bluejack21_1.SunibTinder

import android.R.attr
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.bluejack21_1.SunibTinder.databinding.ActivityHomeBinding
import android.R.attr.delay

import android.app.AlarmManager

import android.app.PendingIntent
import android.content.Context

import android.content.Intent
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList


class Home : AppCompatActivity() {


    val chatList : MutableList<String> = ArrayList<String>()
    var page = 1
    var isLoading = false
    val limit = 5


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val navController = findNavController(R.id.nav_fragment)
        bottomNavView.setupWithNavController(navController)

    }



}