package edu.bluejack21_1.SunibTinder

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.transition.Slide
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import edu.bluejack21_1.SunibTinder.databinding.FragmentProfileBinding
import edu.bluejack21_1.SunibTinder.databinding.FragmentSuggestedBinding
import kotlinx.coroutines.selects.select
import java.util.*
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

    private lateinit var listOfLikes : MutableList<String>
    private lateinit var listOfUnlikes : MutableList<String>
    private lateinit var listOfMatches : MutableList<String>
    private lateinit var passionLayout : LinearLayout
    val limit : Long = 2

    private var currIdx : Long = 0

    private lateinit var pd : ProgressDialog

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

        pd = ProgressDialog(this.requireContext())


        sharedPref = SharedPrefConfig(this.requireContext())

        docId = sharedPref.getString("Uid").toString()
        imageArray = mutableListOf<Uri>()
        imageStrArray = mutableListOf<String>()
        listOfDocIds= mutableListOf<String>()

        listOfLikes = mutableListOf<String>()
        listOfUnlikes = mutableListOf<String>()
        listOfMatches = mutableListOf<String>()

        val btnNo = binding.btnNo
        val btnYes = binding.btnYes
        val btnInfo = binding.btnInfo

        setNotif()

        passionLayout = binding.passionLayout

        getData {
            es->
            if (es){
                searchSuggested{
                        e->
                    if (e){
                        assignToSlider(listOfDocIds[currIdx.toInt()])
                    }
                }
            }
        }

        btnNo.setOnClickListener{
            addToNo()
        }

        btnYes.setOnClickListener{
            addToYes()
        }

        btnInfo.setOnClickListener{
            activity?.let{
                val intent = Intent (it, OtherProfile::class.java)
                intent.putExtra("Uid", listOfDocIds[currIdx.toInt()])
                it.startActivity(intent)
            }
        }

        return v!!.root
    }


    private fun getData(callback: (Boolean) -> Unit){

        pd.setTitle("Getting Data")
        pd.show()

        db.collection("users").document(docId).get().addOnSuccessListener {
            e->
            if (e["Likes"] != null) {
                listOfLikes = e["Likes"] as MutableList<String>
            }

            pd.setMessage("Getting Liked User.")

            if (e["Unlikes"] != null ){
                listOfUnlikes = e["Unlikes"] as MutableList<String>
            }

            pd.setMessage("Getting Unliked User..")

            if (e["Match"] != null) {
                listOfMatches = e["Match"] as MutableList<String>
            }

            pd.setMessage("Getting Matched User...")

        }.addOnCompleteListener{
            callback(true)
        }
    }

    private fun searchSuggested(callback: (Boolean) -> Unit) {
        var searchGender : String = "Female"
        listOfDocIds.clear()
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

        Log.w("teshaha", "search gender : ${searchGender} location : ${location}")

        var size : Int = 0
        pd.setMessage("Getting Suggested User.")
        db.collection("users").get().addOnSuccessListener {
            e->
            size = e.size()
            pd.setMessage("Getting Suggested User..")
        }.addOnCompleteListener{
            pd.setMessage("Getting Suggested User...")
            val startAt = Math.ceil(Random.nextDouble(0.0,1.0) * size).toInt()

            Log.w("teshehe", startAt.toString())

            if (preferences.equals("Same Campus")){
                db.collection("users")
//                    .whereLessThanOrEqualTo("Age", maxAge)
//                    .whereGreaterThanOrEqualTo("Age", minAge)
                    .whereEqualTo("Gender", searchGender)
                    .whereEqualTo("Location", location)
//                    .orderBy("Age")
                    .orderBy("Number")
                    .startAt(startAt)
                    .limit(limit)
                    .get().addOnSuccessListener {
                            dcs->

                        for (i in dcs){
//                            Log.w("teshehe", i.toString())
                            if (i.id != docId){
                                listOfDocIds.add(i.id)
                            }
                        }
                    }.addOnFailureListener{
                            e ->
                        Log.w("teshehe", e.toString())
                    }.addOnCompleteListener{
                        Log.w("teshehe", listOfDocIds.toString())
                        if (listOfDocIds.size < limit){
                            db.collection("users")
                                .whereEqualTo("Gender", searchGender)
                                .limit((limit - listOfDocIds.size).toLong())
                                .get()
                                .addOnSuccessListener {
                                    e->
                                    for (i in e){

                                        if (i.id != docId){
                                            listOfDocIds.add(i.id)
                                        }

                                    }
                                }.addOnCompleteListener{
                                    Log.w("teshehe", listOfDocIds.toString())
                                    callback(true)
                                }
                        }
                        else {
                            callback(true)
                        }
                    }
            }
            else {
                db.collection("users")
//                    .whereLessThanOrEqualTo("Age", maxAge)
//                    .whereGreaterThanOrEqualTo("Age", minAge)
                    .whereEqualTo("Gender", searchGender)
//                    .orderBy("Age")
                    .orderBy("Number")
                    .startAt(startAt)
                    .limit(limit)
                    .get().addOnSuccessListener {
                            dcs->

                        for (i in dcs){
//                            Log.w("teshehe", i.toString())
                            if (i.id != docId){
                                listOfDocIds.add(i.id)
                            }
                        }
                    }.addOnFailureListener{
                            e ->
                        Log.w("teshehe", e.toString())
                    }.addOnCompleteListener{
                        Log.w("teshehe", listOfDocIds.toString())
                        if (listOfDocIds.size < limit){
                            db.collection("users")
                                .whereEqualTo("Gender", searchGender)
                                .limit((limit - listOfDocIds.size).toLong())
                                .get()
                                .addOnSuccessListener {
                                        e->
                                    for (i in e){

                                        if (i.id != docId){
                                            listOfDocIds.add(i.id)
                                        }

                                    }
                                }.addOnCompleteListener{
                                    Log.w("teshehe", listOfDocIds.toString())
                                    callback(true)
                                }
                        }
                        else {
                            callback(true)
                        }
                    }
            }
        }


    }

    private fun addToYes(){
        // add yes to db
        val selectedDoc : String = listOfDocIds[currIdx.toInt()]

        listOfLikes.add(selectedDoc)

//        Toast.makeText(this.requireContext(), "masuk 1", Toast.LENGTH_SHORT).show()

        db.collection("users").document(docId).update(
            "Likes",
            listOfLikes
        ).addOnSuccessListener {
//            Toast.makeText(this.requireContext(), "Added to like", Toast.LENGTH_SHORT).show()
        }.addOnCompleteListener{

            db.collection("users").document(selectedDoc).get().addOnSuccessListener {
                e->

                var searchArr : MutableList<String> = mutableListOf<String>()
                if (e["Likes"] != null){
                    searchArr = e["Likes"] as MutableList<String>
                }

//                Toast.makeText(this.requireContext(), "masuk 2", Toast.LENGTH_SHORT).show()

                for (i in searchArr){
                    if (i.equals(docId)){
                        Toast.makeText(this.requireContext(), "You have found a match", Toast.LENGTH_SHORT).show()
                        listOfMatches.add(selectedDoc)

                        db.collection("users").document(docId).update("Match", listOfMatches)
                        var tempArr : MutableList<String> = mutableListOf<String>()
                        db.collection("users").document(selectedDoc).get().addOnSuccessListener {
                            e->
                            if (e["Match"] != null){
                                tempArr = e["Match"] as MutableList<String>
                            }
                            tempArr.add(docId)
                        }.addOnCompleteListener{
                            db.collection("users").document(selectedDoc).update(
                                "Match", tempArr
                            )
                        }
                    }
                }
            }
        }

        changeIdx()
    }




    fun setNotif(){
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this.requireContext(), AlarmReceiverService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this.requireContext(), 0, alarmIntent, 0)
        alarmManager.cancel(pendingIntent)
        val cal = Calendar.getInstance()
        cal.add(Calendar.MINUTE, 1)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Log.w("Hensemganteng", "muehehe")
    }

    private fun addToNo(){
        // add no to db
        val selectedDoc : String = listOfDocIds[currIdx.toInt()]

        listOfUnlikes.add(selectedDoc)

//        Toast.makeText(this.requireContext(), "masuk 1", Toast.LENGTH_SHORT).show()

        db.collection("users").document(docId).update(
            "Unlikes",
            listOfUnlikes
        ).addOnSuccessListener {
//            Toast.makeText(this.requireContext(), "Added to like", Toast.LENGTH_SHORT).show()
        }.addOnCompleteListener{

        }

        changeIdx()
    }

    private fun changeIdx(){
        currIdx += 1
        imageStrArray.clear()

        if (currIdx == (listOfDocIds.size).toLong() ){
            searchSuggested {
                e->
                 if (e){
                     currIdx = 0
                     assignToSlider(listOfDocIds[currIdx.toInt()])
                 }
            }
        }
        else {
            assignToSlider(listOfDocIds[currIdx.toInt()])
        }
    }

    private fun assignToSlider(documentId : String){
        val slider = binding.imageSliderSuggested

        val list = mutableListOf<SlideModel>()
        pd.setMessage("Assigning to slider")
        getData(documentId) {
                e->
            if (e){
                for ( i in imageStrArray){
                    list.add(SlideModel(i))
                }
                slider.setImageList(list,true)
                pd.dismiss()
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

            var passions : List<String>

            if (e["Passion"] != null){
                passions = e["Passion"] as List<String>
                var passionTitle : TextView = TextView(this.context)
                passionTitle.setText("Passions: ")
                passionTitle.setTypeface(Typeface.DEFAULT_BOLD)
                passionTitle.setTextColor(Color.parseColor("#FFFFFF"))
                passionTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.0F)
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(30,0, 10, 0)

                passionLayout.addView(passionTitle, layoutParams)
                for(p in passions){
                    var passion : TextView = TextView(this.context)
                    passion.setText(p.toString())
                    passion.setTextColor(Color.parseColor("#FFFFFF"))
                    passionTitle.setTypeface(Typeface.DEFAULT_BOLD)
                    passion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.0F)
                    val layoutParams1 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT)
                    layoutParams1.setMargins(5,0, 5, 0)

                    passionLayout.addView(passion, layoutParams1)

                }

            }




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