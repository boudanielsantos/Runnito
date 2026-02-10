package com.example.runnito.repository

import com.example.runnito.data.registeredevent.RegisteredEventDao
import com.example.runnito.model.registeredevent.RegisteredEvent
import javax.inject.Inject

class RegisteredEventRepository @Inject constructor(val registeredEventDao: RegisteredEventDao) {


   suspend fun getAllRegisteredEvents() = registeredEventDao.getAllRegisteredEvents()
    suspend fun addRegisteredEvent(registeredEvent: RegisteredEvent) =
        registeredEventDao.addRegisteredEvent(registeredEvent)


    suspend fun getRegisteredEventById(eventId: Int) =
        registeredEventDao.getRegisteredEventById(eventId)

}