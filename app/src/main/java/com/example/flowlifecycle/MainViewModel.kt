package com.example.flowlifecycle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val KEY_WITH_LIFECYCLE = "KEY_WITH_LIFECYCLE"
        const val KEY_WITHOUT_LIFECYCLE = "KEY_WITHOUT_LIFECYCLE"
        const val KEY_WITH_LIFECYCLE_WITH_STATEIN = "KEY_WITH_LIFECYCLE_WITH_STATEIN"
        const val KEY_WITHOUT_LIFECYCLE_WITH_STATEIN = "KEY_WITHOUT_LIFECYCLE_WITH_STATEIN"
    }

    private var externalValueWithLifecycle = 0
    private var externalValueWithoutLifecycle = 0


    val savedStateFlowWithLifecycle = savedStateHandle
        .getStateFlow(KEY_WITH_LIFECYCLE, 0)

    val savedStateFlowWithoutLifecycle = savedStateHandle
        .getStateFlow(KEY_WITHOUT_LIFECYCLE, 0)

    val savedStateFlowWithLifecycleWithStateIn = savedStateHandle
        .getStateFlow(KEY_WITH_LIFECYCLE_WITH_STATEIN, 0)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(0),
            0
        )

    val savedStateFlowWithoutLifecycleWithStateIn = savedStateHandle
        .getStateFlow(KEY_WITHOUT_LIFECYCLE_WITH_STATEIN, 0)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(0),
            0
        )

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
        viewModelScope.apply {
            launch {
                while (true) {
                    delay(1000)
                    savedStateHandle[KEY_WITH_LIFECYCLE] =
                        savedStateFlowWithLifecycle.value + 1
                }
            }

            launch {
                while (true) {
                    delay(1000)
                    savedStateHandle[KEY_WITHOUT_LIFECYCLE] =
                        savedStateFlowWithoutLifecycle.value + 1
                }
            }

            launch {
                while (true) {
                    delay(1000)
                    savedStateHandle[KEY_WITH_LIFECYCLE_WITH_STATEIN] =
                        savedStateFlowWithLifecycleWithStateIn.value + 1
                }
            }

            launch {
                while (true) {
                    delay(1000)
                    savedStateHandle[KEY_WITHOUT_LIFECYCLE_WITH_STATEIN] =
                        savedStateFlowWithoutLifecycleWithStateIn.value + 1
                }
            }
        }
    }
}