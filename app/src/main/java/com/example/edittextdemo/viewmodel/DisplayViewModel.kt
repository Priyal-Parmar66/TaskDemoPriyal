package com.example.edittextdemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class DisplayViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val KEY_VALIDATED_DATA = "validated_data"
    }

    private val _validatedList = MutableLiveData<List<String>>()
    val validatedList: LiveData<List<String>> get() = _validatedList

    init {
        _validatedList.value = savedStateHandle.get<List<String>>(KEY_VALIDATED_DATA) ?: emptyList()
    }

    fun setValidatedData(data: List<String>) {
        _validatedList.value = data
        savedStateHandle[KEY_VALIDATED_DATA] = data
    }
}