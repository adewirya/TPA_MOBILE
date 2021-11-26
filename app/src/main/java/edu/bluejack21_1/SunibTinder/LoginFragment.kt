package edu.bluejack21_1.SunibTinder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack21_1.SunibTinder.databinding.FragmentLoginBinding
import edu.bluejack21_1.SunibTinder.databinding.FragmentRegisterBinding

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

    private var v: FragmentLoginBinding? = null
    private val binding get() = v!!

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
        v = FragmentLoginBinding.inflate(inflater, container, false)

        var loginBtn = binding.loginBtn

        loginBtn.setOnClickListener{
            checkExistsUser {
                e->
                if (!e){
                    Toast.makeText(
                        this.requireContext(),
                        "Wrong email / password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    // redirect ke home
                }
            }
        }

        return v!!.root
    }

    private fun checkExistsUser(callback: (Boolean) -> Unit){


        var email = binding.email.text.toString()
        var password = binding.password.text.toString()

        db.collection("users").whereEqualTo("email",email)
            .whereEqualTo("password",password).get().addOnSuccessListener { e->
                if (e.size() < 0){
                    callback(false)
                }
                else {
                    callback(true)
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
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}