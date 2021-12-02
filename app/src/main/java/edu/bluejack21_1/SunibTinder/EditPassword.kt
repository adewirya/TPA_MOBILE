package edu.bluejack21_1.SunibTinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditPassword : AppCompatActivity() {
    private var db = Firebase.firestore

    private lateinit var oldPassword : EditText
    private lateinit var newPassword : EditText
    private lateinit var confirmPassword : EditText
    private lateinit var doneBtn : Button

    private lateinit var currPass : String

    private lateinit var sharedPref : SharedPrefConfig




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)

        sharedPref = SharedPrefConfig(this)
        currPass = sharedPref.getString("Password").toString()

        oldPassword = findViewById<EditText>(R.id.oldPassword)
        newPassword = findViewById<EditText>(R.id.newPassword)
        confirmPassword = findViewById<EditText>(R.id.confirmPassword)
        doneBtn = findViewById<Button>(R.id.doneBtn)

        doneBtn.setOnClickListener{
            validateEditPassword()
        }
    }

    private fun validateEditPassword() {
        val oPass = oldPassword.text.toString()
        val newPass = newPassword.text.toString()
        val confirm = confirmPassword.text.toString()

//        Toast.makeText(this@EditPassword, oPass + " " +newPass + " " + confirm, Toast.LENGTH_SHORT).show()

        if (oPass.isEmpty() || newPass.isEmpty() || confirm.isEmpty()){
            Toast.makeText(this@EditPassword, "All fields must be filled", Toast.LENGTH_SHORT).show()
            return
        }
        else if (newPass != confirm ){
            Toast.makeText(this@EditPassword, "Password must be the same", Toast.LENGTH_SHORT).show()
            return
        }
        else if (!oPass.equals(currPass)){
            Toast.makeText(this@EditPassword, "Wrong Password", Toast.LENGTH_SHORT).show()
            return
        }
        else if (!newPass.contains(Regex("[^A-Za-z]")) || !newPass.contains(Regex("[A-Za-z]")) || !newPass.contains(Regex("[!\\\"#\$%&'()*+,-./:;\\\\\\\\<=>?@\\\\[\\\\]^_`{|}~]"))){
            Toast.makeText(this@EditPassword, "Password must contains at least letter, number, and special character", Toast.LENGTH_SHORT).show()
            return
        }
        else {

            // update ke databasenya karena lewati semua validasi
            val docId = sharedPref.getString("Uid").toString()
            db.collection("users").document(docId).update("Password", newPass).addOnSuccessListener {
                    e->
                Toast.makeText(this@EditPassword, "Password updated succefully", Toast.LENGTH_SHORT).show()
                sharedPref.putString("Password", newPass)
            }.addOnCompleteListener{
                startActivity(Intent(this, Home::class.java))
            }
        }

    }
}