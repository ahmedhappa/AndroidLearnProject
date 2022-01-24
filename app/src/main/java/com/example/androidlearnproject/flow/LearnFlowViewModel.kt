package com.example.androidlearnproject.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LearnFlowViewModel : ViewModel() {

    //this flow is a clod flow which means that it doesn't emit values unless there is an observer to it
    //every time there is a new observe to this flow the block starts it's execution from the beginning
    //we can't emit values from this flow outside this block
    /* not lifecycle aware which means it can emit data even if the activity is not visible
       unlike LiveData which emits data only when the activity is visible */
    val normalFLow = flow {
        emit(LearnViewModelEnum.LOADING)
        delay(2000)
        emit(LearnViewModelEnum.SUCCESS)
        emit(LearnViewModelEnum.COMPLETE)
    }

    /* this type of flow is exactly like the liveData except it is not lifecycle aware which means it can emit data to the activity
    even when the activity is not visible we can use repeatOnLifecycle Method to make it act like liveData */
    // this flow is a hot flow which means it can emit data even if there is no one observe it
    // this flow holds the last value that has been emitted
    // state flow must created with an initial value
    private val _myStateFlow = MutableStateFlow(LearnViewModelEnum.LOADING)
    val myStateFlow = _myStateFlow.asStateFlow()
    fun useStateFlow() {
//        we can emit date using value without scope or emit but it has to be in a scope
        viewModelScope.launch {
            _myStateFlow.value = LearnViewModelEnum.LOADING
            delay(2000)
            _myStateFlow.emit(LearnViewModelEnum.SUCCESS)
            _myStateFlow.emit(LearnViewModelEnum.COMPLETE)
        }
    }

    //shared flow is exactly like state flow except it doesn't preserve date which means all the emitted date gets lost.
    private val _mySharedFlow = MutableSharedFlow<LearnViewModelEnum>()
    val mySharedFlow = _mySharedFlow.asSharedFlow()
    fun useSharedFlow() {
//        shared flow must emit data in scope
        viewModelScope.launch {
            _mySharedFlow.emit(LearnViewModelEnum.LOADING)
            delay(2000)
            _mySharedFlow.emit(LearnViewModelEnum.SUCCESS)
            _mySharedFlow.emit(LearnViewModelEnum.COMPLETE)
        }
    }

    //this example can be applied on normal flow and shared flow also
    private val _flowUseWithOperators = MutableStateFlow(0)
    val flowUseWithOperators = _flowUseWithOperators.asStateFlow()
    fun useFlowWithOperators() {
        _flowUseWithOperators.value = 4
        _flowUseWithOperators.value = 7
        _flowUseWithOperators.value = 8
        _flowUseWithOperators.value = 12
        _flowUseWithOperators.value = 15
    }

    enum class LearnViewModelEnum {
        LOADING, SUCCESS, COMPLETE
    }
}