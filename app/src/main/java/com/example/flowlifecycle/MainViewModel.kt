package com.example.flowlifecycle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    companion object {
        const val KEY_WITH_LIFECYCLE = "KEY_WITH_LIFECYCLE"
        const val KEY_WITHOUT_LIFECYCLE = "KEY_WITHOUT_LIFECYCLE"
    }
    private var externalValueWithLifecycle = 0
    private var externalValueWithoutLifecycle = 0


    val savedStateFlowWithLifecycle
            = savedStateHandle.getStateFlow(KEY_WITH_LIFECYCLE, 0)

    val savedStateFlowWithoutLifecycle
            = savedStateHandle.getStateFlow(KEY_WITHOUT_LIFECYCLE, 0)


    val counter = flow {
        var value = 0
        while (true) {
           emit(value++)
           delay(1000)
        }
    }

    val externalDataWithLifecycle = flow {
        while (true) {
            emit(externalValueWithLifecycle++)
            delay(1000)
        }
    }

    val externalDataWithoutLifecycle = flow {
        while (true) {
            emit(externalValueWithoutLifecycle++)
            delay(1000)
        }
    }

    init {

        viewModelScope.launch {
            while (true) {
                delay(1000)
                savedStateHandle[KEY_WITH_LIFECYCLE] = savedStateFlowWithLifecycle.value + 1
            }
        }

        viewModelScope.launch {
            while (true) {
                delay(1000)
                savedStateHandle[KEY_WITHOUT_LIFECYCLE] = savedStateFlowWithoutLifecycle.value + 1
            }
        }
    }
}