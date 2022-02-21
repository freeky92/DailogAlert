package com.asurspace.whatisalertdialog_pl.dialog_fragments

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.asurspace.whatisalertdialog_pl.R

typealias MultiChoiceResultListener = (Int) -> Unit

class MultiplyChoiceDialogFragment : DialogFragment() {

    private val receivedColor get() = requireArguments().getInt(ARG_COLOR)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val colorItem = resources.getStringArray(R.array.colors)
        val colList = mutableListOf(
            Color.red(receivedColor),
            Color.green(receivedColor),
            Color.blue(receivedColor)
        )
        val checkBox = colList
            .map { it > 0 && savedInstanceState == null }
            .toBooleanArray()
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.set_color)
            .setMultiChoiceItems(colorItem, checkBox) { dialog, which, isChecked ->
                val checkedPositions = (dialog as AlertDialog).listView.checkedItemPositions
                setFragmentResult(Color.rgb(
                    booleanToColor(checkedPositions[0]),
                    booleanToColor(checkedPositions[1]),
                    booleanToColor(checkedPositions[2]),
                ))
            }
            .setPositiveButton("Close", null)
            .create()
    }

    private fun setFragmentResult(response: Int) {
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(KEY_COLOR_RESPONSE to response)
        )
    }

    private fun booleanToColor(state: Boolean): Int {
        return if (state) 255 else 0
    }

    companion object {
        @JvmStatic
        private val TAG = MultiplyChoiceDialogFragment::class.java.simpleName

        @JvmStatic
        private val ARG_COLOR = "ARG_COLOR"

        @JvmStatic
        private val KEY_COLOR_RESPONSE = "COLOR_RESPONSE"

        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(fragmentManager: FragmentManager, color: Int) {
            val multiplyChoiceDialogFragment = MultiplyChoiceDialogFragment()
            multiplyChoiceDialogFragment.arguments = bundleOf(ARG_COLOR to color)
            multiplyChoiceDialogFragment.show(fragmentManager, TAG)
        }

        fun setUpListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: MultiChoiceResultListener
        ) {
            fragmentManager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner) { _, result ->
                listener.invoke(result.getInt(KEY_COLOR_RESPONSE))
            }
        }
    }
}