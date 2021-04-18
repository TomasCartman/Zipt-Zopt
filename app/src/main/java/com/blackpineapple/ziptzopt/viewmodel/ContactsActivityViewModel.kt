package com.blackpineapple.ziptzopt.viewmodel

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackpineapple.ziptzopt.data.model.Contact
import com.blackpineapple.ziptzopt.firebase.Auth
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabase
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ContactsActivityViewModel : ViewModel() {
    private val auth = Auth.firebaseAuth
    private lateinit var firebaseRealtimeDatabaseImplementation: FirebaseRealtimeDatabaseImpl
    private lateinit var realtimeDatabase: FirebaseRealtimeDatabase
    private var userContacts = mutableListOf<Contact>()
    private var contactListMutableLiveData = MutableLiveData<List<Contact>>()
    val contactListLiveData: LiveData<List<Contact>>
        get() = contactListMutableLiveData

    init {
        if(auth.currentUser != null) {
            firebaseRealtimeDatabaseImplementation = FirebaseRealtimeDatabaseImpl.getInstance(auth.currentUser.uid)
            realtimeDatabase = firebaseRealtimeDatabaseImplementation
        }
    }

    fun getContacts(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            getPhoneUserContactsAsync(context).await()
            val firebaseNumbers = getAllFirebaseUsersNumberSync()
            Timber.d("firebaseNumbers: $firebaseNumbers")

            val contactsInZiptZopt = mutableListOf<Contact>()
            if(userContacts.isNotEmpty() && firebaseNumbers.isNotEmpty()) {
                for (contact in userContacts) {
                    if(contact.refactorNumber().length == 14)  contact.number = contact.refactorNumber()

                    if(firebaseNumbers[contact.number] != null) {
                        if(contactsInZiptZopt.find { it.number == contact.number } == null) {
                            contactsInZiptZopt.add(contact)
                        }
                    }
                }
            }

            withContext(Dispatchers.Main) {
                Timber.d("$contactsInZiptZopt")
                contactListMutableLiveData.postValue(contactsInZiptZopt)
            }
        }
    }

    // This function should be placed somewhere else
    private fun getPhoneUserContactsAsync(context: Context) = viewModelScope.async(Dispatchers.IO) {
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

            userContacts = contactList
    }

    private fun getAllFirebaseUsersNumberSync(): HashMap<String, String> {
        return realtimeDatabase.getAllUserFriendPhoneNumbersToUidSync()
    }
}