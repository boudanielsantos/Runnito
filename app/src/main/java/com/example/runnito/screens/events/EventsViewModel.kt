package com.example.runnito.screens.events

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnito.data.DataOrException
import com.example.runnito.model.RunningEvent
import com.example.runnito.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventsViewModel() : ViewModel() {
    private val _runningEvents =
        MutableStateFlow<DataOrException<List<RunningEvent>, Boolean, Exception>>(
            DataOrException(
                listOf(),
                true,
                Exception("")
            )
        )
    val runningEvents = _runningEvents.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _runningEvents.value.loading = true
                val scrapedEvents =
                    withContext(Dispatchers.IO) {
                        scrapeEvents()
                    }

                _runningEvents.value = _runningEvents.value.copy(
                    data = scrapedEvents,
                    loading = false,
                    exception = null
                )
            } catch (e: Exception) {
                _runningEvents.value = _runningEvents.value.copy(loading = false, exception = e)
            }
        }
    }

    private fun scrapeEvents(): List<RunningEvent> {
        val eventsList = mutableListOf<RunningEvent>()
        try {
            //Added user agent to to mimic a browser request
            val doc = Jsoup.connect(Constants.takboEventsUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(10000)
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

                //Extract additional details about the race
                val subtitle = element.select(".evcal_event_subtitle").text().trim()

                if (title.isNotEmpty()) {
                    val dateString = "$fullMonth $day, $year"
                    val parsedDate = try {
                        SimpleDateFormat("MMMM d, yyyy", Locale.US).parse(dateString)
                    } catch (e: Exception) {
                        null
                    }

                    eventsList.add(
                        RunningEvent(
                            title,
                            day,
                            fullMonth,
                            year,
                            subtitle,
                            parsedDate,
                            eventUrl
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