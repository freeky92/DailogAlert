package com.asurspace.d_alert.app_contract

import androidx.fragment.app.Fragment


fun Fragment.navigate(): Navigator{
    return requireActivity() as Navigator
}

interface Navigator {


}