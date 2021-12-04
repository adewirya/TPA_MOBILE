package edu.bluejack21_1.SunibTinder

import Message
import android.app.ProgressDialog
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ChatMessage : AppCompatActivity() {

    val dbUrl = "https://sunibtinder-eb42f-default-rtdb.asia-southeast1.firebasedatabase.app/"

    private lateinit var sendBtn : Button
    private lateinit var uploadBtn : Button
    private lateinit var emojiBtn : Button
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

    private lateinit var doneUrl : String
    private lateinit var imageUrl : Uri
    private lateinit var storage : FirebaseStorage
    private lateinit var storageRef : StorageReference
    private lateinit var emojiTab : String
    private lateinit var emojiTabLayout : LinearLayout
    private lateinit var lastMsg : String
    private lateinit var oldestPostId : String
    var isLoading = false
    var reduce = 5
    private lateinit var viewedList : MutableList<Message>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_message)

        sharedPref = SharedPrefConfig(this)

        sendBtn = findViewById<Button>(R.id.sendBtn)
        uploadBtn = findViewById<Button>(R.id.uploadBtn)
        emojiBtn = findViewById<Button>(R.id.emojiBtn)
        messageField = findViewById<EditText>(R.id.messageField)
        receiverName = findViewById<TextView>(R.id.receiverName)
        receiverPicture = findViewById<ImageView>(R.id.receiverPicture)
        recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        emojiTabLayout = findViewById<LinearLayout>(R.id.emojiTab)
        msgList = mutableListOf<Message>()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference()
        viewedList = mutableListOf()
        emojiTab = "FALSE"
        lastMsg = ""
        oldestPostId = ""
        val db = Firebase.database(dbUrl)
        rDb = db.reference

        receiverId = getIntent().getStringExtra("receiverId").toString()
        senderId = sharedPref.getString("Uid").toString()

        showReceiverProfile()

        sendBtn.setOnClickListener {
            sendMessage()
        }
        uploadBtn.setOnClickListener {
            choosePicture(1)
        }
        emojiBtn.setOnClickListener {
            handleEmojiTab()
        }
//        getLastMessage()
        Log.w("lastmsg", lastMsg)
        setUpEmoji()


        layout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        layout.stackFromEnd = true
        recyclerview.layoutManager = layout
        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(!isLoading){
                    if(!recyclerView.canScrollVertically(-1)){
                        viewedList.clear()
                        isLoading = true
                        var until = 0
                        if(msgList.size - reduce - 1 < 0){
                            until = 0
                        }else{
                            until = msgList.size-reduce-1
                            reduce+=5
                        }
                        Log.w("bicj", msgList.size.toString() + " " + until.toString() + " reduce: " + reduce.toString())
                        for(i in msgList.size-1 downTo until){
                            viewedList.add(msgList.get(i))
                        }
                        viewedList.reverse()
                        if(until != 0){
                            if(viewedList.size != 0){
                                if(messageAdapter == null){
                                    messageAdapter = MessageAdapter(this@ChatMessage,viewedList,senderId)
                                    recyclerview.adapter = messageAdapter
                                }else{
                                    messageAdapter!!.notifyDataSetChanged()
                                }
                            }


                            Handler().postDelayed({
                                recyclerview.smoothScrollToPosition(0)
                                isLoading = false
                                Log.w("done", "done delay")
                            },300)

                        }

                    }
                }

            }
        })


        readMessage()



    }

