package com.blackpineapple.ziptzopt.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
import timber.log.Timber


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

        setupFloatingActionButton()

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

    private fun createViewPager() {
        val fragments = arrayListOf(
                FragmentChat(),
                FragmentStatus(),
                FragmentCalls()
        )

        mViewPager.adapter = ViewPagerAdapter(supportFragmentManager, this.lifecycle, fragments)
    }

    /* The lines below are just temporary, until i discover a better way to do it
    *  The same applies to the classes FragmentChats, FragmentStatus and FragmentCalls */

     fun setupToolbarChat() {
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.fragment_chat_menu)

        val searchItem: MenuItem = toolbar.menu.findItem(R.id.search_menu)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query != null) {
                        Timber.i(query)
                        // Does serch
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText.isNullOrEmpty()) {
                        // Gets all the list if newText is empty
                    }
                    return true
                }
            })
        }

        setupToolbarClicksChat()
    }

    private fun setupToolbarClicksChat() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.configurations_menu -> {
                    val intent = Intent(this, ConfigurationsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

     fun setupToolbarStatus() {
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.fragment_status_menu)

        val searchItem: MenuItem = toolbar.menu.findItem(R.id.search_menu)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query != null) {
                        Timber.i(query)
                        // Does serch
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText.isNullOrEmpty()) {
                        // Gets all the list if newText is empty
                    }
                    return true
                }
            })
        }

        setupToolbarClicksStatus()
    }

    private fun setupToolbarClicksStatus() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.configurations_menu -> {
                    val intent = Intent(this, ConfigurationsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

     fun setupToolbarCalls() {
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.fragment_calls_menu)

        val searchItem: MenuItem = toolbar.menu.findItem(R.id.search_menu)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query != null) {
                        Timber.i(query)
                        // Does serch
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText.isNullOrEmpty()) {
                        // Gets all the list if newText is empty
                    }
                    return true
                }
            })
        }

        setupToolbarClicksCalls()
    }

     private fun setupToolbarClicksCalls() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.configurations_menu -> {
                    val intent = Intent(this, ConfigurationsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupFloatingActionButton() {
        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, ContactsActivity::class.java))
        }
    }

    interface toolbarChanger {
        fun setupToolbar()
    }
}