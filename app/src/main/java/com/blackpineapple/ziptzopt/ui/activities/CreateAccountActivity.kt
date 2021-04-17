package com.blackpineapple.ziptzopt.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LiveData
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.data.model.User
import com.blackpineapple.ziptzopt.firebase.Auth
import com.blackpineapple.ziptzopt.firebase.FirebaseRealtimeDatabaseImplementation

class CreateAccountActivity : AppCompatActivity() {
    private val auth = Auth.firebaseAuth
    private lateinit var firebaseRealtimeDatabaseImplementation: FirebaseRealtimeDatabaseImplementation
    private lateinit var userLiveData: LiveData<User>
    private lateinit var nameEditText: EditText
    private lateinit var nextButton: Button
    private var text: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        nameEditText = findViewById(R.id.name_editText)
        nextButton = findViewById(R.id.next_button)

        nameEditText.doOnTextChanged { text, _, _, _ ->
            this.text = text.toString()
        }

        if(auth.currentUser != null) {
            firebaseRealtimeDatabaseImplementation = FirebaseRealtimeDatabaseImplementation(auth.currentUser.uid)
            firebaseRealtimeDatabaseImplementation.getUserInfo()
            userLiveData = firebaseRealtimeDatabaseImplementation.userMutableLiveData
            userLiveData.observe(this, { user ->
                nameEditText.setText(user.name)
            })
        }

        handleClick()
    }

    private fun handleClick() {
        nextButton.setOnClickListener {
            if (text.isNotBlank()) {
                firebaseRealtimeDatabaseImplementation.setUserName(text)

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, getString(R.string.name_length_too_short_message), Toast.LENGTH_SHORT).show()
            }
        }
    }
}