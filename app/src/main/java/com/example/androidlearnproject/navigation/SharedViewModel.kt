package com.example.androidlearnproject.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _dummyStringLiveData = MutableLiveData<String>("My Dummy String");
    val dummyStringLiveData: LiveData<String> = _dummyStringLiveData

    fun setNewDummyString(str: String) {
        _dummyStringLiveData.value = str
    }
}