package com.example.androidtestproject.dependency_injection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

/* this annotation is used in view model to make them use Hilt because these classes like activity and
fragments needs a special treatment */
@HiltViewModel
class LearnDiViewModel @Inject constructor(
    @CurrentDateCalendar calendar: Calendar
) : ViewModel() {
    private val _calendarLiveData = MutableLiveData(calendar)
    val calendarLiveData: LiveData<Calendar> = _calendarLiveData
}