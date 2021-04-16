package com.blackpineapple.ziptzopt.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blackpineapple.ziptzopt.data.model.Contact
import com.blackpineapple.ziptzopt.data.model.User
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import java.lang.NullPointerException
import java.util.*
import java.util.stream.Collectors.toSet
import java.util.stream.Stream
import kotlin.collections.HashMap

const val USER_CHILD_NAME = "name"
const val USER_CHILD_MESSAGE = "message"
const val USER_CHILD_UID = "uid"
const val USER_CHILD_PHONE_NUMBER = "phoneNumber"

class FirebaseRepository(private val uid: String) {
    private val userDatabaseRef: DatabaseReference = Database.userReference(uid)
    private val phoneNumberToUidRef: DatabaseReference = Database.phoneNumberToUidReference()
    var userMutableLiveData = MutableLiveData<User>()

    fun getUserInfo() {
        userDatabaseRef.addValueEventListener(UserChangeListener())
    }

    @Deprecated("This function has been deprecated. Use setUserName(name: String)," +
            " setUserMessage(message: String), setUserUid(uid: String) and" +
            " setUserPhoneNumber(phoneNumber: String) for the uses")
    fun setUserInfo(user: User) {
        userDatabaseRef.setValue(user.toMap())
    }

    fun setUserName(name: String) {
        userDatabaseRef.updateChildren(mapOf(Pair<String, Any>(USER_CHILD_NAME, name)))
    }

    fun setUserMessage(message: String) {
        userDatabaseRef.updateChildren(mapOf(Pair<String, Any>(USER_CHILD_MESSAGE, message)))
    }

    fun setUserUid(uid: String) {
        userDatabaseRef.updateChildren(mapOf(Pair<String, Any>(USER_CHILD_UID, uid)))
    }

    fun setUserPhoneNumber(phoneNumber: String) {
        userDatabaseRef.updateChildren(mapOf(Pair<String, Any>(USER_CHILD_PHONE_NUMBER, phoneNumber)))
    }

     fun getUidFromPhoneNumber(phoneNumber: String) {
        phoneNumberToUidRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("snapshot", snapshot.child(phoneNumber).toString())
                //phoneNumberToUidMutableLiveData.value
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.d(error.message)
            }
        })
    }

     fun getPhoneNumberToUid(callback: (contactList: HashMap<String, String>) -> Unit) {
        val hashMapPhoneUid = HashMap<String, String>()
        phoneNumberToUidRef.get().addOnSuccessListener {
            if(it.value != null) {
                for (child in it.children.iterator()){
                    hashMapPhoneUid[child.key.toString()] = child.value.toString()
                }
                callback(hashMapPhoneUid)
            }
        }.addOnFailureListener {
            Timber.e(it.stackTraceToString())
        }
    }

    fun setPhoneNumberToUid(phoneNumber: String, uid: String) {
        val hashMap = HashMap<String, Any>()
        hashMap[phoneNumber] = uid
        phoneNumberToUidRef.updateChildren(hashMap)
    }

    private inner class UserChangeListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            try {
                val user = User.createUser(snapshot)
                userMutableLiveData.postValue(user)
            } catch (err: NullPointerException) {
                Timber.e(err.stackTraceToString())
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Timber.d(error.message)
        }
    }
}