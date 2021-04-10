package com.blackpineapple.ziptzopt.data.model

import com.google.firebase.database.Exclude

data class User(
       var uid: String,
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
}