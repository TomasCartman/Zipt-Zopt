package com.blackpineapple.ziptzopt.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.blackpineapple.ziptzopt.R
import timber.log.Timber

class PhoneEnterActivity : AppCompatActivity() {
    private lateinit var countryCodeEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_enter)

        countryCodeEditText = findViewById(R.id.country_code_editText)
        phoneNumberEditText = findViewById(R.id.phone_number_editText)
        nextButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener { callCodeVerificationActivity() }
    }

    private fun callCodeVerificationActivity() {
        if(!validatePhoneNumber()) return

        val userPhoneNumber = phoneNumberEditText.text.toString().trim()
        val userCountryCode = countryCodeEditText.text.toString().trim()
        val completePhoneNumber = "+".plus(userCountryCode).plus(userPhoneNumber)

        val intent = Intent(this, CodeVerificationActivity::class.java)
        intent.putExtra(CodeVerificationActivity.PHONE_NUMBER, completePhoneNumber)
        startActivity(intent)
        finish()
    }

    private fun validatePhoneNumber(): Boolean {
        return true
    }

}