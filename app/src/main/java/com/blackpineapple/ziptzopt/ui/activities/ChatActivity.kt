package com.blackpineapple.ziptzopt.ui.activities

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.data.adapters.ChatAdapter
import com.blackpineapple.ziptzopt.viewmodel.ChatActivityViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class ChatActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var sendMessageEditText: TextInputEditText
    private lateinit var chatActivityViewModel: ChatActivityViewModel
    private lateinit var messagesRecyclerView: RecyclerView
    private val chatAdapter = ChatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatActivityViewModel = ViewModelProvider
            .NewInstanceFactory().create(ChatActivityViewModel::class.java)

        setupViewModel()

        toolbar = findViewById(R.id.configurations_toolbar)
        setupToolbar()

        messagesRecyclerView = findViewById(R.id.messages_recyclerView)
        setupRecyclerView()

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
            chatActivityViewModel.setGroup(intent.getBooleanExtra(ARG_IS_GROUP, false))
        }
    }

    private fun setupSendMessageEditText() {
        sendMessageEditText.doOnTextChanged { text, _, _, _ ->
            if(text != null && text.isNotEmpty()) {
                floatingActionButton.setImageResource(R.drawable.icon_send)
                floatingActionButton.setOnClickListener {
                    sendMessage(text.toString())
                }
            } else {
                floatingActionButton.setImageResource(R.drawable.icon_mic)
            }
        }

        sendMessageEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                messagesRecyclerView.scrollToPosition(0)
            }
        }
    }

    private fun setupRecyclerView() {
        messagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                reverseLayout = true
                stackFromEnd = true
            }
            adapter = chatAdapter
        }

        chatActivityViewModel.messagesLiveData.observe(this, {
            chatAdapter.submitList(it.reversed())
            messagesRecyclerView.smoothScrollToPosition(0)
        })
    }

    private fun sendMessage(message: String) {
        chatActivityViewModel.sendMessage(message)
        sendMessageEditText.setText("")
    }

    companion object {
        const val ARG_NAME = "name"
        const val ARG_NUMBER = "phoneNumber"
        const val ARG_PICTURE = "picture"
        const val ARG_IS_GROUP = "isGroup"
    }
}