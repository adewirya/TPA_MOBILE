package edu.bluejack21_1.SunibTinder

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