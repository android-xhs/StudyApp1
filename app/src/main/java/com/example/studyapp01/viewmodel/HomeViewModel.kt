package com.example.studyapp01.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime

class HomeViewModel : ViewModel() {
    val mData = MutableLiveData<String>()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            delay(1000)
            val data = "模拟数据 ${LocalTime.now()}"
            mData.value = data
//            mData.postValue(data)
        }
    }

}