//    private fun getLastMessage(){
//
//
//        try {
//            rDb.child("Message").addValueEventListener(object : ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for(snap in snapshot.children){
//                        val msg = snap.getValue(Message::class.java)
//                        if(msg != null){
//                            if(msg.senderId.equals(senderId) && msg.receiverId.equals(receiverId)
//                                || msg?.senderId.equals(receiverId) && msg?.receiverId.equals(senderId)
//                            ){
//                                lastMsg = msg.text.toString()
//                            }
//                        }
//
//                    }
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//            Log.w("lastmsg", lastMsg)
//        }catch (e : Exception){
//            e.printStackTrace()
//        }
//
//    }


    private fun setUpEmoji(){
        var e1: Button = findViewById<Button>(R.id.e1)
        e1.setOnClickListener {
            val new = messageField.text.append(e1.text)
            messageField.setText(new)
        }
        var e2: Button = findViewById<Button>(R.id.e2)
        e2.setOnClickListener {
            val new = messageField.text.append(e2.text)
            messageField.setText(new)
        }
        var e3: Button = findViewById<Button>(R.id.e3)
        e3.setOnClickListener {
            val new = messageField.text.append(e3.text)
            messageField.setText(new)
        }
        var e4: Button = findViewById<Button>(R.id.e4)
        e4.setOnClickListener {
            val new = messageField.text.append(e4.text)
            messageField.setText(new)
        }
        var e5: Button = findViewById<Button>(R.id.e5)
        e5.setOnClickListener {
            val new = messageField.text.append(e5.text)
            messageField.setText(new)
        }
        var e6: Button = findViewById<Button>(R.id.e6)
        e6.setOnClickListener {
            val new = messageField.text.append(e6.text)
            messageField.setText(new)
        }
    }

    private fun handleEmojiTab(){
        if(emojiTab.equals("FALSE")){
            emojiTab = "TRUE"
            //SHOW
            emojiTabLayout.visibility = View.VISIBLE
        }else{
            emojiTab = "FALSE"
            //HIDE
            emojiTabLayout.visibility = View.GONE
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
            imageUrl = datas.data!!
            val pd = ProgressDialog(this)
            pd.setTitle("Uploading Image ...")
            pd.show()
            val randomId = UUID.randomUUID().toString()

            val storageReference = storageRef.child("images/" + randomId)

            storageReference.putFile(imageUrl).addOnSuccessListener { e->
                Toast.makeText( this@ChatMessage, "Picture Uploaded Succesfully", Toast.LENGTH_SHORT).show()

            }.addOnProgressListener {
                    e->

                val progress = ((100*e.bytesTransferred) / (e.totalByteCount))
                pd.setMessage("Percentage : $progress%")
                Log.w("progress", progress.toString())
                if (progress.toInt() == 100){
                    storageReference.downloadUrl.addOnSuccessListener { e->
                        doneUrl = e.toString()
                        sendData(true, doneUrl)
                        pd.dismiss()
                    }
                }
            }
        }
    }

    private fun showReceiverProfile(){
//        Log.w("tesaaa", receiverId)
        db.collection("users").document(receiverId).get().addOnSuccessListener { e ->
            val imageUrl = Uri.parse(e["Profile"].toString())
            Picasso.get().load(imageUrl).into(receiverPicture)

            receiverName.setText(e["FullName"].toString())

        }


    }

    private fun sendMessage(){
        if(!messageField.text.toString().isEmpty()){
            sendData(false, "")
        }else{
            Toast.makeText(this, "Please write a message!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun readMessage(){
        try {
            rDb.child("Message").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    msgList.clear()
                    var lastMsg : Message = Message()
                    for(snap in snapshot.children){
                        val msg = snap.getValue(Message::class.java)
                        if(msg != null){
                            if(msg.senderId.equals(senderId) && msg.receiverId.equals(receiverId)
                                    || msg?.senderId.equals(receiverId) && msg?.receiverId.equals(senderId)
                            ){
                                msgList.add(msg)
                                lastMsg = msg

                            }
                        }

                    }

                    if(lastMsg.text != null){
                        viewedList.add(lastMsg)
                    }


                    Log.w("viewed", viewedList.toString())
                    if(viewedList.size != 0){
                        if(messageAdapter == null){
                            messageAdapter = MessageAdapter(this@ChatMessage,viewedList,senderId)
                            recyclerview.adapter = messageAdapter
                        }else{
                            messageAdapter!!.notifyDataSetChanged()
                        }
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

    private fun sendData(isImage : Boolean, imageUrl : String){
        val date = Calendar.getInstance().time
        val tgl = SimpleDateFormat("dd-MM-yyyy")
        val day = tgl.format(date)

        val cal = Calendar.getInstance()
        val time = SimpleDateFormat("hh:mm")
        val hour = time.format(cal.time)

        var newMsg : Message
        if(isImage){
            Log.w("UploadImage", imageUrl)
            newMsg = Message(imageUrl, senderId, receiverId, day + " , " + hour, "TRUE")
        }else{
            newMsg = Message(messageField.text.toString(), senderId, receiverId, day + " , " + hour, "FALSE")
        }

        rDb.child("Message").push().setValue(newMsg).addOnSuccessListener {
            e ->
            Log.w("teschat", "Sent Message")
        }

        val Rdb1 = Firebase.database(dbUrl).getReference("Chat List").child(senderId).child(receiverId)
        Rdb1.child("chatID").setValue(receiverId)

        val Rdb2 = Firebase.database(dbUrl).getReference("Chat List").child(receiverId).child(senderId)
        Rdb2.child("chatID").setValue(receiverId)

        messageField.setText("")
    }

}