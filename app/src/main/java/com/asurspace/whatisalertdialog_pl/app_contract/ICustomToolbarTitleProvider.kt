package com.asurspace.whatisalertdialog_pl.app_contract

import androidx.annotation.StringRes

interface ICustomToolbarTitleProvider {

    @StringRes
    fun getStringRes(): Int

}