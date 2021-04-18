package com.blackpineapple.ziptzopt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackpineapple.ziptzopt.data.model.Message
import com.blackpineapple.ziptzopt.firebase.Auth
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabase
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabaseImplementation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class ChatActivityViewModel : ViewModel() {
    private val auth = Auth.firebaseAuth
    private lateinit var firebaseRealtimeDatabaseImplementation: FirebaseRealtimeDatabaseImplementation
    private lateinit var realtimeDatabase: FirebaseRealtimeDatabase
    private var _pushKey: String? = null
    private var isGroup: Boolean = false
    private var friendPhoneNumber: String = ""
    private val messagesMutableLiveData = MutableLiveData<List<Message>>()
    val messagesLiveData: LiveData<List<Message>>
        get() = messagesMutableLiveData

    init {
        if(auth.currentUser != null) {
            firebaseRealtimeDatabaseImplementation = FirebaseRealtimeDatabaseImplementation(auth.currentUser.uid)
            realtimeDatabase = FirebaseRealtimeDatabaseImplementation(auth.currentUser.uid)
        }
    }

    fun setFriendNumber(friendPhoneNumber: String) {
        this.friendPhoneNumber = friendPhoneNumber
        viewModelScope.launch(Dispatchers.IO) {
            getPushKeyAsync().await()
            getCompleteChatMessages()
        }
    }

    fun setGroup(isGroup: Boolean) {
        this.isGroup = isGroup
    }

    private fun getPushKeyAsync() = viewModelScope.async(Dispatchers.IO) {
        if(isGroup) {

        } else {
            val pushKey = realtimeDatabase.getUserContactNumberToPushKey(friendPhoneNumber)

            Timber.d(pushKey)
            if (pushKey != null) {
                _pushKey = pushKey
            } else {
                addNewFriendPrivateAsync(friendPhoneNumber).await()
            }
        }
    }

    fun sendMessage(message: String) {
        _pushKey?.let { realtimeDatabase.sendMessage(message, it) }
    }

    private fun getCompleteChatMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            _pushKey?.let { _pushKey ->
                realtimeDatabase.getCompletePrivateChat(_pushKey).collect {
                    when {
                        it.isSuccess -> {
                            val messages = it.getOrNull()
                            messagesMutableLiveData.postValue(messages)
                            Timber.d(messages.toString())
                        }
                        it.isFailure -> {
                            Timber.e(it.exceptionOrNull()?.stackTraceToString())
                        }
                    }
                }
            }
        }
    }

    private fun getAllPrivateChatFriends() {
        firebaseRealtimeDatabaseImplementation.getAllUserPrivateChatsFriends({ hashMap ->
            if(hashMap[friendPhoneNumber] == null) {
                addNewFriendPrivateAsync(friendPhoneNumber)
            }
        }, {

        })
    }

    /*
    private fun getAllPrivateChatFriends2() {
        viewModelScope.launch(Dispatchers.IO) {
            realtimeDatabase.getAllUserPrivateChatFriends().collect {
                when {
                    it.isSuccess -> {
                        val hashMap = it.getOrNull()
                        if (hashMap != null && hashMap[friendPhoneNumber] == null) {
                            addNewFriendPrivate(friendPhoneNumber)
                        }
                    }
                    it.isFailure -> {
                        Timber.e(it.exceptionOrNull()?.stackTraceToString())
                    }
                }
            }
        }
    }

     */

    private fun addNewFriendPrivateAsync(friendPhoneNumber: String) = viewModelScope.async {
        val pushKey = realtimeDatabase.addNewFriendPrivateChatToUser(friendPhoneNumber)
        _pushKey = pushKey
        Timber.d(pushKey)
    }
}