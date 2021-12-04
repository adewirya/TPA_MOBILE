package edu.bluejack21_1.SunibTinder

import android.content.Intent
import android.icu.text.Transliterator
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class BimbingAdapter() : RecyclerView.Adapter<BimbingAdapter.ViewHolder>() {


    val db = Firebase.firestore

    val starts = 0
    val limit = 5
    val continueLoad = true

    lateinit var adapter: BimbingAdapter
//    var listName : MutableList<String> = mutableListOf<String>()
//    var listMsg : MutableList<String> = mutableListOf<String>()
    var listImgUrl : MutableList<String> = mutableListOf<String>()
    var listDocIds : MutableList<String> = mutableListOf<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_chat, parent , false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        if (listImgUrl[position])
        Picasso.get().load(listImgUrl[position]).into(holder.imgView)

        db.collection("users").document(listDocIds[position]).get()
            .addOnSuccessListener {
                e->
                holder.msg.text =  e["FullName"].toString()
                holder.senderTitle.text = e["FullName"].toString()
                holder.ids.text = listDocIds[position]
                holder.ids.visibility = View.INVISIBLE

                Log.w("teshoho", holder.senderTitle.text.toString())
            }
    }

    override fun getItemCount(): Int {
        return listImgUrl.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imgView : ImageView = itemView.findViewById(R.id.rvImageView)
        var senderTitle : TextView = itemView.findViewById(R.id.rvTitle)
        var msg : TextView = itemView.findViewById(R.id.rvMsg)
        var ids : TextView = itemView.findViewById(R.id.rv_id)

        init {
            itemView.setOnClickListener{
                // redirect ke chatroom
                // bebas bimbing mau kasi extrasnya apa
                // addphotos diganti class bimbing
                val intent  = Intent(itemView.context, ChatMessage::class.java)
                intent.putExtra("receiverId", ids.text.toString())

                itemView.context.startActivity(intent)
            }
        }
    }
}