package edu.bluejack21_1.SunibTinder

import android.app.ProgressDialog
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class   EditInfo : AppCompatActivity() {


    val db = Firebase.firestore
    private lateinit var storage : FirebaseStorage
    private lateinit var storageRef : StorageReference
    private lateinit var uId : String
    private lateinit var sharedPref : SharedPrefConfig
    private lateinit var genderSpinner : Spinner
    private lateinit var locationSpinner : Spinner
    private lateinit var nameTxtField : EditText
    private lateinit var aliasTxtField : EditText
    private lateinit var bioTxtField : EditText
    private lateinit var ageTxtField : EditText
    private lateinit var livingInTxtField : EditText
    private lateinit var doneBtn : Button
    private lateinit var doneBtn2 : Button
    private lateinit var addPassionBtn : Button
    private lateinit var passionTextView : TextView
    private lateinit var img1 : ImageView
    private lateinit var img2 : ImageView
    private lateinit var img3 : ImageView
    private lateinit var img4 : ImageView

    private lateinit var pd : ProgressDialog

    private var bol1 : Boolean = false
    private var bol2 : Boolean = false
    private var bol3 : Boolean = false
    private var bol4 : Boolean = false
    private lateinit var imageUrl1 : Uri
    private lateinit var imageUrl2 : Uri
    private lateinit var imageUrl3 : Uri
    private lateinit var imageUrl4 : Uri
    private lateinit var listOfUrl : MutableList<String>

    private lateinit var btn1 : Button
    private lateinit var btn2 : Button
    private lateinit var btn3 : Button
    private lateinit var btn4 : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)


        pd = ProgressDialog(this)
        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference()

        listOfUrl = MutableList(5){
            it -> ""
        }
