package com.blackpineapple.ziptzopt.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.viewpager2.widget.ViewPager2
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.data.adapters.ViewPagerAdapter
import com.blackpineapple.ziptzopt.ui.fragments.FragmentCalls
import com.blackpineapple.ziptzopt.ui.fragments.FragmentChat
import com.blackpineapple.ziptzopt.ui.fragments.FragmentStatus
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager2
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.chat_toolbar)
        //setupToolbar()

        mViewPager = findViewById(R.id.pager)
        createViewPager()

        setupTabLayout()
    }

    private fun createViewPager() {
        val fragments = arrayListOf(
                FragmentChat.newInstance(toolbar),
                FragmentStatus.newInstance(toolbar),
                FragmentCalls.newInstance(toolbar)
        )

        mViewPager.adapter = ViewPagerAdapter(supportFragmentManager, this.lifecycle, fragments)
    }

    private fun setupToolbar() {
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

        setupToolbarClicks()
    }

    private fun setupToolbarClicks() {
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