package com.blackpineapple.ziptzopt.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blackpineapple.ziptzopt.data.model.User
import com.google.firebase.database.*
import timber.log.Timber
import java.lang.NullPointerException

class FirebaseRepository(private val uid: String) {
    private val databaseRef: DatabaseReference = Database.userReference(uid)
    private var userMutableLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User>
        get() = userMutableLiveData

    fun getUserInfo() {
        databaseRef.addValueEventListener(UserChangeListener())
    }

    fun setUserInfo(user: User) {
        databaseRef.setValue(user.toMap())
    }

    private inner class UserChangeListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            try {
                val user = User.createUser(snapshot)
                userMutableLiveData.value = user
            } catch (err: NullPointerException) {
                Timber.e(err.stackTraceToString())
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Timber.d(error.message)
        }
    }
}