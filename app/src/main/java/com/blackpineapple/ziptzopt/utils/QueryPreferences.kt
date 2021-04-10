package com.blackpineapple.ziptzopt.utils

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

private const val MESSAGE_SELECTOR = "messageSelector"

object QueryPreferences {
    fun getWhichMessageSelector(context: Context): Int {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(MESSAGE_SELECTOR, 0)
    }

    fun setWhichMessageSelector(context: Context, messageSelectorNumber: Int) {
        return PreferenceManager.getDefaultSharedPreferences(context).edit {
            putInt(MESSAGE_SELECTOR, messageSelectorNumber)
        }
    }
}