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

    lateinit var adapter : fragment_chat.RecyclerAdapter
    lateinit var layoutManager  : LinearLayoutManager
    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = layoutManager
        getPage()

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val navController = findNavController(R.id.nav_fragment)
        bottomNavView.setupWithNavController(navController)
    }

    private fun getPage() {
        val start = (page-1) * limit
        val end : Int = (page) * limit

        for (i in start..end){
            chatList.add("Item " +i.toString())
        }

        Handler().postDelayed({
            if (::adapter.isInitialized){
                adapter.notifyDataSetChanged()
            } else{
                adapter = fragment_chat.RecyclerAdapter(this)
                recyclerView.adapter = adapter
            }
        }, 5000)
    }

}