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
import com.blackpineapple.ziptzopt.firebase.FirebaseRepository

class CreateAccountActivity : AppCompatActivity() {
    private val auth = Auth.firebaseAuth
    private lateinit var firebaseRepository: FirebaseRepository
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
            firebaseRepository = FirebaseRepository(auth.currentUser.uid)
            firebaseRepository.getUserInfo()
            userLiveData = firebaseRepository.userMutableLiveData
            userLiveData.observe(this, { user ->
                nameEditText.setText(user.name)
            })
        }

        handleClick()
    }

    private fun handleClick() {
        nextButton.setOnClickListener {
            if (text.isNotBlank()) {
                firebaseRepository.setUserName(text)

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, getString(R.string.name_length_too_short_message), Toast.LENGTH_SHORT).show()
            }
        }
    }
}