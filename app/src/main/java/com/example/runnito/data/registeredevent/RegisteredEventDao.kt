package com.example.runnito.data.registeredevent

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.runnito.model.registeredevent.RegisteredEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface RegisteredEventDao {

    @Insert
    suspend fun addRegisteredEvent(registeredEvent: RegisteredEvent)

    @Query("SELECT * FROM registered_event WHERE eventId = :eventId")
    fun getRegisteredEventById(eventId: Int): Flow<RegisteredEvent?>
}

