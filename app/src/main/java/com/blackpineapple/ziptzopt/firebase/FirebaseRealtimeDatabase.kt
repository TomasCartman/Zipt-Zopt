package com.blackpineapple.ziptzopt.firebase

import com.blackpineapple.ziptzopt.data.model.User
import kotlinx.coroutines.flow.Flow

interface FirebaseRealtimeDatabase {

    @Deprecated("Do not user this function anymore. Use getUserInfo2() instead")
    fun getUserInfo()

    fun getUserInfo2(): Flow<Result<User>>

    fun setUserName(name: String)

    fun setUserMessage(message: String)

    fun setUserUid(uid: String)

    fun setUserPhoneNumber(phoneNumber: String)

    fun getPhoneNumberToUidSync(): HashMap<String, String>

    fun getAllUserPrivateChatFriends(): Flow<Result<HashMap<String, String>>>
}