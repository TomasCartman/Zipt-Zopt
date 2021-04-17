package com.blackpineapple.ziptzopt.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.ui.activities.ConfigurationsActivity
import com.blackpineapple.ziptzopt.ui.activities.MainActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class FragmentCalls : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calls, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setupToolbarCalls()
    }

    /*
    private fun setupToolbar() {
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


    companion object {
        fun newInstance(
                toolbar: MaterialToolbar,
                floatingActionButton: FloatingActionButton
        ): FragmentCalls = FragmentCalls(toolbar, floatingActionButton)
    }

     */
}