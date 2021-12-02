package edu.bluejack21_1.SunibTinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner


class EditSuggestion : AppCompatActivity() {

    private lateinit var spinner : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_suggestion)

        spinner = findViewById<Spinner>(R.id.suggestionPreferences)

        ArrayAdapter.createFromResource(
            this,
            R.array.preferences,
            android.R.layout.simple_spinner_item
        ).also {
            e->
            e.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = e
        }




    }
}