package com.asurspace.whatisalertdialog_pl.dialog_fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.asurspace.whatisalertdialog_pl.R
import com.asurspace.whatisalertdialog_pl.app_contract.model.AvailableVoleValues

typealias SingleChoiceResultListener = (Int) -> Unit

class SingleChoiceDialogFragment : DialogFragment() {

    private val volume get() = requireArguments().getInt(ARG_VOLUME)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val volumeItems = AvailableVoleValues.createVolumeValues(volume)
        val volumeTextItems = volumeItems.values
            .map { getString(R.string.volume_eq, it) }
            .toTypedArray()

        return AlertDialog.Builder(requireContext())
            .setTitle("Volume setup")
            .setCancelable(true)
            .setSingleChoiceItems(volumeTextItems, volumeItems.currentIndex, null)
            .setPositiveButton("Confirm") { dialog, _ ->
                val index = (dialog as AlertDialog).listView.checkedItemPosition
                val volumeValue = volumeItems.values[index]
                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(KEY_VOLUME_RESPONSE to volumeValue)
                )
            }
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d(TAG, "onDismiss")
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Log.d(TAG, "onCancel")
    }

    companion object {
        @JvmStatic
        private val TAG = SingleChoiceDialogFragment::class.java.name

        @JvmStatic
        private val KEY_VOLUME_RESPONSE = "VOLUME_RESPONSE"

        @JvmStatic
        private val ARG_VOLUME = "ARG_VOLUME"

        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(fragmentManager: FragmentManager, volumeValue: Int) {
            val dialogFragment = SingleChoiceDialogFragment()
            dialogFragment.arguments = bundleOf(ARG_VOLUME to volumeValue)
            dialogFragment.show(fragmentManager, TAG)
        }

        fun setUpListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: SingleChoiceResultListener
        ) {
            fragmentManager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner) { _, result ->
                listener.invoke(result.getInt(KEY_VOLUME_RESPONSE))
            }
        }
    }
}