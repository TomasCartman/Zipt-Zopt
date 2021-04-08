package com.blackpineapple.ziptzopt.ui.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blackpineapple.ziptzopt.R
import com.google.android.material.appbar.MaterialToolbar

class MessageActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        toolbar = findViewById(R.id.configurations_toolbar)
        setupToolbar()

        setClickHandle()
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

    private fun setDefaultMessage(i: Int = 0) {
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
        val transparentDrawable = ColorDrawable(Color.TRANSPARENT)

        icons.forEach { it.setImageDrawable(transparentDrawable) }

        when(i) {
            0 -> {
                // Change this later
                actualMessageTextView.text = getString(R.string.your_message_text)
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
            1 -> {
                actualMessageTextView.text = getString(R.string.available_text)
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
            2 -> {
                actualMessageTextView.text = getString(R.string.occupied_text)
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
            3 -> {
                actualMessageTextView.text = getString(R.string.in_school_text)
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
            4 -> {
                actualMessageTextView.text = getString(R.string.at_the_movies_text)
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
            5 -> {
                actualMessageTextView.text = getString(R.string.at_work_text)
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
            6 -> {
                actualMessageTextView.text = getString(R.string.battery_about_to_run_out_text)
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
            7 -> {
                actualMessageTextView.text = getString(R.string.i_cant_speak_only_ziptzopt_text)
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
            8 -> {
                actualMessageTextView.text = getString(R.string.in_meeting_text)
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
            9 -> {
                actualMessageTextView.text = getString(R.string.at_the_gym_text)
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
            10 -> {
                actualMessageTextView.text = getString(R.string.sleeping_text)
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
            11 -> {
                actualMessageTextView.text = getString(R.string.only_urgent_calls_text)
                icons[i].setImageDrawable(getDrawable(R.drawable.icon_check))
            }
        }
    }
}