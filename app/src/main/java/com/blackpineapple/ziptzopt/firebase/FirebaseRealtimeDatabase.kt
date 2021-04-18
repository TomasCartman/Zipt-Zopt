package com.blackpineapple.ziptzopt.firebase

import com.blackpineapple.ziptzopt.data.model.Message
import com.blackpineapple.ziptzopt.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface FirebaseRealtimeDatabase {

    @Deprecated("Do not user this function anymore. Use getUserInfo2() instead")
    fun getUserInfo()

    fun getUserInfo2(): Flow<Result<User>>

    fun setUserName(name: String)

    fun setUserMessage(message: String)

    fun setUserUid(uid: String)

    fun setUserPhoneNumber(phoneNumber: String)

    suspend fun getUidFromPhoneNumberSync(phoneNumber: String): String?

    fun getAllUserPrivateChatFriends(): Flow<Result<HashMap<String, String>>>

    fun sendMessage(message: String, pushKey: String)

    suspend fun getUserContactNumberToPushKey(phoneNumber: String): String?

    fun getCompletePrivateChat(pushKey: String):Flow<Result<List<Message>>>

    fun addNewFriendPrivateChatToUser(phoneNumber: String): String

    fun addNewFriendPrivateChatToUser(phoneNumber: String, pushKey: String, friendUid: String)

    fun getAllUserFriendPhoneNumbersToUidSync(): HashMap<String, String>
}