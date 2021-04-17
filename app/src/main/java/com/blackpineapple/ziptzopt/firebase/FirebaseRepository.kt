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
import java.lang.Exception
import java.lang.NullPointerException
import java.util.*
import java.util.stream.Collectors.toSet
import java.util.stream.Stream
import kotlin.collections.HashMap

class FirebaseRepository(private val uid: String) {
    private val userDatabaseRef: DatabaseReference = Database.userReference(uid)
    private val phoneNumberToUidRef: DatabaseReference = Database.phoneNumberToUidReference()
    var userMutableLiveData = MutableLiveData<User>()


    fun getUserInfo() = userDatabaseRef.addValueEventListener(UserChangeListener())


    fun setUserName(name: String) = userDatabaseRef
            .updateChildren(mapOf(Pair<String, Any>(USER_CHILD_NAME, name)))


    fun setUserMessage(message: String) = userDatabaseRef
            .updateChildren(mapOf(Pair<String, Any>(USER_CHILD_MESSAGE, message)))


    fun setUserUid(uid: String) = userDatabaseRef
            .updateChildren(mapOf(Pair<String, Any>(USER_CHILD_UID, uid)))


    fun setUserPhoneNumber(phoneNumber: String) = userDatabaseRef
            .updateChildren(mapOf(Pair<String, Any>(USER_CHILD_PHONE_NUMBER, phoneNumber)))


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

     fun getPhoneNumberToUid(
         successCallback: (contactHashMap: HashMap<String, String>) -> Unit,
         errorCallback: (exception: Exception) -> Unit
     ) {
        val hashMapPhoneUid = HashMap<String, String>()
        phoneNumberToUidRef.get().addOnSuccessListener {
            if(it.value != null) {
                for (child in it.children.iterator()){
                    hashMapPhoneUid[child.key.toString()] = child.value.toString()
                }
                successCallback(hashMapPhoneUid)
            }
        }.addOnFailureListener {
            Timber.e(it.stackTraceToString())
            errorCallback(it)
        }
    }

    fun setPhoneNumberToUid(phoneNumber: String, uid: String) {
        val hashMap = HashMap<String, Any>()
        hashMap[phoneNumber] = uid
        phoneNumberToUidRef.updateChildren(hashMap)
    }

    fun addNewFriendPrivateChatToUser(phoneNumber: String) {
        val ref = Database.usersToChatContacts(uid)
        val hashMap = HashMap<String, Any>()
        hashMap[phoneNumber] = ref.push().key.toString()
        Database.usersToChatContacts(uid).updateChildren(hashMap)
    }

    fun getAllUserPrivateChatsFriends(
        successCallback: (privateChatsFriendsHashMap: HashMap<String, String>) -> Unit,
        errorCallback: (exception: Exception) -> Unit
    ) {
        val ref = Database.usersToChatContacts(uid)
        val hashMap = HashMap<String, String>()
        ref.get().addOnSuccessListener {
            if (it != null) {
                for (child in it.children.iterator()) {
                    hashMap[child.key.toString()] = child.value.toString()
                }
                successCallback(hashMap)
            }
        }.addOnFailureListener {
            Timber.e(it.stackTraceToString())
            errorCallback(it)
        }
    }

    fun getUserPrivateChatFriend(
        phoneNumber: String,
        successCallback: (privateChatsFriendsHashMap: HashMap<String, String>) -> Unit,
        errorCallback: (exception: Exception) -> Unit
    ) {
        val ref = Database.usersToChatContacts(uid).child(phoneNumber)
        ref.get().addOnSuccessListener {
            val hashMap = HashMap<String, String>()
            if(it != null) {
                Timber.d(it.toString())
            }
        }.addOnFailureListener {
            Timber.e(it.stackTraceToString())
            errorCallback(it)
        }
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

    companion object {
        const val USER_CHILD_NAME = "name"
        const val USER_CHILD_MESSAGE = "message"
        const val USER_CHILD_UID = "uid"
        const val USER_CHILD_PHONE_NUMBER = "phoneNumber"
    }
}