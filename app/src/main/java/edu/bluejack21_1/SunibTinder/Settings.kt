package edu.bluejack21_1.SunibTinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.internal.artificialFrame
import org.w3c.dom.Text
import androidx.annotation.NonNull
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient

import com.google.android.gms.tasks.OnCompleteListener




class Settings : AppCompatActivity() {
    lateinit var googleSignInAccount : GoogleSignInAccount
    lateinit var googleSignInClient : GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnSuggest = findViewById<TextView>(R.id.textView6)
        val btnPassword = findViewById<TextView>(R.id.textView4)
        val btnEmail = findViewById<TextView>(R.id.textView9)
        val btnLogout = findViewById<TextView>(R.id.textView21)
        val sharedPref = SharedPrefConfig(this)

        if (sharedPref.getBoolean("IsGoogle") == true){
            btnPassword.visibility = View.INVISIBLE
            btnEmail.visibility = View.INVISIBLE
        }

        btnEmail.setOnClickListener{
            startActivity(Intent(this, EditEmail::class.java))
        }

        btnPassword.setOnClickListener{
            startActivity(Intent(this, EditPassword::class.java))
        }

        btnSuggest.setOnClickListener{
            startActivity(Intent(this, EditSuggestion::class.java))
        }



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)

        btnLogout.setOnClickListener{
            if(googleSignInAccount != null){
                signOut()
            }

        }


    }
    fun signOut(){



        googleSignInClient.signOut()
            .addOnCompleteListener(this, OnCompleteListener<Void?> {
                // redirect ke login page.
                startActivity(Intent(this, MainActivity::class.java))
            })
    }
}