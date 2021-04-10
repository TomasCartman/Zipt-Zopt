package com.blackpineapple.ziptzopt.ui.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.data.model.User
import com.blackpineapple.ziptzopt.viewmodel.MessageActivityViewModel
import com.google.android.material.appbar.MaterialToolbar

class MessageActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var actualMessageTextView: TextView
    private lateinit var messageActivityViewModel: MessageActivityViewModel
    private lateinit var userLiveData: LiveData<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        messageActivityViewModel = ViewModelProvider
                .NewInstanceFactory()
                .create(MessageActivityViewModel::class.java)

        userLiveData = messageActivityViewModel.userLiveData
        userLiveData.observe(this, {
            actualMessageTextView.text = it.message
        })

        actualMessageTextView = findViewById(R.id.actual_message_textView)
        setUserMessageOnFirebase()

        toolbar = findViewById(R.id.configurations_toolbar)
        setupToolbar()

        setClickHandle()
        setDefaultMessage(messageActivityViewModel.getMessageSelectorNumber(this))
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setClickHandle() {
        val lastMessageLayout = findViewById<LinearLayout>(R.id.last_message_layout)
        lastMessageLayout.setOnClickListener { setDefaultMessage(0) }
        val firstMessageLayout = findViewById<LinearLayout>(R.id.first_message_layout)
        firstMessageLayout.setOnClickListener { setDefaultMessage(1) }
        val secondMessageLayout = findViewById<LinearLayout>(R.id.second_message_layout)
        secondMessageLayout.setOnClickListener { setDefaultMessage(2) }
        val thirdMessageLayout = findViewById<LinearLayout>(R.id.third_message_layout)
        thirdMessageLayout.setOnClickListener { setDefaultMessage(3) }
        val fourthMessageLayout = findViewById<LinearLayout>(R.id.fourth_message_layout)
        fourthMessageLayout.setOnClickListener { setDefaultMessage(4) }
        val fifthMessageLayout = findViewById<LinearLayout>(R.id.fifth_message_layout)
        fifthMessageLayout.setOnClickListener { setDefaultMessage(5) }
        val sixthMessageLayout = findViewById<LinearLayout>(R.id.sixth_message_layout)
        sixthMessageLayout.setOnClickListener { setDefaultMessage(6) }
        val seventhMessageLayout = findViewById<LinearLayout>(R.id.seventh_message_layout)
        seventhMessageLayout.setOnClickListener { setDefaultMessage(7) }
        val eighthMessageLayout = findViewById<LinearLayout>(R.id.eighth_message_layout)
        eighthMessageLayout.setOnClickListener { setDefaultMessage(8) }
        val ninthMessageLayout = findViewById<LinearLayout>(R.id.ninth_message_layout)
        ninthMessageLayout.setOnClickListener { setDefaultMessage(9) }
        val tenthMessageLayout = findViewById<LinearLayout>(R.id.tenth_message_layout)
        tenthMessageLayout.setOnClickListener { setDefaultMessage(10) }
        val eleventhMessageLayout = findViewById<LinearLayout>(R.id.eleventh_message_layout)
        eleventhMessageLayout.setOnClickListener { setDefaultMessage(11) }
    }

    private fun setUserMessageOnFirebase() {
        actualMessageTextView.doOnTextChanged { text, _, _, _ ->
            if(text != null) {
                messageActivityViewModel.setUserMessage(text.toString())
            }
        }
    }

    private fun setDefaultMessage(i: Int) {
        val actualMessageTextView = findViewById<TextView>(R.id.actual_message_textView)
        val icons = arrayListOf<ImageView>(
            findViewById(R.id.last_message_icon),
            findViewById(R.id.first_message_icon),
            findViewById(R.id.second_message_icon),
            findViewById(R.id.third_message_icon),
            findViewById(R.id.fourth_message_icon),
            findViewById(R.id.fifth_message_icon),
            findViewById(R.id.sixth_message_icon),
            findViewById(R.id.seventh_message_icon),
            findViewById(R.id.eighth_message_icon),
            findViewById(R.id.ninth_message_icon),
            findViewById(R.id.tenth_message_icon),
            findViewById(R.id.eleventh_message_icon)
        )
        val messages = arrayListOf(
                getString(R.string.available_text),
                getString(R.string.occupied_text),
                getString(R.string.in_school_text),
                getString(R.string.at_the_movies_text),
                getString(R.string.at_work_text),
                getString(R.string.battery_about_to_run_out_text),
                getString(R.string.i_cant_speak_only_ziptzopt_text),
                getString(R.string.in_meeting_text),
                getString(R.string.at_the_gym_text),
                getString(R.string.sleeping_text),
                getString(R.string.only_urgent_calls_text)
        )

        val transparentDrawable = ColorDrawable(Color.TRANSPARENT)
        icons.forEach { it.setImageDrawable(transparentDrawable) }

        when(i) {
            0 -> {
                // Change this later
                actualMessageTextView.text = getString(R.string.your_message_text)
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
            else -> {
                actualMessageTextView.text = messages[i-1]
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
        }
        messageActivityViewModel.setMessageSelectorNumber(this, i)
    }
}