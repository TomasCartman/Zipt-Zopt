package com.blackpineapple.ziptzopt.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.data.model.Message

class ChatAdapter : ListAdapter<Message, ChatAdapter.ChatViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.speech_bubble_outgoing, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = getItem(position)
        holder.messageTextView.text = message?.messageText.toString()
        holder.metadataSendTimeTextView.text = message?.timestamp.toString()
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearLayout: LinearLayout = itemView.findViewById(R.id.metadata_bubble_textView)
        val messageTextView: TextView = itemView.findViewById(R.id.message_bubble_textView)
        val metadataSendTimeTextView: TextView = itemView.findViewById(R.id.metadata_time_send_textView)
        val hasSeenImageView: ImageView = itemView.findViewById(R.id.metadata_has_seen_imageView)

        fun setLayoutAsIncoming() {
            linearLayout.apply {
                setPadding(20, 8, 10, 8)
            }
        }
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