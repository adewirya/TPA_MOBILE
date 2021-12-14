package edu.bluejack21_1.SunibTinder

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import edu.bluejack21_1.SunibTinder.databinding.FragmentLoginBinding
import edu.bluejack21_1.SunibTinder.databinding.FragmentProfileBinding
import java.net.URI
import java.util.*

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
    private lateinit var imageUri : Uri

    private lateinit var pd : ProgressDialog

    private lateinit var imageView : ImageView


    val db = Firebase.firestore
    private lateinit var loadingCircle : ProgressDialog

    private lateinit var docId : String

    private var v: FragmentProfileBinding? = null
    private val binding get() = v!!
    private lateinit var sharedPref : SharedPrefConfig

    private lateinit var storage : FirebaseStorage
    private lateinit var storageRef : StorageReference


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
        docId = sharedPref.getString("Uid").toString()
        pd = ProgressDialog(this.requireContext())

        // Inflate the layout for this fragment
        v = FragmentProfileBinding.inflate(inflater, container, false)

        imageView = binding.imageView5

        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference()

        var imageUrl : Uri
        imageUrl = Uri.parse("")

        pd.setTitle(context?.resources?.getString(R.string.getting_data))
//        pd.setMessage("Getting Data from database.")
        pd.show()
        db.collection("users").document(docId).get().addOnSuccessListener {
            e ->
            val arr : List<String> = e["Carousel"] as List<String>
            imageUrl = Uri.parse(arr[0])
            Picasso.get().load(imageUrl).into(imageView)
//            pd.setMessage("Getting Data from database..")

            binding.textView2.setText(e["FullName"].toString() + ", ")
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
//            pd.setMessage("Getting Data from database...")
        }.addOnCompleteListener{
            db.collection("users").document(docId).update("Profile", imageUrl.toString())
            pd.dismiss()
        }

        imageView.setOnClickListener{
            choosePicture(1)
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

    private fun choosePicture(RequestCode: Int) {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, RequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, datas: Intent?) {
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK && datas != null && datas.data != null) {
            imageUri = datas.data!!
            imageView.setImageURI(imageUri)
            updateAndUpload()
        }
    }

    private fun updateAndUpload() {
        val pd = ProgressDialog(this.requireContext())
        pd.setTitle("Updating Image ")
        pd.show()

        val randomId = UUID.randomUUID().toString()

        val storageReference = storageRef.child("images/" + randomId)

        storageReference.putFile(imageUri).addOnSuccessListener { e->
            Toast.makeText(this.requireContext(), "Image Updated Succesfully", Toast.LENGTH_SHORT)

        }.addOnProgressListener {
                e->

            val progress = ((100*e.bytesTransferred) / (e.totalByteCount))
            pd.setMessage("Percentage : $progress%")

            if (progress.toInt() == 100){


                storageReference.downloadUrl.addOnSuccessListener { e->
                    db.collection("users").document(docId).update("Profile", e.toString())
                }

                pd.dismiss()
            }
        }


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