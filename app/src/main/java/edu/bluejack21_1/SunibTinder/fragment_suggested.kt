package edu.bluejack21_1.SunibTinder

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

    /*
    * Suggested List
    * - Orang yang like
    * - Orang yang berbeda gender
    * - Orang yang sudah like tidak bisa muncul lagi klo sudah di like / unlike
    * - Pastiin berdasarkan age range preferences
    * - Pastiin sesuai dengan location Preferences
    * -
    * */


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
        val slider = binding.imageSliderSuggested

        val list = mutableListOf<SlideModel>()

        getData {
            e->
            if (e){

                //
                for ( i in imageStrArray){
                    list.add(SlideModel(i))
                }

                slider.setImageList(list,true)

            }
        }

        return v!!.root
    }

    private fun getData(callback: (Boolean) -> Unit){
        db.collection("users").document(docId).get().addOnSuccessListener {
                e->
            imageStrArray = e["Carousel"] as MutableList<String>
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