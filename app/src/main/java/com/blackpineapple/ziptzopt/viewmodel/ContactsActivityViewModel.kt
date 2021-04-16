package com.blackpineapple.ziptzopt.viewmodel

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackpineapple.ziptzopt.data.model.Contact
import com.blackpineapple.ziptzopt.firebase.Auth
import com.blackpineapple.ziptzopt.firebase.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class ContactsActivityViewModel : ViewModel() {
    private val auth = Auth.firebaseAuth
    private lateinit var firebaseRepository: FirebaseRepository
    private var userContacts = mutableListOf<Contact>()
    private var firebaseNumbers = hashMapOf<String, String>()
    private var contactListMutableLiveData = MutableLiveData<List<Contact>>()
    val contactListLiveData: LiveData<List<Contact>>
        get() = contactListMutableLiveData

    init {
        if(auth.currentUser != null) {
            firebaseRepository = FirebaseRepository(auth.currentUser.uid)
        }
    }

    fun getContacts(context: Context) { // Delete 'i' variable after tests
        viewModelScope.launch(Dispatchers.Default) {
            getFirebaseUsersNumberAsync(context).await()
            getPhoneUserContactsAsync(context).await()

            val contactsInZiptZopt = mutableListOf<Contact>()
            var i = 0
            if(userContacts.isNotEmpty() && firebaseNumbers.isNotEmpty()) {
                for (contact in userContacts) {
                    i += 1
                    val contactNumber =
                            if(contact.refectorNumber().length == 14) contact.refectorNumber()
                            else contact.number

                    if(firebaseNumbers[contactNumber] != null) {
                        if(contactsInZiptZopt.find { it.number == contact.number } == null) {
                            contactsInZiptZopt.add(contact)
                        }
                    }
                }
            }


            withContext(Dispatchers.Main) {
                Timber.d("i: $i")
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

    private fun getFirebaseUsersNumberAsync(context: Context) = viewModelScope.async(Dispatchers.IO) {
            firebaseRepository.getPhoneNumberToUid({ numberHashMap ->
                    firebaseNumbers = numberHashMap
            }, {
                Toast.makeText(context, "You must have a internet connection", Toast.LENGTH_SHORT).show()
            })
        }
}