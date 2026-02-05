package com.example.runnito.screens.eventdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnito.data.DataOrException
import com.example.runnito.model.Distance
import com.example.runnito.model.EventModel
import com.example.runnito.repository.EventRepository
import com.example.runnito.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
) : ViewModel() {
    private val _event =
        MutableStateFlow<DataOrException<EventModel, Boolean, Exception>>(
            DataOrException(
                null,
                true,
                Exception("")
            )
        )
    val event = _event.asStateFlow()


    fun loadEvent(eventId: Int?) {
        viewModelScope.launch {
            try {
                _event.value = _event.value.copy(loading = true)
                if (eventId == null) return@launch
                eventRepository.getEventById(eventId).collect { eventFromDb ->


                    if (!eventFromDb.isDetailsPopulated) {
                        withContext(Dispatchers.IO) {
                            val eventDetails = scrapeEventDetails(eventFromDb)
                            eventDetails?.let { updateEventDetail(eventFromDb, eventDetails) }
                        }
                    }
                    _event.value =
                        _event.value.copy(data = eventFromDb, loading = false, exception = null)
                }
            } catch (e: Exception) {
                _event.value = _event.value.copy(exception = e, loading = false, data = null)
            }
        }
    }

    private fun updateEventDetail(event: EventModel, eventDetails: EventDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            event.apply {
                registrationLink = eventDetails.registrationLink
                distanceAvailable = eventDetails.distances
                bannerUrl = eventDetails.bannerUrl
//                isDetailsPopulated = true
                description = eventDetails.description
            }
            eventRepository.updateEvent(event)
        }

    }


    private fun scrapeEventDetails(event: EventModel): EventDetails? {
        val url = event.eventDetailsUrl

        try {
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .get()

            val eventDistances = doc.getEventDistances()
            val registrationLink = doc.getRegistrationLink()
            val bannerImageSrc = doc.select("meta[property=og:image]").attr("content")
            val description = doc.getEventDescription()
            return EventDetails(
                registrationLink = registrationLink,
                bannerUrl = bannerImageSrc,
                distances = eventDistances,
                description = description
            )
        } catch (e: Exception) {
            println("Error Scraping Event Details : $e")
        }
        return null
    }

    private fun Document.getEventDescription(): String {
        val fullDescription = select("meta[property=og:description]").attr("content")
        val description = if (fullDescription.contains("EVENT DISTANCES", ignoreCase = true)) {
            fullDescription.substringBefore("EVENT DISTANCES")
                .trim()
        } else if (fullDescription.contains("Distances:", ignoreCase = true)) {
            fullDescription.substringBefore("Distances:")
                .trim()
        } else {
            fullDescription
        }
        return description
    }

    private fun Document.getEventDistances(): List<Distance> {
        val bodyText = body().text()
        val eventDistances = if (bodyText.contains("EVENT DISTANCES", ignoreCase = true)) {
            bodyText.substringAfter("EVENT DISTANCES")
                .substringBefore("REGISTRATION")
                .trim()
        } else if (bodyText.contains("Distances:", ignoreCase = true)) {
            bodyText.substringAfter("Distances:")
                .substringBefore("\n")
                .trim()
        } else {
            ""
        }

        val availableDistance: MutableList<Distance> = mutableListOf()

        if (eventDistances.isNotEmpty()) {
            val distancesList = eventDistances.split(",")
            distancesList.forEach { distance ->
                when (distance.trim()) {
                    "3KM" -> availableDistance.add(Distance.THREE_KM)
                    "5KM" -> availableDistance.add(Distance.FIVE_KM)
                    "10KM" -> availableDistance.add(Distance.TEN_KM)
                    "21KM" -> availableDistance.add(Distance.TWENTY_ONE_KM)
                    "42KM" -> availableDistance.add(Distance.FORTY_TWO_KM)
                }

            }
        }

        return availableDistance
    }

    private fun Document.getRegistrationLink(): String {
        val onlineLabel = getElementsContainingOwnText("Online").firstOrNull()
        var registrationLink = ""
        if (onlineLabel != null) {
            val paragraphText = onlineLabel.text()

            val urlRegex = "https?://[\\w\\d./?=#-]+".toRegex()

            registrationLink = urlRegex.find(paragraphText)?.value ?: ""
        }
        return registrationLink
    }

    data class EventDetails(
        val registrationLink: String,
        val bannerUrl: String,
        val distances: List<Distance>,
        val description: String
    )
}