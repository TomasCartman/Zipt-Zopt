package com.blackpineapple.ziptzopt.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blackpineapple.ziptzopt.R

class FragmentStatus : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_status, container, false)
    }

    companion object {
        fun newInstance(): FragmentStatus = FragmentStatus()
    }
}