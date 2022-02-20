package com.asurspace.whatisalertdialog_pl.app_contract.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class CustomAction(
    @DrawableRes val drawableRes: Int,
    @StringRes val descriptionRes: Int,
    val runnable: Runnable
)