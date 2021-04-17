package com.blackpineapple.ziptzopt.ui.activities

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.viewmodel.ChatActivityViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class ChatActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var sendMessageEditText: TextInputEditText
    private lateinit var chatActivityViewModel: ChatActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatActivityViewModel = ViewModelProvider
            .NewInstanceFactory().create(ChatActivityViewModel::class.java)

        setupViewModel()

        toolbar = findViewById(R.id.configurations_toolbar)
        setupToolbar()

        floatingActionButton = findViewById(R.id.floating_action_button)
        sendMessageEditText = findViewById(R.id.send_message_editText)
        setupSendMessageEditText()
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbar.title = intent.getStringExtra(ARG_NAME)
    }

    private fun setupViewModel() {
        val friendNumber = intent.getStringExtra(ARG_NUMBER)
        if (friendNumber != null) {
            chatActivityViewModel.setFriendNumber(friendNumber)
        }
    }

    private fun setupSendMessageEditText() {
        sendMessageEditText.doOnTextChanged { text, start, before, count ->
            if(text != null && text.isNotEmpty()) {
                floatingActionButton.setImageResource(R.drawable.icon_send)
            } else {
                floatingActionButton.setImageResource(R.drawable.icon_mic)
            }
        }
    }

    companion object {
        const val ARG_NAME = "name"
        const val ARG_NUMBER = "phoneNumber"
        const val ARG_PICTURE = "picture"
    }
}