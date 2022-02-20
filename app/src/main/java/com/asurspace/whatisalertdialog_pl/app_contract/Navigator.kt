package com.asurspace.whatisalertdialog_pl.app_contract

import androidx.fragment.app.Fragment


fun Fragment.navigate(): Navigator{
    return requireActivity() as Navigator
}

interface Navigator {

    fun showTimePickerFragment()

}