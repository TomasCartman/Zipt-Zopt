package com.blackpineapple.ziptzopt.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.data.model.User
import com.blackpineapple.ziptzopt.ui.dialogs.TextGetterDialog
import com.blackpineapple.ziptzopt.viewmodel.ProfileActivityViewModel
import com.google.android.material.appbar.MaterialToolbar
import timber.log.Timber

class ProfileActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var nameProfileLayout: LinearLayout
    private lateinit var messageProfileLayout: LinearLayout
    private lateinit var nameTextView: TextView
    private lateinit var messageTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var profileActivityViewModel: ProfileActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileActivityViewModel = ViewModelProvider
                .NewInstanceFactory()
                .create(ProfileActivityViewModel::class.java)

        nameTextView = findViewById(R.id.your_name)
        messageTextView = findViewById(R.id.your_message)
        phoneTextView = findViewById(R.id.your_phone)

        profileActivityViewModel.userLiveData.observe(this, { user ->
            Timber.d(user.toString())
            Timber.d("aa")
            if (user != null) {
                nameTextView.text = user.name
                messageTextView.text = user.message
                phoneTextView.text = user.phoneNumber
            }
        })

        toolbar = findViewById(R.id.configurations_toolbar)
        setupToolbar()

        nameProfileLayout = findViewById(R.id.name_profile_layout)
        messageProfileLayout = findViewById(R.id.message_profile_layout)
        setClickHandle()
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setClickHandle() {
        nameProfileLayout.setOnClickListener {
            val textGetterDialog = TextGetterDialog.newInstance(
                    getString(R.string.insert_your_name_text),
                    25,
                    nameTextView.text.toString()
            )
            textGetterDialog.show(supportFragmentManager, "TextGetterDialog")
            textGetterDialog.setGetterDialogCallback(object : TextGetterDialog.GetterDialogCallback {
                override fun onSaveButtonClick(s: String) {
                    if(s.isNotBlank()) {
                        nameTextView.text = s
                        profileActivityViewModel.setUserName(s)
                    }
                }

                override fun onCancelButtonClick() {

                }
            })
        }

        messageProfileLayout.setOnClickListener {
            val intent = Intent(this, MessageActivity::class.java)
            startActivity(intent)
        }
    }
}