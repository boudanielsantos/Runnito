package com.example.runnito.screens.profile

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnito.data.DataOrException
import com.example.runnito.data.registeredevent.RegisteredEventWithDetails
import com.example.runnito.model.Distance
import com.example.runnito.model.user.User
import com.example.runnito.repository.RegisteredEventRepository
import com.example.runnito.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

@HiltViewModel
class ProfileViewModel @Inject constructor(
    registeredEventRepository: RegisteredEventRepository,
    val userRepository: UserRepository
) :
    ViewModel() {
    private val _totalDistanceRan = MutableStateFlow(0.0)
    private val _totalEventsThisYear = MutableStateFlow(0)
    private val _totalEventsOverall = MutableStateFlow(0)

    private var _currentUser = MutableStateFlow<DataOrException<User, Boolean, Exception>>(
        DataOrException(
            null,
            true,
            Exception("")
        )
    )
    val totalDistanceRan: StateFlow<Double> = _totalDistanceRan.asStateFlow()
    val totalEventsThisYear: StateFlow<Int> = _totalEventsThisYear.asStateFlow()
    val totalEventsOverall: StateFlow<Int> = _totalEventsOverall.asStateFlow()

    val currentUser = _currentUser.asStateFlow()


    init {
        viewModelScope.launch {
            _currentUser.value = _currentUser.value.copy(loading = true)
            try {
                userRepository.getFirstUser().collect { user ->
                    _currentUser.value = DataOrException(data = user, loading = false)
                }
            } catch (e: Exception) {
                _currentUser.value = DataOrException(loading = false, exception = e)
                Log.e(TAG, "Error fetching user data: ${e.message}")
            }
        }

        viewModelScope.launch {
            try {
                registeredEventRepository.getAllRegisteredEventsWithDetails().collect { events ->
                    processEventData(events)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching registered events: ${e.message}")
            }
        }
    }

    private fun processEventData(events: List<RegisteredEventWithDetails>) {
        val currentDate = Date()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val calendar = Calendar.getInstance()

        var calculatedDistance = 0.0
        var eventsThisYearCount = 0

        // Filter for events that have already passed
        val pastEvents = events.filter { it.event.dateObj!!.before(currentDate) }

        //Calculate total distance from past events
        pastEvents.forEach { details ->
            calculatedDistance += details.registeredEvent.distanceJoined.inKilometers()
        }

        //Calculate total events joined this year
        events.forEach { details ->
            calendar.time = details.event.dateObj
            if (calendar.get(Calendar.YEAR) == currentYear) {
                eventsThisYearCount++
            }
        }

        _totalDistanceRan.value = calculatedDistance
        _totalEventsThisYear.value = eventsThisYearCount
        _totalEventsOverall.value = events.size
    }

    fun Distance.inKilometers(): Double {
        return when (this) {
            Distance.THREE_KM -> 3.0
            Distance.FIVE_KM -> 5.0
            Distance.TEN_KM -> 10.0
            Distance.TWENTY_ONE_KM -> 21.1
            Distance.FORTY_TWO_KM -> 42.2
        }
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}

