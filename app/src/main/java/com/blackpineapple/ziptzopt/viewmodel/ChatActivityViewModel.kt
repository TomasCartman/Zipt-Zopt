package com.blackpineapple.ziptzopt.viewmodel

import androidx.lifecycle.ViewModel
import com.blackpineapple.ziptzopt.firebase.Auth
import com.blackpineapple.ziptzopt.firebase.FirebaseRepository

class ChatActivityViewModel : ViewModel() {
    private val auth = Auth.firebaseAuth
    private lateinit var firebaseRepository: FirebaseRepository
    private var friendPhoneNumber: String = ""

    init {
        if(auth.currentUser != null) {
            firebaseRepository = FirebaseRepository(auth.currentUser.uid)
        }
    }

    fun setFriendNumber(friendPhoneNumber: String) {
        this.friendPhoneNumber = friendPhoneNumber
        getAllPrivateChatFriends()
    }

    fun sendMessage(message: String) {

    }

    private fun getAllPrivateChatFriends() {
        firebaseRepository.getAllUserPrivateChatsFriends({ hashMap ->
            if(hashMap[friendPhoneNumber] == null) {
                addNewFriendPrivate(friendPhoneNumber)
            }
        }, {

        })
    }

    private fun addNewFriendPrivate(friendPhoneNumber: String) =
        firebaseRepository.addNewFriendPrivateChatToUser(friendPhoneNumber)

}