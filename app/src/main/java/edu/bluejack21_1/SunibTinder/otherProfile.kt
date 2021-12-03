package edu.bluejack21_1.SunibTinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class otherProfile : AppCompatActivity() {


    private lateinit var uId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_profile)

        val extras = intent.extras
        if (extras != null) {
            uId = extras.getString("Uid").toString()
        }


    }
}