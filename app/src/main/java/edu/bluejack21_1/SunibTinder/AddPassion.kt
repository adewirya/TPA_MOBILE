package edu.bluejack21_1.SunibTinder

import android.R.attr
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.marginTop
import android.R.attr.button
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddPassion : AppCompatActivity() {
    private lateinit var passionLayout : LinearLayout
    private lateinit var passionDoneBtn : Button
    private lateinit var sharedPref : SharedPrefConfig
    private lateinit var passionPref : String
    private lateinit var listOfPassion : MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_passion)
        sharedPref = SharedPrefConfig(this)
        passionLayout = findViewById<LinearLayout>(R.id.passionLayout)
        passionDoneBtn = findViewById<Button>(R.id.donePassionBtn)
        passionLayout.orientation = LinearLayout.VERTICAL
        passionPref = sharedPref.getString("Passion").toString()

        val passionSelectedList = passionPref.split(",")

        val passionList = resources.getStringArray(R.array.passions).toList()
        val checkList = mutableListOf<Boolean>()
        var index = 0

        listOfPassion = mutableListOf<String>()

        for(p in passionList){
            val btn : Button = Button(this)

            btn.setText(p.toString())
            btn.id = index
            checkList.add(false)
            index++
            btn.setBackgroundColor(Color.WHITE)
            for(s in passionSelectedList){
                if(p.equals(s)){
                    btn.setBackgroundColor(Color.LTGRAY)
                    checkList.set(btn.id, true)
                    break
                }
            }

            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0, 20, 0, 20)

            passionLayout.addView(btn, layoutParams)

            btn.setOnClickListener{
                if(!checkList.get(btn.id)){
                    btn.setBackgroundColor(Color.LTGRAY)
                    checkList.set(btn.id, true)
                }else{
                    btn.setBackgroundColor(Color.WHITE)
                    checkList.set(btn.id, false)
                }
            }
        }

        passionDoneBtn.setOnClickListener {

            var index = 0
            var count = 0
            var selected = ""
            for(b in checkList){
                if(b){
                    listOfPassion.add(passionList.get(index).toString())
                    selected += passionList.get(index).toString() + ","
                    count++
                }

                index++
            }
            if(count > 3){
                Toast.makeText(this, "Passions must be less than or equals to 3", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            selected = selected.dropLast(1)
//            Toast.makeText(this, selected, Toast.LENGTH_SHORT).show()
            sharedPref.putString("Passion", selected)

            val docId = sharedPref.getString("Uid").toString()
            val db  = Firebase.firestore

            db.collection("users").document(docId).update("Passion", listOfPassion).addOnSuccessListener {
                e->
//                    Toast.makeText(this@AddPassion, "Passion added succesfully", Toast.LENGTH_SHORT).show()
            }.addOnCompleteListener{
                finish()
            }

        }

    }

}