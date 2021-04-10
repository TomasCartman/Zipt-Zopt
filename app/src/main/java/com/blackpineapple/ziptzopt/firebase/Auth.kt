package com.blackpineapple.ziptzopt.firebase

import com.google.firebase.auth.FirebaseAuth

class Auth {
    companion object {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    }
}