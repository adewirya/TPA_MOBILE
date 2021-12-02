package edu.bluejack21_1.SunibTinder

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.viewbinding.ViewBindings
import com.google.firebase.firestore.core.ViewSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack21_1.SunibTinder.databinding.FragmentRegisterBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_register.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var spinner: Spinner
    private lateinit var spinner2 : Spinner
    val db = Firebase.firestore
    private lateinit var loadingCircle : ProgressDialog

    private var v: FragmentRegisterBinding? = null
    private val binding get() = v!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun checkEmailExists(email: String, callback: (Boolean) -> Unit) {
        db.collection("users").whereEqualTo("Email",email).get().addOnSuccessListener {
            doc ->
            if (doc.size() > 0){
                callback(false)
            }
            else{
                callback(true)
            }
        }
    }

    private  fun  validateRegister() {


        val email = binding.Email.text.toString()
        val fullName = binding.FullName.text.toString()
        val password = binding.password.text.toString()
        val location = binding.locationSpinner.selectedItem.toString()
        val gender = binding.GenderSpinner.selectedItem.toString()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if (email.isEmpty() || password.isEmpty() || location.isEmpty() || gender.isEmpty() || fullName.isEmpty()) {
            Toast.makeText(
                this.requireContext(), "All fields can't be empty",
                Toast.LENGTH_SHORT
            ).show()
            loadingCircle.dismiss()
            return
        }
        // valid email format
        else if (!email.matches(emailPattern.toRegex())){
            Toast.makeText(
                this.requireContext(),
                "Email must match the email format",
                Toast.LENGTH_SHORT
            ).show()
            loadingCircle.dismiss()
            return
        }
        else if(!password.contains(Regex("[^A-Za-z]")) || !password.contains(Regex("[A-Za-z]")) || !password.contains(Regex("[!\\\"#\$%&'()*+,-./:;\\\\\\\\<=>?@\\\\[\\\\]^_`{|}~]"))) {
            Toast.makeText(
                this.requireContext(),
                "Password must contains at least letter, number, and special character",
                Toast.LENGTH_SHORT
            ).show()
            loadingCircle.dismiss()
            return
        }

        checkEmailExists(email){ result ->
                if (!result){
                    Toast.makeText(
                        this.requireContext(),
                        "Email already exists",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingCircle.dismiss()
                }
                else {
                    val data = hashMapOf(
                        "FullName" to fullName,
                        "Alias" to "",
                        "Email" to email,
                        "Password" to password,
                        "FromGoogle" to false,
                        "Location" to location,
                        "Gender" to gender,
                        "City" to "",
                        "Passions" to listOf(""),
                        "Age" to 0,
                        "Bio" to "",
                        "Profile" to "",
                        "Carousel" to listOf(""),
                        "Preferences" to "Same Campus"
                    )

                    val sharedPref = SharedPrefConfig(this.requireContext())

                    db.collection("users").add(data).addOnSuccessListener { documentReference ->
                        Log.d("add new user", "DocumentSnapshot written with ID: ${documentReference.id}")
                        Toast.makeText(
                            this.requireContext(),
                            "Registered Succesfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        sharedPref.putString("Uid", documentReference.id.toString())
                        sharedPref.putString("FullName", fullName)
                        sharedPref.putString("Email", email)
                        sharedPref.putBoolean("IsGoogle", false)
                        sharedPref.putString("Password", password)
                        sharedPref.putString("Location", location)
                        sharedPref.putString("Gender", gender)
                        sharedPref.putString("Preferences", "Same Campus")

                        loadingCircle.dismiss()
                        activity?.let{

                            if (sharedPref.getString("Uid") != ""){
                                val intent = Intent (it, AddPhotos::class.java)
                                it.startActivity(intent)
                            } else {
                                sharedPref.putString("Uid", documentReference.id.toString())
                            }
                        }

                    }.addOnFailureListener{ e ->
                        Log.w("teseror", "Error adding document", e)
                        Toast.makeText(this.requireContext(), "Failed add new user", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = FragmentRegisterBinding.inflate(inflater, container, false)
//        spinner = v.findViewById(R.id.locationSpinner)
        spinner = v!!.locationSpinner
        spinner2 = v!!.GenderSpinner

        loadingCircle = ProgressDialog(this.requireContext())
        loadingCircle.dismiss()

        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.genders,
            android.R.layout.simple_spinner_item
        ).also { adapter2 ->
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2.adapter = adapter2
        }

        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.binus_locations,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }


        var registerBtn = binding.registerButton
        registerBtn.setOnClickListener {
            loadingCircle.setIndeterminate(false)
            loadingCircle.setCancelable(true)
            loadingCircle.show()
            validateRegister()
        }


        return v!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        v = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_register.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}