package com.asurspace.whatisalertdialog_pl.app_contract

import com.asurspace.whatisalertdialog_pl.app_contract.model.CustomAction

interface IProvideCustomAction {
    fun getCustomAction(): CustomAction
}