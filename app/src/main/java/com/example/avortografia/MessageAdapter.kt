package com.example.avortografia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*class MessageAdapter(private val messages: MutableList<String>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.message_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.messageText.text = messages[position]
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: String) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}*/
class MessageAdapter(private val messages: MutableList<Pair<String, Boolean>>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userMessage: TextView = itemView.findViewById(R.id.text_user_message)
        val botMessage: TextView = itemView.findViewById(R.id.text_bot_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val (message, isUser) = messages[position]

        if (isUser) {
            holder.userMessage.visibility = View.VISIBLE
            holder.botMessage.visibility = View.GONE
            holder.userMessage.text = message
        } else {
            holder.botMessage.visibility = View.VISIBLE
            holder.userMessage.visibility = View.GONE
            holder.botMessage.text = message
        }
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: Pair<String, Boolean>) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}
