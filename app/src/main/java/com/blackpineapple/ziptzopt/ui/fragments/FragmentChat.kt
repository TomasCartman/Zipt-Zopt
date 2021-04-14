package com.blackpineapple.ziptzopt.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.ui.activities.ConfigurationsActivity
import com.blackpineapple.ziptzopt.ui.activities.ContactsActivity
import com.blackpineapple.ziptzopt.viewmodel.FragmentConversationsViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class FragmentChat(val toolbar: MaterialToolbar, val floatingActionButton: FloatingActionButton) : Fragment() {
    private lateinit var fragmentConversationsViewModel: FragmentConversationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentConversationsViewModel = ViewModelProvider.NewInstanceFactory()
            .create(FragmentConversationsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()

        setupToolbar()
        setupFloatingActionButton()
    }

    private fun setupToolbar() {
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

        setupToolbarClicks()
    }

    private fun setupToolbarClicks() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.configurations_menu -> {
                    val intent = Intent(context, ConfigurationsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupFloatingActionButton() {
        floatingActionButton.setOnClickListener {
            startActivity(Intent(context, ContactsActivity::class.java))
        }
    }

    companion object {
        fun newInstance(
                toolbar: MaterialToolbar,
                floatingActionButton: FloatingActionButton
        ): FragmentChat = FragmentChat(toolbar, floatingActionButton)
    }
}