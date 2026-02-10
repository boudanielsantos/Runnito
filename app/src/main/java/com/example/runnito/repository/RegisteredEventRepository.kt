package com.example.runnito.repository

import com.example.runnito.data.registeredevent.RegisteredEventDao
import com.example.runnito.data.registeredevent.RegisteredEventWithDetails
import com.example.runnito.model.registeredevent.RegisteredEvent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisteredEventRepository @Inject constructor(val registeredEventDao: RegisteredEventDao) {


    fun getAllRegisteredEventsWithDetails(): Flow<List<RegisteredEventWithDetails>> {
        return registeredEventDao.getAllRegisteredEventsWithDetails()
    }

    suspend fun getAllRegisteredEvents() = registeredEventDao.getAllRegisteredEvents()
    suspend fun addRegisteredEvent(registeredEvent: RegisteredEvent) =
        registeredEventDao.addRegisteredEvent(registeredEvent)


    fun getRegisteredEventById(eventId: Int) =
        registeredEventDao.getRegisteredEventById(eventId)


}