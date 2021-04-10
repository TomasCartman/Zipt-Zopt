package com.blackpineapple.ziptzopt.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.alimuzaffar.lib.pin.PinEntryEditText
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.firebase.Auth

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CodeVerificationActivity : AppCompatActivity() {
    private lateinit var verifyTextView: TextView
    private lateinit var waitingTextView: TextView
    private lateinit var pinEntryEditText: PinEntryEditText
    private var codeSentByFirebase: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_verification)

        verifyTextView = findViewById(R.id.verify_textView)
        waitingTextView = findViewById(R.id.waiting_textView)
        pinEntryEditText = findViewById(R.id.pin_entry)
        pinEntryListener()

        val phoneNumber = intent.getStringExtra(PHONE_NUMBER)
        if (phoneNumber != null) {
            sendVerificationCodeToUser(phoneNumber)
            changeNumberInTextViews(phoneNumber)
        }
    }

    private fun changeNumberInTextViews(phoneNumber: String) {
        verifyTextView.text = getString(R.string.verify_number, phoneNumber)
        waitingTextView.text = getString(R.string.waiting_for_verifying, phoneNumber)
    }

    private fun sendVerificationCodeToUser(phoneNumber: String) {
        val phoneAuthOptions = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(60, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(VerificationCallback())
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }

    private inner class VerificationCallback : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            val code = p0.smsCode
            if(code != null) {
                pinEntryEditText.setText(code)
                verifyCode(code)
            }
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            p0.message?.let { Timber.e(it) }
            Toast.makeText( this@CodeVerificationActivity, p0.message, Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            codeSentByFirebase = p0
            Timber.d(codeSentByFirebase)
        }

    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(codeSentByFirebase, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val auth = Auth.firebaseAuth

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithCredential:success")

                    Toast.makeText(this, "Verification completed", Toast.LENGTH_SHORT).show()

                    val user = task.result?.user
                    val firebaseAuth = Auth.firebaseAuth
                    firebaseAuth.updateCurrentUser(user)

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // Sign in failed, display a message and update the UI
                    Timber.w("signInWithCredential:failure ${task.exception}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                       // Toast.makeText(this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }

    private fun pinEntryListener() {
        pinEntryEditText.doOnTextChanged { text, _, _, _ ->
            if(text != null) {
                if(text.length >= 6) {
                    Timber.d(text.toString())
                    verifyCode(text.toString())
                }
            }
        }
    }

    companion object {
        const val PHONE_NUMBER = "phoneNumber"
    }
}