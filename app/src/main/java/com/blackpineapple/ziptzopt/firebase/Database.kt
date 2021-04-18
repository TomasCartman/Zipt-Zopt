package com.blackpineapple.ziptzopt.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Database {
    companion object {
        private const val REF_USERS = "users"
        private const val REF_PHONE_NUMBER_TO_UID = "phoneNumberToUID"
        private const val REF_USERS_TO_CHAT_CONTACTS = "usersToChatContacts"
        private const val REF_PRIVATE_CHATS = "privateChats"
        private const val REF_PRIVATE_CHATS_METADATA = "privateChatsMetadata"
        private const val REF_GROUP_METADATA = "groupMetadata"
        private const val REF_GROUP_MESSAGES = "groupsMessages"
        private const val REF_GROUP_HAS_SEEN = "groupsHasSeen"

        private val firebaseDatabase: FirebaseDatabase = Firebase.database

        init {
            firebaseDatabase.setPersistenceEnabled(true)
        }

        private val usersReference: DatabaseReference = firebaseDatabase.reference.child(REF_USERS)
        private val phoneNumberToUidReference: DatabaseReference = firebaseDatabase.reference.child(REF_PHONE_NUMBER_TO_UID)
        private val usersToChatContacts: DatabaseReference = firebaseDatabase.reference.child(REF_USERS_TO_CHAT_CONTACTS)
        private val privateChats: DatabaseReference = firebaseDatabase.reference.child(REF_PRIVATE_CHATS)
        private val privateChatsMetadata: DatabaseReference = firebaseDatabase.reference.child(REF_PRIVATE_CHATS_METADATA)
        private val groupMetadata: DatabaseReference = firebaseDatabase.reference.child(REF_GROUP_METADATA)
        private val groupMessages: DatabaseReference = firebaseDatabase.reference.child(REF_GROUP_MESSAGES)
        private val groupHasSeen: DatabaseReference = firebaseDatabase.reference.child(REF_GROUP_HAS_SEEN)


        fun userReference(uid: String): DatabaseReference = usersReference.child(uid)

        fun phoneNumberToUidReference(): DatabaseReference = phoneNumberToUidReference

        fun usersToChatContacts(uid: String): DatabaseReference = usersToChatContacts.child(uid)

        fun usersToChatContacts(): DatabaseReference = usersToChatContacts

        fun privateChats(pushKey: String): DatabaseReference = privateChats.child(pushKey)

        fun getServerTimestamp(): Map<String, Any> = ServerValue.TIMESTAMP
    }
}