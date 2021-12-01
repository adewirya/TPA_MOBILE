package edu.bluejack21_1.SunibTinder

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import edu.bluejack21_1.SunibTinder.databinding.FragmentLoginBinding
import edu.bluejack21_1.SunibTinder.databinding.FragmentProfileBinding
import java.net.URI

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_profile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val db = Firebase.firestore
    private lateinit var loadingCircle : ProgressDialog

    private var v: FragmentProfileBinding? = null
    private val binding get() = v!!
    private lateinit var sharedPref : SharedPrefConfig



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPref = SharedPrefConfig(this.requireContext())
        val docId = sharedPref.getString("Uid").toString()

        // Inflate the layout for this fragment
        v = FragmentProfileBinding.inflate(inflater, container, false)

        val imageView = binding.imageView5

//        imageView.setImageURI(Uri.parse("https://lh3.googleusercontent.com/a-/AOh14GiNJNRukVh2-pNdmr70A_40Q5mJNlXv7QqjfLVw"))
        var imageUrl : Uri



        db.collection("users").document(docId).get().addOnSuccessListener {
            e ->
            imageUrl = Uri.parse(e["Profile"].toString())
            Picasso.with(this.requireContext()).load(imageUrl).into(imageView)
            binding.textView2.setText(e["FullName"].toString())
            binding.userAge.setText(e["Age"].toString())

            if (e["Location"].toString() == ""){
                binding.userLocation.setText("Not Yet Defined")
            } else {
                binding.userLocation.setText(e["Location"].toString())
            }

            if (e["Bio"] == ""){
                binding.textView5.setText("Add your bio")
            }
            else {
                binding.textView5.setText(e["Bio"].toString())
            }



        }

        imageView.setOnClickListener{
            Toast.makeText(this.requireContext(), "KECLICK", Toast.LENGTH_SHORT).show()
            imageView.setImageURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/sunibtinder-eb42f.appspot.com/o/images%2Ffac3ec74-6d18-44b6-81bc-3438b4fe0fde?alt=media&token=e62a8bc3-ab3e-4aff-a25d-2aef30bd543d"))

        }

        binding.editBtn.setOnClickListener{
            activity?.let{
                val intent = Intent (it, EditInfo::class.java)
                it.startActivity(intent)
            }
        }

        binding.settingBtn.setOnClickListener{
            activity?.let{
                val intent = Intent (it, Settings::class.java)
                it.startActivity(intent)
            }
        }


        return v!!.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}