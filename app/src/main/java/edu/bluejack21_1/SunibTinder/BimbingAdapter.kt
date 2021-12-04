package edu.bluejack21_1.SunibTinder

import android.content.Intent
import android.icu.text.Transliterator
import android.net.Uri
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

    val listName : MutableList<String> = mutableListOf<String>()
    val listMsg : MutableList<String> = mutableListOf<String>()
    val listImgUrl : MutableList<Uri> = mutableListOf<Uri>()
    var listDocIds : MutableList<String> = mutableListOf<String>()


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imgView : ImageView = itemView.findViewById(R.id.rvImageView)
        var senderTitle : TextView = itemView.findViewById(R.id.rvTitle)
        var msg : TextView = itemView.findViewById(R.id.rvMsg)
        var id : String = itemView.findViewById<TextView>(R.id.rv_id).text.toString()
        var ids : TextView = itemView.findViewById(R.id.rv_id)
        init {
            itemView.setOnClickListener{
                // redirect ke chatroom
                // bebas bimbing mau kasi extrasnya apa
                // addphotos diganti class bimbing
                 val intent  = Intent(itemView.context, AddPhotos::class.java)
                intent.putExtra("Uid", id)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_chat, parent , false)
        return ViewHolder(v)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(listImgUrl[position]).into(holder.imgView)
        holder.msg.text = listMsg[position]
        holder.senderTitle.text = listName[position]
        holder.ids.text = listDocIds[position]
    }

    override fun getItemCount(): Int {
        return listDocIds.size
    }
}