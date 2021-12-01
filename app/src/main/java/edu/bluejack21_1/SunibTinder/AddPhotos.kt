package edu.bluejack21_1.SunibTinder

import android.app.ProgressDialog
import android.content.ComponentCallbacks
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import edu.bluejack21_1.SunibTinder.databinding.ActivityAddPhotosBinding
import java.util.*

class AddPhotos : AppCompatActivity() {

//    private lateinit var binding : ActivityAddPhotosBinding
    private lateinit var img1 : ImageView
    private lateinit var img2 : ImageView
    private lateinit var img3 : ImageView
    private lateinit var img4 : ImageView
    private lateinit var imageUrl1 : Uri
    private lateinit var imageUrl2 : Uri
    private lateinit var imageUrl3 : Uri
    private lateinit var imageUrl4 : Uri
    private var bol1 : Boolean = false
    private var bol2 : Boolean = false
    private var bol3 : Boolean = false
    private var bol4 : Boolean = false

    private var db = Firebase.firestore
    private lateinit var listOfUrl : MutableList<String>
    private lateinit var listOfNum : MutableList<Int>

    private lateinit var userId : String
    private lateinit var sharedPref : SharedPrefConfig

    var uploaded : Int = 0

    private lateinit var storage : FirebaseStorage
    private lateinit var storageRef : StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = SharedPrefConfig(this)



        listOfNum = mutableListOf<Int>()
        listOfUrl = mutableListOf<String>()

        setContentView(R.layout.activity_add_photos)

        userId = sharedPref.getString("Uid").toString()

        Log.w("emailtest", userId);

        val doneBtn = findViewById<Button>(R.id.button)

        img1 = findViewById<ImageView>(R.id.imageView)
        img2 = findViewById<ImageView>(R.id.imageView2)
        img3 = findViewById<ImageView>(R.id.imageView3)
        img4 = findViewById<ImageView>(R.id.imageView4)


        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference()

        doneBtn.setOnClickListener{
            // if done is clicked then

            // validate that image must be inputted
            if (!bol1 || !bol2 || !bol3 || !bol4){
                Toast.makeText(this, "All photo must be uploaded", Toast.LENGTH_SHORT).show()
            }
            else {

                uploadPicture(1){
                    e->
                    if(e){
                        uploadPicture(2){
                            e2 ->
                            if (e2){
                                uploadPicture(3){
                                    e3->
                                        if (e3){
                                            uploadPicture(4){
                                                e4->
                                                if (e4){

                                                    // update data in database
                                                    val data = hashMapOf(
                                                        "Carousel" to listOfUrl,
                                                        "Profile" to listOfUrl[0]
                                                        )

                                                    db.collection("users").document(userId).update(data).addOnSuccessListener { e->
//                                                        Toast.makeText(this@AddPhotos, "Profile updated succesfully", Toast.LENGTH_SHORT).show()
                                                        startActivity(Intent(this, Home::class.java))
                                                    }.addOnFailureListener{
                                                            e->
                                                        Toast.makeText(this@AddPhotos, "Profile failed to update", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        }

                                }
                            }
                        }
                    }
                }


            }
        }

        img1.setOnClickListener{

            choosePicture(1)
        }

        img2.setOnClickListener{

            if (!bol1){
                choosePicture(1)
            }
            else {
                choosePicture(2)
            }
        }

        img3.setOnClickListener{
            if (!bol1){
                choosePicture(1)
            }
            else if (!bol2) {
                choosePicture(2)
            } else {
                choosePicture(3)
            }
        }

        img4.setOnClickListener{
            if (!bol1){
                choosePicture(1)
            }
            else if (!bol2) {
                choosePicture(2)
            }
            else if (!bol3){
                choosePicture(3)
            }
            else {
                choosePicture(4)
            }

        }

//        Toast.makeText(this@AddPhotos, "onclick listener ada", Toast.LENGTH_SHORT).show()
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
            uploaded += 1
            bol1 = true
            listOfNum.add(1)
        }

        else if (requestCode == 2 && resultCode == RESULT_OK && datas != null && datas.data != null) {
            imageUrl2 = datas.data!!
            img2.setImageURI(imageUrl2)
            uploaded += 1
            bol2 = true
            listOfNum.add(2)
        }

        else if (requestCode == 3 && resultCode == RESULT_OK && datas != null && datas.data != null) {
            imageUrl3 = datas.data!!
            img3.setImageURI(imageUrl3)
            uploaded += 1
            bol3 = true
//            Toast.makeText(this@AddPhotos, "masuk", Toast.LENGTH_SHORT).show()
            listOfNum.add(3)
        }

        else if (requestCode == 4 && resultCode == RESULT_OK && datas != null && datas.data != null) {
            imageUrl4 = datas.data!!
            img4.setImageURI(imageUrl4)
            uploaded += 1
            bol4 = true
            listOfNum.add(4)
        }

    }

    private fun uploadPicture(Que : Int, callback : (Boolean) -> Unit) {

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

        val randomId = UUID.randomUUID().toString()

        val storageReference = storageRef.child("images/" + randomId)

        storageReference.putFile(url).addOnSuccessListener { e->
            Toast.makeText( this@AddPhotos, "Picture Uploaded Succesfully", Toast.LENGTH_SHORT).show()

        }.addOnProgressListener {
            e->

            val progress = ((100*e.bytesTransferred) / (e.totalByteCount))
            pd.setMessage("Percentage : $progress%")

            if (progress.toInt() == 100){
                storageReference.downloadUrl.addOnSuccessListener { e->
                    listOfUrl.add(e.toString())
                    callback(true)
                }
            }
        }
    }

}