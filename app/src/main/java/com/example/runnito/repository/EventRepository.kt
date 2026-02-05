package com.example.runnito.repository

import com.example.runnito.data.event.EventDao
import com.example.runnito.model.event.EventModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRepository @Inject constructor(val eventDao: EventDao) {


    fun getAllEvents(): Flow<List<EventModel>> = eventDao.getAllEvents()

    fun getEventById(id: Int): Flow<EventModel> = eventDao.getCardById(id)

    suspend fun createEvent(event: EventModel) =
        eventDao.createEvent(event)

    suspend fun deleteAllEvents() = eventDao.deleteAllEvents()

    suspend fun deleteEvent(event: EventModel) =
        eventDao.deleteEvent(event)

    suspend fun updateEvent(event: EventModel) =
        eventDao.updateEvent(event)

    suspend fun insertEvents(events: List<EventModel>) = eventDao.insertAll(events)

}