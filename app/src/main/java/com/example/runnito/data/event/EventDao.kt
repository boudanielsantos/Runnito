package com.example.runnito.data.event

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.runnito.model.event.EventModel
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM event_model")
    fun getAllEvents(): Flow<List<EventModel>>

    @Insert
    suspend fun createEvent(event: EventModel)


    @Update
    suspend fun updateEvent(event: EventModel)

    @Query("DELETE FROM EVENT_MODEL")
    suspend fun deleteAllEvents()

    @Delete
    suspend fun deleteEvent(event: EventModel)


    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertAll(events: List<EventModel>)

    @Query("SELECT * FROM event_model WHERE id = :id")
    fun getEventById(id: Int): Flow<EventModel>

    @Query("SELECT * FROM event_model WHERE id IN (:ids)")
    suspend fun getEventsByIds(ids: List<Int>): List<EventModel>


}