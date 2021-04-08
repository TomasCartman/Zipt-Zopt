package com.blackpineapple.ziptzopt.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.blackpineapple.ziptzopt.R
import com.google.android.material.appbar.MaterialToolbar

class ProfileActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var nameProfileLayout: LinearLayout
    private lateinit var messageProfileLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile2)

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
            //val intent = Intent(this, ::class.java)
            //startActivity(intent)
        }

        messageProfileLayout.setOnClickListener {
            val intent = Intent(this, MessageActivity::class.java)
            startActivity(intent)
        }
    }
}