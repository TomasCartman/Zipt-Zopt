package com.blackpineapple.ziptzopt.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Database {
    companion object {
        private const val REF_USERS = "users"
        private const val REF_PHONE_NUMBER_TO_UID = "phoneNumberToUID"
        private val firebaseDatabase: FirebaseDatabase = Firebase.database
        private val usersReference: DatabaseReference = firebaseDatabase.reference.child(REF_USERS)
        private val phoneNumberToUidReference: DatabaseReference = firebaseDatabase.reference.child(REF_PHONE_NUMBER_TO_UID)

        fun userReference(uid: String): DatabaseReference {
            return usersReference.child(uid)
        }

        fun phoneNumberToUidReference(): DatabaseReference {
            return phoneNumberToUidReference
        }
    }
}