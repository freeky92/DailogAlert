package com.asurspace.whatisalertdialog_pl.app_contract.model

class AvailableVoleValues(
    val values: List<Int>,
    val currentIndex: Int
) {
    companion object {
        fun createVolumeValues(currentVolumeValue: Int): AvailableVoleValues {
            val values = (0..100 step 10)
            val currentIndex = values.indexOf(currentVolumeValue)
            return if (currentIndex == -1) {
                val list = values + currentVolumeValue
                AvailableVoleValues(list, list.lastIndex)
            } else {
                AvailableVoleValues(values.toList(), currentIndex)
            }
        }
    }
}
