package com.blackpineapple.ziptzopt.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.data.model.Message

class ChatFragmentAdapter : ListAdapter<Message, ChatFragmentAdapter.ChatFragmentViewHolder>(ChatFragmentAdapter.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatFragmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item, parent, false)
        return ChatFragmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatFragmentViewHolder, position: Int) {
        val message = getItem(position)

        holder.nameTextView.text = message.sender
        holder.chatLastMessageTextView.text = message.messageText
        holder.timeLastMessageTextView.text = message.timestamp.toString()
    }

    inner class ChatFragmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.conversation_contact_name)
        val chatLastMessageTextView: TextView = itemView.findViewById(R.id.chat_last_message)
        val timeLastMessageTextView: TextView = itemView.findViewById(R.id.chat_last_time_message)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.messageId == newItem.messageId
            }
        }
    }
}