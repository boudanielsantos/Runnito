package com.example.runnito.screens.events

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnito.data.DataOrException
import com.example.runnito.model.event.EventModel
import com.example.runnito.repository.EventRepository
import com.example.runnito.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.Locale

@HiltViewModel
class EventsViewModel @Inject constructor(private val eventRepository: EventRepository) :
    ViewModel() {
    private val _events =
        MutableStateFlow<DataOrException<List<EventModel>, Boolean, Exception>>(
            DataOrException(
                listOf(),
                true,
                Exception("")
            )
        )
    val events = _events.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _events.value = _events.value.copy(loading = true)
            eventRepository.getAllEvents().distinctUntilChanged().collect { eventsFromDb ->
                _events.value = _events.value.copy(data = eventsFromDb, loading = false)
            }
        }
        scrapeAndInsertNewEvents()
    }

    private fun scrapeAndInsertNewEvents() {
        viewModelScope.launch {
            try {
                _events.value = _events.value.copy(loading = true, exception = null)

                // Scrape events from the web
                val scrapedEvents = withContext(Dispatchers.IO) {
                    scrapeEvents()
                }

                if (scrapedEvents.isNotEmpty()) {
                    val existingEvents = eventRepository.getAllEvents().first()

                    val existingEventUrls = existingEvents.map { it.eventDetailsUrl }.toSet()

                    val newEvents =
                        scrapedEvents.filter { it.eventDetailsUrl !in existingEventUrls }

                    if (newEvents.isNotEmpty()) {
                        eventRepository.insertEvents(newEvents)
                    }
                }

                _events.value = _events.value.copy(loading = false, exception = null)

            } catch (e: Exception) {
                _events.value = _events.value.copy(loading = false, exception = e)
                Log.e(TAG, "Error scraping or inserting events: $e")
            }
        }
    }

    private fun scrapeEvents(): List<EventModel> {
        val eventsList = mutableListOf<EventModel>()
        try {
            //Added user agent to to mimic a browser request
            val doc = Jsoup.connect(Constants.takboEventsUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(50000)
                .get()

            val eventElements = doc.select(".eventon_list_event")

            for (element in eventElements) {

                //Get the link to the event details
                val eventUrl = element.select("a.evcal_list_a").attr("href").trim()

                //Extract the title of the event
                val title = element.select(".evcal_event_title").text().trim()

                //Extract the day part of the race date
                val day = element.select(".date").text().trim()

                //Extract the month and year of the race date
                val fullMonth = element.select(".evcal_cblock").attr("data-smon")
                val year = element.select(".evcal_cblock").attr("data-syr")


                if (title.isNotEmpty()) {
                    val dateString = "$fullMonth $day, $year"
                    val parsedDate = try {
                        SimpleDateFormat("MMMM d, yyyy", Locale.US).parse(dateString)
                    } catch (e: Exception) {
                        null
                    }

                    eventsList.add(
                        EventModel(
                            title = title,
                            day = day,
                            month = fullMonth,
                            year = year,
                            dateObj = parsedDate,
                            eventDetailsUrl = eventUrl
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error encountered while scraping events: $e")
        }
        return eventsList
    }


    companion object {
        val TAG: String = EventsViewModel::class.java.simpleName

    }
}