package com.blackpineapple.ziptzopt.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blackpineapple.ziptzopt.data.model.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.lang.Exception
import java.lang.NullPointerException
import java.util.concurrent.ExecutionException
import kotlin.collections.HashMap

class FirebaseRealtimeDatabaseImplementation(private val uid: String) : FirebaseRealtimeDatabase {
    private val userDatabaseRef: DatabaseReference = Database.userReference(uid)
    private val phoneNumberToUidRef: DatabaseReference = Database.phoneNumberToUidReference()
    var userMutableLiveData = MutableLiveData<User>()

    override fun getUserInfo() {
        userDatabaseRef.addValueEventListener(UserChangeListener())
    }

    @ExperimentalCoroutinesApi
    override fun getUserInfo2() =  callbackFlow<Result<User>> {
        val  userValueListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val user = User.createUser(snapshot)
                    this@callbackFlow.sendBlocking(Result.success(user))
                } catch (err: NullPointerException) {
                    Timber.e(err.stackTraceToString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }
        }
        Database.userReference(uid).addValueEventListener(userValueListener)

        awaitClose {
            Database.userReference(uid).removeEventListener(userValueListener)
        }
    }

    override fun setUserName(name: String) {
        userDatabaseRef.updateChildren(mapOf(Pair<String, Any>(USER_CHILD_NAME, name)))
    }

    override fun setUserMessage(message: String) {
        userDatabaseRef.updateChildren(mapOf(Pair<String, Any>(USER_CHILD_MESSAGE, message)))
    }

    override fun setUserUid(uid: String) {
        userDatabaseRef.updateChildren(mapOf(Pair<String, Any>(USER_CHILD_UID, uid)))
    }

    override fun setUserPhoneNumber(phoneNumber: String) {
        userDatabaseRef
                .updateChildren(mapOf(Pair<String, Any>(USER_CHILD_PHONE_NUMBER, phoneNumber)))
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

    /*
    @ExperimentalCoroutinesApi
    override fun getPhoneNumberToUid() = callbackFlow<Result<HashMap<String, String>>> {
        val hashMap = HashMap<String, String>()
        phoneNumberToUidRef.get().addOnSuccessListener {
            if (it != null && it.value != null) {
                for (child in it.children.iterator()) {
                    hashMap[child.key.toString()] = child.value.toString()
                }
            }
            sendBlocking(Result.success(hashMap))
        }.addOnFailureListener {
            sendBlocking(Result.failure(it))
        }
    }

     */

    override fun getPhoneNumberToUidSync(): HashMap<String, String> {
        val hashMap = HashMap<String, String>()
        val request = phoneNumberToUidRef.get()

        try {
            Tasks.await(request)
        } catch (e: ExecutionException) {
            Timber.e(e)
        } catch (e: InterruptedException) {
            Timber.e(e)
        }

        return if(request.isSuccessful) {
            val dataSnapshot = request.result
            if (dataSnapshot != null) {
                for (child in dataSnapshot.children.iterator()) {
                    hashMap[child.key.toString()] = child.value.toString()
                }
                hashMap
            } else {
                HashMap()
            }
        } else {
            Timber.e(request.exception)
            HashMap()
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

    @ExperimentalCoroutinesApi
    override fun getAllUserPrivateChatFriends() = callbackFlow<Result<HashMap<String, String>>> {

        val hashMap = HashMap<String, String>()
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children.iterator()) {
                    hashMap[child.key.toString()] = child.value.toString()
                }
                this@callbackFlow.sendBlocking(Result.success(hashMap))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }

        }
        Database.usersToChatContacts(uid).addValueEventListener(eventListener)

        awaitClose {
            Database.usersToChatContacts(uid).removeEventListener(eventListener)
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