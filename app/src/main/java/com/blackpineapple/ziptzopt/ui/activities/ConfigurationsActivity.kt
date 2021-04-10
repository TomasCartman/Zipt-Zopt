package com.blackpineapple.ziptzopt.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.data.model.User
import com.blackpineapple.ziptzopt.viewmodel.ConfigurationsActivityViewModel
import com.google.android.material.appbar.MaterialToolbar
import org.w3c.dom.Text

class ConfigurationsActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var profileLayout: LinearLayout
    private lateinit var accountLayout: LinearLayout
    private lateinit var nameTextView: TextView
    private lateinit var messageTextView: TextView
    private lateinit var configurationsActivityViewModel: ConfigurationsActivityViewModel
    private lateinit var userLiveData: LiveData<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configurations)

        configurationsActivityViewModel = ViewModelProvider
                .NewInstanceFactory()
                .create(ConfigurationsActivityViewModel::class.java)
        userLiveData = configurationsActivityViewModel.userLiveData

        nameTextView = findViewById(R.id.your_name_textView)
        messageTextView = findViewById(R.id.your_message_textView)

        userLiveData.observe(this, { user ->
            if(user != null) {
                nameTextView.text = user.name
                messageTextView.text = user.message
            }
        })

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