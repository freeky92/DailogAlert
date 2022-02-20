package com.asurspace.whatisalertdialog_pl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asurspace.whatisalertdialog_pl.app_contract.ICustomToolbarTitleProvider
import com.asurspace.whatisalertdialog_pl.databinding.FragmentTimePickerBinding

class TimePickerFragment : Fragment(), ICustomToolbarTitleProvider {

    private var _binding: FragmentTimePickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        private val TAG = this::class.java.simpleName

        @JvmStatic
        private val ARG = "ARG"

        @JvmStatic
        fun newInstance() =
            TimePickerFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun getStringRes() = R.string.time_picker_tb_text
}