package com.blackpineapple.ziptzopt.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blackpineapple.ziptzopt.data.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import timber.log.Timber

class FirebaseRepository(uid: String) {
    private val databaseRef = Database.userReference(uid)
    private var userMutableLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User>
        get() = userMutableLiveData

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        databaseRef.addValueEventListener(valueChangeListener)
    }

    fun setUserInfo(user: User) {
        databaseRef.updateChildren(user.toMap())
    }

    private val valueChangeListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val user = snapshot.getValue(User::class.java)
            userMutableLiveData.value = user
        }

        override fun onCancelled(error: DatabaseError) {
            Timber.d(error.message)
        }
    }
}