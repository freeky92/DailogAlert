package com.asurspace.d_alert.app_contract.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class CustomAction(
    @DrawableRes val drawableRes: Int,
    @StringRes val descriptionRes: Int,
    val runnable: Runnable
)