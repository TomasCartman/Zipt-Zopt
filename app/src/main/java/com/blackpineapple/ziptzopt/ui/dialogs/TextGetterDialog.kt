package com.blackpineapple.ziptzopt.ui.dialogs

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.blackpineapple.ziptzopt.R

const val ARG_TITLE = "title"
const val ARG_MAX_EDIT_TEXT_SIZE = "maxEditTextSize"

class TextGetterDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            var title = ""
            var maxEditTextSize = 0

            setStyle(STYLE_NO_FRAME, R.style.AppTheme_Dialog_Custom)
            val layoutInflater = onGetLayoutInflater(savedInstanceState)
            val view = layoutInflater.inflate(R.layout.dialog_text_getter, null)
            val titleTextView = view.findViewById<TextView>(R.id.title_textView)
            val editTextView = view.findViewById<EditText>(R.id.editText)
            val cancelButton = view.findViewById<Button>(R.id.cancel_button)
            val saveButton = view.findViewById<Button>(R.id.save_button)

            if (arguments != null) {
                title = requireArguments().get(ARG_TITLE)  as String
                maxEditTextSize = requireArguments().get(ARG_MAX_EDIT_TEXT_SIZE) as Int
            }

            titleTextView.text = title
            builder.setView(view)
            val dialog = builder.create()

            val window = dialog.window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setGravity(Gravity.BOTTOM)
            val params = window?.attributes
            params?.gravity = Gravity.BOTTOM
            params?.verticalMargin = 0F
            params?.width = WindowManager.LayoutParams.MATCH_PARENT
            window?.attributes = params

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
    
    companion object {
        fun newInstance(title: String, maxEditTextSize: Int): TextGetterDialog {
            val args = Bundle().apply {
                putSerializable(ARG_TITLE, title)
                putSerializable(ARG_MAX_EDIT_TEXT_SIZE, maxEditTextSize)
            }
            return TextGetterDialog().apply { arguments = args }
        }
    }
}