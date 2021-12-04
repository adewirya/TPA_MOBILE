package edu.bluejack21_1.SunibTinder

import Message
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
        init{

            text = itemView.findViewById<TextView>(R.id.text)
        }
        fun join(m : Message){
            text.setText(m.text)
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