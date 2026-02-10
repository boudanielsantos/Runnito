package com.example.runnito.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnito.model.Distance
import com.example.runnito.model.event.EventModel
import com.example.runnito.repository.EventRepository
import com.example.runnito.repository.RegisteredEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisteredEventsViewModel @Inject constructor(
    private val registeredEventRepository: RegisteredEventRepository,
    private val eventRepository: EventRepository // Assuming you have a repository for EventModel
) : ViewModel() {

    private val _groupedEvents = MutableStateFlow<Map<String, List<EventModel>>>(emptyMap())
    val groupedEvents: StateFlow<Map<String, List<EventModel>>> = _groupedEvents.asStateFlow()

    init {
        loadAndGroupRegisteredEvents()
    }

    private fun loadAndGroupRegisteredEvents() {
        viewModelScope.launch {
            val registeredEvents =
                registeredEventRepository.getAllRegisteredEvents() // This function should be defined in your repository

            val eventIds = registeredEvents.map { it.eventId }.distinct()

            if (eventIds.isNotEmpty()) {
                val eventModels =
                    eventRepository.getEventsByIds(eventIds) // This function should be defined in your repository

                val eventModelMap = eventModels.associateBy { it.id }

                val grouped = registeredEvents
                    .mapNotNull { registeredEvent ->
                        val eventModel = eventModelMap[registeredEvent.eventId]
                        if (eventModel != null) {
                            Pair(registeredEvent.distanceJoined.displayName, eventModel)
                        } else {
                            null
                        }
                    }
                    .groupBy(
                        keySelector = { it.first },
                        valueTransform = { it.second }
                    )

                _groupedEvents.update { grouped }
            } else {
                _groupedEvents.update { emptyMap() }
            }
        }
    }
}
