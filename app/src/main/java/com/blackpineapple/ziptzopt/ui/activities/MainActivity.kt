package com.blackpineapple.ziptzopt.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.ui.fragments.FragmentConversations

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, FragmentConversations())
                .commit()
        }
    }
}