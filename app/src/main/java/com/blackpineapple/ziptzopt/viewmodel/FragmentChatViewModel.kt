package com.blackpineapple.ziptzopt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackpineapple.ziptzopt.data.model.Message
import com.blackpineapple.ziptzopt.firebase.Auth
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabase
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class FragmentChatViewModel : ViewModel() {
    private val auth = Auth.firebaseAuth
    private lateinit var firebaseRealtimeDatabaseImplementation: FirebaseRealtimeDatabaseImpl
    private lateinit var realtimeDatabase: FirebaseRealtimeDatabase
    private var messagesMutableLiveData = MutableLiveData<List<Message>>()
    val messagesLiveData: LiveData<List<Message>>
        get() = messagesMutableLiveData

    init {
        if(auth.currentUser != null) {
            firebaseRealtimeDatabaseImplementation = FirebaseRealtimeDatabaseImpl.getInstance(auth.currentUser.uid)
            realtimeDatabase = FirebaseRealtimeDatabaseImpl.getInstance(auth.currentUser.uid)
            getAllPrivateMessagesPushKeys()
        }
    }

    private fun getAllPrivateMessagesPushKeys() {
        viewModelScope.launch(Dispatchers.IO) {
            realtimeDatabase.getAllUserPrivateChatFriends().collect {
                when {
                    it.isSuccess -> {
                        val privateNumberToPushKey = it.getOrNull()
                        Timber.d(privateNumberToPushKey.toString())
                        if (privateNumberToPushKey != null) {
                            for (pushKey in privateNumberToPushKey.values) {
                                getAllChatsMetadata(pushKey)
                                Timber.d("getAllPrivateMessagesPushKeys")
                            }
                        }
                    }
                    it.isFailure -> {
                        Timber.d(it.exceptionOrNull())
                    }
                }
            }
        }
    }

    private fun getAllChatsMetadata(pushKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            realtimeDatabase.getPrivateChatsMetadata(pushKey).collect {
                when {
                    it.isSuccess -> {
                        val chatToMessagePushKeys = it.getOrNull()
                        Timber.d(chatToMessagePushKeys.toString())
                        if (chatToMessagePushKeys != null) {
                            val messagesList = mutableListOf<Message>()
                            chatToMessagePushKeys.forEach { (t, u) ->
                                val message = getLastMessage(t, u)
                                if (message != null) {
                                    messagesList.add(message)
                                }
                                Timber.d("getAllChatsMetadata")
                            }
                            messagesMutableLiveData.postValue(messagesList)
                            Timber.d(messagesList.toString())
                        }
                    }
                    it.isFailure -> {

                    }
                }
            }
        }
    }

    private suspend fun getLastMessage(chatPushKey: String, messagePushKey: String): Message? {
        val message = realtimeDatabase.getMessageInPrivateChat(chatPushKey, messagePushKey)
        Timber.d(message.toString())
        return message
    }
}