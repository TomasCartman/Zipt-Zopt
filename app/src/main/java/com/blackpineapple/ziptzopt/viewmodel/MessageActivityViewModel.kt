package com.blackpineapple.ziptzopt.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blackpineapple.ziptzopt.data.model.User
import com.blackpineapple.ziptzopt.firebase.Auth
import com.blackpineapple.ziptzopt.firebase.FirebaseRepository
import com.blackpineapple.ziptzopt.utils.QueryPreferences

class MessageActivityViewModel : ViewModel() {
    private val auth = Auth.firebaseAuth
    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var userMutableLiveData: MutableLiveData<User>
    val userLiveData: LiveData<User>
        get() = userMutableLiveData

    init {
        if(auth.currentUser != null) {
            firebaseRepository = FirebaseRepository(auth.currentUser.uid)
            firebaseRepository.getUserInfo()
            userMutableLiveData = firebaseRepository.userMutableLiveData
        }
    }

    fun setUserMessage(message: String) {
        val user = userLiveData.value
        if(user != null) {
            user.message = message
            firebaseRepository.setUserInfo(user)
        }
    }

    fun setMessageSelectorNumber(context: Context, num: Int) {
        QueryPreferences.setWhichMessageSelector(context, num)
    }

    fun getMessageSelectorNumber(context: Context): Int = QueryPreferences.getWhichMessageSelector(context)
}