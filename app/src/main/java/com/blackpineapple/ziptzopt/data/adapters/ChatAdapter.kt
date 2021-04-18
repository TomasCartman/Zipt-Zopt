package com.blackpineapple.ziptzopt.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.style
import com.airbnb.paris.utils.getStyle
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.data.model.Message
import com.blackpineapple.ziptzopt.firebase.Auth

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
        if (message.sender != Auth.firebaseAuth.uid) {
            holder.setLayoutAsIncoming()
        } else {
            holder.setLayoutAsOutgoing()
        }
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val linearLayout: LinearLayout = itemView.findViewById(R.id.bubble_container)
        val messageTextView: TextView = itemView.findViewById(R.id.message_bubble_textView)
        val metadataSendTimeTextView: TextView = itemView.findViewById(R.id.metadata_time_send_textView)
        val hasSeenImageView: ImageView = itemView.findViewById(R.id.metadata_has_seen_imageView)

        fun setLayoutAsIncoming() {
            val parentConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.bubble_constraint_layout)
            val constraintSet = ConstraintSet()
            constraintSet.clone(parentConstraintLayout)
            constraintSet.clear(R.id.bubble_container, ConstraintSet.END)
            constraintSet.connect(R.id.bubble_container, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            constraintSet.applyTo(parentConstraintLayout)
            linearLayout.apply {
                background = itemView.resources.getDrawable(R.drawable.bg_speech_bubble_incoming)
                style(R.style.ZiptZopt_BubbleIncoming)
            }
        }

        fun setLayoutAsOutgoing() {
            val parentConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.bubble_constraint_layout)
            val constraintSet = ConstraintSet()
            constraintSet.clone(parentConstraintLayout)
            constraintSet.clear(R.id.bubble_container, ConstraintSet.START)
            constraintSet.connect(R.id.bubble_container, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            constraintSet.applyTo(parentConstraintLayout)
            linearLayout.apply {
                background = itemView.resources.getDrawable(R.drawable.bg_speech_bubble_outgoing)
                style(R.style.ZiptZopt_BubbleOutgoing)
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