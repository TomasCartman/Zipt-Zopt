package com.blackpineapple.ziptzopt.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.data.adapters.ChatFragmentAdapter
import com.blackpineapple.ziptzopt.ui.activities.MainActivity
import com.blackpineapple.ziptzopt.viewmodel.FragmentChatViewModel

class FragmentChat : Fragment() {
    private lateinit var fragmentChatViewModel: FragmentChatViewModel
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatFragmentAdapter: ChatFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentChatViewModel = ViewModelProvider.NewInstanceFactory()
            .create(FragmentChatViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        chatRecyclerView = view.findViewById(R.id.chat_recyclerView)
        chatFragmentAdapter = ChatFragmentAdapter()
        chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatFragmentAdapter
        }

        fragmentChatViewModel.messagesLiveData.observe(viewLifecycleOwner, {
            chatFragmentAdapter.submitList(it)
        })

        return view
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).setupToolbarChat()
        //setupFloatingActionButton()
    }
    /*

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

        fun newInstance2(
                toolbar: MaterialToolbar,
                floatingActionButton: FloatingActionButton
        ): FragmentChat {
            val a = FragmentChat()
            val args = Bundle()
            args.putSerializable("a", toolbar)
        }

        fun newInstance(
                toolbar: MaterialToolbar,
                floatingActionButton: FloatingActionButton
        ): FragmentChat = FragmentChat(toolbar, floatingActionButton)
    }

 */
}