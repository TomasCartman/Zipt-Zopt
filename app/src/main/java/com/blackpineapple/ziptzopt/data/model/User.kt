package com.blackpineapple.ziptzopt.data.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Exclude

data class User(
       val uid: String,
       var name: String = "~",
       var message: String = "",
       var phoneNumber: String
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "uid" to uid,
                "name" to name,
                "message" to message,
                "phoneNumber" to phoneNumber
        )
    }

    companion object {
        @Exclude
        fun createUser(snapshot: DataSnapshot): User {
            val uid = snapshot.child("uid").value as String
            val name = snapshot.child("name").value as String
            val message = snapshot.child("message").value as String
            val phoneNumber = snapshot.child("phoneNumber").value as String

            return User(uid, name, message, phoneNumber)
        }
    }

}