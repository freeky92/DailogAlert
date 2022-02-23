package com.asurspace.d_alert.dialog_fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import java.util.*

typealias TimePickerResultListener = (Long) -> Unit

class TimePickerDialogFragment : DialogFragment() {

    private val receivedValue get() = requireArguments().getLong(ARG_DATE)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = receivedValue
        val dialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                calendar[Calendar.MINUTE] = minute
                setFragmentResult(calendar.timeInMillis)
            },
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            true
        )
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

    private fun setFragmentResult(response: Long) {
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(KEYS_TIME_RESPONSE to response)
        )
    }

    companion object {
        @JvmStatic
        private val TAG = TimePickerDialogFragment::class.java.simpleName

        @JvmStatic
        private val KEYS_TIME_RESPONSE = "TIME_RESPONSE"

        @JvmStatic
        private val ARG_DATE = "ARG_DATE"

        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(fragmentManager: FragmentManager, date: Long) {
            val timePickerDialogFragment = TimePickerDialogFragment()
            timePickerDialogFragment.arguments = bundleOf(ARG_DATE to date)
            timePickerDialogFragment.show(fragmentManager, TAG)
        }

        fun setUpListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: TimePickerResultListener
        ) {
            fragmentManager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner) { _, result ->
                listener.invoke(result.getLong(KEYS_TIME_RESPONSE))
            }
        }
    }

}