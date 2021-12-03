package edu.bluejack21_1.SunibTinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class EditInfo : AppCompatActivity() {


    val db = Firebase.firestore
    private lateinit var uId : String
    private lateinit var sharedPref : SharedPrefConfig
    private lateinit var genderSpinner : Spinner
    private lateinit var locationSpinner : Spinner
    private lateinit var nameTxtField : EditText
    private lateinit var aliasTxtField : EditText
    private lateinit var bioTxtField : EditText
    private lateinit var ageTxtField : EditText
    private lateinit var livingInTxtField : EditText
    private lateinit var doneBtn : Button
    private lateinit var doneBtn2 : Button
    private lateinit var addPassionBtn : Button
    private lateinit var passionTextView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)
        sharedPref = SharedPrefConfig(this)
        genderSpinner = findViewById<Spinner>(R.id.genderSpinner)
        locationSpinner = findViewById<Spinner>(R.id.locationSpinner)
        nameTxtField = findViewById<EditText>(R.id.nameTxtField)
        aliasTxtField = findViewById<EditText>(R.id.aliasTxtField)
        bioTxtField = findViewById<EditText>(R.id.bioTxtField)
        ageTxtField = findViewById<EditText>(R.id.ageTxtField)
        livingInTxtField = findViewById<EditText>(R.id.livingInTxtField)
        passionTextView = findViewById<TextView>(R.id.passionTextView)
        doneBtn = findViewById<Button>(R.id.doneEditBtn)
        doneBtn2 = findViewById<Button>(R.id.doneInfoBtn)
        addPassionBtn = findViewById<Button>(R.id.addPassionBtn)

        uId = sharedPref.getString("Uid").toString()

        setDefaultValues()

        ArrayAdapter.createFromResource(
            this,
            R.array.genders,
            android.R.layout.simple_spinner_item
        ).also { adapter2 ->
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            genderSpinner.adapter = adapter2
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.binus_locations,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            locationSpinner.adapter = adapter
        }
        doneBtn.setOnClickListener{
            saveData()
        }
        doneBtn2.setOnClickListener{
            saveData()
        }
        addPassionBtn.setOnClickListener{
            startActivity(Intent(this, AddPassion::class.java))
        }
    }
    private fun setDefaultValues(){
        db.collection("users").document(uId).get().addOnSuccessListener { e ->
            nameTxtField.setText(e["FullName"].toString())
            aliasTxtField.setText(e["Alias"].toString())
            bioTxtField.setText(e["Bio"].toString())
            ageTxtField.setText(e["Age"].toString())
            livingInTxtField.setText(e["City"].toString())
            genderSpinner.setSelection(getGenderIndex(e["Gender"].toString()))
            locationSpinner.setSelection(getCampusLocationIndex(e["Location"].toString()))
            val passions : List<String> = e["Passion"] as List<String>
            var passionStr = ""
            for(p in passions){
                passionStr += p + ","
            }
            passionStr = passionStr.dropLast(1)
            sharedPref.putString("passion", passionStr)
            passionTextView.setText(passionStr)
        }
    }

    override fun onResume() {
        super.onResume()
        passionTextView.setText(sharedPref.getString("passion").toString())
    }

    private fun getGenderIndex(gender : String) : Int{
        val genderList = resources.getStringArray(R.array.genders).toList()
        var index : Int = 0
        for (g in genderList) {

            if(gender.equals(g)){
                return index
            }
            index++
            Log.w("gender", g.toString())
        }
        return 0
    }
    private fun getCampusLocationIndex(location : String) : Int{
        val locationList = resources.getStringArray(R.array.binus_locations).toList()
        var index : Int = 0
        for (l in locationList) {

            if(location.equals(l)){
                return index
            }
            index++
            Log.w("Location", l.toString())
        }
        return 0
    }
    private fun saveData(){
        //klo mau validasi validasi dlu

        val fullName = nameTxtField.text.toString()
        val alias = aliasTxtField.text.toString()
        val bio = bioTxtField.text.toString()
        val age = ageTxtField.text.toString()
        val livingIn = livingInTxtField.text.toString()
        val gender = genderSpinner.selectedItem.toString()
        val location = locationSpinner.selectedItem.toString()

        if(bioTxtField.lineCount > 3){
            Toast.makeText(this, "Bio Line Count Must Be Smaller Than 3", Toast.LENGTH_SHORT).show()
            return
        }
        if(gender.equals("")){
            Toast.makeText(this, "Gender Must Be Selected", Toast.LENGTH_SHORT).show()
            return
        }
        if(location.equals("")){
            Toast.makeText(this, "Campus Location Must Be Selected", Toast.LENGTH_SHORT).show()
            return
        }
        if(fullName.length <= 0){
            Toast.makeText(this, "Name Must Be Filled", Toast.LENGTH_SHORT).show()
            return
        }

        var passionPref = sharedPref.getString("passion").toString()
        val passions = passionPref.split(",")

        db.collection("users").document(uId).update(mapOf(
            "FullName" to fullName,
            "Alias" to  alias,
            "Bio" to bio,
            "Age" to age,
            "City" to livingIn,
            "Gender" to gender,
            "Location" to location,
            "Passion" to passions
        )).addOnSuccessListener { e->
            Toast.makeText(this, "Successfully Updated User Data", Toast.LENGTH_SHORT).show()
            // ni aku gatau apa aja
            //sharedPref.putString(

        }.addOnCompleteListener{
            startActivity(Intent(this, Home::class.java))
        }


    }


}