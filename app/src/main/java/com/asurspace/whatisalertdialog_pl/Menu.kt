package com.asurspace.whatisalertdialog_pl

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.asurspace.whatisalertdialog_pl.app_contract.ICustomToolbarTitleProvider
import com.asurspace.whatisalertdialog_pl.app_contract.model.AvailableVoleValues
import com.asurspace.whatisalertdialog_pl.databinding.CustomEditablieItemBinding
import com.asurspace.whatisalertdialog_pl.databinding.FragmentMenuBinding
import com.asurspace.whatisalertdialog_pl.databinding.VolumeItemBinding
import java.util.*
import kotlin.properties.Delegates


class Menu : Fragment(), ICustomToolbarTitleProvider {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private var volLevel by Delegates.notNull<Int>()
    private var colorBG by Delegates.notNull<Int>()
    private lateinit var date: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        date = Date(savedInstanceState?.getLong(KEY_TIME) ?: System.currentTimeMillis())
        volLevel = savedInstanceState?.getInt(KEY_VOLUME) ?: (1..99).random()
        colorBG = savedInstanceState?.getInt(KEY_COLOR) ?: Color.BLUE
        Log.d("Current time", System.currentTimeMillis().toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)

        binding.timePickerTb.setOnClickListener {
            showTimePickerDialog()
        }
        binding.setVolumeTb.setOnClickListener {
            changeVolume()
        }
        binding.colorTbMultiChoice.setOnClickListener {
            setColorMultiChoiceDialog()
        }
        binding.singleChoice.setOnClickListener {
            setSingleChoice()
        }
        binding.setVolumeEt.setOnClickListener {
            setEditableAlertDialogChangeTime()
        }

        updateUi()

        return binding.root
    }

    private fun updateUi() {
        binding.timeTv.text = String.format("Time: ${hourFormat.format(date)}")
        binding.volumeTv.text = getString(R.string.volume_eq, volLevel)
        binding.progressbar.progress = volLevel
        binding.colorTbMultiChoice.setBackgroundColor(colorBG)
    }

    private fun showTimePickerDialog() {
        val setTime = Calendar.getInstance()
        Log.d("Current time", setTime.time.toString())
        val dialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                setTime[Calendar.HOUR_OF_DAY] = hourOfDay
                setTime[Calendar.MINUTE] = minute
                date = setTime.time
                updateUi()
            },
            setTime[Calendar.HOUR_OF_DAY],
            setTime[Calendar.MINUTE],
            true
        )
        dialog.show()

    }

    private fun changeVolume() {
        val volBinding = VolumeItemBinding.inflate(layoutInflater)
        val seekbar = volBinding.setVolSeekbar

        seekbar.progress = volLevel
        var internalVolume = volLevel
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                volBinding.textSetVolume.text = getString(R.string.volume_eq, value)
                internalVolume = value
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
                volLevel = internalVolume
                updateUi()
            }
            .create()
        dialog.show()
        updateUi()
    }

    private fun setColorMultiChoiceDialog() {
        val colorItem = resources.getStringArray(R.array.colors)
        val colList = mutableListOf(
            Color.red(this.colorBG),
            Color.green(this.colorBG),
            Color.blue(this.colorBG)
        )

        val checkBox = colList
            .map { it > 0 }
            .toBooleanArray()

        var color: Int = this.colorBG
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Choose ll bg color")
            .setMultiChoiceItems(colorItem, checkBox) { _, which, isChecked ->
                colList[which] = if (isChecked) 255 else 0

                color = Color.rgb(
                    colList[0],
                    colList[1],
                    colList[2]
                )
                updateUi()
            }
            .setPositiveButton("Confirm") { _, _ ->
                colorBG = color
                updateUi()
            }
            .create()
        dialog.show()
    }

    private fun setSingleChoice() {
        val volumeItems = AvailableVoleValues.createVolumeValues(volLevel)
        val volumeTextItems = volumeItems.values
            .map { getString(R.string.volume_eq, it) }
            .toTypedArray()

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Volume setup")
            .setCancelable(true)
            .setSingleChoiceItems(volumeTextItems, volumeItems.currentIndex) { dialog, which ->
                volLevel = volumeItems.values[which]
                updateUi()
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }

    private fun setEditableAlertDialogChangeTime() {
        val editableBinding = CustomEditablieItemBinding.inflate(layoutInflater)
        editableBinding.tietEt.setText(volLevel.toString())

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Set volume")
            .setView(editableBinding.root)
            .setPositiveButton("Change", null)
            .create()
        dialog.setOnShowListener {
            editableBinding.tietEt.requestFocus()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enteredText = editableBinding.tietEt.text.toString()
                if (enteredText.isBlank()) {
                    editableBinding.tietEt.error = getString(R.string.field_is_empty)
                    return@setOnClickListener
                }
                val digit = enteredText.toIntOrNull()
                if (digit == null || digit > 100) {
                    editableBinding.tietEt.error = getString(R.string.invalid_value)
                    return@setOnClickListener
                }
                this.volLevel = digit
                updateUi()
                dialog.dismiss()
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_TIME, date.time)
        outState.putInt(KEY_VOLUME, volLevel)
        outState.putInt(KEY_COLOR, colorBG)
    }

    override fun getStringRes() = R.string.main_menu

    companion object {

        @JvmStatic
        private val KEY_TIME = "KET_TIME"

        @JvmStatic
        private val KEY_COLOR = "KEY_COLOR"

        @JvmStatic
        private val KEY_VOLUME = "KET_VOLUME"

        @JvmStatic
        private val TAG = this::class.java.simpleName


    }

}
