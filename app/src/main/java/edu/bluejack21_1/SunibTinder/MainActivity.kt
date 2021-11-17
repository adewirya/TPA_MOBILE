package edu.bluejack21_1.SunibTinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack21_1.SunibTinder.databinding.ActivityMainBinding
import edu.bluejack21_1.SunibTinder.databinding.LoginPageBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var binding2: LoginPageBinding
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding = ActivityMainBinding.inflate(layoutInflater)
        binding2 = LoginPageBinding.inflate(layoutInflater)
        val view = binding2.root
        setContentView(view)

        val viewPager = binding2.viewPager
        viewPager.adapter = PageAdapter(supportFragmentManager)

        val tabLayout = binding2.tabLayout
        tabLayout.setupWithViewPager(viewPager)
//        setContentView(R.layout.activity_main)

//        var addUserBtn = binding.addUserbutton

//        addUserBtn.setOnClickListener {
//            val data = hashMapOf(
//                "name" to "niko",
//                "email" to "niko",
//                "isFromGoogle" to false
//            )
//            db.collection("users")
//                .add(data)
//                .addOnSuccessListener { documentReference ->
//                    Log.d("add new user", "DocumentSnapshot written with ID: ${documentReference.id}")
//                    Toast.makeText(this@MainActivity, "Success add new user", Toast.LENGTH_SHORT).show()
//
//                }
//                .addOnFailureListener { e ->
//                    Log.d("goblog", "Error adding document" + e.toString(), e)
//                    Toast.makeText(this@MainActivity, "Failed add new user", Toast.LENGTH_SHORT).show()
//                }
//        }
    }
}