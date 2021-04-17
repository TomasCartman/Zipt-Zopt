package com.blackpineapple.ziptzopt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackpineapple.ziptzopt.firebase.Auth
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabase
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabaseImplementation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class ChatActivityViewModel : ViewModel() {
    private val auth = Auth.firebaseAuth
    private lateinit var firebaseRealtimeDatabaseImplementation: FirebaseRealtimeDatabaseImplementation
    private lateinit var realtimeDatabase: FirebaseRealtimeDatabase
    private var friendPhoneNumber: String = ""

    init {
        if(auth.currentUser != null) {
            firebaseRealtimeDatabaseImplementation = FirebaseRealtimeDatabaseImplementation(auth.currentUser.uid)
            realtimeDatabase = FirebaseRealtimeDatabaseImplementation(auth.currentUser.uid)
        }
    }

    fun setFriendNumber(friendPhoneNumber: String) {
        this.friendPhoneNumber = friendPhoneNumber
        //getAllPrivateChatFriends()
        getAllPrivateChatFriends2()
    }

    fun sendMessage(message: String) {

    }

    private fun getAllPrivateChatFriends() {
        firebaseRealtimeDatabaseImplementation.getAllUserPrivateChatsFriends({ hashMap ->
            if(hashMap[friendPhoneNumber] == null) {
                addNewFriendPrivate(friendPhoneNumber)
            }
        }, {

        })
    }

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

    private fun addNewFriendPrivate(friendPhoneNumber: String) =
        firebaseRealtimeDatabaseImplementation.addNewFriendPrivateChatToUser(friendPhoneNumber)

}