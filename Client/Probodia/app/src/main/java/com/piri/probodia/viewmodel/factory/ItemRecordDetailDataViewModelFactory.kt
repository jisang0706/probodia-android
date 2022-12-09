package com.piri.probodia.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.viewmodel.ItemRecordDetailDataViewModel

class ItemRecordDetailDataViewModelFactory(
    private val baseDataName : String,
    private val baseDataUnit : String,
    private val baseDataText : Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemRecordDetailDataViewModel::class.java)) {
            return ItemRecordDetailDataViewModel(baseDataName, baseDataUnit, baseDataText) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}