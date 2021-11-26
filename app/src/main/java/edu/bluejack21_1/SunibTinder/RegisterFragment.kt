package edu.bluejack21_1.SunibTinder

import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
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
            return
        }
        // valid email format
        else if (!email.matches(emailPattern.toRegex())){
            Toast.makeText(
                this.requireContext(),
                "Email must match the email format",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        else if(!password.contains(Regex("[^A-Za-z]")) || !password.contains(Regex("[A-Za-z]")) || !password.contains(Regex("[!\\\"#\$%&'()*+,-./:;\\\\\\\\<=>?@\\\\[\\\\]^_`{|}~]"))) {
            Toast.makeText(
                this.requireContext(),
                "Password must contains at least letter, number, and special character",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        checkEmailExists(email){ result ->
                if (!result){
                    Toast.makeText(
                        this.requireContext(),
                        "Email already exists",
                        Toast.LENGTH_SHORT
                    ).show()
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
                        "Passions" to listOf("bimbing"),
                        "Age" to 0,
                        "Bio" to "",
                        "Profile" to "",
                        "Carousel" to listOf("bimbing")
                    )

                    db.collection("users").add(data).addOnSuccessListener { documentReference ->
                        Log.d("add new user", "DocumentSnapshot written with ID: ${documentReference.id}")
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