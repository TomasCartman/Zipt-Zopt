package com.blackpineapple.ziptzopt.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blackpineapple.ziptzopt.R
import com.google.android.material.appbar.MaterialToolbar

const val PERMISSION_CODE_READ_CONTACTS = 1

class ContactsActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        toolbar = findViewById(R.id.configurations_toolbar)
        setupToolbar()

        if(ContextCompat.checkSelfPermission(
                        this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        ) {
            getContacts()
        } else {
            this.requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_CODE_READ_CONTACTS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            PERMISSION_CODE_READ_CONTACTS -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else {
                    Toast.makeText(this, "Read contacts permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getContacts() {
        val cursor: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                Phone.DISPLAY_NAME + " ASC")

        if(cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val cursorInfo: Cursor? = contentResolver.query(
                            Phone.CONTENT_URI,
                            null,
                            Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                    )
                    if( cursorInfo != null) {
                        while (cursorInfo.moveToNext()) {
                            val name  = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                            val number = cursorInfo.getString(
                                    cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            Log.d("contact: ", name + " -> number: " + number)
                        }
                    }
                }
            }
        }
    }
}