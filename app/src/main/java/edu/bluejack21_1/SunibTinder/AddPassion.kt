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


class AddPassion : AppCompatActivity() {
    private lateinit var passionLayout : LinearLayout
    private lateinit var passionDoneBtn : Button
    private lateinit var sharedPref : SharedPrefConfig
    private lateinit var passionPref : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_passion)
        sharedPref = SharedPrefConfig(this)
        passionLayout = findViewById<LinearLayout>(R.id.passionLayout)
        passionDoneBtn = findViewById<Button>(R.id.donePassionBtn)
        passionLayout.orientation = LinearLayout.VERTICAL
        passionPref = sharedPref.getString("passion").toString()

        val passionSelectedList = passionPref.split(",")

        val passionList = resources.getStringArray(R.array.passions).toList()
        val checkList = mutableListOf<Boolean>()
        var index = 0

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
            var selected = ""
            for(b in checkList){
                if(b){
                    selected += passionList.get(index).toString() + ","
                }

                index++
            }
            selected = selected.dropLast(1)
            Toast.makeText(this, selected, Toast.LENGTH_SHORT).show()
            sharedPref.putString("passion", selected)
            finish()
        }

    }

}