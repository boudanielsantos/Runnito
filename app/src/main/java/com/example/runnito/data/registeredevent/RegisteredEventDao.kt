package com.example.runnito.data.registeredevent

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.runnito.model.registeredevent.RegisteredEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface RegisteredEventDao {

    @Insert
    suspend fun addRegisteredEvent(registeredEvent: RegisteredEvent)

    @Query("SELECT * FROM registered_event WHERE eventId = :eventId")
    fun getRegisteredEventById(eventId: Int): Flow<RegisteredEvent?>

    @Query("SELECT * FROM REGISTERED_EVENT")
    suspend fun getAllRegisteredEvents(): List<RegisteredEvent>


    @Query("SELECT * FROM registered_event")
    fun getAllRegisteredEventsWithDetails(): Flow<List<RegisteredEventWithDetails>>

}

