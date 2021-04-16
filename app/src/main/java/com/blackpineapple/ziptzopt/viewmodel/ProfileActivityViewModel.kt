package com.blackpineapple.ziptzopt.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blackpineapple.ziptzopt.data.model.User
import com.blackpineapple.ziptzopt.firebase.Auth
import com.blackpineapple.ziptzopt.firebase.FirebaseRepository

class ProfileActivityViewModel : ViewModel() {
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

    fun setUserName(name: String) {
        firebaseRepository.setUserName(name)
    }
}