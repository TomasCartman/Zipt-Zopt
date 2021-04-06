package com.blackpineapple.ziptzopt.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.blackpineapple.ziptzopt.R
import com.google.android.material.appbar.MaterialToolbar

class ConfigurationsActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var profile_layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configurations2)

        toolbar = findViewById(R.id.configurations_toolbar)
        setupToolbar()

        profile_layout = findViewById(R.id.profile_layout)
        setClickHandle()
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setClickHandle() {
        profile_layout.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}