//        Log.w("tess", listOfUrl.toString())



        sharedPref = SharedPrefConfig(this)
        genderSpinner = findViewById<Spinner>(R.id.genderSpinner)
        locationSpinner = findViewById<Spinner>(R.id.locationSpinner)
        nameTxtField = findViewById<EditText>(R.id.nameTxtField)
        aliasTxtField = findViewById<EditText>(R.id.aliasTxtField)
        bioTxtField = findViewById<EditText>(R.id.bioTxtField)
        ageTxtField = findViewById<EditText>(R.id.ageTxtField)
        livingInTxtField = findViewById<EditText>(R.id.livingInTxtField)
        passionTextView = findViewById<TextView>(R.id.passionTextView)
        doneBtn = findViewById<Button>(R.id.doneEditBtn)
        doneBtn2 = findViewById<Button>(R.id.doneInfoBtn)
        addPassionBtn = findViewById<Button>(R.id.addPassionBtn)
        img1 = findViewById<ImageView>(R.id.imageView)
        img2 = findViewById<ImageView>(R.id.imageView2)
        img3 = findViewById<ImageView>(R.id.imageView3)
        img4 = findViewById<ImageView>(R.id.imageView4)

        btn1 = findViewById<Button>(R.id.btnDel1)
        btn2 = findViewById<Button>(R.id.btnDel2)
        btn3 = findViewById<Button>(R.id.btnDel3)
        btn4 = findViewById<Button>(R.id.btnDel4)

        uId = sharedPref.getString("Uid").toString()

        setDefaultValues()

        ArrayAdapter.createFromResource(
            this,
            R.array.genders,
            android.R.layout.simple_spinner_item
        ).also { adapter2 ->
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            genderSpinner.adapter = adapter2
        }

        img1.setOnClickListener{
            choosePicture(1)
        }

        img2.setOnClickListener{
            choosePicture(2)
        }

        img3.setOnClickListener{
            choosePicture(3)

        }

        img4.setOnClickListener{
            choosePicture(4)

        }

        ArrayAdapter.createFromResource(
            this,
            R.array.binus_locations,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            locationSpinner.adapter = adapter
        }
        doneBtn.setOnClickListener{
            saveData()
        }
        doneBtn2.setOnClickListener{
            saveData()
        }
        addPassionBtn.setOnClickListener{
            startActivity(Intent(this, AddPassion::class.java))
        }


        btn1.setOnClickListener{
            delPict(1)
        }

        btn2.setOnClickListener{
            delPict(2)
        }

        btn3.setOnClickListener{

            delPict(3)
        }

        btn4.setOnClickListener{

            delPict(4)
        }
    }

    private fun clearImg(callback: (Boolean) -> Unit){
        pd.setMessage("Sorting Images Datas..")
        img1.setImageURI(Uri.parse(""))
        img2.setImageURI(Uri.parse(""))
        img3.setImageURI(Uri.parse(""))
        img4.setImageURI(Uri.parse(""))
        btn1.visibility = View.INVISIBLE
        btn2.visibility = View.INVISIBLE
        btn3.visibility = View.INVISIBLE
        btn4.visibility = View.INVISIBLE
        callback(true)
    }

    private fun shiftImg(){
        pd.setTitle("Sorting Images")
        pd.setMessage("Sorting Images Datas.")
        pd.show()
        clearImg {
            e->
            if (e){
                pd.setMessage("Sorting Images Datas...")
                if (listOfUrl.getOrNull(0) != null && listOfUrl.getOrNull(0) != ""){
                    Picasso.get().load(listOfUrl[0]).into(img1)
                    btn1.visibility = View.VISIBLE
                }
                else {
                    btn1.visibility = View.INVISIBLE
                }

                if (listOfUrl.getOrNull(1) != null && listOfUrl.getOrNull(1) != ""){
                    Picasso.get().load(listOfUrl[1]).into(img2)
                    btn2.visibility = View.VISIBLE
                }
                else {
                    btn2.visibility = View.INVISIBLE
                }

                if (listOfUrl.getOrNull(2) != null && listOfUrl.getOrNull(2) != ""){
                    Picasso.get().load(listOfUrl[2]).into(img3)
                    btn3.visibility = View.VISIBLE
                }
                else {
                    btn3.visibility = View.INVISIBLE
                }

                if (listOfUrl.getOrNull(3) != null && listOfUrl.getOrNull(3) != ""){
                    Picasso.get().load(listOfUrl[3]).into(img4)
                    btn4.visibility = View.VISIBLE
                }
                else {
                    btn4.visibility = View.INVISIBLE
                }
                pd.dismiss()
            }
        }
    }

    private fun delPict(RequestCode: Int){
        if (RequestCode == 1){
            img1.setImageURI(Uri.parse(""))
            btn1.visibility = View.INVISIBLE
            imageUrl1 = Uri.parse("")
        }
        else if (RequestCode == 2){
            img2.setImageURI(Uri.parse(""))
            btn2.visibility = View.INVISIBLE
            imageUrl2 = Uri.parse("")

        }
        else if (RequestCode == 3){
            img3.setImageURI(Uri.parse(""))
            btn3.visibility = View.INVISIBLE
            imageUrl3 = Uri.parse("")

        }
        else if (RequestCode == 4){
            img4.setImageURI(Uri.parse(""))
            btn4.visibility = View.INVISIBLE
            imageUrl4 = Uri.parse("")

        }

        listOfUrl[RequestCode-1] = ""
//        Log.w("tess", listOfUrl.toString())
        updatePicture{
            e->
            if (e){
                shiftImg()
            }
        }
    }

    private fun choosePicture(RequestCode : Int) {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, RequestCode)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, datas: Intent?) {
        super.onActivityResult(requestCode, resultCode, datas)

        if (requestCode == 1 && resultCode == RESULT_OK && datas != null && datas.data != null) {
            imageUrl1 = datas.data!!
            img1.setImageURI(imageUrl1)
            bol1 = true
            uploadPicture(1)
            btn1.visibility = View.VISIBLE
        }

        else if (requestCode == 2 && resultCode == RESULT_OK && datas != null && datas.data != null) {
            imageUrl2 = datas.data!!
            img2.setImageURI(imageUrl2)
            bol2 = true
            uploadPicture(2)
            btn2.visibility = View.VISIBLE
        }

        else if (requestCode == 3 && resultCode == RESULT_OK && datas != null && datas.data != null) {
            imageUrl3 = datas.data!!
            img3.setImageURI(imageUrl3)
            bol3 = true
            uploadPicture(3)
            btn3.visibility = View.VISIBLE
        }

        else if (requestCode == 4 && resultCode == RESULT_OK && datas != null && datas.data != null) {
            imageUrl4 = datas.data!!
            img4.setImageURI(imageUrl4)
            bol4 = true
            uploadPicture(4)
            btn4.visibility = View.VISIBLE
        }

    }

    private fun uploadPicture(Que : Int) {

        val pd = ProgressDialog(this)
        pd.setTitle("Uploading Image "+ Que)
        pd.show()

        var url : Uri
        url = Uri.EMPTY

        if (Que == 1){
            url = imageUrl1
        } else if (Que == 2){
            url = imageUrl2
        } else if (Que == 3 ){
            url = imageUrl3
        } else{
            url = imageUrl4
        }

        val idx : Int = Que-1

        val randomId = UUID.randomUUID().toString()

        val storageReference = storageRef.child("images/" + randomId)

        storageReference.putFile(url).addOnSuccessListener { e->
            Toast.makeText( this@EditInfo, "Picture Uploaded Succesfully", Toast.LENGTH_SHORT).show()

        }.addOnProgressListener {
                e->

            val progress = ((100*e.bytesTransferred) / (e.totalByteCount))
            pd.setMessage("Percentage : $progress%")

            if (progress.toInt() == 100){
                storageReference.downloadUrl.addOnSuccessListener { e->
                    listOfUrl.add(e.toString())
                    pd.dismiss()
                    updatePicture{
                        e->
                        if (e){
                            shiftImg()
                        }
                    }
                }
            }
        }
    }

    private fun updatePicture(callback : (Boolean) -> Unit){
        val docId = sharedPref.getString("Uid").toString()
        val sortedList : MutableList<String> = mutableListOf<String>()

        for ( i in listOfUrl){
            if (i != ""){
                sortedList.add(i)
            }
        }

        listOfUrl.clear()
        listOfUrl = sortedList

//        Log.w("tess", listOfUrl.toString())

        db.collection("users").document(docId)
            .update("Carousel", sortedList).addOnSuccessListener {
                e->

            }.addOnCompleteListener{
                callback(true)
            }
    }

    private fun setDefaultValues(){
        pd.setTitle("Getting Data from Database")
        pd.setMessage("Getting User's Info.")
        pd.show()
        db.collection("users").document(uId).get().addOnSuccessListener { e ->
            pd.setMessage("Getting User's Info..")
            nameTxtField.setText(e["FullName"].toString())
            aliasTxtField.setText(e["Alias"].toString())
            bioTxtField.setText(e["Bio"].toString())
            ageTxtField.setText(e["Age"].toString())
            livingInTxtField.setText(e["City"].toString())
            genderSpinner.setSelection(getGenderIndex(e["Gender"].toString()))
            locationSpinner.setSelection(getCampusLocationIndex(e["Location"].toString()))

            var passions : List<String>

            if (e["Passion"] != null){
                passions = e["Passion"] as List<String>
                var passionStr = ""
                for(p in passions){
                    passionStr += p + ","
                }
                passionStr = passionStr.dropLast(1)
                sharedPref.putString("Passion", passionStr)
                passionTextView.setText(passionStr)
            }

            pd.setMessage("Getting User's Info...")

            val picts : List<String> = e["Carousel"] as List<String>
            var idx : Int = 0
            for (i in picts){
                if (i != ""){
                    listOfUrl[idx] = i
                    idx += 1
                }
            }
//            img1.setImageURI(Uri.parse(picts[0]))
            pd.setMessage("Getting User's Info....")

            if (picts.getOrNull(0) != null  && picts.getOrNull(0) != ""){
                Picasso.get().load(picts[0]).into(img1)
            }
            else {
                btn1.visibility = View.INVISIBLE
            }

            if (picts.getOrNull(1) != null  && picts.getOrNull(1) != ""){
                Picasso.get().load(picts[1]).into(img2)
            }
            else {
                btn2.visibility = View.INVISIBLE
            }

            if (picts.getOrNull(2) != null  && picts.getOrNull(2) != ""){
                Picasso.get().load(picts[2]).into(img3)
            }
            else {
                btn3.visibility = View.INVISIBLE
            }

            if (picts.getOrNull(3) != null  && picts.getOrNull(3) != ""){
                Picasso.get().load(picts[3]).into(img4)
            }
            else {
                btn4.visibility = View.INVISIBLE
            }

            if (picts.getOrNull(3) != null  && picts.getOrNull(3) != ""){
                Picasso.get().load(picts[3]).into(img4)
            }
            else {
                btn4.visibility = View.INVISIBLE
            }

            pd.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        passionTextView.setText(sharedPref.getString("Passion").toString())
    }

    private fun getGenderIndex(gender : String) : Int{
        val genderList = resources.getStringArray(R.array.genders).toList()
        var index : Int = 0
        for (g in genderList) {

            if(gender.equals(g)){
                return index
            }
            index++
            Log.w("gender", g.toString())
        }
        return 0
    }
    private fun getCampusLocationIndex(location : String) : Int{
        val locationList = resources.getStringArray(R.array.binus_locations).toList()
        var index : Int = 0
        for (l in locationList) {

            if(location.equals(l)){
                return index
            }
            index++
            Log.w("Location", l.toString())
        }
        return 0
    }
    private fun saveData(){
        //klo mau validasi validasi dlu

        val fullName = nameTxtField.text.toString()
        val alias = aliasTxtField.text.toString()
        val bio = bioTxtField.text.toString()
        val age = ageTxtField.text.toString()
        val livingIn = livingInTxtField.text.toString()
        val gender = genderSpinner.selectedItem.toString()
        val location = locationSpinner.selectedItem.toString()

        if(bioTxtField.lineCount > 3){
            Toast.makeText(this, "Bio Line Count Must Be Smaller Than 3", Toast.LENGTH_SHORT).show()
            return
        }
        if(gender.equals("")){
            Toast.makeText(this, "Gender Must Be Selected", Toast.LENGTH_SHORT).show()
            return
        }
        if(location.equals("")){
            Toast.makeText(this, "Campus Location Must Be Selected", Toast.LENGTH_SHORT).show()
            return
        }
        if(fullName.length <= 0){
            Toast.makeText(this, "Name Must Be Filled", Toast.LENGTH_SHORT).show()
            return
        }

        val passionPref = sharedPref.getString("Passion").toString()
        val passions = passionPref.split(",")

        // update data in database
        val data = hashMapOf(
            "FullName" to fullName,
            "Alias" to  alias,
            "Bio" to bio,
            "Age" to age,
            "City" to livingIn,
            "Gender" to gender,
            "Location" to location,
            "Passion" to passions,
            "Carousel" to listOfUrl
        )

        db.collection("users").document(uId).update(data).addOnSuccessListener { e->
            Toast.makeText(this, "Successfully Updated User Data", Toast.LENGTH_SHORT).show()
            // ni aku gatau apa aja
            sharedPref.putString("FullName", fullName)
            sharedPref.putString("Alias", alias)
            sharedPref.putString("Bio", bio)
            sharedPref.putInt("Age", Integer.parseInt(age))
            sharedPref.putString("Location", location)
            sharedPref.putString("Gender", gender)
        }.addOnFailureListener{
                e->
            Toast.makeText(this@EditInfo, "Profile failed to update", Toast.LENGTH_SHORT).show()
        }.addOnCompleteListener{
            startActivity(Intent(this, Home::class.java))
        }


    }


}