package edu.bluejack21_1.SunibTinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class EditSuggestion : AppCompatActivity() {

    private lateinit var spinner : Spinner
    private lateinit var doneBtn : Button

    private lateinit var minimumAge : EditText
    private lateinit var maximumAge : EditText

    private lateinit var sharedPref : SharedPrefConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_suggestion)

        sharedPref = SharedPrefConfig(this)


        spinner = findViewById<Spinner>(R.id.suggestionPreferences)
        doneBtn = findViewById<Button>(R.id.registerButton2)
        minimumAge = findViewById<EditText>(R.id.minAge)
        maximumAge = findViewById<EditText>(R.id.maxAge)



        ArrayAdapter.createFromResource(
            this,
            R.array.preferences,
            android.R.layout.simple_spinner_item
        ).also {
            e->
            e.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = e
        }

        doneBtn.setOnClickListener{
            updatePreferences()
        }


    }

    private fun updatePreferences() {
        val selectedSpinner = spinner.selectedItem.toString()
        val minAge = Integer.parseInt(minimumAge.text.toString())
        val maxAge = Integer.parseInt(maximumAge.text.toString())


        if (selectedSpinner == "" || minAge < 0 || maxAge == 0){
            Toast.makeText(this@EditSuggestion, "Please Choose Your Preferences", Toast.LENGTH_SHORT) .show()
            return
        }
        else if (minAge > maxAge){
            Toast.makeText(this@EditSuggestion, "Minimum Age can't be greater than the Maximum Age", Toast.LENGTH_SHORT).show()
            return
        }
        else {
            val docId = sharedPref.getString("Uid").toString()
            val data = hashMapOf(
                "Preferences" to selectedSpinner,
                "Min Age" to minAge,
                "Max Age" to maxAge
            )


            val db = Firebase.firestore
            db.collection("users").document(docId).update(data as Map<String, Any>).addOnSuccessListener {
                e->

                Toast.makeText(this, "Preferences Updated Succesfully", Toast.LENGTH_SHORT).show()
            }.addOnCompleteListener{
                startActivity(Intent(this, Home::class.java))
            }
        }
    }
}