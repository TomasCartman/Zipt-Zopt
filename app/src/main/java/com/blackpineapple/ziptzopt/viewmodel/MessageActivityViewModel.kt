package com.blackpineapple.ziptzopt.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackpineapple.ziptzopt.data.model.User
import com.blackpineapple.ziptzopt.firebase.Auth
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabase
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabaseImplementation
import com.blackpineapple.ziptzopt.utils.QueryPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class MessageActivityViewModel : ViewModel() {
    private val auth = Auth.firebaseAuth
    private lateinit var firebaseRealtimeDatabaseImplementation: FirebaseRealtimeDatabaseImplementation
    private lateinit var realtimeDatabase: FirebaseRealtimeDatabase
    private var userMutableLiveData: MutableLiveData<User> = MutableLiveData()
    val userLiveData: LiveData<User>
        get() = userMutableLiveData

    init {
        if(auth.currentUser != null) {
            firebaseRealtimeDatabaseImplementation = FirebaseRealtimeDatabaseImplementation(auth.currentUser.uid)
            realtimeDatabase = FirebaseRealtimeDatabaseImplementation(auth.currentUser.uid)
            //firebaseRealtimeDatabaseImplementation.getUserInfo()
            //userMutableLiveData = firebaseRealtimeDatabaseImplementation.userMutableLiveData
            getUserInfo()
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

    fun setUserMessage(message: String) = firebaseRealtimeDatabaseImplementation.setUserMessage(message)

    fun setMessageSelectorNumber(context: Context, num: Int) =
        QueryPreferences.setWhichMessageSelector(context, num)

    fun getMessageSelectorNumber(context: Context): Int =
        QueryPreferences.getWhichMessageSelector(context)
}