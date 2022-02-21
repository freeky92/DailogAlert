package com.asurspace.d_alert.dialog_fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.asurspace.d_alert.R
import com.asurspace.d_alert.databinding.CustomEditablieItemBinding

typealias EditableReusableDialogResultListener = (requestKey: String, volume: Int) -> Unit

class EditableReusableDialogFragment : DialogFragment() {

    private val receivedVolumeValue get() = requireArguments().getInt(ARG_VALUE)
    private val requestKey get() = requireArguments().getString(ARG_REQUEST_KEY)!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val editableBinding = CustomEditablieItemBinding.inflate(layoutInflater)
        editableBinding.tietEt.setText(receivedVolumeValue.toString())

        val dialog = AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setIcon(R.drawable.ic_baseline_keyboard_24)
            .setTitle(R.string.set_volume_level)
            .setView(editableBinding.root)
            .setPositiveButton(getString(R.string.set_volume), null)
            .create()
        dialog.setOnShowListener {
            editableBinding.tietEt.requestFocus()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val inText = editableBinding.tietEt.text.toString()
                if (inText.isBlank()) {
                    editableBinding.tietEt.error = "Field is empty"
                    return@setOnClickListener
                }
                val digit = inText.toIntOrNull()
                if (digit == null || digit > 100) {
                    editableBinding.tietEt.error = "Wrong value"
                    return@setOnClickListener
                }
                setFragmentResult(digit)
                dialog.dismiss()
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d(TAG, "onDismiss")
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Log.d(TAG, "onCancel")
    }

    private fun setFragmentResult(response: Int) { parentFragmentManager
        .setFragmentResult(requestKey, bundleOf(KEY_VOLUME_RESPONSE to response)) }

    companion object {
        @JvmStatic
        private val TAG = EditableReusableDialogFragment::class.java.simpleName

        @JvmStatic
        private val KEY_VOLUME_RESPONSE = "VOLUME_RESPONSE"

        @JvmStatic
        private val ARG_VALUE = "ARG_VALUE"

        @JvmStatic
        private val ARG_REQUEST_KEY = "ARG_REQUEST_KEY"

        @JvmStatic
        val DEFAULT_REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(fragmentManager: FragmentManager, requestKey: String, volumeValue: Int) {
            val editableDialogFragment = EditableReusableDialogFragment()
            editableDialogFragment.arguments = bundleOf(
                ARG_REQUEST_KEY to requestKey,
                ARG_VALUE to volumeValue
            )
            editableDialogFragment.show(fragmentManager, TAG)
        }

        fun setUpListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            requestKey: String,
            listener: EditableReusableDialogResultListener
        ) {
            fragmentManager.setFragmentResultListener(requestKey, lifecycleOwner) { key, result ->
                listener.invoke(key, result.getInt(KEY_VOLUME_RESPONSE))
            }
        }
    }
}