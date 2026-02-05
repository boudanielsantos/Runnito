package com.example.runnito.model.registeredevent

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.runnito.model.Distance
import com.example.runnito.model.event.EventModel

@Entity(
    tableName = "registered_event",
    foreignKeys = [
        ForeignKey(
            entity = EventModel::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RegisteredEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val eventId: Int,
    val distanceJoined: Distance,
) {
}