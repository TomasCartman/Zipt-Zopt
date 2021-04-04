package com.blackpineapple.ziptzopt.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.viewmodel.FragmentConversationsViewModel
import com.google.android.material.appbar.MaterialToolbar
import timber.log.Timber

class FragmentConversations : Fragment() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var fragmentConversationsViewModel: FragmentConversationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentConversationsViewModel = ViewModelProvider.NewInstanceFactory()
            .create(FragmentConversationsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_conversations, container, false)

        toolbar = view.findViewById(R.id.conversations_toolbar)
        setupToolBar()

        return view
    }

    private fun setupToolBar() {
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
    }
}