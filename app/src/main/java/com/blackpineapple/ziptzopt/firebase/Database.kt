package com.blackpineapple.ziptzopt.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Database {
    companion object {
        private const val REF_USERS = "users"
        private val firebaseDatabase: FirebaseDatabase = Firebase.database
        private val usersReference: DatabaseReference = firebaseDatabase.reference.child(REF_USERS)

        fun userReference(uid: String): DatabaseReference {
            return usersReference.child(uid)
        }
    }
}