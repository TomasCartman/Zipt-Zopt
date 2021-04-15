package com.blackpineapple.ziptzopt.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blackpineapple.ziptzopt.data.model.User
import com.google.firebase.database.*
import timber.log.Timber
import java.lang.NullPointerException

const val USER_CHILD_NAME = "name"
const val USER_CHILD_MESSAGE = "message"
const val USER_CHILD_UID = "uid"
const val USER_CHILD_PHONE_NUMBER = "phoneNumber"

class FirebaseRepository(private val uid: String) {
    private val userDatabaseRef: DatabaseReference = Database.userReference(uid)
    private val phoneNumberToUidRef: DatabaseReference = Database.phoneNumberToUidReference()
    var userMutableLiveData = MutableLiveData<User>()
    var phoneNumberToUidMutableLiveData = MutableLiveData<String>()

    fun getUserInfo() {
        /*
        userDatabaseRef.get().addOnSuccessListener {
            val user = User.createUser(it)
            userMutableLiveData.postValue(user)
        }.addOnFailureListener {
            Timber.e(it.stackTraceToString())
        }

         */
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