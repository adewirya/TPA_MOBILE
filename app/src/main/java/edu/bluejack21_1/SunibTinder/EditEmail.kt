package edu.bluejack21_1.SunibTinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditEmail : AppCompatActivity() {

    private var db = Firebase.firestore

    private lateinit var oldEmail : EditText
    private lateinit var newEmail : EditText
    private lateinit var confirmEmail : EditText
    private lateinit var doneBtn : Button

    private lateinit var sharedPref : SharedPrefConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_email)

        sharedPref = SharedPrefConfig(this)


        oldEmail = findViewById<EditText>(R.id.oldEmail)
        newEmail = findViewById<EditText>(R.id.newEmail)
        confirmEmail = findViewById<EditText> (R.id.confirmEmail)
        doneBtn = findViewById<Button>(R.id.doneBtn)

        doneBtn.setOnClickListener{
            validateInput()
        }
    }

    private fun validateInput() {
        val oMail = oldEmail.text.toString()
        val newMail = newEmail.text.toString()
        val confirm = confirmEmail.text.toString()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (oMail.isEmpty() || newMail.isEmpty() || confirm.isEmpty()){
            Toast.makeText(this@EditEmail, "All fields must be filled", Toast.LENGTH_SHORT).show()
            return
        }
        else if (newMail != confirm ){
            Toast.makeText(this@EditEmail, "Email must be the same", Toast.LENGTH_SHORT).show()
            return
        }
        else if (oMail != sharedPref.getString("Email")){
            Toast.makeText(this@EditEmail, "Wrong email", Toast.LENGTH_SHORT).show()
            return
        }
        else if (!newMail.matches(emailPattern.toRegex())){
            Toast.makeText(this@EditEmail, "Wrong email pattern", Toast.LENGTH_SHORT).show()
            return
        }
        else {
            // update ke databasenya karena lewati semua validasi
            val docId = sharedPref.getString("Uid").toString()
            db.collection("users").document(docId).update("Email", newMail).addOnSuccessListener {
                e->
                Toast.makeText(this@EditEmail, "Email updated succefully", Toast.LENGTH_SHORT).show()
                sharedPref.putString("Email", newMail)
            }.addOnCompleteListener{
                startActivity(Intent(this, Home::class.java))
            }
        }



    }
}