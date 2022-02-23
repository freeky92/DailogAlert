package com.asurspace.d_alert

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.asurspace.d_alert.app_contract.ICustomToolbarTitleProvider
import com.asurspace.d_alert.databinding.FragmentMenuDialogFragmentBinding
import com.asurspace.d_alert.dialog_fragments.*
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.properties.Delegates


class MenuDialogFragment : Fragment(), ICustomToolbarTitleProvider {

    private var _binding: FragmentMenuDialogFragmentBinding? = null
    private val binding get() = _binding!!

    private var volLevel by Delegates.notNull<Int>()
    private var appVolLevel by Delegates.notNull<Int>()
    private var colorBG by Delegates.notNull<Int>()
    private lateinit var date: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        date = Date(savedInstanceState?.getLong(KEY_TIME) ?: System.currentTimeMillis())
        appVolLevel = savedInstanceState?.getInt(KEY_APP_VOLUME) ?: (1..99).random()
        volLevel = savedInstanceState?.getInt(KEY_MAIN_VOLUME) ?: (1..99).random()
        colorBG = savedInstanceState?.getInt(KEY_COLOR) ?: Color.BLUE
        Log.d("Current time", System.currentTimeMillis().toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuDialogFragmentBinding.inflate(inflater, container, false)

        binding.timePickerTb.setOnClickListener {
            showTimePickerDialogFragment()
        }
        binding.simpleDialogFragment.setOnClickListener {
            showSimpleDialogFragment()
        }
        binding.setVolumeTb.setOnClickListener {
            setVolumeSeekbar()
        }
        binding.singleChoice.setOnClickListener {
            setSingleChoiceDialogFragment()
        }
        binding.colorTbMultiChoice.setOnClickListener {
            setColorMultiChoiceDialog()
        }
        binding.setMainVolumeEt.setOnClickListener {
            setEditableDialogFragment(REQUEST_KEY_MAIN_VOLUME, volLevel)
        }
        binding.setAppVolumeEt.setOnClickListener {
            setEditableDialogFragment(REQUEST_KEY_APP_VOLUME, appVolLevel)
        }

        updateUi()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // !!set custom Dialog Fragment result listener here
        setTimePickerDialogListener()
        setSimpleDialogFragmentListener()
        setSeekbarDialogFragmentListener()
        setSingleChoiceListener()
        setMultiplyChoiceDialogFragmentListener()
        setEditableReusableDialogFragmentListener()
    }

    private fun updateUi() {
        binding.timeTv.text = String.format("Time: ${hourFormat.format(date)}")
        binding.appVolumeTv.text = getString(R.string.app_volume_eq, appVolLevel)
        binding.mainVolumeTv.text = getString(R.string.volume_eq, volLevel)
        binding.progressbar.progress = volLevel
        binding.colorTbMultiChoice.setBackgroundColor(colorBG)
    }

    private fun showTimePickerDialogFragment() {
        TimePickerDialogFragment.show(parentFragmentManager, date.time)
    }

    private fun setTimePickerDialogListener() {
        TimePickerDialogFragment.setUpListener(parentFragmentManager, viewLifecycleOwner) {
            date = Date(it)
            updateUi()
        }
    }

    private fun showSimpleDialogFragment() {
        SimpleDialogFragment.show(parentFragmentManager, "Deletion request", "Delete OS")
    }

    private fun setSimpleDialogFragmentListener() {
        SimpleDialogFragment.setUpListener(parentFragmentManager, viewLifecycleOwner) {
            showSnackBar(it)
        }
    }

    private fun setVolumeSeekbar() {
        SeekbarDialogFragment.show(parentFragmentManager, volLevel)
    }

    private fun setSeekbarDialogFragmentListener() {
        SeekbarDialogFragment.setUpListener(parentFragmentManager, viewLifecycleOwner) {
            volLevel = it
            updateUi()
        }
    }

    private fun setColorMultiChoiceDialog() {
        MultiplyChoiceDialogFragment.show(parentFragmentManager, colorBG)
    }

    private fun setMultiplyChoiceDialogFragmentListener() {
        MultiplyChoiceDialogFragment.setUpListener(parentFragmentManager, viewLifecycleOwner) {
            colorBG = it
            updateUi()
        }
    }

    private fun setSingleChoiceDialogFragment() {
        SingleChoiceDialogFragment.show(parentFragmentManager, volLevel)
    }

    private fun setSingleChoiceListener() {
        SingleChoiceDialogFragment.setUpListener(parentFragmentManager, viewLifecycleOwner) {
            volLevel = it
            updateUi()
        }
    }

    private fun setEditableDialogFragment(requestKey: String, volumeValue: Int) {
        EditableReusableDialogFragment.show(parentFragmentManager, requestKey, volumeValue)
    }

    private fun setEditableReusableDialogFragmentListener() {
        val listener: EditableReusableDialogResultListener = { requestKey, volume ->
            when (requestKey) {
                REQUEST_KEY_APP_VOLUME -> {
                    appVolLevel = volume
                }
                REQUEST_KEY_MAIN_VOLUME -> {
                    volLevel = volume
                }
            }
            updateUi()
        }
        EditableReusableDialogFragment.setUpListener(
            parentFragmentManager,
            viewLifecycleOwner,
            REQUEST_KEY_MAIN_VOLUME,
            listener
        )
        EditableReusableDialogFragment.setUpListener(
            parentFragmentManager,
            viewLifecycleOwner,
            REQUEST_KEY_APP_VOLUME,
            listener
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_TIME, date.time)
        outState.putInt(KEY_APP_VOLUME, appVolLevel)
        outState.putInt(KEY_MAIN_VOLUME, volLevel)
        outState.putInt(KEY_COLOR, colorBG)
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        snackBar.view.setOnClickListener {
            snackBar.dismiss()
        }
        snackBar.show()
    }

    override fun getStringRes() = R.string.main_menu

    companion object {

        @JvmStatic
        private val KEY_TIME = "KET_TIME"

        @JvmStatic
        private val KEY_COLOR = "KEY_COLOR"

        @JvmStatic
        private val KEY_MAIN_VOLUME = "KEY_MAIN_VOLUME"

        @JvmStatic
        private val KEY_APP_VOLUME = "KEY_APP_VOLUME"

        @JvmStatic
        private val TAG = this::class.java.simpleName

        @JvmStatic
        private val REQUEST_KEY_MAIN_VOLUME = "REQUEST_KEY_MAIN_VOLUME"

        @JvmStatic
        private val REQUEST_KEY_APP_VOLUME = "REQUEST_KEY_APP_VOLUME"

    }

}
