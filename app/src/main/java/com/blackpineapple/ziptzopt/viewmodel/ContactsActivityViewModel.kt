package com.blackpineapple.ziptzopt.viewmodel

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackpineapple.ziptzopt.data.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactsActivityViewModel : ViewModel() {
    private var contactListMutableLiveData = MutableLiveData<List<Contact>>()
    val contactListLiveData: LiveData<List<Contact>>
        get() = contactListMutableLiveData

    fun getContacts(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val contactList = mutableListOf<Contact>()

            val cursor: Cursor? = context.contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")

            if(cursor != null) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        val cursorInfo: Cursor? = context.contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                arrayOf(id),
                                null
                        )
                        if( cursorInfo != null) {
                            while (cursorInfo.moveToNext()) {
                                val name  = cursor.getString(
                                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                                val number = cursorInfo.getString(
                                        cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                                contactList.add(Contact(name, number))
                            }
                        }
                        cursorInfo?.close()
                    }
                }
            }
            cursor?.close()

            contactListMutableLiveData.postValue(contactList)
        }

    }
}