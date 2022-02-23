package com.asurspace.d_alert.app_contract

import com.asurspace.d_alert.app_contract.model.CustomAction

interface IProvideCustomAction {
    fun getCustomAction(): CustomAction
}