package com.blackpineapple.ziptzopt.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blackpineapple.ziptzopt.data.model.Message
import com.blackpineapple.ziptzopt.data.model.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.lang.Exception
import java.lang.NullPointerException
import java.util.*
import java.util.concurrent.ExecutionException
import kotlin.collections.HashMap

class FirebaseRealtimeDatabaseImpl(private val uid: String) : FirebaseRealtimeDatabase {
    private val userDatabaseRef: DatabaseReference = Database.userRef(uid)
    var userMutableLiveData = MutableLiveData<User>()

    init {

    }

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
        Database.userRef(uid).addValueEventListener(userValueListener)

        awaitClose {
            Database.userRef(uid).removeEventListener(userValueListener)
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
/*
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

 */

    /*
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

     */

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

    override suspend fun getUidFromPhoneNumberSync(phoneNumber: String): String? {
        val snapshot = Database.phoneNumberToUidRef().child(phoneNumber).get().await()
        return snapshot.value.toString()
    }

    fun setPhoneNumberToUid(phoneNumber: String, uid: String) {
        val hashMap = HashMap<String, Any>()
        hashMap[phoneNumber] = uid
        Database.phoneNumberToUidRef().updateChildren(hashMap)
    }

    override fun addNewFriendPrivateChatToUser(friendPhoneNumber: String): String {
        val ref = Database.usersToChatContactsRef(uid)
        val hashMap = HashMap<String, Any>()
        val chatPushKey = ref.push().key.toString()
        hashMap[friendPhoneNumber] = chatPushKey
        ref.updateChildren(hashMap)
        return chatPushKey
    }

    override fun addNewFriendPrivateChatToUser(phoneNumber: String, pushKey: String, friendUid: String) {
        val ref = Database.usersToChatContactsRef().child(friendUid)
        val hashMap = HashMap<String, Any>()
        hashMap[phoneNumber] = pushKey
        ref.updateChildren(hashMap)
    }

    override fun getAllUserFriendPhoneNumbersToUidSync(): HashMap<String, String> {
        val hashMap = HashMap<String, String>()
        val request = Database.phoneNumberToUidRef().get()

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

    fun getAllUserPrivateChatsFriends(
        successCallback: (privateChatsFriendsHashMap: HashMap<String, String>) -> Unit,
        errorCallback: (exception: Exception) -> Unit
    ) {
        val ref = Database.usersToChatContactsRef(uid)
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
        Database.usersToChatContactsRef(uid).addValueEventListener(eventListener)

        awaitClose {
            Database.usersToChatContactsRef(uid).removeEventListener(eventListener)
        }
    }

    override fun sendMessage(messageText: String, pushKey: String) {
        val refPrivateChat = Database.privateChatsRef(pushKey)
        val timestamp = Database.getServerTimestamp()
        val messageHashMap = HashMap<String, Any>()
        val messageId = refPrivateChat.push().key.toString()

        messageHashMap["messageId"] = messageId
        messageHashMap["sender"] = uid
        messageHashMap["hasSeen"] = false
        messageHashMap["messageText"] = messageText
        messageHashMap["timestamp"] = timestamp

        val updateChild = hashMapOf<String, Any>(
            "/privateChats/$pushKey/$messageId" to messageHashMap,
            "/privateChatMetadata/$pushKey" to messageId
        )

        Database.getDatabaseRef().updateChildren(updateChild)
    }

    override suspend fun getUserContactNumberToPushKey(phoneNumber: String): String? {
        val request = Database.usersToChatContactsRef(uid).child(phoneNumber).get().await()
        if(request.value != null) {
            return request.value.toString()
        }
        return null
    }

    override suspend fun getMessageInPrivateChat(chatPushKey: String, messagePushKey: String): Message? {
        val request = Database.privateChatsRef(chatPushKey).child(messagePushKey).get().await()
        if(request.hasChildren()) {
            return request.getValue(Message::class.java)
        }
        return null
    }

    @ExperimentalCoroutinesApi
    override fun getPrivateChatsMetadata(pushKey: String) = callbackFlow<Result<HashMap<String, String>>> {
        val ref = Database.privateChatsMetadata().child(pushKey)
        val hashMap = HashMap<String, String>()
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Timber.d(snapshot.toString())
                hashMap[snapshot.key.toString()] = snapshot.value.toString()
                this@callbackFlow.sendBlocking(Result.success(hashMap))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }
        }
        ref.addValueEventListener(eventListener)

        awaitClose {
            ref.removeEventListener(eventListener)
        }
    }

    @ExperimentalCoroutinesApi
    override fun getCompletePrivateChat(pushKey: String) = callbackFlow<Result<List<Message>>> {
        val ref = Database.privateChatsRef(pushKey).orderByChild("timestamp")
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.map { message ->
                    message.getValue(Message::class.java)
                }
                this@callbackFlow.sendBlocking(Result.success(messages.filterNotNull()))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }
        }
        ref.addValueEventListener(eventListener)

        awaitClose {
            ref.removeEventListener(eventListener)
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
        private var INSTANCE: FirebaseRealtimeDatabaseImpl? = null
        const val USER_CHILD_NAME = "name"
        const val USER_CHILD_MESSAGE = "message"
        const val USER_CHILD_UID = "uid"
        const val USER_CHILD_PHONE_NUMBER = "phoneNumber"

        fun getInstance(uid: String): FirebaseRealtimeDatabaseImpl {
            if (INSTANCE == null) {
                INSTANCE = FirebaseRealtimeDatabaseImpl(uid)
            }
            return INSTANCE as FirebaseRealtimeDatabaseImpl
        }
    }
}