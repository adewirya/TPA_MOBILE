package edu.bluejack21_1.SunibTinder

import Message
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ChatMessage : AppCompatActivity() {

    val dbUrl = "https://sunibtinder-eb42f-default-rtdb.asia-southeast1.firebasedatabase.app/"

    private lateinit var sendBtn : Button
    private lateinit var messageField : EditText
    private lateinit var receiverName : TextView
    private lateinit var receiverPicture : ImageView
    private lateinit var rDb : DatabaseReference
    val db = Firebase.firestore
    private lateinit var sharedPref : SharedPrefConfig
    private lateinit var receiverId : String
    private lateinit var senderId : String
    private var messageAdapter : MessageAdapter? = null
    private lateinit var msgList : MutableList<Message>
    private lateinit var layout : LinearLayoutManager
    private lateinit var recyclerview : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_message)

        sharedPref = SharedPrefConfig(this)

        sendBtn = findViewById<Button>(R.id.sendBtn)
        messageField = findViewById<EditText>(R.id.messageField)
        receiverName = findViewById<TextView>(R.id.receiverName)
        receiverPicture = findViewById<ImageView>(R.id.receiverPicture)
        recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        msgList = mutableListOf<Message>()
//        senderId = ""

        val db = Firebase.database(dbUrl)
        rDb = db.reference

        receiverId = getIntent().getStringExtra("receiverId").toString()
        senderId = sharedPref.getString("Uid").toString()

        showReceiverProfile()

        sendBtn.setOnClickListener {
            sendMessage()
        }

        layout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        layout.stackFromEnd = true
        recyclerview.layoutManager = layout

        readMessage()



    }

    private fun showReceiverProfile(){
        db.collection("users").document(receiverId).get().addOnSuccessListener { e ->
            val imageUrl = Uri.parse(e["Profile"].toString())
            Picasso.get().load(imageUrl).into(receiverPicture)

            receiverName.setText(e["Name"].toString())

        }


    }

    private fun sendMessage(){
        if(!messageField.text.toString().isEmpty()){
            sendData()
        }else{
            Toast.makeText(this, "Please write a message!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun readMessage(){
        try {
            rDb.child("Pesan").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snap in snapshot.children){
                        val msg = snap.getValue(Message::class.java)
                        if(msg != null){
                            if(msg.senderId.equals(senderId) && msg.receiverId.equals(receiverId)
                                    || msg?.senderId.equals(receiverId) && msg?.receiverId.equals(senderId)
                            ){
                                msgList.add(msg)
                            }
                        }


                    }
                    if(messageAdapter != null){
                        messageAdapter!!.notifyDataSetChanged()
                    }else{
                        messageAdapter = MessageAdapter(this@ChatMessage,msgList,senderId)
                        recyclerview.adapter = messageAdapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun sendData(){
        val date = Calendar.getInstance().time
        val tgl = SimpleDateFormat("dd-mm-yyyy")
        val day = tgl.format(date)

        val cal = Calendar.getInstance()
        val time = SimpleDateFormat("hh: mm")
        val hour = tgl.format(date)
//        rDb?.
//            child("messages")?.
//                child(java.lang.String.valueOf(System.currentTimeMillis()))?.
//                setValue(Message(messageField.text.toString()))
        val newMsg = Message(messageField.text.toString(), senderId, receiverId, day + " " + hour)
        rDb.child("Message").push().setValue(newMsg).addOnSuccessListener {
            e ->
            Log.w("teschat", "Sent Message")
        }

        val Rdb1 = Firebase.database(dbUrl).getReference("Chat List").child(senderId).child(receiverId)
        Rdb1.child("chatID").setValue(receiverId)

        val Rdb2 = Firebase.database(dbUrl).getReference("Chat List").child(receiverId).child(senderId)
        Rdb2.child("chatID").setValue(receiverId)
    }

}