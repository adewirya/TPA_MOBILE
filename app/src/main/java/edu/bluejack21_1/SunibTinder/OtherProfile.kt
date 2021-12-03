package edu.bluejack21_1.SunibTinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class OtherProfile : AppCompatActivity() {


    private lateinit var uId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_profile)

        val extras = intent.extras
        if (extras != null) {
            uId = extras.getString("Uid").toString()
        }

        Toast.makeText(this@OtherProfile, uId, Toast.LENGTH_SHORT).show()
    }
}