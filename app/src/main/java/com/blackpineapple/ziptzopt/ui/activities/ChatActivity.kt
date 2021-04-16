package com.blackpineapple.ziptzopt.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blackpineapple.ziptzopt.R
import com.google.android.material.appbar.MaterialToolbar

class ChatActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toolbar = findViewById(R.id.configurations_toolbar)
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbar.title = intent.getStringExtra(ARG_NAME)
    }

    companion object {
        const val ARG_NAME = "name"
        const val ARG_NUMBER = "phoneNumber"
        const val ARG_PICTURE = "picture"
    }
}