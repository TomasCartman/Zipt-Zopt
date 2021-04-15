package com.blackpineapple.ziptzopt.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.data.adapters.ViewPagerAdapter
import com.blackpineapple.ziptzopt.firebase.Auth
import com.blackpineapple.ziptzopt.ui.fragments.FragmentCalls
import com.blackpineapple.ziptzopt.ui.fragments.FragmentChat
import com.blackpineapple.ziptzopt.ui.fragments.FragmentStatus
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions


class MainActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager2
    private lateinit var toolbar: MaterialToolbar
    private lateinit var floatingActionButton: FloatingActionButton
    private val firebaseAuth: FirebaseAuth = Auth.firebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.chat_toolbar)
        floatingActionButton = findViewById(R.id.floating_action_button)

        mViewPager = findViewById(R.id.pager)
        createViewPager()

        setupTabLayout()

        authStateListener = FirebaseAuth.AuthStateListener {
            val user = firebaseAuth.currentUser
            if(user != null) {
                //User is signed in
                Toast.makeText(this, "You are signed in", Toast.LENGTH_SHORT).show()
                //firebaseAuth.signOut()
            } else {
                Toast.makeText(this, "You are not signed in", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, PhoneEnterActivity::class.java))
                finish()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    private fun createViewPager() {
        val fragments = arrayListOf(
                FragmentChat.newInstance(toolbar, floatingActionButton),
                FragmentStatus.newInstance(toolbar, floatingActionButton),
                FragmentCalls.newInstance(toolbar, floatingActionButton)
        )

        mViewPager.adapter = ViewPagerAdapter(supportFragmentManager, this.lifecycle, fragments)
    }

    private fun setupTabLayout() {
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        TabLayoutMediator(tabLayout, mViewPager) { tab, position ->
            when(position) {
                0 -> {
                    tab.text = getString(R.string.chat_tab_name)
                }
                1 -> {
                    tab.text = getString(R.string.status_tab_name)
                }
                2 -> {
                    tab.text = getString(R.string.calls_tab_name)
                }
            }
        }.attach()
    }
}