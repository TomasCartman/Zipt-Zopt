package com.blackpineapple.ziptzopt.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.blackpineapple.ziptzopt.R
import com.google.android.material.appbar.MaterialToolbar

class ConfigurationsActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var profileLayout: LinearLayout
    private lateinit var accountLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configurations2)

        toolbar = findViewById(R.id.configurations_toolbar)
        setupToolbar()

        profileLayout = findViewById(R.id.profile_layout)
        accountLayout = findViewById(R.id.account_layout)
        setClickHandle()
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setClickHandle() {
        profileLayout.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        accountLayout.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }
    }
}