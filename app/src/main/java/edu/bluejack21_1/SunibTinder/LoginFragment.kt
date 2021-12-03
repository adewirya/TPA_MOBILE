package edu.bluejack21_1.SunibTinder

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
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
//import edu.bluejack21_1.SunibTinder.
import edu.bluejack21_1.SunibTinder.databinding.FragmentLoginBinding
import edu.bluejack21_1.SunibTinder.databinding.FragmentRegisterBinding
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import edu.bluejack21_1.SunibTinder.databinding.ActivityAddPhotosBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val db = Firebase.firestore
    private lateinit var loadingCircle : ProgressDialog

    private var v: FragmentLoginBinding? = null
    private val binding get() = v!!
    private lateinit var sharedPref : SharedPrefConfig

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

        sharedPref = SharedPrefConfig(this.requireContext())
        sharedPref.clearSharedPreference()

        // Inflate the layout for this fragment
        v = FragmentLoginBinding.inflate(inflater, container, false)

        var loginBtn = binding.loginBtn

        loadingCircle = ProgressDialog(this.requireContext())
        loadingCircle.dismiss()

        loginBtn.setOnClickListener{
            loadingCircle.setIndeterminate(false)
            loadingCircle.setCancelable(true)
            loadingCircle.show()
            checkExistsUser {
                sse->
                if (!sse){
                    Toast.makeText(
                        this.requireContext(),
                        "Wrong email / password",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingCircle.dismiss()
                }
                else {
                    // redirect ke home
                        loadingCircle.dismiss()
                    Toast.makeText(this.requireContext(), "suk $sse",  Toast.LENGTH_SHORT).show()
                    if (sse){
                        activity?.let{
                            val intent = Intent (it, Home::class.java)
                            it.startActivity(intent)
                        }
                    }
                }

            }
        }

        val loginGoogleBtn = binding.loginGoogleBtn

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this.activity, gso);

        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this.requireContext())


        // liat user udh sign in atau blom
        if(googleSignInAccount != null) {
            // buka home

        }


        loginGoogleBtn.setOnClickListener{
            loadingCircle.setIndeterminate(false)
            loadingCircle.setCancelable(true)
            loadingCircle.show()
            val signInIntent: Intent = googleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, 1)
        }

        return v!!.root
    }




    private fun checkExistsUser(callback: (Boolean) -> Unit){


        val email = binding.email.text.toString()
        val password = binding.password.text.toString()


        db.collection("users").whereEqualTo("Email",email)
            .whereEqualTo("Password",password).get().addOnSuccessListener { e->

                if (e.size() <= 0){
                    callback(false)
                }
                else {

                    for (i in e){
                        sharedPref.putString("Uid", i.id)
                    }
                    sharedPref.putBoolean("IsGoogle", false)
                    sharedPref.putString("Email", email)
                    sharedPref.putString("Password", password)

                    callback(true)
                }


            }.addOnFailureListener{ e ->
                Toast.makeText(
                    this.requireContext(),
                    "Failed to access database",
                    Toast.LENGTH_LONG
                ).show()

            }


    }
    private fun checkExistsGoogleAccount(email : String, callback: (Boolean) -> Unit){

        db.collection("users").whereEqualTo("Email",email)
            .get()
            .addOnSuccessListener { e->

                if (e.size() <= 0){
                    callback(false)
                }
                else {
                    // get document id based on fields
                    for(i in e.documents){
                        sharedPref.putString("Uid", i.id)
                    }
                    callback(true)
                }
            }
            .addOnFailureListener{ e ->
                Toast.makeText(
                    this.requireContext(),
                    "Failed to access database",
                    Toast.LENGTH_LONG
                ).show()

            }


    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            Toast.makeText(
                this.requireContext(),
                "Sign in successful",
                Toast.LENGTH_SHORT
            ).show()
            binding.loginGoogleBtn.setText("Log in success")
            val acct = GoogleSignIn.getLastSignedInAccount(activity)
            if (acct != null) {
                val personName: String = acct.getDisplayName().toString()
                val personGivenName: String = acct.getGivenName().toString()
                val personFamilyName: String = acct.getFamilyName().toString()
                val personEmail: String = acct.getEmail().toString()
                val personId: String = acct.getId().toString()
                val personPhoto: Uri = acct.getPhotoUrl()


                checkExistsGoogleAccount(personEmail){
                        e ->
                    if(!e){
                        // register
                        val data = hashMapOf(
                            "FullName" to personName,
                            "Alias" to personGivenName,
                            "Email" to personEmail,
                            "Password" to "",
                            "FromGoogle" to true,
                            "Location" to "Anggrek",
                            "Gender" to "Male",
                            "City" to "",
                            "Passions" to listOf(""),
                            "Age" to 0,
                            "Bio" to "",
                            "Profile" to "",
                            "Carousel" to listOf(""),
                            "Preferences" to "Same Campus",
                            "Min Age" to 0,
                            "Max Age" to 100
                        )


                        sharedPref.putInt("MinAge", 0)
                        sharedPref.putInt("MaxAge", 100)
                        sharedPref.putString("FullName", personName)
                        sharedPref.putString("Email", personEmail)
                        sharedPref.putBoolean("IsGoogle",true)
                        sharedPref.putString("Preferences", "Same Campus")

                        db.collection("users").add(data).addOnSuccessListener { e ->
                            Toast.makeText(this.requireContext(), "User " + e.id, Toast.LENGTH_SHORT).show()
                            sharedPref.putString("Uid", e.id)

                        }.addOnFailureListener{ e ->
                            Log.w("teseror", "Error adding document", e)
                            Toast.makeText(this.requireContext(), "Failed add new user", Toast.LENGTH_SHORT).show()
                        }.addOnCompleteListener{

                            activity?.let{
                                val intent = Intent (it, AddPhotos::class.java)
                                it.startActivity(intent)
                            }
                        }

                    }else{

                        // redirect.
                        sharedPref.putInt("MinAge", 0)
                        sharedPref.putInt("MaxAge", 100)
                        sharedPref.putString("FullName", personName)
                        sharedPref.putString("Email", personEmail)
                        sharedPref.putBoolean("IsGoogle",true)
                        activity?.let{
                            val intent = Intent (it, Home::class.java)
                            it.startActivity(intent)
                        }


                    }
                }
            }




        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("google err", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(
                this.requireContext(),
                "Sign in unsuccessful" + e.statusCode,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}