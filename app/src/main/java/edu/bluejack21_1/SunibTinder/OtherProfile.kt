package edu.bluejack21_1.SunibTinder

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OtherProfile : AppCompatActivity() {


    private lateinit var uId : String
    private lateinit var imageArray : MutableList<Uri>
    private lateinit var imageStrArray : MutableList<String>

    private lateinit var backBtn : Button

    private var currIdx : Int = 0

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_profile)

        backBtn = findViewById<Button>(R.id.btnBack)

        val extras = intent.extras
        if (extras != null) {
            uId = extras.getString("Uid").toString()
        }

        imageArray = mutableListOf<Uri>()
        imageStrArray = mutableListOf<String>()

        assignToSlider(uId)

        backBtn.setOnClickListener {
            finish()
        }
    }
    private fun changeIdx(){
        imageStrArray.clear()
        assignToSlider(uId)
    }

    private fun assignToSlider(documentId : String){
        val slider = findViewById<ImageSlider>(R.id.imageSliderSuggested)

        val list = mutableListOf<SlideModel>()

        getData(documentId) {
                e->
            if (e){
                for ( i in imageStrArray){
                    list.add(SlideModel(i))
                }
                slider.setImageList(list,true)
            }
        }
    }
    private fun getData(documentId : String, callback: (Boolean) -> Unit){
        db.collection("users").document(documentId).get().addOnSuccessListener {
                e->
            imageStrArray = e["Carousel"] as MutableList<String>

            val fullName = findViewById<TextView>(R.id.suggestFullName)
            val bio = findViewById<TextView>(R.id.suggestBio)
            val age = findViewById<TextView>(R.id.suggestAge)
            val location = findViewById<TextView>(R.id.suggestLocation)

            fullName.text = e["FullName"].toString()
            bio.text = e["Bio"].toString()
            age.text = e["Age"].toString()
            location.text = e["Location"].toString()



            callback(true)
        }.addOnFailureListener {
            Log.w("haha", "fail")
            callback(false)
        }
    }
}