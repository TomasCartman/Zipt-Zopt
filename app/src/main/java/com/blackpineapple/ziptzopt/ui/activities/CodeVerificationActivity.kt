package com.blackpineapple.ziptzopt.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.alimuzaffar.lib.pin.PinEntryEditText
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.firebase.Auth
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabaseImplementation

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
        try {
            val credential = PhoneAuthProvider.getCredential(codeSentByFirebase, code)
            signInWithPhoneAuthCredential(credential)
        } catch (err: IllegalArgumentException) {
            Timber.e(err.stackTraceToString())
            Toast.makeText(this, "An error happened", Toast.LENGTH_LONG).show()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val auth = Auth.firebaseAuth

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Verification completed", Toast.LENGTH_SHORT).show()

                    val user = task.result?.user
                    auth.updateCurrentUser(user)
                    if(user != null) {
                        val firebaseRepository = FirebaseRealtimeDatabaseImplementation(user.uid)
                        firebaseRepository.setUserPhoneNumber(user.phoneNumber)
                        firebaseRepository.setUserUid(user.uid)
                        firebaseRepository.setPhoneNumberToUid(user.phoneNumber, user.uid)
                        firebaseRepository.setUserMessage("")
                    }

                    startActivity(Intent(this, CreateAccountActivity::class.java))
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