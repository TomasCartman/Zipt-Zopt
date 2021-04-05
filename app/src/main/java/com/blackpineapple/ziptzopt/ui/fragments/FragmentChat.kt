package com.blackpineapple.ziptzopt.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.blackpineapple.ziptzopt.R
import com.blackpineapple.ziptzopt.viewmodel.FragmentConversationsViewModel

class FragmentChat : Fragment() {
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
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    companion object {
        fun newInstance(): FragmentChat = FragmentChat()
    }
}