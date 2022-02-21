package com.asurspace.d_alert.app_contract

import androidx.annotation.StringRes

interface ICustomToolbarTitleProvider {

    @StringRes
    fun getStringRes(): Int

}