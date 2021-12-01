package edu.bluejack21_1.SunibTinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnSuggest = findViewById<TextView>(R.id.textView6)
        val btnPassword = findViewById<TextView>(R.id.textView4)
        val btnEmail = findViewById<TextView>(R.id.textView9)

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