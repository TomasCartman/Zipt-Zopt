package com.blackpineapple.ziptzopt.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.data.model.Contact
import de.hdodenhof.circleimageview.CircleImageView

class ContactsAdapter(private val contactList: List<Contact>) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.contact_item, parent, false)

        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
       val contact = contactList[position]

        holder.nameTextView.text = contact.name
        if (contact.message.isBlank()) holder.messageTextView.visibility = View.GONE
        else holder.messageTextView.text = contact.message
        holder.setPicture(contact.picture)
    }

    override fun getItemCount(): Int = contactList.size

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name_textView)
        val messageTextView: TextView = itemView.findViewById(R.id.message_textView)
        private val picture: CircleImageView = itemView.findViewById(R.id.picture_imageView)

        fun setPicture(pictureLink: String) {
            if (pictureLink.isBlank()) {
                picture.circleBackgroundColor = itemView.resources.getColor(R.color.gray)
                picture.setImageDrawable(itemView.resources.getDrawable(R.drawable.image_person))
            }
        }
    }
}