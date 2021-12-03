package edu.bluejack21_1.SunibTinder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.transition.Slide
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import edu.bluejack21_1.SunibTinder.databinding.FragmentProfileBinding
import edu.bluejack21_1.SunibTinder.databinding.FragmentSuggestedBinding
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlin.reflect.typeOf

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_suggested.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_suggested : Fragment() {


    private lateinit var listOfDocIds : MutableList<String>

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var v: FragmentSuggestedBinding? = null
    private val binding get() = v!!

    private lateinit var sharedPref : SharedPrefConfig
    private lateinit var docId : String

    private lateinit var imageArray : MutableList<Uri>
    private lateinit var imageStrArray : MutableList<String>

    private var currIdx : Int = 0

    private lateinit var fullName : String
    private lateinit var location : String
    private var age : Int = 0
    private lateinit var bio : String

    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        v = FragmentSuggestedBinding.inflate(inflater, container, false)



        sharedPref = SharedPrefConfig(this.requireContext())

        docId = sharedPref.getString("Uid").toString()
        imageArray = mutableListOf<Uri>()
        imageStrArray = mutableListOf<String>()
        listOfDocIds= mutableListOf<String>()
        val btnNo = binding.btnNo
        val btnYes = binding.btnYes
        val btnInfo = binding.btnInfo



        searchSuggested()

        btnNo.setOnClickListener{
            addToNo()
        }

        btnYes.setOnClickListener{
            addToYes()
        }

        btnInfo.setOnClickListener{
            activity?.let{
                val intent = Intent (it, OtherProfile::class.java)
                intent.putExtra("Uid", docId)
                it.startActivity(intent)
            }
        }


        assignToSlider(docId)

        return v!!.root
    }

    private fun searchSuggested() {
        var searchGender : String = "Female"

        if (sharedPref.getString("Gender").equals("Female")){
            searchGender = "Male"
        }

        /*
        * Suggested List
        * - Orang yang like
        * - Orang yang berbeda gender
        * - Orang yang sudah like tidak bisa muncul lagi klo sudah di like / unlike
        * - Pastiin berdasarkan age range preferences
        * - Pastiin sesuai dengan location Preferences
        * -
        * */
        val minAge = Integer.parseInt(sharedPref.getInt("MinAge").toString())
        val maxAge = Integer.parseInt(sharedPref.getInt("MaxAge").toString())
        val location = sharedPref.getString("Location").toString()
        val email = sharedPref.getString("Email").toString()
        val preferences = sharedPref.getString("Preferences").toString()

        var size : Int = 0

        db.collection("users").get().addOnSuccessListener {
            e->
            size = e.size()
        }

//        Log.w("teshehe", size.toString())

        val startAt = Math.ceil(Random.nextDouble(0.0,1.0) * size)

        Log.w("teshehe", startAt.toString())


        if (preferences.equals("Same Campus")){
            db.collection("users")
                .whereLessThanOrEqualTo("Age", maxAge)
                .whereGreaterThanOrEqualTo("Age", minAge)
                .whereEqualTo("Gender", searchGender)
                .whereEqualTo("Location", location)
                .orderBy("Age")
                .orderBy("Number")
                .get().addOnSuccessListener {
                        dcs->

                    for (i in dcs){
                        if (i.id != docId){
                            listOfDocIds.add(i.id)
                        }
                    }
                }.addOnFailureListener{
                        e ->
                    Log.w("teshehe", e.toString())
                }.addOnCompleteListener{
                    Log.w("teshehe", listOfDocIds.toString())
                }
        }
        else {
            Log.w("teshehe", "hohohoho")
        }


    }

    private fun addToYes(){
        // add yes to db



        changeIdx()
    }

    private fun addToNo(){
        // add no to db


        changeIdx()
    }

    private fun changeIdx(){
        imageStrArray.clear()
        assignToSlider(docId)

    }

    private fun assignToSlider(documentId : String){
        val slider = binding.imageSliderSuggested

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

            val fullName = binding.suggestFullName
            val bio = binding.suggestBio
            val age = binding.suggestAge
            val location = binding.suggestLocation

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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_suggested.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_suggested().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}