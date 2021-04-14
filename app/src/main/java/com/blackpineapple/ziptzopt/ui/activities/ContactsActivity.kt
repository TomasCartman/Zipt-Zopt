package com.blackpineapple.ziptzopt.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.data.adapters.ContactsAdapter
import com.blackpineapple.ziptzopt.viewmodel.ContactsActivityViewModel
import com.google.android.material.appbar.MaterialToolbar

const val PERMISSION_CODE_READ_CONTACTS = 1

class ContactsActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var contactActivityViewModel: ContactsActivityViewModel
    private lateinit var contactsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        contactActivityViewModel = ViewModelProvider
                .NewInstanceFactory().create(ContactsActivityViewModel::class.java)

        toolbar = findViewById(R.id.configurations_toolbar)
        setupToolbar()

        contactsRecyclerView = findViewById(R.id.contacts_recyclerView)
        contactsRecyclerView.layoutManager = LinearLayoutManager(this@ContactsActivity)


        if(ContextCompat.checkSelfPermission(
                        this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        ) {
            setRecyclerViewAdapter()
        } else {
            this.requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_CODE_READ_CONTACTS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            PERMISSION_CODE_READ_CONTACTS -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setRecyclerViewAdapter()
                } else {
                    Toast.makeText(this, "Read contacts permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setRecyclerViewAdapter() {
        val contactList = contactActivityViewModel.getContacts(this)
        contactsRecyclerView.adapter = ContactsAdapter(contactList)
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}