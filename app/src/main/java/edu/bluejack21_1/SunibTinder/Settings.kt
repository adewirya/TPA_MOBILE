package edu.bluejack21_1.SunibTinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.internal.artificialFrame

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnSuggest = findViewById<TextView>(R.id.textView6)
        val btnPassword = findViewById<TextView>(R.id.textView4)
        val btnEmail = findViewById<TextView>(R.id.textView9)

        val sharedPref = SharedPrefConfig(this)

        if (sharedPref.getBoolean("IsGoogle") == true){
            btnPassword.visibility = View.INVISIBLE
            btnEmail.visibility = View.INVISIBLE
        }

        btnEmail.setOnClickListener{
            startActivity(Intent(this, EditEmail::class.java))
        }

        btnPassword.setOnClickListener{
            startActivity(Intent(this, EditPassword::class.java))
        }

        btnSuggest.setOnClickListener{
            startActivity(Intent(this, EditSuggestion::class.java))
        }
    }
}