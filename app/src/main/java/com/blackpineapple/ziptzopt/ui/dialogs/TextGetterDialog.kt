package com.blackpineapple.ziptzopt.ui.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.view.Gravity
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
const val ARG_DEFAULT_TEXT_EDIT_TEXT = "defaultTextEditText"

class TextGetterDialog : DialogFragment() {
    private lateinit var dialogCallback: GetterDialogCallback

    interface GetterDialogCallback {
        fun onSaveButtonClick(s: String)
        fun onCancelButtonClick()
    }

    fun setGetterDialogCallback(dialogCallback: GetterDialogCallback) {
        this@TextGetterDialog.dialogCallback = dialogCallback
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            var title = ""
            var maxEditTextSize = 0
            var defaultTextEditText = ""

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
                defaultTextEditText = requireArguments().get(ARG_DEFAULT_TEXT_EDIT_TEXT) as String
            }

            editTextView.setText(defaultTextEditText)
            editTextView.filters = arrayOf(InputFilter.LengthFilter(maxEditTextSize))

            saveButton.setOnClickListener {
                dialogCallback.onSaveButtonClick(editTextView.text.toString())
                dismiss()
            }

            cancelButton.setOnClickListener {
                dialogCallback.onCancelButtonClick()
                dismiss()
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
        fun newInstance(title: String, maxEditTextSize: Int, defaultTextEditText: String): TextGetterDialog {
            val args = Bundle().apply {
                putSerializable(ARG_TITLE, title)
                putSerializable(ARG_MAX_EDIT_TEXT_SIZE, maxEditTextSize)
                putSerializable(ARG_DEFAULT_TEXT_EDIT_TEXT, defaultTextEditText)
            }
            return TextGetterDialog().apply { arguments = args }
        }
    }
}