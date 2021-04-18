package com.blackpineapple.ziptzopt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackpineapple.ziptzopt.data.model.User
import com.blackpineapple.ziptzopt.firebase.Auth
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabase
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileActivityViewModel : ViewModel() {
    private val auth = Auth.firebaseAuth
    private lateinit var firebaseRealtimeDatabaseImplementation: FirebaseRealtimeDatabaseImpl
    private lateinit var realtimeDatabase: FirebaseRealtimeDatabase
    private var userMutableLiveData: MutableLiveData<User> = MutableLiveData()
    val userLiveData: LiveData<User>
        get() = userMutableLiveData

    init {
        if(auth.currentUser != null) {
            firebaseRealtimeDatabaseImplementation = FirebaseRealtimeDatabaseImpl.getInstance(auth.currentUser.uid)
            realtimeDatabase = FirebaseRealtimeDatabaseImpl.getInstance(auth.currentUser.uid)
            getUserInfo()
            //firebaseRealtimeDatabaseImplementation.getUserInfo()
            //userMutableLiveData = firebaseRealtimeDatabaseImplementation.userMutableLiveData
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            realtimeDatabase.getUserInfo2().collect {
                when {
                    it.isSuccess -> {
                        val user = it.getOrNull()
                        userMutableLiveData.postValue(user)
                    }
                    it.isFailure -> {
                        Timber.e(it.exceptionOrNull()?.stackTraceToString())
                    }
                }
            }
        }
    }

    fun setUserName(name: String) = firebaseRealtimeDatabaseImplementation.setUserName(name)
}