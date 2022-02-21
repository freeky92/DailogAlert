package com.asurspace.whatisalertdialog_pl.dialog_fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.asurspace.whatisalertdialog_pl.R
import com.asurspace.whatisalertdialog_pl.databinding.VolumeItemBinding

typealias SeekbarResultListener = (Int) -> Unit

class SeekbarDialogFragment : DialogFragment() {

    private val volume get() = requireArguments().getInt(ARG_VOLUME)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val volBinding = VolumeItemBinding.inflate(layoutInflater)
        val seekbar = volBinding.setVolSeekbar

        seekbar.progress = volume
        var resultVolume = volume
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                volBinding.textSetVolume.text = getString(R.string.volume_eq, value)
                resultVolume = value
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.set_volume_level))
            .setView(volBinding.root)
            .setCancelable(true)
            .setPositiveButton("Confirm") { _, _ ->
                setFragmentResult(resultVolume)
            }
            .create()

        return dialog
    }

    private fun setFragmentResult(response: Int) {
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(KEY_VOLUME_RESPONSE to response)
        )
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
        private val TAG = SeekbarDialogFragment::class.java.name

        @JvmStatic
        private val KEY_VOLUME_RESPONSE = "VOLUME_RESPONSE"

        @JvmStatic
        private val ARG_VOLUME = "ARG_VOLUME"

        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(fragmentManager: FragmentManager, volumeValue: Int) {
            val dialogFragment = SeekbarDialogFragment()
            dialogFragment.arguments = bundleOf(ARG_VOLUME to volumeValue)
            dialogFragment.show(fragmentManager, TAG)
        }

        fun setUpListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: SeekbarResultListener
        ) {
            fragmentManager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner) { _, result ->
                listener.invoke(result.getInt(KEY_VOLUME_RESPONSE))
            }
        }
    }

}