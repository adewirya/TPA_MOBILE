package edu.bluejack21_1.SunibTinder

import Message
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.rangeTo
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class MessageAdapter() : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    lateinit var context : Context
    lateinit var msgList : List<Message>
    lateinit var currId : String
    val sentMsg = 0
    val receivedMsg = 1


    constructor(ctx : Context, msgs : List<Message>, currUid : String) : this() {
        context = ctx
        msgList = msgs
        currId = currUid

    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var text : TextView
        var time : TextView
        var image : ImageView
        init{
            image = itemView.findViewById<ImageView>(R.id.chatImage)
            text = itemView.findViewById<TextView>(R.id.text)
            time = itemView.findViewById<TextView>(R.id.time)
        }
        fun join(m : Message){
            Log.w("tesss", "view")
            if(m.isImage.equals("TRUE")){
                image.visibility = View.VISIBLE
                Picasso.get().load(Uri.parse(m.text)).into(image)
                text.visibility = View.GONE
            }else{
                image.visibility = View.GONE
                text.visibility = View.VISIBLE
                text.setText(m.text)
            }
            time.setText(m.date.toString().substring(m.date.toString().length-5, m.date.toString().length))
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(msgList.get(position).senderId.equals(currId)){
            return sentMsg
        }else{
            return receivedMsg
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType == sentMsg){
            val view = LayoutInflater.from(context).inflate(R.layout.send_message_layout, parent, false)
            return ViewHolder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.receive_message_layout, parent, false)
            return ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.join(msgList.get(position))
    }



    override fun getItemCount(): Int {
        return msgList.size
    }


}